package rsa;

import java.io.Serializable;
import java.math.BigInteger;

public class RSAKey implements Serializable{

	private static final long serialVersionUID = 1L;
	BigInteger n;
	BigInteger e;
	boolean isPublic;
	
	RSAKey(BigInteger n, BigInteger e,boolean isPublic){
		this.n = n;
		this.e = e;
		this.isPublic = isPublic;
	}
	
	public BigInteger getExponent() {
		return this.e;
	}
	
	public BigInteger getBase() {
		return this.n;
	}
	
	public byte[] process(byte[] message) {
		byte[] text = new BigInteger(message).modPow(this.e, this.n).toByteArray();
		return text;
	}
}
