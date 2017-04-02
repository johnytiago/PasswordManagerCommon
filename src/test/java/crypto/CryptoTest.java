package crypto;

import static org.junit.Assert.*;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.Test;

public class CryptoTest {

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
  public void testMac() {
    Crypto client = new Crypto();
    Crypto server = new Crypto();
    client.init("client", "password");
    server.init("server", "password");
    String message = "This is an envelope message";

    SecretKey secretKeyCli = client.generateDH(client.getDHPrivateKey(), server.getDHPublicKey());
    byte[] mac = client.genMac( message.getBytes(), secretKeyCli );
    byte[] clientMsgEncrypted = client.encrypt( message.getBytes(), server.getPublicKey() );
    // message and mac gets sent to server

    SecretKey secretKeySrv = server.generateDH(server.getDHPrivateKey(), client.getDHPublicKey());
    byte[] clientMsgDecyphered = server.decrypt( clientMsgEncrypted, server.getPrivateKey() );
    byte[] macVerification = server.genMac( clientMsgDecyphered, secretKeySrv );

    assertArrayEquals( mac , macVerification );
  }

  // TODO: Add more tests
}
