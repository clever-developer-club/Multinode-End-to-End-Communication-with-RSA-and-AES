package rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
	
	public static String toHexString(byte[] hash){
		String hex = new BigInteger(1,hash).toString(16);
		
		while(hex.length() < 32) {
			hex = "0" + hex;
		}
		
		return hex;
	}
	
	public static byte[] getHashString(byte[] input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(input);
		return toHexString(hash).getBytes();
	}
	
	public static boolean compareHash(byte[] input,byte[] hash) throws NoSuchAlgorithmException {
		String genHash = new String(getHashString(input));
		String originalHash = new String(hash);
		System.out.println("Recieved Hash Value : " + originalHash);
		System.out.println("Generated Hash Value : " + genHash + "\n");
		return genHash.equals(originalHash);
	}
	
}
