package pruebas;

public class FFmpegNativeHelper {
	public FFmpegNativeHelper() {

	}

	static {
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavcodec.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavformat.dylib"); // hello.dll (Windows) or libhello.so
																			// (Unixes)
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavutil.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavfilter.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavdevice.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libswresample.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libavformat.dylib");
		System.load("/usr/local/Cellar/ffmpeg/3.4/lib/libswscale.dylib");

	}

	public int ffmpegCommand(String comand) {
		if (comand.isEmpty()) {
			return 1;
		}
		String args[] = comand.split(" ");
		for (int i = 0; i < args.length; i++) {
			System.out.println("ffmpeg-jni" + args[i]);
		}
		return ffmpeg_entry(args.length, args);
	}

	public native int ffmpeg_entry(int argc, String[] args);
}