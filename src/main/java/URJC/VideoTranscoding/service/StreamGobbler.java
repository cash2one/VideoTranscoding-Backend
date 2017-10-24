package URJC.VideoTranscoding.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author luisca
 */
@SuppressWarnings("unused")
public class StreamGobbler extends Thread {
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
	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		// try {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		Scanner sc = new Scanner(is);

		Pattern durPattern = Pattern.compile("(?<=Duration: )[^,]*");
		String dur = sc.findWithinHorizon(durPattern, 0);
		if (dur == null) {
			sc.close();

			throw new RuntimeException("Could not parse duration.");
		}
		String[] hms = dur.split(":");
		double totalSecs = Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60 + Double.parseDouble(hms[2]);
		System.out.println("Total duration: " + totalSecs + " seconds.");

		// Find time as long as possible.
		Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
		String match;
		String[] matchSplit;

		while (null != (match = sc.findWithinHorizon(timePattern, 0))) {
			matchSplit = match.split(":");
			double progress = Integer.parseInt(matchSplit[0]) * 3600 + Integer.parseInt(matchSplit[1]) * 60
					+ Double.parseDouble(matchSplit[2]) / totalSecs;
			System.out.printf("Progress: %.2f%%%n", progress * 100);
		}
		sc.close();

	}

	// if (line.contains(" Duration:")) {
	// // System.out.println(line);
	// String durationFinal[] = line.split("Duration: ");
	// String cutStart[] = durationFinal[1].split(", start");
	// DateFormat timeProcessFinal = new SimpleDateFormat("HH:mm:ss.SS");
	// System.out.println(durationFinal[1]);
	// dateFinal = timeProcessFinal.parse(cutStart[0]);
	// cal = Calendar.getInstance();
	// cal.setTime(dateFinal);
	// // finalT = durationConversionFinal(timeProcessFinal, dateFinal,
	// cutStart[0]);
	// System.out.println("****" + finalT);
	// }
	// while (line.startsWith("frame")) {
	// String timeSplit[] = line.split("time=");
	// System.out.println(timeSplit[0] + "***********" + timeSplit[1]);
	// String bitrateSplit[] = timeSplit[1].split(" bitrate");
	//
	// DateFormat timeProcess = new SimpleDateFormat("HH:mm:ss.SS");
	// Date date;
	//
	// if (contador == 0) {
	// principio = timeProcess.parse(bitrateSplit[0]).getTime();
	// contador++;
	// }
	//
	// long x = timeProcess.parse(bitrateSplit[0]).getTime();
	// System.out.println(bitrateSplit[0]);
	//
	// // System.out.println("TiempoProcesado: " + timeProcess.format(date));
	// // String otroString = timeProcess.format(date);
	// // System.out.println(x, principio, y);
	// System.out.println("El final:" + finalT);
	// System.out.println("El principio:" + -principio);
	// System.out.println("El variante:" + -x);
	// System.out.println((x) + (finalT - x));
	// line = "";
	// }

	// } catch (IOException ioe) {
	// // TODO Exception
	// ioe.printStackTrace();
	// } catch (ParseException e) {
	// // TODO Date date=timeProcess...
	// e.printStackTrace();
	// }


}
