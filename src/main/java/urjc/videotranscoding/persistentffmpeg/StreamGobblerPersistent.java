package urjc.videotranscoding.persistentffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.repository.ConversionVideoRepository;

/**
 * @author luisca
 */


public class StreamGobblerPersistent implements Runnable {
	private final Logger logger = LogManager
			.getLogger(StreamGobblerPersistent.class);
	private static Double finalTime;
	private final String GENERAL_PATTERN = ".*size= *(\\d+)kB.*time= *(\\d\\d):(\\d\\d):(\\d\\d\\.\\d\\d).*bitrate= *(\\d+\\.\\d)+kbits/s *speed= *(\\d+.\\d+)x.*";
	private final String PROGRESS_VIDEO_PATTERN = "(?<=time=)[\\d:.]*";
	private final String DURATION_VIDEO_PATTERN = "(?<=Duration: )[^,]*";
	private final InputStream is;
	private volatile String progress;
	private volatile String duration;
	private volatile String fileSize;
	private volatile String speed;
	private volatile String bitrate;
	private final String type;
	private final ConversionVideo conversionVideo;
	
	private final ConversionVideoRepository conversionVideoRepository;	

	public StreamGobblerPersistent(InputStream is, String type,
			ConversionVideo conversionVideo,
			ConversionVideoRepository conversionVideoRepository) {
		this.is = is;
		this.type = type;
		this.conversionVideo = conversionVideo;
		this.conversionVideoRepository = conversionVideoRepository;
	}




	public String getType() {
		return type;
	}
	/**
	 * 
	 */
	@Override
	public void run() {
		// TODO Try resource-->
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		// https://stackoverflow.com/questions/17739362/java7-try-with-resources-statement-advantage
		try {
			logger.info("Se va a convertir un video");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			Pattern durationVideoPattern = Pattern
					.compile(DURATION_VIDEO_PATTERN);
			Pattern progreesVideoPattern = Pattern
					.compile(PROGRESS_VIDEO_PATTERN);
			Pattern generalPattern = Pattern.compile(GENERAL_PATTERN);
			String line = null;
			while ((line = br.readLine()) != null) {
				Matcher progressMatcher = progreesVideoPattern.matcher(line);
				Matcher generalMatcher = generalPattern.matcher(line);
				Matcher durationVideoMatcher = durationVideoPattern
						.matcher(line);
				while (progressMatcher.find()) {
					double diference = getDifference(finalTime,
							progressMatcher.group(0));
					// System.out.print("Progress conversion: " +
					// String.format("%.2f",diference) + "%");
					setProgress(String.format("%.2f", diference));
					conversionVideo.setProgress(String.format("%.2f", diference));
				}
				while (durationVideoMatcher.find()) {
					// System.out.println(durationVideoMatcher.group(0));
					finalTime = getDuration(durationVideoMatcher.group(0));
					// System.out.println("Duration time Video : " + finalTime +
					// " Secs");
					setDuration(String.valueOf(finalTime));
				}
				while (generalMatcher.find()) {
					setFileSize(generalMatcher.group(1));
					setSpeed(generalMatcher.group(6));
					setBitrate(generalMatcher.group(5));
					// System.out.print(" // File Size: " +
					// generalMatcher.group(1) + "kB");
					// System.out.print(" // Speed: " + generalMatcher.group(6)
					// + "x");
					// System.out.println(" // Bitrate: " +
					// generalMatcher.group(5) + "kbits/s");
				}
				conversionVideoRepository.save(conversionVideo);

			}
			conversionVideo.setProgress("100");
			conversionVideo.setFinished(true);
			conversionVideoRepository.save(conversionVideo);
			logger.info("Se ha terminado de convertir el video");

		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("IO Exception", e);
		}
	}

	/**
	 * @param finalTime2
	 * @param timeVariable
	 * @return
	 */
	private double getDifference(Double finalTime2, String timeVariable) {
		String matchSplit[] = timeVariable.split(":");
		return ((Integer.parseInt(matchSplit[0]) * 3600
				+ Integer.parseInt(matchSplit[1]) * 60
				+ Double.parseDouble(matchSplit[2])) / finalTime2) * 100;
	}

	/**
	 * @param group
	 * @return
	 */
	private double getDuration(String group) {
		String[] hms = group.split(":");
		return Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60
				+ Double.parseDouble(hms[2]);
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

	public ConversionVideo getConversionVideo() {
		return conversionVideo;
	}

	
	
}
