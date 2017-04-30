package util;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class CounterStore {
	
	private HashMap<ByteBuffer, Integer > counters = new HashMap<ByteBuffer, Integer>();
	
	public int get(byte[] pubKey) {
		return counters.get(ByteBuffer.wrap(pubKey)).intValue();
	}
	
	public void put(byte[] pubKey, int counter) {
		counters.putIfAbsent(ByteBuffer.wrap(pubKey), Integer.valueOf(counter));
	}
	
	public boolean exists(byte[] pubKey){
		return counters.containsKey(ByteBuffer.wrap(pubKey));
	}
}
