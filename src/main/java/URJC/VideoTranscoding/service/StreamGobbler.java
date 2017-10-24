package URJC.VideoTranscoding.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author luisca
 */
@SuppressWarnings("unused")
public class StreamGobbler extends Thread{
	// TOODO JAVADOC
	private static final Logger logger = Logger.getLogger(StreamGobbler.class);
	public static double percentajeConversion;
	private Date dateFinal;
	private DateFormat timeProcessFinal;
	private static String finalTime;
	private static int finalT;
	private static long principio;
	private static Calendar cal;
	private static Calendar cal2;
	InputStream is;
	String type;

	/**
	 * @param is
	 * @param type
	 */
	public StreamGobbler(InputStream is,String type){
		this.is = is;
		this.type = type;
	}

	/**
	 * 
	 */
	@Override
	public void run(){
		Scanner sc = new Scanner(is);
		Pattern durPattern = Pattern.compile("(?<=Duration: )[^,]*");
		String dur = sc.findWithinHorizon(durPattern,0);
		if(dur == null){
			sc.close();
			throw new RuntimeException("Could not parse duration.");
		}
		String[] hms = dur.split(":");
		double totalSecs = Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60 + Double.parseDouble(hms[2]);
		System.out.println("Total duration: " + totalSecs + " seconds.");
		Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
		Pattern speedPattern = Pattern.compile("(?<=speed=)[\\d:. ]*");
		Pattern speedPattern2 = Pattern.compile("(?<=speed=)[ \\d.\\d ]*");
		Pattern bitratePattern = Pattern.compile("(?<=bitrate=)[\\d:. ]*");
		String match;
		String speed;
		String bitrate;
		String[] matchSplit;
		while(null != (match = sc.findWithinHorizon(timePattern,0))){
			while(null != (speed = sc.findWithinHorizon(speedPattern,0))){
				// bitrate = sc.findWithinHorizon(bitratePattern,0);
				// System.out.println("*****" + match + "***");
				System.out.println();
				System.out.println("*****" + speed + "***");
				matchSplit = speed.split("\\.");
				System.out.println("match[0]= " + matchSplit[0] + " match[1]= " + matchSplit[1]);
				double speedF = (Integer.parseInt(matchSplit[0]) + Integer.parseInt(matchSplit[1]));
				// System.out.println(speed);
				matchSplit = match.split(":");
				double progress = (Integer.parseInt(matchSplit[0]) * 3600 + Integer.parseInt(matchSplit[1]) * 60
							+ Double.parseDouble(matchSplit[2])) / totalSecs;
				System.out.printf("Progress: %.2f%% ",progress * 100);
				System.out.print(speedF + "x");
				// System.out.printf("Speed: %%.2f" + speedF);
				// System.out.println(" Bitrate:" + bitrate + " kb/s");
			}
		}
		sc.close();
	}
}
