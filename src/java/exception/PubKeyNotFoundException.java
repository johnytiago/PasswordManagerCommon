package exception;
import message.PasswordManagerMessages;

public class PubKeyNotFoundException extends PasswordManagerExceptionHandler{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1909944234780451268L;

	public PubKeyNotFoundException() {
		super(PasswordManagerMessages.PUBKEY_NOT_FOUND);
	}

}
