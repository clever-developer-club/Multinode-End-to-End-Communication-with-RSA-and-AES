package rsa;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
	
	BigInteger p,q,n,phi;
	RSAKey publicKey,privateKey;
	int bitLength;
	
	public RSA(int bitLength){
		this.bitLength = bitLength;
	}
	
	public void initialize() {
		Random r = new Random();
		this.p = BigInteger.probablePrime(bitLength,r);
		this.q = BigInteger.probablePrime(bitLength,r);
		this.n = p.multiply(q);
		this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		this.publicKey = new RSAKey(n,this.calculateExp(),true);
		this.privateKey = new RSAKey(n,this.calculatePrivateKey(this.publicKey.getExponent()),false);
	}
	
	public RSAKey getPublicKey(){
		return this.publicKey;
	}
	
	public void setPublicKey(RSAKey publicKey){
		this.publicKey = publicKey;
	}

	public RSAKey getPrivateKey(){
		return this.privateKey;
	}
	
	public void setPrivateKey(RSAKey privateKey){
		this.privateKey = privateKey;
	}
	
	public BigInteger calculateExp() {
		Random r = new Random();
		BigInteger e = BigInteger.probablePrime(bitLength/2,r);
		while(this.phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(this.phi) < 0) {
			e.add(BigInteger.ONE);
		}
		return e;
	}
	
	public BigInteger calculatePrivateKey(BigInteger e) {
		return e.modInverse(this.phi);
	}
	
	public byte[] encrypt(String message) {
		return this.publicKey.process(message.getBytes());
	}
	
	public String decrypt(byte[] message) {
		return new String(this.privateKey.process(message));
	}	
}
