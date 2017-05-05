package exception;

public class FailedToGetQuorumException extends PasswordManagerException{

  /**
   * 
   */
  private static final long serialVersionUID = -4190392524210022832L;

  public FailedToGetQuorumException() {
    super("Could not acquire a quorum.");
    // TODO Auto-generated constructor stub
  }

}
