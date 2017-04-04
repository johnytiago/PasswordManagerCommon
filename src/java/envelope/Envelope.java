package envelope;

import java.io.Serializable;
import java.security.Key;

import org.apache.commons.lang3.SerializationUtils;

public class Envelope {

  public Message message;
  public byte[] hmac;

  public Envelope(byte[] message){
    this.message = (Message) SerializationUtils.deserialize(message);
  }

  public Envelope(
      byte[] publicKey,
      byte[] domainHash,
      byte[] usernameHash,
      byte[] password,
      byte[] tripletHash,
      int counter) { 

    this.message = new Message(publicKey, usernameHash, domainHash, password, tripletHash, counter);
  }

  public byte[] serialize(){
    return SerializationUtils.serialize(this.message);
  }

  public void setHMAC( byte[] hmac ) {
    this.hmac = hmac;
  }

  public static class Message implements Serializable{

    public byte[] password;
    public byte[] usernameHash;
    public byte[] domainHash;
    public byte[] tripletHash;
    public int counter;
    public byte[] publicKey;
    
    private static final long serialVersionUID = 1L;
    
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
  }
}
