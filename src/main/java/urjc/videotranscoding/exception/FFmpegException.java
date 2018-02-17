package urjc.videotranscoding.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import urjc.proc.videotranscoding.ApplicationContextProvider;
import urjc.videotranscoding.wrapper.FfmpegResourceBundle;

/**
 * @author luisca
 */
public class FFmpegException extends FFmpegUtilException {

	private Object[] parametros;
	public static final String FICH_ERRORES = "fichero.mensajes.errores";
	public static final String CLASSPATH = "classpath*:xml/exception-config.xml";
	public static final String PROP_FICH_CORE = "propertiesFicheroCore";
	public static final String TRAZAS = "ffmpegResourceBundle";
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
	 *            Par�metros para componer el mensaje de error.
	 * @param cause
	 *            Excepci�n por la que se lanza esta excepci�n (causa).
	 */
	public FFmpegException(int code, Object[] params, Throwable cause) {
		super(code, cause);
		parametros = params;
		detailMessage = getLocalizedMessage();
	}

	/**
	 * Debido a que esta clase no se invoca como recurso desde las otras clases, si
	 * no que siempre que se quiere lanzar una excepci�n se crea un objeto nuevo, no
	 * se puede recoger por inyecci�n ning�n par�metro. Es por ello que para recoger
	 * los mensajes de los errores se carga un xml espec�fico
	 * (xml/excepcion-config.xml) donde se han a�adido los recursos necesarios que
	 * contienen los mensajes (propertiesFichero y excepcionMessageSource).
	 * 
	 * @return Mensaje internacionalizado correspondiente al c�digo de error de la
	 *         excepci�n.
	 */
	@Override
	public String getLocalizedMessage() {
		Properties propertiesFicheroCore = (Properties) ApplicationContextProvider.getApplicationContext()
				.getBean("propertiesFicheroCore");
		FfmpegResourceBundle ffmpegResourceBundle = (FfmpegResourceBundle) ApplicationContextProvider
				.getApplicationContext().getBean("ffmpegResourceBundle");
		ResourceBundle messages = ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_ERRORES), Locale.getDefault());
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(Locale.getDefault());
		formatter.applyPattern(messages.getString("error." + getCodigo()));
		return formatter.format(parametros);
	}

}
