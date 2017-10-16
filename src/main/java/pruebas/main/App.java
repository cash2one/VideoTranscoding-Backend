package pruebas.main;

import java.io.File;

public class App {
	public static void main(String[] Args) {

		File file = new File("");
		Transcoding nuevaTrans = new Transcoding();
		nuevaTrans.sendTranscoding4(file);

	}

}
