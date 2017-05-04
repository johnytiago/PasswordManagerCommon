package util;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class PublicKeyStore {
	
	private HashMap<ByteBuffer, byte[] > publicKeys = new HashMap<ByteBuffer, byte[]>();
	
	public byte[] get(String server) {
		return publicKeys.get(ByteBuffer.wrap(server.getBytes()));
	}
	
	public void put(String server, byte[] pubKey) {
		publicKeys.putIfAbsent(ByteBuffer.wrap(server.getBytes()), pubKey);
	}
}
