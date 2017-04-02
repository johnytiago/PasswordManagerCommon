package exception;
import message.PasswordManagerMessages;

public class CredentialsNotFoundException extends PasswordManagerExceptionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5496687374220263254L;

	public CredentialsNotFoundException() {
		super(PasswordManagerMessages.PASSWORD_NOT_FOUND);
	}

}
