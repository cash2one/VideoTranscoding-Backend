package URJC.VideoTranscoding.exception;

public class FFmpegException extends Exception {

	private static final long serialVersionUID = 5434866761753051706L;
	public static final String EX_FILE_INPUT_NOT_VALID = "File input for transcode is null or blank";
	public static final String EX_FFMPEG_NOT_FOUND = "FFmpeg not found on this device.";


	public FFmpegException() {
	}

	public FFmpegException(String message) {
		super(message);
	}

	public FFmpegException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FFmpegException(Throwable throwable) {
		super(throwable);

	}

}
