package exception;

public class SecurityVerificationException extends PasswordManagerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4190326892510022832L;

	public SecurityVerificationException(String message) {
		super("Security verifications failed.");
		// TODO Auto-generated constructor stub
	}

}
