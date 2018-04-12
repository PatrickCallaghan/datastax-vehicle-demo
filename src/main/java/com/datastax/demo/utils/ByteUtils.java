package com.datastax.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ByteUtils {

	public static ByteBuffer toByteBuffer(Object obj) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
			return ByteBuffer.wrap(baos.toByteArray());
		}
	}

	public static Object fromByteBuffer(ByteBuffer bytes) throws Exception {
		if ((bytes == null) || !bytes.hasRemaining()) {
			return null;
		}
		int l = bytes.remaining();
		Object obj = null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes.array(), bytes.arrayOffset() + bytes.position(), l);
		    ObjectInputStream ois = new ObjectInputStream(bais)) {
			obj = ois.readObject();
			bytes.position(bytes.position() + (l - ois.available()));
		}
		return obj;
	}
	
	public static byte[] toBytes(Object obj) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
			return baos.toByteArray();
		}
	}
	
	public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        		ObjectInputStream ois = new ObjectInputStream(bis)) {
            obj = ois.readObject();
        }
        return obj;
    }
}
