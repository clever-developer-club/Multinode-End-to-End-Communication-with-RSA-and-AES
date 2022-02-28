package aes;

import java.util.ArrayList;
import java.util.List;

public class KeyGenerator {
	
	SBox substitutionBox;
	List<List<List<String>>> roundKeys;
	int currentRound;
	int keyLength;
	
	KeyGenerator(String key, int rounds, int keyLength){
		this.substitutionBox = new SBox(Constants.sboxTable);
		this.currentRound = 0;
		this.keyLength = keyLength;
		this.roundKeys = getRoundKeys(key,rounds);
	}
	
	public void reset() {
		this.currentRound = 0;
	}
	
	public List<List<String>> getNextKey() {
		return this.roundKeys.get(this.currentRound++);
	}
	
	public List<List<String>> getPrevKey() {
		int index = this.roundKeys.size() - 1 - this.currentRound;
		this.currentRound++;
		return this.roundKeys.get(index);
	}
	
	public List<String> calculateTempWord(List<String> word, int round){
		List<String> tempWord = Util.shiftLeft(word, 1);
		for(int i=0;i<word.size();i++) {
			tempWord.set(i, this.substitutionBox.process(tempWord.get(i)));
		}
		tempWord = Util.add(tempWord, Constants.roundConstants.get(round));
		return tempWord;
	}
	
	public int getFactor(int rounds) {
		int factor = 4;
		if(rounds == 12) {
			factor = 6;
		}else if(rounds == 14) {
			factor = 8;
		}
		return factor;
	}
	
	public List<List<List<String>>> getRoundKeys(String key,int rounds){
		int factor = getFactor(rounds);
		int totalWords = (rounds+1)*4;
		
		List<List<String>> keys = getAllKeys(key,factor,totalWords);
		
		List<List<List<String>>> roundKeys = new ArrayList<>();
		for(int i = 0;i<totalWords;i=i+this.keyLength) {
			List<List<String>> currentRoundKey = new ArrayList<>();
			for(int j = i;j < i+this.keyLength;j++) {
				currentRoundKey.add(keys.get(j));
			}
			roundKeys.add(currentRoundKey);
		}
			
		return roundKeys;
	}

	public List<List<String>> getAllKeys(String key,int factor,int totalWords){
		
		List<List<String>> result = new ArrayList<>();
		result.addAll(Util.toKeyArray(key));
		
		for(int i=factor;i<totalWords;i++) {
			List<String> prevWord = result.get(i-1);
			if(i%factor == 0) {
				prevWord = calculateTempWord(result.get(i-1), (i/4) - 1);
			}
			result.add(Util.add(prevWord, result.get(i-factor)));
		}
		
		return result;
		
	}
}
