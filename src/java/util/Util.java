package util;

import java.io.ByteArrayOutputStream;

import envelope.Message;

public class Util {
  public byte[] msgToByteArray(Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      outputStream.write( msg.publicKey );
      outputStream.write( msg.usernameHash );
      outputStream.write( msg.password );
      outputStream.write( msg.tripletHash );
      outputStream.write( msg.counter );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
