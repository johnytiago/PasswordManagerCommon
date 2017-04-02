package exception;

import message.PasswordManagerMessages;

public class PubKeyAlreadyExistsException extends PasswordManagerExceptionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9034748457743928571L;

	public PubKeyAlreadyExistsException() {
		super(PasswordManagerMessages.PUBKEY_ALREADY_EXISTS);
	}
	

}
