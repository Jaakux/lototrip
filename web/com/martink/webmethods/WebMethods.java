package com.martink.webmethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.martink.methods.ValidationMethods;

public class WebMethods {
	
	public static int getPageNumber(int gameCode, int gameNumber) {
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			URL url = new URL("https://www.eestiloto.ee/osi/draws.do?gameCode=" + gameCode);
			isr = new InputStreamReader(url.openStream(), "UTF-8");
		    br = new BufferedReader(isr);
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	if (line.isEmpty()) continue;
		    	if (line.matches(".*gameCode=" + gameCode + "&gameName=" + gameCode +
		    			"&sortProperty=DRAWDATE&amp;drawNumber=.*")) {
		    		return (calculatePageNumber(line, gameNumber));
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
		
		return 0;
	}
	
	private static int calculatePageNumber(String str, int gameNumber) {
		String[] split = str.split("<|>");

		if (split.length > 2) {
			String value = split[2].replace(",", ".").replace(" ", "");
			if (ValidationMethods.isInteger(value)) {
				int difference = Integer.valueOf(value) - gameNumber;
				int page = difference / 17;
				
				return page + 1;
			}
		}
		return 0;
	}
}
