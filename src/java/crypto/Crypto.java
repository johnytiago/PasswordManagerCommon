package crypto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Crypto {

  private static final String PRIVATEKEY = "PrivateKey";
  private static final String PUBLICKEY = "PublicKey";
  private static final String KEYSTORE_TYPE = KeyStore.getDefaultType(); // "JCEKS";
  private static final String KEYPAIRGEN_TYPE = "RSA";
  private static final String RNG_TYPE = "SHA1PRNG";
  private static final String ENCRYPTION_TYPE = "RSA/ECB/PKCS1Padding";
  private static final String HASH_TYPE = "SHA256withRSA";

  private static String _username;
  private static String _password;
  private static KeyStore _keyStore;
  private static KeyPair _keyPair;

  private String getUsername() {
    return _username;
  }

  private String getPassword() {
    return _password;
  }

  public Key getPublicKey() {
    //try {
    //if (_keyStore != null)
      //return _keyStore.getKey(PUBLICKEY, getPassword().toCharArray());
    
    //loadKeyStore();
    //return _keyStore.getKey(PUBLICKEY, getPassword().toCharArray());
    //} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e){
      //e.printStackTrace();
      //return null;
    //}
    return _keyPair.getPublic();
  }

  public Key getPrivateKey() {
    //try {
    //if (_keyStore != null)
      //return _keyStore.getKey(PRIVATEKEY, getPassword().toCharArray());
    
    //loadKeyStore();
    //return _keyStore.getKey(PRIVATEKEY, getPassword().toCharArray());
    //} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e){
      //e.printStackTrace();
      //return null;
    //}
    return _keyPair.getPrivate();
  }

  private String getKeyStoreDirectory(){
    return getUsername() + ".jce";
  }

  public void init(String username, String password){
    _username = username;
    _password = password;
    KeyStore ks = null;

    if (keyStoreExists()){
      ks = loadKeyStore();
      _keyPair = retrieveKeyPair(ks);
    } else {
      _keyPair = generateKeyPair();
      ks = createKeyStore(_keyPair);
      storeKeyStore(ks);
    }
  }

  private boolean keyStoreExists(){
    return new File(getKeyStoreDirectory()).isFile();
  }

  private KeyPair generateKeyPair() {
    try {

      KeyPairGenerator keyGen = KeyPairGenerator.getInstance( KEYPAIRGEN_TYPE );
      SecureRandom random = SecureRandom.getInstance( RNG_TYPE );
      keyGen.initialize(1024, random);
      KeyPair pair = keyGen.generateKeyPair();
      Key pubKey = pair.getPublic();
      Key privKey = pair.getPrivate();
      
      return pair;

    } catch ( NoSuchAlgorithmException e){
      e.printStackTrace();
      return null;
    }
  }

  private KeyPair retrieveKeyPair(KeyStore keystore) {
      KeyPair kp = null;
    try {
      Key key = keystore.getKey(PRIVATEKEY, getPassword().toCharArray());

      if (key instanceof PrivateKey) {
        // Get certificate of public key
        Certificate cert = keystore.getCertificate(PRIVATEKEY);

        // Get public key
        PublicKey publicKey = cert.getPublicKey();

        // Return a key pair
        kp = new KeyPair(publicKey, (PrivateKey) key);
        return kp;
      }
      return null;

    } catch ( UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e){
      e.printStackTrace();
      return null;
    }
  }

  private KeyStore createKeyStore(KeyPair keyPair){
    KeyStore keyStore = null;
    try {
      keyStore = KeyStore.getInstance( KEYSTORE_TYPE );  
    } catch ( KeyStoreException e) {
      System.out.println("1");
      e.printStackTrace();
    }

    try {
      keyStore.load(null, getPassword().toCharArray());
    } catch ( NoSuchAlgorithmException | CertificateException | IOException e) {
      System.out.println("2");
      e.printStackTrace();
    }

    X509Certificate[] certificate = null;
    try {
      certificate = GenCert.generateCertificate(keyPair);  
    } catch ( Exception e) {
      System.out.println("3");
      e.printStackTrace();
    }

    Certificate[] certChain = new Certificate[1];  
    certChain[0] = certificate[0];  
    try {
      keyStore.setKeyEntry( PRIVATEKEY, (Key)keyPair.getPrivate(), getPassword().toCharArray(), certChain);  
    } catch ( KeyStoreException e) {
      System.out.println("4");
      e.printStackTrace();
    }
    _keyStore = keyStore;

    return keyStore;
  }

  private void storeKeyStore(KeyStore ks) {
    try {

      FileOutputStream fos = new FileOutputStream(getKeyStoreDirectory());
      ks.store(fos, getPassword().toCharArray());
      fos.close();   

    } catch ( IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
      e.printStackTrace();
    }
  }

  private KeyStore loadKeyStore() {
    try {

      FileInputStream fis = new FileInputStream(getKeyStoreDirectory());
      KeyStore ks = KeyStore.getInstance( KEYSTORE_TYPE );
      ks.load(fis, getPassword().toCharArray());
      fis.close();  
      _keyStore = ks;
      return ks;

    } catch ( IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] encrypt(byte[] data, Key key) {
    try {

      Cipher cipher = Cipher.getInstance(ENCRYPTION_TYPE);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(data);

    } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
      return null;

    } catch ( NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] decrypt(byte[] ciphertext, Key key){
    try {

      Cipher cipher = Cipher.getInstance(ENCRYPTION_TYPE);
      cipher.init(Cipher.DECRYPT_MODE, key);
      return cipher.doFinal(ciphertext);

    } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
      return null;

    } catch ( NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] genSign(byte[] data, PrivateKey privateKey){
    try {

      Signature signature = Signature.getInstance(HASH_TYPE);
      signature.initSign(privateKey);
      signature.update(data);
      return signature.sign();

    } catch ( NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
      e.printStackTrace();
      return null;
    }
  }

  public boolean verSign(byte[] data, PublicKey publicKey, byte[] signature){
    try {

      Signature verify = Signature.getInstance(HASH_TYPE);
      verify.initVerify(publicKey);
      verify.update(data);
      return verify.verify(signature);

    } catch ( NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
      // TODO: Should return maybe exception?
      e.printStackTrace();
      return false;
    }
  }

  public byte[] generateDH(){
    try {

      KeyAgreement ka = KeyAgreement.getInstance("DH");
      ka.init( null );
      ka.doPhase( null, true );
      return ka.generateSecret(); 

    } catch ( NoSuchAlgorithmException | InvalidKeyException e) {
      e.printStackTrace();
      return null;
    }
  }

  public SecretKey generateSessionKey(){
    try {

      KeyGenerator keygen = KeyGenerator.getInstance("AES");
      keygen.init(128);
      return keygen.generateKey();

    } catch ( NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  // timestamp
  // nonce
}
