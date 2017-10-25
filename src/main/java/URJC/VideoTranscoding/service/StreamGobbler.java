package URJC.VideoTranscoding.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author luisca
 */
@SuppressWarnings("unused")
public class StreamGobbler extends Thread{
	// TODO JAVADOC
	private static final Logger logger = Logger.getLogger(StreamGobbler.class);
	public static double percentajeConversion;
	private Date dateFinal;
	private DateFormat timeProcessFinal;
	private static int finalT;
	private static long principio;
	private static Calendar cal;
	private static Calendar cal2;
	private static Double finalTime;
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
		try{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			Pattern durationVideoPattern = Pattern.compile("(?<=Duration: )[^,]*");
			Pattern progreesVideoPattern = Pattern.compile("(?<=time=)[\\d:.]*");
			Pattern generalPattern = Pattern.compile(
						".*size= *(\\d+)kB.*time= *(\\d\\d):(\\d\\d):(\\d\\d\\.\\d\\d).*bitrate= *(\\d+\\.\\d)+kbits/s *speed= *(\\d+.\\d+)x.*");
			String line = null;
			while((line = br.readLine()) != null){
				// System.out.println("CommandLineOutput" + "> " + line);
				Matcher progressMatcher = progreesVideoPattern.matcher(line);
				Matcher generalMatcher = generalPattern.matcher(line);
				Matcher durationVideoMatcher = durationVideoPattern.matcher(line);
				while(progressMatcher.find()){
					// System.out.println(timeVariable.group(0));
					double diference = getDifference(finalTime,progressMatcher.group(0));
					System.out.print("Progress conversion: " + String.format("%.2f",diference) + "%");
				}
				while(durationVideoMatcher.find()){
					System.out.println(durationVideoMatcher.group(0));
					finalTime = getDuration(durationVideoMatcher.group(0));
					System.out.println("Duration time Video : " + finalTime + " Secs");
				}
				while(generalMatcher.find()){
					System.out.print(" // File Size: " + generalMatcher.group(1) + "kB");
					System.out.print(" // Speed: " + generalMatcher.group(6) + "x");
					System.out.println(" // Bitrate: " + generalMatcher.group(5) + "kbits/s");
				}
				line = "";
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private double getDifference(Double finalTime2,String timeVariable){
		String matchSplit[] = timeVariable.split(":");
		return ((Integer.parseInt(matchSplit[0]) * 3600 + Integer.parseInt(matchSplit[1]) * 60
					+ Double.parseDouble(matchSplit[2])) / finalTime2) * 100;
	}

	private double getDuration(String group){
		String[] hms = group.split(":");
		return Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60 + Double.parseDouble(hms[2]);
	}

	// TODO DELETE
	private void toDelete(){
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
		Pattern p = Pattern.compile(
					".*size= *(\\d+)kB.*time= *(\\d\\d):(\\d\\d):(\\d\\d\\.\\d\\d).*bitrate= *(\\d+\\.\\d)+kbits/s *speed= *(\\d+.\\d+)x.*");
		String match;
		String speed;
		String bitrate;
		String[] matchSplit;
		while(null != (match = sc.findWithinHorizon(timePattern,0))){
			while(null != (speed = sc.findWithinHorizon(speedPattern,0))){
				Matcher m = p.matcher(sc.nextLine());
				if(!m.matches()){
					System.out.println(sc.nextLine());
					System.out.println("Esperando a que el mongolo de ffmpeg me de una salida que coincida...");
				}else{
					System.out.println("Size: " + m.group(1) + "kB");
					System.out.println("Time (Hours): " + m.group(2));
					System.out.println("Time (Minutes): " + m.group(3));
					System.out.println("Time (Seconds): " + m.group(4));
					System.out.println("Bitrate: " + m.group(5) + "kbits/s");
					System.out.println("Speed: " + m.group(6) + "x");
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
					// if(!generalMather.matches()){
					// System.out.println("Esperando a que el mongolo de ffmpeg me de una salida que coincida...");
					// }else{
					// System.out.println("Size: " + m.group(1) + "kB");
					// System.out.println("Time (Hours): " + m.group(2));
					// System.out.println("Time (Minutes): " + m.group(3));
					// System.out.println("Time (Seconds): " + m.group(4));
					// System.out.println("Bitrate: " + m.group(5) + "kbits/s");
					// System.out.println("Speed: " + m.group(6) + "x");
					// }
				}
			}
			sc.close();
		}
	}
}
