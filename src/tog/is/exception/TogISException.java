package tog.is.exception;

public class TogISException extends RuntimeException {
	static final long serialVersionUID = 123L;
	public TogISException (String message){
		super(message);		
	}
}
