package envelope;

import org.apache.commons.lang3.SerializationUtils;

public class Envelope {

  public Message message;
  public byte[] hmac;

  public Envelope(){}

  public Envelope(
      byte[] publicKey,
      byte[] domainHash,
      byte[] usernameHash,
      byte[] password,
      byte[] tripletHash,
      int counter) { 

    this.message = new Message(publicKey, usernameHash, domainHash, password, tripletHash, counter);
  }

  public Envelope(byte[] message){
    this.message = (Message) SerializationUtils.deserialize(message);
  }

  public byte[] serialize(){
    return SerializationUtils.serialize(this.message);
  }

  public void setHMAC( byte[] hmac ) {
    this.hmac = hmac;
  }

  public void setMessage( Message msg ){
    this.message = msg;
  }
}
