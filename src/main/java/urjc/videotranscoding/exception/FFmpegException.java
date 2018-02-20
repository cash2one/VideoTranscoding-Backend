package urjc.videotranscoding.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import urjc.videotranscoding.utils.ApplicationContextProvider;
import urjc.videotranscoding.wrapper.FfmpegResourceBundle;

/**
 * @author luisca
 */
public class FFmpegException extends FFmpegUtilException {
	// TODO rehacer javadoc
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
	public static final int EX_IO_EXCEPTION_BY_EXEC=15007;

	/**
	 * Default builder
	 */
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

	/**
	 * Constructor con mensaje y causa.
	 * 
	 * @param code
	 *            int c�digo del mensaje de la excepci�n
	 * @param cause
	 *            Throwable Excepci�n que se ha generado
	 */
	public FFmpegException(int code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructor con mensaje y causa.
	 * 
	 * @param code
	 *            int c�digo del mensaje de la excepci�n
	 * @param mensaje
	 *            String Mensaje adicional.
	 * @param cause
	 *            Throwable Excepci�n que se ha generado
	 */
	public FFmpegException(int code, String mensaje, Throwable cause) {
		super(code, mensaje, cause);
	}

	/**
	 * Constructor con mensaje.
	 * 
	 * @param code
	 *            int c�digo del mensaje de la excepci�n
	 */
	public FFmpegException(int code) {
		super(code);
	}

	/**
	 * Constructor con mensaje y causa.
	 * 
	 * @param code
	 *            int c�digo del mensaje de la excepci�n
	 * @param mensaje
	 *            String Mensaje adicional.
	 */
	public FFmpegException(int code, String mensaje) {
		super(code, mensaje);
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            C�digo del mensaje de la excepcion
	 * @param params
	 *            Par�metros para componer el mensaje de error.
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
	 *            C�digo del mensaje de la excepcion
	 * @param params
	 *            Parametros para componer el mensaje de error.
	 * @param cause
	 *            Excepcion por la que se lanza esta excepci�n (causa).
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
