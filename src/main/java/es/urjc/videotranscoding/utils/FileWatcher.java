package es.urjc.videotranscoding.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * This class is for the the watcher the video on real streaming.
 **/
@Component
public class FileWatcher {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int DEFAULT_BUFFER_SIZE = 2048000; // ..bytes = 20KB.
	private static final long DEFAULT_EXPIRE_TIME = 60480000000L; // ..Ms = 1
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	Path filepath;
	HttpServletRequest request;
	HttpServletResponse response;

	protected FileWatcher() {
	}

	private FileWatcher setFilepath(Path filepath) {
		this.filepath = filepath;
		return this;
	}

	public static FileWatcher fromPath(Path path) {
		return new FileWatcher().setFilepath(path);
	}

	public static FileWatcher fromFile(File file) {
		return new FileWatcher().setFilepath(file.toPath());
	}

	public static FileWatcher fromURIString(String uri) {
		return new FileWatcher().setFilepath(Paths.get(uri));
	}

	public FileWatcher with(HttpServletRequest httpRequest) {
		request = httpRequest;
		return this;
	}

	public FileWatcher with(HttpServletResponse httpResponse) {
		response = httpResponse;
		return this;
	}

	public void serveResource() {
		if (response == null || request == null) {
			return;
		}
		try {
			if (!Files.exists(filepath)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			Long length = Files.size(filepath);
			String fileName = filepath.getFileName().toString();
			FileTime lastModifiedObj = Files.getLastModifiedTime(filepath);

			if (StringUtils.isEmpty(fileName) || lastModifiedObj == null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			long lastModified = LocalDateTime
					.ofInstant(lastModifiedObj.toInstant(), ZoneId.of(ZoneOffset.systemDefault().getId()))
					.toEpochSecond(ZoneOffset.UTC);
			String ifNoneMatch = request.getHeader("If-None-Match");
			if (ifNoneMatch != null && HttpUtils.matches(ifNoneMatch, fileName)) {
				response.setHeader("ETag", fileName); // Required in 304.
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			long ifModifiedSince = request.getDateHeader("If-Modified-Since");
			if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
				response.setHeader("ETag", fileName); // Required in 304.
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			String ifMatch = request.getHeader("If-Match");
			if (ifMatch != null && !HttpUtils.matches(ifMatch, fileName)) {
				response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				return;
			}
			long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
			if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
				response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				return;
			}
			Range full = new Range(0, length - 1, length);
			List<Range> ranges = new ArrayList<>();
			String range = request.getHeader("Range");
			if (range != null) {
				if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
					response.setHeader("Content-Range", "bytes */" + length);
					response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					return;
				}
				String ifRange = request.getHeader("If-Range");
				if (ifRange != null && !ifRange.equals(fileName)) {
					try {
						long ifRangeTime = request.getDateHeader("If-Range");
						if (ifRangeTime != -1) {
							ranges.add(full);
						}
					} catch (IllegalArgumentException ignore) {
						ranges.add(full);
					}
				}
				if (ranges.isEmpty()) {
					for (String part : range.substring(6).split(",")) {
						long start = Range.sublong(part, 0, part.indexOf("-"));
						long end = Range.sublong(part, part.indexOf("-") + 1, part.length());
						if (start == -1) {
							start = length - end;
							end = length - 1;
						} else if (end == -1 || end > length - 1) {
							end = length - 1;
						}
						if (start > end) {
							response.setHeader("Content-Range", "bytes */" + length);
							response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
							return;
						}
						ranges.add(new Range(start, end, length));
					}
				}
			}
			String disposition = "inline";
			String contentType = null;
			if (filepath.toString().contains(".webm") || filepath.toString().contains(".mkv")) {
				contentType = "video/webm";
			} else if (filepath.toString().contains(".mp4")) {
				contentType = "video/mp4";
			} else
				contentType = "application/octet-stream";
			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setHeader("Content-Type", contentType);
			response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("ETag", fileName);
			response.setDateHeader("Last-Modified", lastModified);
			response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
			try (FileInputStream input = new FileInputStream(filepath.toString());
					OutputStream output = response.getOutputStream()) {
				if (ranges.isEmpty() || ranges.get(0) == full) {
					response.setContentType(contentType);
					response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
					response.setHeader("Content-Length", String.valueOf(full.length));
					Range.copy(input, output, length, full.start, full.length);
				} else if (ranges.size() == 1) {
					Range r = ranges.get(0);
					response.setContentType(contentType);
					response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
					response.setHeader("Content-Length", String.valueOf(r.length));
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
					Range.copy(input, output, length, r.start, r.length);
				} else {
					response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
					ServletOutputStream sos = (ServletOutputStream) output;
					for (Range r : ranges) {
						sos.println();
						sos.println("--" + MULTIPART_BOUNDARY);
						sos.println("Content-Type: " + contentType);
						sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
						Range.copy(input, output, length, r.start, r.length);
					}
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY + "--");
					sos.close();
				}
			}
		} catch (IOException e) {
			// NOT RETURN
		}
	}

	private static class Range {
		long start;
		long end;
		long length;
		long total;

		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}

		public static long sublong(String value, int beginIndex, int endIndex) {
			String substring = value.substring(beginIndex, endIndex);
			return (substring.length() > 0) ? Long.parseLong(substring) : -1;
		}

		private static void copy(FileInputStream input, OutputStream output, long inputSize, long start, long length)
				throws IOException {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int read;
			if (inputSize == length) {

				while ((read = input.read(buffer)) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				}
			} else {
				input.skip(start);
				long toRead = length;
				while ((read = input.read(buffer)) > 0) {
					if ((toRead -= read) > 0) {
						output.write(buffer, 0, read);
						output.flush();
					} else {
						output.write(buffer, 0, (int) toRead + read);
						output.flush();
						break;
					}
				}
			}
		}
	}

	private static class HttpUtils {

		/**
		 * Returns true if the given match header matches the given value.
		 * 
		 * @param matchHeader
		 *            The match header.
		 * @param toMatch
		 *            The value to be matched.
		 * @return True if the given match header matches the given value.
		 */
		public static boolean matches(String matchHeader, String toMatch) {
			String[] matchValues = matchHeader.split("\\s*,\\s*");
			Arrays.sort(matchValues);
			return Arrays.binarySearch(matchValues, toMatch) > -1 || Arrays.binarySearch(matchValues, "*") > -1;
		}
	}

}
