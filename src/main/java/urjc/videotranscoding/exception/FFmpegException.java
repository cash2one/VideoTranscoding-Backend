package urjc.videotranscoding.exception;

/**
 * @author luisca
 */
public class FFmpegException extends Exception {
	private static final long serialVersionUID = 5434866761753051706L;
	public static final String EX_FILE_INPUT_NOT_VALID = "File input for transcode is null or blank";
	public static final String EX_FFMPEG_NOT_FOUND = "FFmpeg not found on this device.";
	public static final String EX_FOLDER_OUTPUT_NOT_FOUND = "Folder output for files not found ";
	public static final String EX_FOLDER_OUTPUT_NULL = "Folder output for files is null";
	public static final String EX_NO_CONVERSION_TYPE_FOUND = "No type conversion found for this call";
	public static final String EX_CONVERSION_TYPE_EMPTY = "Conversion type is empty";
	public static final int EX_CONVERSION_TYPE_EMPT = 15000;

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
