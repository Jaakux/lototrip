package com.martink.eurojackpot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.martink.methods.ValidationMethods;
import com.martink.webmethods.WebMethods;

public class EurojackpotData {

	private int gameNumber;
	
	private List<Integer> gameNumbers = new ArrayList<Integer>();
	//5+2, 5+1, 5+0, 4+2, 4+1, 4+0, 3+2, 3+1, 3+0, 2+2, 2+1, 1+2
	private Double[] gameWinnings = new Double[12];
	private String[] winParsers = new String[]{
			"<td>Eurojackpot 5+2 tabamust</td>",
			"<td>Eurojackpot 5+1 tabamust</td>",
			"<td>Eurojackpot 5+0 tabamust</td>",
			"<td>Eurojackpot 4+2 tabamust</td>",
			"<td>Eurojackpot 4+1 tabamust</td>",
			"<td>Eurojackpot 4+0 tabamust</td>",
			"<td>Eurojackpot 3+2 tabamust</td>",
			"<td>Eurojackpot 3+1 tabamust</td>",
			"<td>Eurojackpot 3+0 tabamust</td>",
			"<td>Eurojackpot 2+2 tabamust</td>",
			"<td>Eurojackpot 2+1 tabamust</td>",
			"<td>Eurojackpot 1+2 tabamust</td>"
	};
	
	public EurojackpotData(int gameNumber) {
		this.gameNumber = gameNumber;
		getEurojackpotData(gameNumber);
	}
	
	public List<Integer> getGameNumbers() {
		if (gameNumbers.size() == 7) return gameNumbers;
		return null;
	}
	
	public List<Integer> getMainNumbers() {
		if (gameNumbers.size() >= 5) return gameNumbers.subList(0, 5);
		return gameNumbers;
	}
	
	public List<Integer> getExtraNumbers() {
		if (gameNumbers.size() >= 7) return gameNumbers.subList(5, 7);
		return gameNumbers;
	}
  	
	public Double[] getGameWinnings() {
		return gameWinnings;
	}
	
	private void getEurojackpotData(int gameNumber) {
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			URL url = new URL("https://www.eestiloto.ee/osi/draws.do?pageNumber="
					+ WebMethods.getPageNumber(15, gameNumber) + "&gameCode=15"
					+ "&gameName=15&sortProperty=DRAWDATE&drawNumber=" + gameNumber);
			isr = new InputStreamReader(url.openStream(), "UTF-8");
		    br = new BufferedReader(isr);
		    String line;
		    boolean check = false;
		    
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	if (line.isEmpty()) continue;
		    	if (line.equals("<td><strong>Põhinumbrid:</strong></td>")) {
		    		check = true;
		    		continue;
		    	}
		    	if (check) {
		    		if (line.equals("<td><strong>Lisainfo:</strong></td>")) break;
		    		if (ValidationMethods.isInteger(line)) {
		    			gameNumbers.add(Integer.valueOf(line));
		    		} else {
		    			if (gameNumbers.size() == 7) {
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
		
		data += "Eurojackpoti loosimine " + gameNumber + "\n\n";
		
		if (getGameNumbers() != null) {
			String main = String.valueOf(getMainNumbers());
			String extra = String.valueOf(getExtraNumbers());
			
			data += "Põhinumbrid: " + main.substring(1, main.length() -1);
			data += "\nLisanumbrid: " + extra.substring(1, extra.length() - 1) + "\n";
		} else {
			return null;
		}
		
		return data;
	}
}
