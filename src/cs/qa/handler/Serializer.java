package cs.qa.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Serializer {
	
    public static Object fromString(String str){
    	try{
	        byte [] data = new BASE64Decoder().decodeBuffer(str);
	        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
	        Object obj  = ois.readObject();
	        ois.close();
	        return obj;
    	}catch(Exception e){
    		System.out.println("An unexpected error occurred");
    		return null;
    	}
    }

    public static String toString(Serializable obj){
    	try{
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream( baos );
	        oos.writeObject(obj);
	        oos.close();
	        return new String(new BASE64Encoder().encode(baos.toByteArray()));
		}catch(Exception e){
			System.out.println("An unexpected error occurred");
			return null;
		}
    }

}
