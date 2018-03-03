package es.urjc.videotranscoding.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

public class FFmpegUtilRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	protected String detailMessage = null;
	private int codigo = -1;
	protected static Hashtable<Integer, String> msgErrors = new Hashtable<Integer, String>();
	public static final int ERROR_UNKOWN = 0;
	private static final String FJEX_NO_MSG = "Not message found";


	/**
	 * Default constructor
	 */
	public FFmpegUtilRuntimeException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message for the exception
	 * @param cause
	 *            of the exception
	 */
	public FFmpegUtilRuntimeException(String message, Throwable cause) {
		super(message, cause);
		detailMessage = message;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of the message for the exception
	 * @param cause
	 *            of the exception
	 */
	public FFmpegUtilRuntimeException(int code, Throwable cause) {
		super(cause);
		detailMessage = getMessage(code);
		codigo = code;
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
	public FFmpegUtilRuntimeException(int code, String mensaje, Throwable cause) {
		super(cause);
		detailMessage = getMessage(code) + " " + mensaje;
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message for the exception
	 */
	public FFmpegUtilRuntimeException(String message) {
		super(message);
		detailMessage = message;
	}

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of the message for the exception
	 */
	public FFmpegUtilRuntimeException(int code) {
		detailMessage = getMessage(code);
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            of the exception
	 * @param message
	 *            message for the exception
	 */
	public FFmpegUtilRuntimeException(int code, String mensaje) {
		super(mensaje);
		detailMessage = getMessage(code) + " " + mensaje;
		codigo = code;
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            Throwable Cause of the exception
	 */
	public FFmpegUtilRuntimeException(Throwable cause) {
		super(cause);
		detailMessage = (cause == null ? null : cause.toString());
	}

	/**
	 * 
	 * @return String message for the exception
	 */
	public String getMessage() {
		if (codigo != -1) {
			return "FFMPEG-" + codigo + " : " + detailMessage;
		} else {
			return detailMessage;
		}
	}

	/**
	 * Return the mesage of the code error.
	 * 
	 * @param codError
	 *            error code.
	 * @return String message of the code.
	 */
	private String getMessage(int codError) {
		if (FFmpegUtilException.msgErrors.containsKey(codError)) {
			return (String) FFmpegUtilException.msgErrors.get(codError);
		} else {
			return FJEX_NO_MSG;
		}
	}

	/**
	 * Return the printstackstrace of the exception
	 * 
	 * @return String printStackTraceStr
	 */
	public String printStackTraceStr() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.printStackTrace(new PrintWriter(baos));
		return new String(baos.toByteArray());
	}

	/**
	 * Return the printstackstrace cause of the exception
	 * 
	 * @return printStackTraceCauseStr
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
	 * Return the code of the exception;
	 * 
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}

}
