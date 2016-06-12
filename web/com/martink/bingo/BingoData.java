package com.martink.bingo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.martink.methods.ValidationMethods;
import com.martink.webmethods.WebMethods;

public class BingoData {

	private int gameNumber;
	
	private List<Integer> gameNumbers = new ArrayList<Integer>();
	//jackpot, täismäng, diagonaalid, nurgad
	private Double[] gameWinnings = new Double[4];
	private String[] winParsers = new String[]{
			"<td>Bingo jackpot</td>",
			"<td>Bingo täismäng</td>",
			"<td>Bingo diagonaalidemäng</td>",
			"<td>Bingo nurkademäng</td>"
	};
	
	public BingoData(int gameNumber) {
		this.gameNumber = gameNumber;
		getBingoData(gameNumber);
	}
	
	public List<Integer> getGameNumbers() {
		if (gameNumbers.size() >= 38) return gameNumbers; //Min
		return null;
	}
	
	public Double[] getGameWinnings() {
		return gameWinnings;
	}
	
	private void getBingoData(int gameNumber) {
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			URL url = new URL("https://www.eestiloto.ee/osi/draws.do?pageNumber="
					+ WebMethods.getPageNumber(13, gameNumber) + "&gameCode=13"
					+ "&gameName=13&sortProperty=DRAWDATE&drawNumber=" + gameNumber);
			isr = new InputStreamReader(url.openStream(), "UTF-8");
		    br = new BufferedReader(isr);
		    String line;
		    boolean check = false;
		    
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	if (line.isEmpty()) continue;
		    	if (line.equals("<td><strong>Võiduklass I - nurgad:</strong></td>")) {
		    		check = true;
		    		continue;
		    	}
		    	if (check) {
		    		if (line.equals("<td><strong>Lisainfo:</strong></td>")) break;
		    		if (ValidationMethods.isInteger(line)) {
		    			gameNumbers.add(Integer.valueOf(line));
		    		} else {
		    			for (int i = 0; i < winParsers.length; i++) {
	    					if (line.equals(winParsers[i])) {
	    						br.readLine();
	    						gameWinnings[i] = ValidationMethods.extractWinnings(br.readLine());
	    						break;
	    					}
	    				}
		    		}
		    	}
		    }
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if (isr != null) isr.close();
				if (br != null) br.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		String data = "";
		
		data += "Bingo Loto loosimine " + gameNumber + "\n\n";
		
		if (getGameNumbers() != null) {
			String corners = String.valueOf(gameNumbers.subList(0, 33));
			String diagonals = String.valueOf(gameNumbers.subList(33, 38));
			String fullgame = String.valueOf(gameNumbers.subList(38, gameNumbers.size()));
			
			data += "Nurgad: " + corners.substring(1, corners.length() -1);
			data += "\nDiagonaalid: " + diagonals.substring(1, diagonals.length() - 1);
			data += "\nTäismäng: " + fullgame.substring(1, fullgame.length() -1) + "\n";
		} else {
			return null;
		}
		
		return data;
	}
}
