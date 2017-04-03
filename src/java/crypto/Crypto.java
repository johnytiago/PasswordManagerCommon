package crypto;

import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyFactory;
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
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import java.text.SimpleDateFormat;
import java.util.Date;

//imports for MAC_ADDRESS generator
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Crypto {

  private static final String PRIVATEKEY = "PrivateKey";
  private static final String PUBLICKEY = "PublicKey";
  private static final String DHPRIVATEKEY = "DHPrivateKey";
  private static final String DHPUBLICKEY = "DHPublicKey";
  private static final String KEYSTORE_TYPE = KeyStore.getDefaultType(); // "JCEKS";
  private static final String KEYPAIRGEN_TYPE = "RSA";
  private static final String RNG_TYPE = "SHA1PRNG";
  private static final String ENCRYPTION_TYPE = "RSA/ECB/PKCS1Padding";
  private static final String HASH_TYPE = "SHA256withRSA";
  private static final String MAC_TYPE = "HmacSHA256";

  private static String _username;
  private static String _password;
  private static KeyStore _keyStore;
  private static KeyPair _keyPair;
  private static KeyPair _DHKeyPair;
  private static SecretKey _secretKey;

  private String getUsername() {
    return _username;
  }

  private String getPassword() {
    return _password;
  }

  public Key getPublicKey() {
    return _keyPair.getPublic();
  }

  public Key getPrivateKey() {
    return _keyPair.getPrivate();
  }

  public SecretKey getSecretKey() {
    return _secretKey;
  }

  public Key getDHPublicKey() {
    return _DHKeyPair.getPublic();
  }

  public Key getDHPrivateKey() {
    return _DHKeyPair.getPrivate();
  }

  private String getKeyStoreDirectory(){
    return "keys/" + getUsername() + ".jce";
  }

  private String getPubKeyDirectory(){
    return "keys/" + getUsername() + ".pubKey";
  }

  private String getPrivateKeyDirectory(){
    return "keys/" + getUsername() + ".privKey";
  }

  public void init(String username, String password){
    _username = username;
    _password = password;
    KeyStore ks = null;

    if (keyStoreExists()){
      ks = loadKeyStore();
      _keyPair = retrieveKeyPair(ks);
      _DHKeyPair = retrieveKeyPairDH();
    } else {
      _keyPair = generateKeyPair();
      _DHKeyPair = generateKeyPairDH();
      ks = createKeyStore( _keyPair);
      storeKeyStore(ks);
      storeKeyPairDH(_DHKeyPair);
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
      return pair;

    } catch ( NoSuchAlgorithmException e){
      e.printStackTrace();
      return null;
    }
  }

  private KeyPair retrieveKeyPair(KeyStore keystore) {
    try {
      Key key = keystore.getKey(PRIVATEKEY, getPassword().toCharArray());

      if (key instanceof PrivateKey) {
        // Get certificate of public key
        Certificate cert = keystore.getCertificate(PRIVATEKEY);
        PublicKey publicKey = cert.getPublicKey();
        return new KeyPair(publicKey, (PrivateKey) key);
      }
      return null;

    } catch ( UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e){
      e.printStackTrace();
      return null;
    }
  }

  public KeyPair generateKeyPairDH() {
    try {

      final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
      keyPairGenerator.initialize(1024);
      final KeyPair pair = keyPairGenerator.generateKeyPair();
      return pair;

    } catch ( NoSuchAlgorithmException e){
      e.printStackTrace();
      return null;
    }
  }

  public void storeKeyPairDH(KeyPair keypair) {
    try {
      byte[] publicKeyBytes = keypair.getPublic().getEncoded();
      byte[] privateKeyBytes = keypair.getPrivate().getEncoded();

      FileOutputStream fos = new FileOutputStream(getPubKeyDirectory());
      fos.write(publicKeyBytes);
      fos.close();

      fos = new FileOutputStream(getPrivateKeyDirectory());
      fos.write(privateKeyBytes);
      fos.close();

    } catch ( IOException e){
      e.printStackTrace();
    }
  }

  private KeyPair retrieveKeyPairDH() {
    try {

      Key pk = null;
      byte[] pubKeyBytes = Files.readAllBytes(new File(getPubKeyDirectory()).toPath());
      byte[] privKeyBytes = Files.readAllBytes(new File(getPrivateKeyDirectory()).toPath());

      KeyFactory kf = KeyFactory.getInstance("DH");
      X509EncodedKeySpec pubspec = new X509EncodedKeySpec(pubKeyBytes);
      PKCS8EncodedKeySpec privspec = new PKCS8EncodedKeySpec(privKeyBytes);
      PublicKey pubKey = kf.generatePublic(pubspec);
      PrivateKey privKey = kf.generatePrivate(privspec);
      return new KeyPair(pubKey, privKey);

    } catch ( IOException | InvalidKeySpecException | NoSuchAlgorithmException e){
      e.printStackTrace();
      return null;
    }
  }

  private KeyStore createKeyStore(KeyPair keyPair){
    KeyStore keyStore = null;
    try {
      keyStore = KeyStore.getInstance( KEYSTORE_TYPE );  
    } catch ( KeyStoreException e) {
      e.printStackTrace();
    }

    try {
      keyStore.load(null, getPassword().toCharArray());
    } catch ( NoSuchAlgorithmException | CertificateException | IOException e) {
      e.printStackTrace();
    }

    X509Certificate[] certificate = null;
    try {
      certificate = GenCert.generateCertificate(keyPair);  
    } catch ( Exception e) {
      e.printStackTrace();
    }

    Certificate[] certChain = new Certificate[1];  
    certChain[0] = certificate[0];  
    try {
      keyStore.setKeyEntry( PRIVATEKEY, (Key)keyPair.getPrivate(), getPassword().toCharArray(), certChain);  
    } catch ( KeyStoreException e) {
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

  public SecretKey generateDH( Key privateKey, Key publicKey){
    try {
      KeyAgreement ka = KeyAgreement.getInstance("DH");
      ka.init( privateKey );
      ka.doPhase( publicKey, true );
      byte[] key = ka.generateSecret(); 

      SecretKey secretKey = new SecretKeySpec(key,"DH");
      _secretKey = secretKey;
      return secretKey;

    } catch ( NoSuchAlgorithmException | InvalidKeyException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] genMac(byte[] data, SecretKey secretKey){
    try { 

        Mac mac = Mac.getInstance( MAC_TYPE );
        mac.init( secretKey );
        return mac.doFinal( data );

    } catch ( NoSuchAlgorithmException | IllegalStateException | InvalidKeyException e) {
      e.printStackTrace();
      return null;
    }
  }
  // timestamp
	  public static String getMacAddress() throws UnknownHostException,
	  SocketException{
		  
		InetAddress ipAddress = InetAddress.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface
		      .getByInetAddress(ipAddress);
		byte[] macAddressBytes = networkInterface.getHardwareAddress();
		StringBuilder macAddressBuilder = new StringBuilder();
		
		for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++)
		{
		  String macAddressHexByte = String.format("%02X",
		          macAddressBytes[macAddressByteIndex]);
		  macAddressBuilder.append(macAddressHexByte);
		
		  if (macAddressByteIndex != macAddressBytes.length - 1)
		  {
		      macAddressBuilder.append(":");
		  }
		}
	
	return macAddressBuilder.toString();
}
}
