package com.martink.filtering;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class FilterValidation {

	private final static String TAG = "FilterValidation";
	
	public static synchronized List<List<Integer>> validateVikingNumbers(List<Integer> numbers) {
		List<List<Integer>> ticketNumbers = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < numbers.size(); i+=6) {
			ticketNumbers.add(numbers.subList(i, i + 6));
		}
		
		for (int i = 0; i < ticketNumbers.size(); i++) {
			Log.d(TAG, "Parsing Viking Lotto list: " + ticketNumbers.get(i));
			for (int j = 0; j < ticketNumbers.get(i).size(); j++) {
				if (ticketNumbers.get(i).get(j) < 1 || ticketNumbers.get(i).get(j) > 48) {
					Log.d(TAG, "Invalid Viking Lotto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
			}
		}
		
		return ticketNumbers;
	}
	
	public static synchronized List<List<Integer>> validateEurojackpotNumbers(List<Integer> numbers) {
		List<List<Integer>> ticketNumbers = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < numbers.size(); i+=7) {
			ticketNumbers.add(numbers.subList(i, i + 7));
		}
		
		for (int i = 0; i < ticketNumbers.size(); i++) {
			Log.d(TAG, "Parsing Eurojackpot list: " + ticketNumbers.get(i));
			for (int j = 0; j < ticketNumbers.get(i).size(); j++) {
				if (j <= 4) {
					if (ticketNumbers.get(i).get(j) < 1 || ticketNumbers.get(i).get(j) > 50) {
						Log.d(TAG, "Invalid Eurojackpot number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
						return null;
					}
				} else {
					if (ticketNumbers.get(i).get(j) < 1 || ticketNumbers.get(i).get(j) > 10) {
						Log.d(TAG, "Invalid Eurojackpot number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
						return null;
					}
				}
			}
		}
		
		return ticketNumbers;
	}
	
	public static synchronized List<List<Integer>> validateBingoNumbers(List<Integer> numbers) {
		List<List<Integer>> ticketNumbers = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < numbers.size(); i+=25) {
			ticketNumbers.add(numbers.subList(i, i + 25));
		}
		
		for (int i = 0; i < ticketNumbers.size(); i++) {
			Log.d(TAG, "Parsing Bingo Loto list: " + ticketNumbers.get(i));
			for (int j = 0; j < ticketNumbers.get(i).size(); j += 5) {
				if (ticketNumbers.get(i).get(j) < 1 || ticketNumbers.get(i).get(j) > 15) {
					Log.d(TAG, "Invalid Bingo Loto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
				if (ticketNumbers.get(i).get(j + 1) < 16 || ticketNumbers.get(i).get(j) > 30) {
					Log.d(TAG, "Invalid Bingo Loto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
				if (ticketNumbers.get(i).get(j + 2) < 31 || ticketNumbers.get(i).get(j) > 45) {
					Log.d(TAG, "Invalid Bingo Loto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
				if (ticketNumbers.get(i).get(j + 3) < 46 || ticketNumbers.get(i).get(j) > 60) {
					Log.d(TAG, "Invalid Bingo Loto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
				if (ticketNumbers.get(i).get(j + 4) < 61 || ticketNumbers.get(i).get(j) > 75) {
					Log.d(TAG, "Invalid Bingo Loto number at: [" + i + "][" + j + "] (" + ticketNumbers.get(i).get(j) + ")");
					return null;
				}
			}
		} 
		
		return ticketNumbers;
	}
}
