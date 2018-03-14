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

import es.urjc.videotranscoding.exception.FFmpegRuntimeException;

@Component
public class FileWatcher {
//TODO 
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
				logger.error("File doesn't exist at URI : {}", filepath.toAbsolutePath().toString());
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

			// Validate request headers for caching
			// ---------------------------------------------------

			// If-None-Match header should contain "*" or ETag. If so, then return
			// 304.
			String ifNoneMatch = request.getHeader("If-None-Match");
			if (ifNoneMatch != null && HttpUtils.matches(ifNoneMatch, fileName)) {
				response.setHeader("ETag", fileName); // Required in 304.
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			// If-Modified-Since header should be greater than LastModified. If so,
			// then return 304.
			// This header is ignored if any If-None-Match header is specified.
			long ifModifiedSince = request.getDateHeader("If-Modified-Since");
			if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
				response.setHeader("ETag", fileName); // Required in 304.
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			// Validate request headers for resume
			// ----------------------------------------------------

			// If-Match header should contain "*" or ETag. If not, then return 412.
			String ifMatch = request.getHeader("If-Match");
			if (ifMatch != null && !HttpUtils.matches(ifMatch, fileName)) {
				response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				return;
			}

			// If-Unmodified-Since header should be greater than LastModified. If
			// not, then return 412.
			long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
			if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
				response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				return;
			}

			// Validate and process range
			// -------------------------------------------------------------

			// Prepare some variables. The full Range represents the complete file.
			Range full = new Range(0, length - 1, length);
			List<Range> ranges = new ArrayList<>();

			// Validate and process Range and If-Range headers.
			String range = request.getHeader("Range");
			if (range != null) {

				// Range header should match format "bytes=n-n,n-n,n-n...". If not,
				// then return 416.
				if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
					response.setHeader("Content-Range", "bytes */" + length); // Required
																				// in
																				// 416.
					response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					return;
				}

				String ifRange = request.getHeader("If-Range");
				if (ifRange != null && !ifRange.equals(fileName)) {
					try {
						long ifRangeTime = request.getDateHeader("If-Range"); // Throws
																				// IAE
																				// if
																				// invalid.
						if (ifRangeTime != -1) {
							ranges.add(full);
						}
					} catch (IllegalArgumentException ignore) {
						ranges.add(full);
					}
				}

				// If any valid If-Range header, then process each part of byte
				// range.
				if (ranges.isEmpty()) {
					for (String part : range.substring(6).split(",")) {
						// Assuming a file with length of 100, the following
						// examples returns bytes at:
						// 50-80 (50 to 80), 40- (40 to length=100), -20
						// (length-20=80 to length=100).
						long start = Range.sublong(part, 0, part.indexOf("-"));
						long end = Range.sublong(part, part.indexOf("-") + 1, part.length());

						if (start == -1) {
							start = length - end;
							end = length - 1;
						} else if (end == -1 || end > length - 1) {
							end = length - 1;
						}

						// Check if Range is syntactically valid. If not, then
						// return 416.
						if (start > end) {
							response.setHeader("Content-Range", "bytes */" + length); // Required
																						// in
																						// 416.
							response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
							return;
						}

						// Add range.
						ranges.add(new Range(start, end, length));
					}
				}
			}

			// Prepare and initialize response
			// --------------------------------------------------------

			// Get content type by file name and set content disposition.
			String disposition = "inline";

			// If content type is unknown, then set the default value.
			// For all content types, see:
			// http://www.w3schools.com/media/media_mimeref.asp
			// To add new content types, add new mime-mapping entry in web.xml.

			String contentType = null;

			if (filepath.toString().contains(".webm") || filepath.toString().contains(".mkv")) {
				contentType = "video/webm";
			} else if (filepath.toString().contains(".mp4")) {
				contentType = "video/mp4";
			} // else if (contentType.startsWith("image")) {
				// Else, expect for images, determine content disposition. If
				// content type is supported by
				// the browser, then set to inline, else attachment which will pop a
				// 'save as' dialogue.
				// String accept = request.getHeader("Accept");
				// disposition = accept != null && HttpUtils.accepts(accept,
				// contentType) ? "inline" : "attachment";
			else
				contentType = "application/octet-stream";

			logger.debug("Content-Type : {}", contentType);
			// Initialize response.
			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setHeader("Content-Type", contentType);
			response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");
			logger.debug("Content-Disposition : {}", disposition);
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("ETag", fileName);
			response.setDateHeader("Last-Modified", lastModified);
			response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

			// Send requested file (part(s)) to client
			// ------------------------------------------------

			// Prepare streams.
			try (FileInputStream input = new FileInputStream(filepath.toString());
					// FileOutputStream output = new FileOutputStream(filepath.toString())) {

					OutputStream output = response.getOutputStream()) {

				if (ranges.isEmpty() || ranges.get(0) == full) {

					// Return full file.
					logger.info("Return full file");
					response.setContentType(contentType);
					response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
					response.setHeader("Content-Length", String.valueOf(full.length));
					Range.copy(input, output, length, full.start, full.length);

				} else if (ranges.size() == 1) {

					// Return single part of file.
					Range r = ranges.get(0);
					logger.info("Return 1 part of file : from ({}) to ({})", r.start, r.end);
					response.setContentType(contentType);
					response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
					response.setHeader("Content-Length", String.valueOf(r.length));
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

					// Copy single part range.
					Range.copy(input, output, length, r.start, r.length);

				} else {

					// Return multiple parts of file.
					response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

					// Cast back to ServletOutputStream to get the easy println
					// methods.
					ServletOutputStream sos = (ServletOutputStream) output;

					// Copy multi part range.
					for (Range r : ranges) {
						logger.info("Return multi part of file : from ({}) to ({})", r.start, r.end);
						// Add multipart boundary and header fields for every range.
						sos.println();
						sos.println("--" + MULTIPART_BOUNDARY);
						sos.println("Content-Type: " + contentType);
						sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

						// Copy single part range of multi part range.
						Range.copy(input, output, length, r.start, r.length);
					}

					// End with multipart boundary.
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY + "--");
					sos.close();
				}
			}
		} catch (IOException e) {
			throw new FFmpegRuntimeException(FFmpegRuntimeException.EX_IO_EXCEPTION_READ_LINE);
		}
	}

	private static class Range {
		long start;
		long end;
		long length;
		long total;
		// private static final int MAX_SKIP_BUFFER_SIZE = 2048;

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
			/*
			 * PROBAR CON ESTO APRA VER EL RENDIMIENTO
			 * IOUtils.copy(inputStream,response.getOutputStream()); response.flushBuffer();
			 */

			if (inputSize == length) {
				// Write full range.
				System.out.println("Peli Entera");
				while ((read = input.read(buffer)) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				}
			} else {
				System.out.println("Peli a cachos");
				input.skip(start);
				long toRead = length;
				System.out.println("A ver como vamos");
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
		 * Returns true if the given accept header accepts the given value.
		 * 
		 * @param acceptHeader
		 *            The accept header.
		 * @param toAccept
		 *            The value to be accepted.
		 * @return True if the given accept header accepts the given value.
		 */

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