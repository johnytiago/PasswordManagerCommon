package util;

import java.io.ByteArrayOutputStream;

import envelope.Message;

public class Util {
  public byte[] msgToByteArray(Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      if ( msg.getPublicKey() != null)
        outputStream.write( msg.getPublicKey() );
      if ( msg.getDomainHash() != null)
        outputStream.write( msg.getDomainHash() );
      if ( msg.getUsernameHash() != null)
        outputStream.write( msg.getUsernameHash() );
      if ( msg.getPassword() != null)
        outputStream.write( msg.getPassword() );
      if ( msg.getTripletHash() != null)
        outputStream.write( msg.getTripletHash() );
      if ( msg.getSignature() != null)
        outputStream.write( msg.getSignature() );
      outputStream.write( msg.getWts() );
      outputStream.write( msg.getRid() );
      outputStream.write( msg.getCounter() );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] singnableByteArray(Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      if ( msg.getPassword() != null)
        outputStream.write( msg.getPassword() );
      if ( msg.getTripletHash() != null)
        outputStream.write( msg.getTripletHash() );
      outputStream.write( msg.getWts() );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
