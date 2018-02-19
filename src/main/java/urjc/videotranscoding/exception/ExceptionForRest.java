package urjc.videotranscoding.exception;

public class ExceptionForRest {
	private int Code;
	private String Message;

	public ExceptionForRest(int code,String message) {
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
