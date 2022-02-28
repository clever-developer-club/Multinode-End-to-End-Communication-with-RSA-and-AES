package rsa;

public class RSAWrapper {

	RSA rsa;
	
	public RSAWrapper(){
		this.rsa = new RSA(Constants.KEY_SIZE);
		rsa.initialize();
	}
	
	public RSAWrapper(int length){
		this.rsa = new RSA(length);
		rsa.initialize();
	}
	
	public RSAKey getPublicKey() {
		return this.rsa.getPublicKey();
	}
	
	public byte[] encrypt(RSAKey publicKey,String message) {
		this.rsa.setPublicKey(publicKey);
		return this.rsa.encrypt(message);
	}
	
	public String decrypt(byte[] message) {
		return this.rsa.decrypt(message);
	}
}
