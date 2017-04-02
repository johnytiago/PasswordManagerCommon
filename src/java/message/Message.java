package message;

import java.security.Key;

public class Message {

  public byte[] domain;
  public byte[] username;
  public byte[] password;
  public byte[] usernameHash;
  public byte[] domainHash;
  public int counter;
  public byte[] publicKey;

  public void message( Key publicKey,
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
