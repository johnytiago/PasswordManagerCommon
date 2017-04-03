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
      Key publicKey,
      byte[] domain,
      byte[] username,
      byte[] password,
      byte[] usernameHash,
      byte[] domainHash,
      int counter) { 

    this.message = new Message(publicKey, domain, username, password, usernameHash, domainHash, counter);
  }

  public byte[] serialize(){
    return SerializationUtils.serialize(this.message);
  }

  public void setHMAC( byte[] hmac ) {
    this.hmac = hmac;
  }

  public static class Message implements Serializable{

    public byte[] domain;
    public byte[] username;
    public byte[] password;
    public byte[] usernameHash;
    public byte[] domainHash;
    public int counter;
    public byte[] publicKey;
    
    private static final long serialVersionUID = 1L;
    
    public Message(
        Key publicKey,
        byte[] domain,
        byte[] username,
        byte[] password,
        byte[] usernameHash,
        byte[] domainHash,
        int counter) { 

      this.publicKey = publicKey.getEncoded();
      this.domain = domain;
      this.username = username;
      this.password = password;
      this.usernameHash = usernameHash;
      this.domainHash = domainHash;
      this.counter = counter;
        }
  }
}
