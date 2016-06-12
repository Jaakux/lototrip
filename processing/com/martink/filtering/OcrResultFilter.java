package com.martink.filtering;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.martink.methods.ValidationMethods;

public class OcrResultFilter {

	public final String TAG = "OcrResultFilter";
	
	private int gameType = 0;
	private int gameNumber = 0;
	private List<List<Integer>> ticketNumbers = new ArrayList<List<Integer>>();
	
	private int foundSize = 0;
	
	public OcrResultFilter(String ocrResult) {
		filterOcr(ocrResult);
	}
	
	public int getGameType() {
		return gameType;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public List<List<Integer>> getTicketNumbers() {
		return ticketNumbers;
	}
	
	public String getGameTypeValue() {
		if (gameType > 0 && gameType < 4) {
			switch (gameType) {
				case 1: return "Viking Lotto";
				case 2: return "Eurojackpot";
				case 3: return "Bingo Loto";
			}
		}
		return "";
	}
	
	public String getTicketNumberValues() {
		String data = "";
		
		switch (gameType) {
			case 1:
				for (int i = 0; i < ticketNumbers.size(); i++) {
					data += "" + (i + 1) + ". Mänguvälja numbrid:\n\n";
					String numbers = String.valueOf(ticketNumbers.get(i));
					data += "Numbrid: " + numbers.substring(1, numbers.length() - 1);
					data += "\n\n\n";
				}
				break;
			case 2:
				for (int i = 0; i < ticketNumbers.size(); i++) {
					data += "" + (i + 1) + ". Mänguvälja numbrid:\n\n";
					String mainNumbers = String.valueOf(ticketNumbers.get(i).subList(0, 5));
					String extraNumbers = String.valueOf(ticketNumbers.get(i).subList(5, 7));
					data += "Põhinumbrid: " + mainNumbers.substring(1, mainNumbers.length() - 1);
					data += "\n";
					data += "Lisanumbrid: " + extraNumbers.substring(1, extraNumbers.length() - 1);
					data += "\n\n\n";
				}
				break;
			case 3:
				for (int i = 0; i < ticketNumbers.size(); i++) {
					data += "" + (i + 1) + ". Mänguvälja numbrid:\n\n";
					String row1 = String.valueOf(ticketNumbers.get(i).subList(0, 5));
					String row2 = String.valueOf(ticketNumbers.get(i).subList(5, 10));
					String row3 = String.valueOf(ticketNumbers.get(i).subList(10, 15));
					String row4 = String.valueOf(ticketNumbers.get(i).subList(15, 20));
					String row5 = String.valueOf(ticketNumbers.get(i).subList(20, 25));
					data += row1.substring(1, row1.length() - 1);
					data += ",\n";
					data += row2.substring(1, row2.length() - 1);
					data += ",\n";
					data += row3.substring(1, row3.length() - 1);
					data += ",\n";
					data += row4.substring(1, row4.length() - 1);
					data += ",\n";
					data += row5.substring(1, row5.length() - 1);
					data += "\n\n\n";
				}
				break;
		}
		
		return data;
	}
	
	private void filterOcr(String input) {
		int gameNumber = filterGameNumber(input);
		List<Integer> foundNumbers = filterTicketNumbers(input);
		
		if (gameNumber > 0 && foundNumbers.size() > 0) {
			this.gameNumber = gameNumber;
			
			if (foundNumbers.size() % 6 == 0 && foundSize == 6) { //Viking Lotto
				ticketNumbers = FilterValidation.validateVikingNumbers(foundNumbers);
				if (ticketNumbers != null) gameType = 1;
			} else if (foundNumbers.size() % 7 == 0 && foundSize == 7) { //Eurojackpot
				ticketNumbers = FilterValidation.validateEurojackpotNumbers(foundNumbers);
				if (ticketNumbers != null) gameType = 2;
			} else if (foundNumbers.size() % 25 == 0 && foundSize == 5) { //Bingo Lotto
				ticketNumbers = FilterValidation.validateBingoNumbers(foundNumbers);
				if (ticketNumbers != null) gameType = 3;
			}
		}
		
		Log.d(TAG, "Found game number: " + gameNumber);
		Log.d(TAG, "Found ticket numbers: " + ticketNumbers);
		Log.d(TAG, "Following game type: " + gameType);
	}

	private Integer filterGameNumber(String input) {
		int gameNumber = 0;
		String[] inputLines = input.split("\n");
		
		for (String line: inputLines) {
			if (line.matches(".*num\\w{3}\\s+\\d+.*") || line.matches(".*ber\\s+\\d+.*")) {
			
				if (line.matches(".*num\\w{3}\\s+\\d+.*")) 
					line = line.substring(line.indexOf("num") + 6, line.length());
				else if (line.matches(".*ber\\s+\\d+.*")) 
					line = line.substring(line.indexOf("ber") + 3, line.length());
				
				String[] numbers = line.replaceAll("[^\\d]", " ").split("\\s");
				for (String number: numbers) {
					if (ValidationMethods.isInteger(number)) {
						gameNumber = Integer.parseInt(number);
						return gameNumber;
					}
				}
			}
		}
		
		return gameNumber;
	}
	
	private List<Integer> filterTicketNumbers(String input) {
		List<Integer> ticketNumbers = new ArrayList<Integer>();
		String[] inputLines = input.split("\n");
		int foundType = 0;
		
		for (String line: inputLines) {

			if (foundType == 1 || foundType == 0) {
				//For Viking and Eurojackpot
				if (line.matches(".*[A-K]:\\s+\\d+.*") || line.matches(".*\\d+\\s+PP.*")) { //Matches A: - K: or PP in the end
					if (foundType == 0) foundType = 1;
					
					String[] numbers = line.replaceAll("[^\\d]", " ").split("\\s");
					List<Integer> subNumbers = new ArrayList<Integer>();
					
					for (String number: numbers) {
						if (ValidationMethods.isInteger(number)) {
							subNumbers.add(Integer.parseInt(number));
						}
					}
					
					Log.d(TAG, "Parsing: " + subNumbers);
					
					if (subNumbers.size() == 6 || subNumbers.size() == 7) { //Only valid amount of numbers
						if (foundSize == 0) {
							if (subNumbers.size() == 6) foundSize = 6;
							else foundSize = 7;
						}
						
						if (subNumbers.size() == foundSize) ticketNumbers.addAll(subNumbers);
					}
				} 
			}
			
			if (foundType == 2 || foundType == 0) {
				//For Bingo
				if (line.matches(".*[l|I|\\|]+\\s*\\d+\\s*[l|I|\\|]+.*")) { //Matches | num |
					if (foundType == 0) foundType = 2;
					
					String[] numbers = line.replaceAll("[^\\d]", " ").split("\\s");
					List<Integer> subNumbers = new ArrayList<Integer>();
					
					for (String number: numbers) {
						if (ValidationMethods.isInteger(number)) {
							subNumbers.add(Integer.parseInt(number));
						}
					}
					
					Log.d(TAG, "Parsing: " + subNumbers);
					
					if (subNumbers.size() == 5) { //Only valid amount of numbers
						if (foundSize == 0) foundSize = 5;
						ticketNumbers.addAll(subNumbers);
					}
				}
			}
		}
		
		return ticketNumbers;
	}
}
