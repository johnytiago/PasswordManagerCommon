package envelope;
 
import java.io.Serializable;

public class Message implements Serializable{

  public byte[] password;
  public byte[] usernameHash;
  public byte[] domainHash;
  public byte[] tripletHash;
  public int counter;
  public byte[] publicKey;

  private static final long serialVersionUID = 1L;

  public Message(){};

  public Message(
      byte[] publicKey,
      byte[] usernameHash,
      byte[] domainHash,
      byte[] password,
      byte[] tripletHash,
      int counter) { 

    this.publicKey = publicKey;
    this.domainHash = domainHash;
    this.usernameHash = usernameHash;
    this.password = password;
    this.tripletHash = tripletHash;
    this.counter = counter;
      }

  public void setPassword( byte[] pw ){
    this.password = pw;
  }

  public void setPublicKey( byte[] pk ){
    this.publicKey = pk;
  }

  public void setDomainHash( byte[] dh ){
    this.domainHash = dh;
  }

  public void setUsernameHash( byte[] uh ){
    this.usernameHash = uh;
  }

  public void setTripletHash( byte[] th ){
    this.tripletHash = th;
  }

  public void setCounter( int c ){
    this.counter = c;
  }
}
