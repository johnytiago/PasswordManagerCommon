package crypto;

import envelope.*;
import util.*;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.Test;

public class CryptoTest {

  private Util util = new Util();

  @Test
  public void encryptionPubPriTest() {
    Crypto crypto = new Crypto();
    crypto.init("username", "password");
    String clearText = "This is cleartext";

    byte[] encryptedText = crypto.encrypt(clearText.getBytes(), crypto.getPublicKey());
    byte[] result = crypto.decrypt(encryptedText, crypto.getPrivateKey());
    assertArrayEquals(clearText.getBytes(), result);
  }

  @Test
  public void encryptionPriPubTest() {
    Crypto crypto = new Crypto();
    crypto.init("username", "password");
    String clearText = "This is cleartext";

    byte[] encryptedText = crypto.encrypt(clearText.getBytes(), crypto.getPrivateKey());
    byte[] result = crypto.decrypt(encryptedText, crypto.getPublicKey());
    assertArrayEquals(clearText.getBytes(), result);
  }

  @Test
  public void testSignature() {
    Crypto client = new Crypto();
    Crypto server = new Crypto();
    client.init("client", "password");
    server.init("server", "password");
    String message = "This is cleartext";

    byte[] sign = client.genSign( message.getBytes(), (PrivateKey)client.getPrivateKey() );
    byte[] clientMsgEncrypted = client.encrypt( message.getBytes(), server.getPublicKey() );
    byte[] clientMsgDecyphered = server.decrypt( clientMsgEncrypted, server.getPrivateKey() );
    boolean verSign = server.verSign( clientMsgDecyphered, (PublicKey)client.getPublicKey(), sign );
    assertTrue( verSign );
  }

  @Test
  public void testSalt(){
    Crypto client = new Crypto();
    Crypto client2 = new Crypto();
    client.init("client", "password");
    client2.init("client", "password");
    byte[] salt = client.getSalt();
    byte[] salt2 = client2.getSalt();
    assertArrayEquals(salt,salt2);
  }

  @Test
  public void testHMAC() {
    Crypto client = new Crypto();
    Crypto server = new Crypto();
    client.init("client", "password");
    server.init("server", "password");

    Envelope env = new Envelope(
      "publicKey".getBytes(),
      "domainHash".getBytes(),
      "usernameHash".getBytes(),
      "password".getBytes(),
      client.signTriplet("domainHash".getBytes(), "usernameHash".getBytes(), "password".getBytes(), (PrivateKey)client.getPrivateKey()),
      219831293);

    SecretKey secretKeyCli = client.generateDH(client.getDHPrivateKey(), server.getDHPublicKey());
    env.setHMAC( client.genMac( util.msgToByteArray(env.getMessage()), secretKeyCli) );

    // message and mac gets sent to server

    SecretKey secretKeySrv = server.generateDH(server.getDHPrivateKey(), client.getDHPublicKey());
    byte[] macCalculated = server.genMac( util.msgToByteArray(env.getMessage()) , secretKeySrv);
    assertArrayEquals( env.getHMAC() , macCalculated );
  }

  // TODO: Add more tests
}
