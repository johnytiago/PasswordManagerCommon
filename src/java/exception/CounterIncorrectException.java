package exception;

import message.PasswordManagerMessages;

public class CounterIncorrectException extends PasswordManagerExceptionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5496654774220263254L;
	
	public  CounterIncorrectException(){
		super(PasswordManagerMessages.COUNTER_INCORRECT_EXCEPTION);
	}
}
