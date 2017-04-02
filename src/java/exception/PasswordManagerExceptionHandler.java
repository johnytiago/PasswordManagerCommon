package exception;

public abstract class PasswordManagerExceptionHandler extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1682411887700477976L;
	
	private String _message;
	
	public PasswordManagerExceptionHandler(String message){
		_message = message;
	}
	
	public String getMessage(){
		return _message;
	}
}
