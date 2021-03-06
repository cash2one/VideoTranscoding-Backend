package es.urjc.videotranscoding.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.exception.FFmpegRuntimeException;
import es.urjc.videotranscoding.repository.ConversionRepository;

/**
 * @author luisca
 */

public class StreamGobbler implements Runnable {
	private static final String TRACE_STARTING_CONVERSION = "ffmpeg.conversion.start";
	private static final String TRACE_FINISH_CONVERSION = "ffmpeg.conversion.end";
	private static final String TRACE_IO_EXCEPTION_READ_LINE = "ffmpeg.io.exception.readLine";
	private static Double finalTime;
	private final String GENERAL_PATTERN = ".*size= *(\\d+)kB.*time= *(\\d\\d):(\\d\\d):(\\d\\d\\.\\d\\d).*bitrate= *(\\d+\\.\\d)+kbits\\/s.*speed=*(\\d+.\\d+)x.*";
	private final String PROGRESS_VIDEO_PATTERN = "(?<=time=)[\\d:.]*";
	private final String DURATION_VIDEO_PATTERN = "(?<=Duration: )[^,]*";
	private final InputStream is;
	private volatile String progress;
	private volatile String duration;
	private volatile String fileSize;
	private volatile String speed;
	private volatile String bitrate;
	private final String type;
	private final Conversion conversion;
	private final ConversionRepository conversionRepository;
	private final Logger logger;

	/**
	 * Constructor by the factory
	 * 
	 * @param is
	 *            the type of inputstream
	 * @param type
	 *            of conversion
	 * @param conversion
	 *            the conversion
	 * @param conversionRepository
	 *            to save the video on real time
	 * @param logger
	 *            the logger for logs started.
	 */
	public StreamGobbler(InputStream is, String type, Conversion conversion, ConversionRepository conversionRepository,
			Logger logger) {
		this.is = is;
		this.type = type;
		this.conversion = conversion;
		this.conversionRepository = conversionRepository;
		this.logger = logger;
	}

	/**
	 * 
	 * @return the type of StreamGobbler
	 */
	public String getType() {
		return type;
	}

	/**
	 * The run of the command from VideoTranscodingImpl
	 */
	@Override
	public void run() {
		try {
			logger.l7dlog(Level.INFO, TRACE_STARTING_CONVERSION, new String[] { conversion.getName() }, null);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			Pattern durationVideoPattern = Pattern.compile(DURATION_VIDEO_PATTERN);
			Pattern progreesVideoPattern = Pattern.compile(PROGRESS_VIDEO_PATTERN);
			Pattern generalPattern = Pattern.compile(GENERAL_PATTERN);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.l7dlog(Level.TRACE, line, null);
				Matcher progressMatcher = progreesVideoPattern.matcher(line);
				Matcher generalMatcher = generalPattern.matcher(line);
				Matcher durationVideoMatcher = durationVideoPattern.matcher(line);
				while (progressMatcher.find()) {
					double diference = getDifference(finalTime, progressMatcher.group(0));
					setProgress(String.format("%.2f", diference));
					conversion.setProgress(String.format("%.2f", diference));
				}
				while (durationVideoMatcher.find()) {
					finalTime = getDuration(durationVideoMatcher.group(0));
					setDuration(String.valueOf(finalTime));
				}
				while (generalMatcher.find()) {
					setFileSize(generalMatcher.group(1));
					setSpeed(generalMatcher.group(6));
					setBitrate(generalMatcher.group(5));
					conversion.setFileSize(generalMatcher.group(1) + " KB");
				}
				conversionRepository.save(conversion);
			}
			conversion.setProgress("100");
			conversion.setFinished(true);
			conversion.setActive(false);
			conversionRepository.save(conversion);
			logger.l7dlog(Level.INFO, TRACE_FINISH_CONVERSION, new String[] { conversion.getName() }, null);
		} catch (IOException e) {
			logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_READ_LINE, null);
			throw new FFmpegRuntimeException(FFmpegRuntimeException.EX_IO_EXCEPTION_READ_LINE);
		}
	}

	/**
	 * Return the time that fault of the video to finish them.
	 * 
	 * @param finalTime2
	 * @param timeVariable
	 * @return
	 */
	private double getDifference(Double finalTime2, String timeVariable) {
		String matchSplit[] = timeVariable.split(":");
		try {
			return ((Integer.parseInt(matchSplit[0]) * 3600 + Integer.parseInt(matchSplit[1]) * 60
					+ Double.parseDouble(matchSplit[2])) / finalTime2) * 100;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * The duration of the video
	 * 
	 * @param group
	 * @return
	 */
	private double getDuration(String group) {
		String[] hms = group.split(":");
		return Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60 + Double.parseDouble(hms[2]);
	}

	public String getProgress() {
		return progress;
	}

	private void setProgress(String progress) {
		this.progress = progress;
	}

	public String getDuration() {
		return duration;
	}

	private void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFileSize() {
		return fileSize;
	}

	private void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getSpeed() {
		return speed;
	}

	private void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getBitrate() {
		return bitrate;
	}

	private void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public Conversion getConversionVideo() {
		return conversion;
	}

}
