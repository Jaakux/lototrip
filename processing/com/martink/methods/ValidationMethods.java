package com.martink.methods;

public class ValidationMethods {
	
	public static boolean isInteger(String str) {
	    try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public static boolean isDouble(String str) {
	    try { 
	        Double.parseDouble(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public static double extractWinnings(String str) {
		String[] split = str.split("<|>");

		if (split.length > 2) {
			String value = split[2].replace(",", ".").replace(" ", "");
			if (isDouble(value)) {
				return Double.valueOf(value);
			}
		}
		return 0;
	}
}
