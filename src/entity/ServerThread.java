package entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import aes.AESWrapper;
import rsa.RSAKey;
import rsa.RSAWrapper;

public class ServerThread extends Thread{
	
	protected Socket socket;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
    	
		try {
			
			System.out.println("A new client is trying to connect.....\n");
			
			ObjectOutputStream os = new ObjectOutputStream(this.socket.getOutputStream());
			ObjectInputStream is = new ObjectInputStream(this.socket.getInputStream());

			System.out.println("Generating new RSA Keys.......");
			RSAWrapper rsa = new RSAWrapper();	
			System.out.println("Generated RSA Keys.\n");

			System.out.println("Validating client.......");
			if(!validateClient(os,is,rsa)) {
				System.out.println("Validation failed. Invalid Client Id.\n\n\n");
				return;
			}
			
			System.out.println("Generating new AES Keys.......");
			AESWrapper aes = new AESWrapper();
			System.out.println("Generated AES Keys.\n");
			
			System.out.println("Encrypting AES Keys with Client Public Key.......");
			sendAESKey(os,is,rsa,aes.getKey());
			System.out.println("Sent Encrypted AES Keys.\n");
		
			Message encryptedMessage = (Message)(is.readObject());
			System.out.println("Recieved Cipher Text ==> " + encryptedMessage.getText() + "\n");
			System.out.println("Decrypting recieved cipher text.......\n");
	        String decryptedMessage = aes.decrypt(encryptedMessage.getText());
	        
	        System.out.println("Obtained PlainText ==> " + decryptedMessage + "\n");
			
	        System.out.println("Client has now disconnected.\n\n\n");
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
	
    public boolean validateClient(ObjectOutputStream os,ObjectInputStream is,RSAWrapper rsa) throws Exception {
    	
        os.writeObject(rsa.getPublicKey());
  
        Message encryptedMessage = (Message)(is.readObject());
      
        String decryptedMessage = rsa.decrypt(encryptedMessage.getValue());
        
        int id = Integer.parseInt(decryptedMessage,10); 
        
        if(id < 1 || id > 10) {
        	os.writeObject(new Status(400,"Connection refused. Invalid Client Id."));
        	return false;
        }else {
        	os.writeObject(new Status(200,"Client Authenticated."));
        }
        
        System.out.println("Client with ID = " + id + " connected.\n");
    	return true;
    }
    
    public void sendAESKey(ObjectOutputStream os,ObjectInputStream is, RSAWrapper rsa, String aesKey){
    	
    	try {
			
			RSAKey clientPublicKey = (RSAKey)(is.readObject());
			
			Message encryptedKey = new Message(rsa.encrypt(clientPublicKey, aesKey));
	    
			os.writeObject(encryptedKey);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
}
