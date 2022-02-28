package aes;

import java.util.ArrayList;
import java.util.List;

public class AES {

	SBox substitutionBox;
	SBox invSubstitutionBox;
	KeyGenerator keyGen;
	List<List<String>> state;
	int totalRounds;
	int keyLength = 4;
	String key;
	
	AES() throws Exception{
		this.substitutionBox = new SBox(Constants.sboxTable);
		this.invSubstitutionBox = new SBox(Constants.invSboxTable);
	}

	public String generateKey(int length) {
		String charString = "";
		String key = "";
		
		for(int i=32;i<127;i++) {
			charString += (char)(i);
		}
		
		for(int i=0;i<length/8;i++) {
			int index = (int)Math.floor(Math.random()*key.length());
			key += charString.charAt(index);
		}
		
		return key;
	}
	
	public void initialize(int length) throws Exception{
		this.key = this.generateKey(length);
		this.setKey(this.key);
	}
	
	public void setKey(String key) throws Exception{
		this.totalRounds = getRoundCount(key.length());
		if(this.totalRounds == -1) {
			throw new Exception("Invalid Key Length");
		}
		this.keyGen = new KeyGenerator(Util.toHex(key),this.totalRounds,this.keyLength);
	}
	
	public String getKey(){
		return this.key;
	}
	
	public int getRoundCount(int len) {
		if(len == 16) {
			return 10;
		}else if(len == 24){
			return 12;
		}else if(len == 32) {
			return 14;
		}
		return -1;
	}
	
	public void initialize(String message,boolean encrypt) {
		this.keyGen.reset();
		this.state = Util.toKeyArray(message);
		List<List<String>> roundKey;
		if(encrypt) {
			roundKey = this.keyGen.getNextKey();
		}else {
			roundKey = this.keyGen.getPrevKey();
		}
		this.state = Util.addMatrix(this.state, roundKey);
	}
	
	public String encrypt(String message) {
		initialize(message,true);
		for(int i=0;i<totalRounds;i++) {
			List<List<String>> roundKey = this.keyGen.getNextKey();
			this.state =  this.shiftRows(this.subBytes(state,false),false);
			if(i != totalRounds-1) {
				this.state = this.mixColumns(state,false);
			}
			this.state = Util.addMatrix(this.state, roundKey);
		}
		return Util.toString(state);
	}
	
	public String decrypt(String message) {
		initialize(message,false);
		for(int i=0;i<totalRounds;i++) {
			List<List<String>> roundKey = this.keyGen.getPrevKey();
			this.state =  this.subBytes(this.shiftRows(state,true),true);
			this.state = Util.addMatrix(this.state, roundKey);
			if(i != totalRounds-1) {
				this.state = this.mixColumns(state,true);
			}
		}
		return Util.toString(this.state);
	}
	
	public List<List<String>> shiftRows(List<List<String>> state,boolean inverse){
		int size = state.size();
		for(int i=0;i<size;i++) {
			List<String> tempList = new ArrayList<>();
			for(int j=0;j<size;j++) {
				tempList.add(state.get(j).get(i));
			}
			if(inverse) {
				tempList = Util.shiftRight(tempList, i);
			}else {
				tempList = Util.shiftLeft(tempList, i);
			}
			for(int j=0;j<size;j++) {
				state.get(j).set(i, tempList.get(j));
			}
		}
		return state;
	}
	
	public List<List<String>> subBytes(List<List<String>> state,boolean inverse){
		SBox sbox = this.substitutionBox;
		if(inverse) {
			sbox = this.invSubstitutionBox;
		}
		for(int i=0;i<state.size();i++) {
			for(int j=0;j<state.get(i).size();j++) {
				state.get(i).set(j,sbox.process(state.get(i).get(j)));
			}
		}
		return state;
	}
	
	public List<List<String>> mixColumns(List<List<String>> state,boolean inverse){
		List<List<String>> constant = Constants.mixColumnConstants;
		if(inverse) {
			constant = Constants.invMixColumnConstants;
		}
		for(int i=0;i<state.size();i++) {
			state.set(i, Util.multiplyMatrix(state.get(i),constant));
		}
		return state;
	}
	
}
