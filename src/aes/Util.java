package aes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static String toDecimal(String inp,int base) {
		BigInteger bigNumber = new BigInteger(inp, base);
		return bigNumber.toString(10);
	}

	public static String toBinary(String inp,int base) {
		BigInteger bigNumber = new BigInteger(inp, base);
		String output = bigNumber.toString(2);
		while(output.length()%4 != 0) {
			output = '0' + output;
		}
		if(inp.charAt(0) == '0') {
			output = "0000" + output;
		}
		return output;
	}
	
	public static String toHex(String inp,int base) {
		BigInteger bigNumber = new BigInteger(inp, base);
		String output = bigNumber.toString(16);
		if(output.length()%2 != 0) {
			output = '0' + output;
		}
		return output;
	}
	
	public static String toHex(String text) {
		String result = "";
		for(int i=0;i<text.length();i++) {
			String temp = toHex(Integer.toString((int)(text.charAt(i))),10);
			if(temp.length() == 1) {
				temp = '0' + temp;
			}
			result += temp;
		}
		return result;
	}
	
	public static String toString(String hex) {
		String result = "";
		for(int i=0;i<hex.length();i=i+2) {
			result += (char)(Integer.parseInt(toDecimal(hex.substring(i,i+2),16)));
		}
		return result;
	}
	
	public static String xor(String a,String b) {
		a = toBinary(a,16);
		b = toBinary(b,16);
		String output = "";
		for(int i=0;i<a.length();i++) {
			if(a.charAt(i) == b.charAt(i)) {
				output += '0';
			}else {
				output += '1';
			}
		}
		return toHex(output,2);
	}
	
	public static List<String> shiftLeft(List<String> word,int shiftLen) {
		List<String> newWord = new ArrayList<>();
		for(int i=shiftLen;i<word.size();i++) {
			newWord.add(word.get(i));
		}
		for(int i=0;i<shiftLen;i++) {
			newWord.add(word.get(i));
		}
		return newWord;
	}
	
	public static List<String> shiftRight(List<String> word,int shiftLen) {
		List<String> newWord = new ArrayList<>();
		for(int i=word.size() - shiftLen;i<word.size();i++) {
			newWord.add(word.get(i));
		}
		for(int i=0;i<word.size() - shiftLen;i++) {
			newWord.add(word.get(i));
		}
		return newWord;
	}
	
	public static List<List<String>> toKeyArray(String key){
		List<List<String>> result = new ArrayList<>();
		for(int i=0;i<key.length();i=i+8) {
			List<String> temp = new ArrayList<>();
			for(int j=i;j<i+8;j = j+2) {
				temp.add(key.substring(j,j+2));
			}
			result.add(temp);
		}
		return result;
	}
	
	public static String toString(List<List<String>> key){
		String result = "";
		for(int i=0;i<key.size();i++) {
			for(int j=0;j<key.size();j++) {
				result += key.get(i).get(j);
			}
		}
		return toString(result);
	}
	
	public static List<String> add(List<String> a,List<String> b){
		List<String> result = new ArrayList<>();
		for(int i=0;i<a.size();i++) {
			result.add(xor(a.get(i),b.get(i)));
		}
		return result;
	}
	
	public static String gfmul(String x,String y) {
		int a = Integer.parseInt(toDecimal(x, 16));
		int b = Integer.parseInt(toDecimal(y, 16));
		int p = 0;
		while (a != 0 && b != 0) {
	        if ((b & 1) != 0)
	            p ^= a;

	        if ((a & 0x80) != 0)
	            a = (a << 1) ^ 0x11b; 
	        else
	            a <<= 1;
	        b >>= 1; 
		}
		return toHex(Integer.toString(p),10);
	}
	
	public static String multiply(List<String> a,List<String> b){
		String result = gfmul(a.get(0),b.get(0));
		for(int i=1;i<a.size();i++) {
			result = xor(result,gfmul(a.get(i),b.get(i)));
		}
		return result;
	}
	
	
	public static List<List<String>> addMatrix(List<List<String>> a, List<List<String>> b){
		List<List<String>> result = new ArrayList<>();
		for(int i=0;i<a.size();i++) {
			result.add(add(a.get(i),b.get(i)));
		}
		return result;
	}
	
	public static List<String> multiplyMatrix(List<String> a, List<List<String>> b){
		List<String> result = new ArrayList<>();
		for(int i=0;i<a.size();i++) {
			result.add(multiply(a,b.get(i)));
		}
		return result;
	}
}
