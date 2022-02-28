package aes;

import java.util.List;

public class SBox {
	
	List<List<String>> table;
	
	SBox(List<List<String>> table){
		this.table = table;
	}
	
	public String process(String input) {
		int rowIndex = Integer.parseInt(Util.toDecimal("" + input.charAt(0),16));
		int colIndex = Integer.parseInt(Util.toDecimal("" + input.charAt(1),16));
		return this.table.get(rowIndex).get(colIndex);
	}
	
}
