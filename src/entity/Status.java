package entity;

import java.io.Serializable;

public class Status implements Serializable{

	static final long serialVersionUID = 1L;
	int code;
	String msg;
	
	public Status(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
		
	public String getMessage() {
		return this.msg;
	}
	
	public int getCode() {
		return this.code;
	}
}
