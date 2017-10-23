package URJC.VideoTranscoding.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			String line = null;
			while((line = br.readLine()) != null){
				// System.out.println(line);
				if(line.contains(" Duration:")){
					// System.out.println(line);
					String durationFinal[] = line.split("Duration: ");
					String cutStart[] = durationFinal[1].split(", start");
					DateFormat timeProcessFinal = new SimpleDateFormat("HH:mm:ss.SS");
					finalTime = durationConversionFinal(timeProcessFinal,dateFinal,cutStart[0]);
					System.out.println(finalTime);
				}
				while(line.startsWith("frame")){
					String timeSplit[] = line.split("time=");
					System.out.println(timeSplit[0] + "***********" + timeSplit[1]);
					String bitrateSplit[] = timeSplit[1].split(" bitrate");
					DateFormat timeProcess = new SimpleDateFormat("HH:mm:ss.SS");
					Date date;
					date = timeProcess.parse(bitrateSplit[0]);
					// System.out.println("TiempoProcesado: " + timeProcess.format(date));
					String otroString = timeProcess.format(date);
					System.out.println(Integer.parseInt(finalTime) - Integer.parseInt(otroString));
					line = "";
				}
				// timeProcessFinal = new SimpleDateFormat("HH:mm:ss.SS");
				// dateFinal = timeProcessFinal.parse(cutStart[0]);
				// System.out.println("TIEMPO QUE DURA--:" + timeProcessFinal.format(dateFinal));
				// }
				// while(line.contains("frame")){
				// System.out.println(line);
				// // String timeSplit[] = line.split("time=");
				// // System.out.println(timeSplit[0] + "***********" + timeSplit[1]);
				// // String bitrateSplit[] = timeSplit[1].split(" bitrate");
				// // DateFormat timeProcess = new SimpleDateFormat("HH:mm:ss.SS");
				// // Date date = timeProcess.parse(bitrateSplit[0]);
				// // System.out.println("TiempoProcesodo: " + timeProcess.format(date));
				// System.out.println(line);
				// logger.l7dlog(Level.INFO,line,null);
				// }
				// System.out.println(line);
			}
		}catch(IOException ioe){
			// TODO Exception
			ioe.printStackTrace();
		}catch(ParseException e){
			// TODO Date date=timeProcess...
			e.printStackTrace();
		}
	}

	private String durationConversionFinal(DateFormat timeProcessFinal2,Date dateFinal2,String cutStart){
		try{
			dateFinal2 = timeProcessFinal2.parse(cutStart);
			return timeProcessFinal2.format(dateFinal2);
		}catch(ParseException e){
			e.printStackTrace();
			return null;
		}
	}
}
