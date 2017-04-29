package util;

import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.crypto.SecretKey;

public class SecretKeyStore {
	
	private HashMap<ByteBuffer,SecretKey > sercretKeys = new HashMap<ByteBuffer, SecretKey>();
	
	public SecretKey get(byte[] pubKey) {
		return sercretKeys.get(ByteBuffer.wrap(pubKey));
	}
	
	public void put(byte[] pubKey, SecretKey secretKey) {
		sercretKeys.putIfAbsent(ByteBuffer.wrap(pubKey), secretKey);
	}
}
