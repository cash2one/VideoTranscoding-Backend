package pruebas;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class pruebas {
	private static final String ffmpegMac = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg";
	private static final String uriInput = "/Users/luisca/Documents/TFG17-18/VideosPrueba/Star.mp4";
	private static final String uriOutput = "/Users/luisca/Documents/TFG17-18/VideosPrueba/";

	public static void main(String[] args) throws InterruptedException, IOException {
		Trans x= new Trans();
		Trans2 x2= new Trans2();

	//	x.tran(ffmpegMac, uriInput, uriOutput);
		x2.transcode(ffmpegMac, uriInput, uriOutput);
	}



}
