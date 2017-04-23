package exception;

import message.PasswordManagerMessages;

public class UserAlreadyOnDomainException extends PasswordManagerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5280616003925208636L;

	public UserAlreadyOnDomainException() {
		super(PasswordManagerMessages.USER_ALREADY_EXISTS_DOMAIN);
	}

}
