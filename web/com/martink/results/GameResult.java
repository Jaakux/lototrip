package com.martink.results;

import java.util.ArrayList;
import java.util.List;

public class GameResult {

	private String winMessage = "";
	private double winAmount = 0;
	private int missingAmount = 0;
	private List<Integer> missingNumbers = new ArrayList<Integer>();
	
	public GameResult(String winMessage, double winAmount, List<Integer> missingNumbers) {
		this.winMessage = winMessage;
		this.winAmount = winAmount;
		this.missingNumbers = missingNumbers;
		this.missingAmount = missingNumbers.size();
	}

	public String getWinMessage() {
		return winMessage;
	}

	public double getWinAmount() {
		return winAmount;
	}

	public int getMissingAmount() {
		return missingAmount;
	}

	public List<Integer> getMissingNumbers() {
		return missingNumbers;
	}	
	
	@Override
	public String toString() {
		String output = "";
		
		if (winMessage.equals("error")) return output + "\nPileti kontrollimine ebaõnnestus\n\n";
		
		if (!winMessage.equals("")) {
			output += "Võitsite " + winMessage + " " + winAmount + " eurot!\n";
		} else {
			output += "Kahjuks ei võitnud te midagi.\n";
		}
		
		String missing = String.valueOf(missingNumbers);
		
		output += "Puudu jäänud numbrid: " + missing.substring(1, missing.length() - 1) + "\n\n";
		
		return output;
	}
}
