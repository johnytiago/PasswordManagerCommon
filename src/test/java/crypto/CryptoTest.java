package crypto;

import static org.junit.Assert.*;
import java.security.Key;

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

  // TODO: Add more tests
}
