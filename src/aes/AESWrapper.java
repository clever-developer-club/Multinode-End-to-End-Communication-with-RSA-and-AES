package aes;

public class AESWrapper {

	AES aes;
	int blockSize = 16;
	
	public AESWrapper() throws Exception{
		this.aes = new AES();
		aes.initialize(Constants.KEY_SIZE);
	}
	
	public AESWrapper(int length) throws Exception{
		this.aes = new AES();
		aes.initialize(length);
	}
	
	public String encrypt(String key,String message) throws Exception {
		this.aes.setKey(key);
		while(message.length()%16 != 0) {
			message += 'Z'; 
		}
		String encryptedText = "";
		for(int i=0;i<message.length();i=i+this.blockSize) {
			encryptedText += this.aes.encrypt(Util.toHex(message.substring(i,i+this.blockSize)));
		}
		return encryptedText;
	}
	
	public String decrypt(String message) {
		String decryptedText = "";
		for(int i=0;i<message.length();i=i+this.blockSize) {
			decryptedText += this.aes.decrypt(Util.toHex(message.substring(i,i+this.blockSize)));
		}
		return decryptedText;
	}
	
	public String getKey() {
		return this.aes.getKey();
	}
}
