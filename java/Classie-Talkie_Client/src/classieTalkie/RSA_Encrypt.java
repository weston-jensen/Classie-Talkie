package classieTalkie;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

public class RSA_Encrypt {
	
	public RSA_Encrypt()
	{
		
	}
	
	public static  byte[] int_to_byteArray_le(int myInteger){
	    return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
	}
	
	public int byteArray_to_int(byte[] bytes) {
		//Big Endian
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public byte[] encryptString(Key publicKey, String input)
	{
		byte[] encrypted = null;
		try {
			byte[] message = input.getBytes(); 
			Cipher cipher;
		
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encrypted = cipher.doFinal(message);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		ByteBuffer bb = ByteBuffer.wrap(encrypted);
		bb.order( ByteOrder.BIG_ENDIAN);
		
		return encrypted;// bb.array();
	}
	
	public byte[] encryptInt(Key publicKey, int input)
	{
		byte[] message = int_to_byteArray_le(input);
		byte[] encrypted = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encrypted = cipher.doFinal(message);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		ByteBuffer bb = ByteBuffer.wrap(encrypted);
		bb.order( ByteOrder.BIG_ENDIAN);
		
		return bb.array();
	}
	
	public String decryptToString(Key publicKey, byte[] input)
	{
		ByteBuffer bb = ByteBuffer.wrap(input);
		bb.order( ByteOrder.BIG_ENDIAN);
		input = bb.array();
		
		byte[] decrypted = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			decrypted = cipher.doFinal(input);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		String s = new String(decrypted);
		return s;
	}
	
	public int decryptToInt(Key publicKey, byte[] input)
	{
		ByteBuffer bb = ByteBuffer.wrap(input);
		bb.order( ByteOrder.BIG_ENDIAN);
		input = bb.array();
		
		byte[] decrypted = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			decrypted = cipher.doFinal(input);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		int i = byteArray_to_int(decrypted);
		return i;
	}
	

}
