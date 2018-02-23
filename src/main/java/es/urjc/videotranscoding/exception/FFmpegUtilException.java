package es.urjc.videotranscoding.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

public class FFmpegUtilException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	protected String detailMessage = null;
	private int codigo = -1;
	protected static Hashtable<Integer, String> msgErrores = new Hashtable<Integer, String>();
	public static final int ERROR_DESCONOCIDO = 0;
	private static final String FJEX_NO_MSG = "Código de Error desconocido.";
	public static final int EX_ERROR_RECUPERACION_PARAMETRO = 1;
	public static final int EX_ERROR_RECUPERACION_PARAMETRO_NULO = 2;
	public static final int EX_ERROR_RECUPERACION_PARAMETRO_DISTINTA_CLASE = 3;
	public static final int ERROR_CONEXION_DATASOURCE = 4;
	public static final int ERROR_CONEXION_JNDI = 5;
	public static final int ERROR_EJECUCION = 6;

	/**
	 * Constructor por defecto.
	 */
	public FFmpegUtilException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            mensaje
	 * @param cause
	 *            causa de la excepcion
	 */
	public FFmpegUtilException(String message, Throwable cause) {
		super(message, cause);
		detailMessage = message;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            Código del mensaje de la excepcion
	 * @param cause
	 *            causa de la excepcion
	 */
	public FFmpegUtilException(int code, Throwable cause) {
		super(cause);
		detailMessage = getMessage(code);
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            Código del mensaje de la excepcion
	 * @param mensaje
	 *            Texto a concatenar al mensaje del código.
	 * @param cause
	 *            causa de la excepcion
	 */
	public FFmpegUtilException(int code, String mensaje, Throwable cause) {
		super(cause);
		detailMessage = getMessage(code) + " " + mensaje;
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Mensaje de la excepcion
	 */
	public FFmpegUtilException(String message) {
		super(message);
		detailMessage = message;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            Código del mensaje de la excepcion
	 */
	public FFmpegUtilException(int code) {
		detailMessage = getMessage(code);
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            Código del mensaje de la excepcion
	 * @param mensaje
	 *            Texto a concatenar al mensaje del código.
	 */
	public FFmpegUtilException(int code, String mensaje) {
		super(mensaje);
		detailMessage = getMessage(code) + " " + mensaje;
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            Throwable Causa de la excepcion
	 */
	public FFmpegUtilException(Throwable cause) {
		super(cause);
		detailMessage = (cause == null ? null : cause.toString());
	}

	/**
	 * Sobreescribe el método para devolver el mensaje personalizado.
	 * 
	 * @return String Mensaje de la excepción
	 */
	public String getMessage() {
		if (codigo != -1) {
			return "FFMPEG-" + codigo + " : " + detailMessage;
		} else {
			return detailMessage;
		}
	}

	/**
	 * Devuelve el mensaje correspondiente al codigo de error dado.
	 * 
	 * @param codError
	 *            int Código de error
	 * @return String Mensaje correspondiente al código.
	 */
	private String getMessage(int codError) {
		Integer cod = new Integer(codError);
		if (FFmpegUtilException.msgErrores.containsKey(cod)) {
			return (String) FFmpegUtilException.msgErrores.get(cod);
		} else {
			return FJEX_NO_MSG;
		}
	}

	/**
	 * Devuelve la pila de la excepción como una cadena.
	 * 
	 * @return String Pila de la excepción
	 */
	public String printStackTraceStr() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.printStackTrace(new PrintWriter(baos));
		return new String(baos.toByteArray());
	}

	/**
	 * Devuelve la pila de la causa de la excepción como una cadena. En caso de no
	 * existir causa, devolverá la cadena '<< Exception Cause not available >>'
	 * 
	 * @return String Pila de la excepción o '<< Exception Cause not available >>'
	 */
	public String printStackTraceCauseStr() {
		if (this.getCause() != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			this.getCause().printStackTrace(new PrintWriter(baos));
			return new String(baos.toByteArray());
		} else {
			return "<< Exception Cause not available >>";
		}
	}

	/**
	 * Devuelve el código de la excepción. Si la excepción fue creada sin código
	 * este valdrá -1.
	 * 
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}
}
