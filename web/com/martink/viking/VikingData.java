package com.martink.viking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.martink.methods.ValidationMethods;
import com.martink.webmethods.WebMethods;

public class VikingData {

	private int gameNumber;
	
	private List<Integer> gameNumbers = new ArrayList<Integer>();
	//6, 5+1, 5, 4, 3, 2+1
	private Double[] gameWinnings = new Double[6];
	private String[] winParsers = new String[]{
			"<td>Viking Lotto 6 tabamust</td>",
			"<td>Viking Lotto 5+1 tabamust</td>",
			"<td>Viking Lotto 5 tabamust</td>",
			"<td>Viking Lotto 4 tabamust</td>",
			"<td>Viking Lotto 3 tabamust</td>",
			"<td>Viking Lotto 2+1 tabamust</td>"
	};
	
	public VikingData(int gameNumber) {
		this.gameNumber = gameNumber;
		getVikingData(gameNumber);
	}
	
	public List<Integer> getGameNumbers() {
		if (gameNumbers.size() == 9) return gameNumbers;
		return null;
	}
	
	public List<Integer> getMainNumbers() {
		if (gameNumbers.size() >=6) return gameNumbers.subList(0, 6);
		return gameNumbers;
	}
	
	public List<Integer> getExtraNumbers() {
		if (gameNumbers.size() >= 8) return gameNumbers.subList(6, 8);
		return gameNumbers;
	}
	
	public Integer getLuckyNumber() {
		if (gameNumbers.size() >= 9) return gameNumbers.get(8);
		return gameNumbers.get(0);
	}
  	
	public Double[] getGameWinnings() {
		return gameWinnings;
	}
	
	private void getVikingData(int gameNumber) {
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			URL url = new URL("https://www.eestiloto.ee/osi/draws.do?pageNumber="
					+ WebMethods.getPageNumber(11, gameNumber) + "&gameCode=11"
					+ "&gameName=11&sortProperty=DRAWDATE&drawNumber=" + gameNumber);
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
		    			if (gameNumbers.size() == 9) {
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
		
		data += "Viking Lotto loosimine " + gameNumber + "\n\n";
		
		if (getGameNumbers() != null) {
			String main = String.valueOf(getMainNumbers());
			String extra = String.valueOf(getExtraNumbers());
			String lucky = String.valueOf(getLuckyNumber());
			
			data += "Põhinumbrid: " + main.substring(1, main.length() -1);
			data += "\nLisanumbrid: " + extra.substring(1, extra.length() - 1);
			data += "\nÕnnenumber: " + lucky + "\n";
		} else {
			return null;
		}
		
		return data;
	}
}
