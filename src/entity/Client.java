package entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import aes.AESWrapper;
import rsa.RSAKey;
import rsa.RSAWrapper;

public class Client {

	public static void main(String[] args) throws Exception {
		
		Socket socket = new Socket("localhost", 4444);
		Scanner sc =  new Scanner(System.in);
		
		System.out.print("Enter Client ID: ");
		String id = sc.nextLine();

		System.out.print("Enter Message: ");
		String message = sc.nextLine();
		
		sc.close();
		
		System.out.println("\nConnected to the server.\n");
		
		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("Generating new RSA Keys.......");
		RSAWrapper rsa = new RSAWrapper();	
		System.out.println("Generated RSA Keys.\n");
		
		System.out.println("Validating client.......");
		Status status = validateClient(os,is,id);
		System.out.println(status.getMessage() + "\n");
		
		if(status.getCode() != 200) {
			socket.close();
			return;
		}

		System.out.println("Recieved Encrypted AES Keys.");
		System.out.println("Decrypting AES Key with Client Private Key.......");
		String aesKey = getAESKey(os,is,rsa);
		
		System.out.println("Decrypted AES Key.\n");
		AESWrapper aes = new AESWrapper();
		
		System.out.println("Original PlainText ==> " + message + "\n");
		
		System.out.println("Encrypting message with decrypted AES Key.........");
		Message encryptedMessage = new Message(aes.encrypt(aesKey,message));
		System.out.println("Encrypted Cipher Text ==> " + encryptedMessage.getText() + "\n");
		
		System.out.println("Sent Encrypted Cipher Text.");
		os.writeObject(encryptedMessage);
		
		socket.close();	
		
		System.out.println("Connection closed.\n\n\n");
	}	
	
	private static Status validateClient(ObjectOutputStream os,ObjectInputStream is,String id) throws Exception {
		RSAWrapper rsa = new RSAWrapper();
		RSAKey publicKey = (RSAKey)is.readObject();

		Message encryptedMessage = new Message(rsa.encrypt(publicKey,id));
		
		os.writeObject(encryptedMessage);
		
		Status status = (Status)is.readObject();
		return status;
	}
	
	private static String getAESKey(ObjectOutputStream os,ObjectInputStream is,RSAWrapper rsa) throws Exception {
	
		os.writeObject(rsa.getPublicKey());
		
		Message encryptedMessage = (Message)(is.readObject());
	      
	    String decryptedMessage = rsa.decrypt(encryptedMessage.getValue());
		
		return decryptedMessage;
	}
}
