package es.urjc.videotranscoding.exception;

/**
 * This class is for construct a ResponseBody for the rest Controller in case
 * throw exception.
 * 
 * @author luisca
 * @Since 0.5
 */
public class ExceptionForRest {
	private int Code;
	private String Message;

	/**
	 * Constructor
	 * 
	 * @param code
	 *            of exception
	 * @param message
	 *            de exception
	 */
	public ExceptionForRest(int code, String message) {
		this.Message = message;
		this.Code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		this.Message = message;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		this.Code = code;
	}

}
