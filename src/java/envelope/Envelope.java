package envelope;

import org.apache.commons.lang3.SerializationUtils;
import java.security.Key;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "envelope")
public class Envelope {

  @XmlElement(name = "message", required = true)
  private envelope.Message _msg;
  @XmlElement(name = "HMAC", required = true)
  private byte[] hmac;
  @XmlElement(name = "DHPublicKey", required = true)
  private byte[] _DHKey;

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

  public byte[] getDHPublicKey(){
    return this._DHKey;
  }

  public void setDHPublicKey( Key dhkey){
    this._DHKey = dhkey.getEncoded();
  }
}
