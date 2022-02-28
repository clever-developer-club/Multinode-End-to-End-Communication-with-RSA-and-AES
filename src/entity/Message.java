package entity;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	byte[] msg;
	String text;
	
	Message(byte[] msg){
		this.msg = msg;
	}
	
	Message(String msg){
		this.text = msg;
	}
	
	public byte[] getValue() {
		return this.msg;
	}
	
	public String getText() {
		return this.text;
	}
}
