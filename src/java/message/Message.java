package message;

import java.security.Key;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

public class Message {

  public Envelope envelope;
  public byte[] hmac;

  public Message(byte[] envelope){
    this.envelope = (Envelope) SerializationUtils.deserialize(envelope);
  }

  public Message(
      Key publicKey,
      byte[] domain,
      byte[] username,
      byte[] password,
      byte[] usernameHash,
      byte[] domainHash,
      int counter) { 

    this.envelope = new Envelope(publicKey, domain, username, password, usernameHash, domainHash, counter);
  }

  public byte[] serialize(){
    return SerializationUtils.serialize(this.envelope);
  }

  public void setHMAC( byte[] hmac ) {
    this.hmac = hmac;
  }

  public class Envelope implements Serializable{

    public byte[] domain;
    public byte[] username;
    public byte[] password;
    public byte[] usernameHash;
    public byte[] domainHash;
    public int counter;
    public byte[] publicKey;
    
    private static final long serialVersionUID = 1L;
    
    public Envelope(
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
