package es.urjc.videotranscoding.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import es.urjc.videotranscoding.utils.ApplicationContextProvider;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

/**
 * @author luisca
 * @Since 0.5
 */
public class FFmpegException extends FFmpegUtilException {
	private static final long serialVersionUID = 5434866761753051706L;
	private Object[] parametros;
	private static final String FICH_ERRORES = "fichero.mensajes.errores";
	private static final String PROP_FICH_CORE = "propertiesFicheroCore";
	private static final String FFMPEG_RESOURCE_BUNDLE = "ffmpegResourceBundle";
	private static final String ERROR = "error.";
	/**
	 * EXCEPTIONS
	 */
	public static final int EX_FFMPEG_EMPTY_OR_NULL = 15000;
	public static final int EX_FFMPEG_NOT_FOUND = 15001;
	public static final int EX_FOLDER_OUTPUT_EMPTY_OR_NULL = 15002;
	public static final int EX_FOLDER_OUTPUT_NOT_EXITS = 15003;
	public static final int EX_ORIGINAL_VIDEO_NULL = 15004;
	public static final int EX_ORIGINAL_VIDEO_NOT_IS_SAVE = 15005;
	public static final int EX_EXECUTION_EXCEPTION = 15006;
	public static final int EX_IO_EXCEPTION_BY_EXEC = 15007;
	public static final int EX_NO_CONVERSION_TYPE_FOUND=15008;

	/**
	 * Default constructor
	 */
	public FFmpegException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message for the exception
	 */
	public FFmpegException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message for the exception
	 * @param throwable
	 *            with the cause of the exception
	 * 
	 */
	public FFmpegException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            Throwable Cause of the exception
	 */
	public FFmpegException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message for the exception
	 * @param cause
	 *            of the exception
	 */
	public FFmpegException(int code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of the message for the exception
	 * @param message
	 *            message for the exception
	 * @param cause
	 *            of the exception
	 */
	public FFmpegException(int code, String mensaje, Throwable cause) {
		super(code, mensaje, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of the message for the exception
	 */
	public FFmpegException(int code) {
		super(code);
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of the message for the exception
	 * @param message
	 *            message for the exception
	 */
	public FFmpegException(int code, String mensaje) {
		super(code, mensaje);
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            code for the exception
	 * @param params
	 *            for build the message error
	 */
	public FFmpegException(int code, Object[] params) {
		super(code);
		parametros = params;
		detailMessage = getLocalizedMessage();
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            code for the exception
	 * @param params
	 *            for build the message error
	 * @param cause of the exception
	 */
	public FFmpegException(int code, Object[] params, Throwable cause) {
		super(code, cause);
		parametros = params;
		detailMessage = getLocalizedMessage();
	}

	@Override
	public String getLocalizedMessage() {
		Properties propertiesFicheroCore = (Properties) ApplicationContextProvider.getApplicationContext()
				.getBean(PROP_FICH_CORE);
		FfmpegResourceBundle ffmpegResourceBundle = (FfmpegResourceBundle) ApplicationContextProvider
				.getApplicationContext().getBean(FFMPEG_RESOURCE_BUNDLE);
		ResourceBundle messages = ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_ERRORES), Locale.getDefault());
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(Locale.getDefault());
		formatter.applyPattern(messages.getString(ERROR + getCodigo()));
		return formatter.format(parametros);
	}

}
