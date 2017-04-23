package exception;

public abstract class PasswordManagerException extends Exception{

	private static final long serialVersionUID = -1682411887700477976L;
	
	private String _message;
	
	public PasswordManagerException(String message){
		_message = message;
	}
	
	public String getMessage(){
		return _message;
	}
}
