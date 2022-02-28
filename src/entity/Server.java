package entity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) throws Exception {
    	
		int port = 4444;
		ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Server is now active on Port " + port + ".\n\n");
        
        int count = 0;
        
        while(count < 2) {
        	try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new ServerThread(socket).start();
            count++;
        }
        
        serverSocket.close();
        System.out.println("Server is no longer active.");
    }
	
}
