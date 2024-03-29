package envelope;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "message")
public class Message implements Serializable{

  @XmlElement(name = "password")
    public byte[] password;
  @XmlElement(name = "usernameHash")
    public byte[] usernameHash;
  @XmlElement(name = "domainHash")
    public byte[] domainHash;
  @XmlElement(name = "tripletHash")
    public byte[] tripletHash;
  @XmlElement(name = "counter", required = true)
    public int counter;
  @XmlElement(name = "publicKey")
    public byte[] publicKey;
  @XmlElement(name = "wts")
    public int wts;
  @XmlElement(name = "rid")
    public int rid;
  @XmlElement(name = "Signature")
  private byte[] _signature;


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

  public byte[] getPassword(){
    return this.password;
  }

  public void setPublicKey( byte[] pk ){
    this.publicKey = pk;
  }

  public byte[] getPublicKey(){
    return this.publicKey;
  }

  public void setDomainHash( byte[] dh ){
    this.domainHash = dh;
  }

  public byte[] getDomainHash(){
    return this.domainHash;
  }

  public void setUsernameHash( byte[] uh ){
    this.usernameHash = uh;
  }

  public byte[] getUsernameHash(){
    return this.usernameHash;
  }

  public void setTripletHash( byte[] th ){
    this.tripletHash = th;
  }
  public byte[] getTripletHash(){
    return this.tripletHash;
  }

  public void setCounter( int c ){
    this.counter = c;
  }

  public int getCounter(){
    return this.counter;
  }

  public void setWts( int wts ){
    this.wts = wts;
  }

  public int getWts(){
    return this.wts;
  }

  public void setRid( int rid ){
    this.rid = rid;
  }

  public int getRid(){
    return this.rid;
  }

  public byte[] getSignature(){
    return this._signature;
  }

  public void setSignature( byte[] sign){
    this._signature = sign;
  }
}
