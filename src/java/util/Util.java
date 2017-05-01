package util;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import envelope.Message;

public class Util {
  public byte[] msgToByteArray(Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      if ( msg.publicKey != null)
        outputStream.write( msg.publicKey );
      if ( msg.usernameHash != null)
        outputStream.write( msg.usernameHash );
      if ( msg.password != null)
        outputStream.write( msg.password );
      if ( msg.tripletHash != null)
        outputStream.write( msg.tripletHash );
      outputStream.write( msg.counter );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
