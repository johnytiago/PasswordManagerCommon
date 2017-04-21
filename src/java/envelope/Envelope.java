package envelope;

import org.apache.commons.lang3.SerializationUtils;

public class Envelope {

  private envelope.Message _msg;
  private byte[] hmac;

  public Envelope(){}

  public Envelope(
      byte[] publicKey,
      byte[] domainHash,
      byte[] usernameHash,
      byte[] password,
      byte[] tripletHash,
      int counter) { 

    this._msg = new Message(publicKey, usernameHash, domainHash, password, tripletHash, counter);
  }

  public Envelope(byte[] message){
    this._msg = (Message) SerializationUtils.deserialize(message);
  }

  public byte[] serialize(){
    return SerializationUtils.serialize(this._msg);
  }

  public void setHMAC( byte[] hmac ) {
    this.hmac = hmac;
  }

  public void setMessage( Message msg ){
    this._msg = msg;
  }

  public byte[] getHMAC() {
    return this.hmac;
  }

  public Message getMessage(){
    return this._msg;
  }
}
