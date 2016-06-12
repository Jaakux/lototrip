package com.martink.bingo;

import java.util.ArrayList;
import java.util.List;

import com.martink.results.GameResult;

public class BingoChecker {

	private List<Integer> ticketNumbers = new ArrayList<Integer>(); //25 numbers
	private BingoData gameData;
	
	//25 total numbers
	private int[] cornerIndexes = new int[]{0, 4, 20, 24}; //4 corners
	private int[] diagonalIndexes = new int[]{0, 4, 6, 8, 12, 16, 18, 20, 24}; //9 diagonal indexs
	
	public BingoChecker(List<Integer> ticketNumbers, int gameNumber, BingoData gameData) {
		this.ticketNumbers = ticketNumbers;
		this.gameData = gameData;
	}
	
	public GameResult checkGame() {
		if (gameData.getGameNumbers() == null) return new GameResult("error", 0, new ArrayList<Integer>()); //Failed to recieve right data
		
		List<Integer> missingNumbers = checkWinnings(new ArrayList<Integer>(ticketNumbers), 
				gameData.getGameNumbers().size());
		
		String winMessage = "";
		double winAmount = 0;
		
		if (missingNumbers == null) {
			//JACKPOT
			if (checkWinnings(new ArrayList<Integer>(ticketNumbers), 41) == null) {
				winMessage = "Bingo Lotto jackpotiga";
				winAmount = gameData.getGameWinnings()[0];
			}
			//FULL GAME
			if (gameData.getGameWinnings()[1] > winAmount) {
				winMessage = "Bingo Lotto täismänguga";
				winAmount = gameData.getGameWinnings()[1];
			}
		}
		if (checkWinnings(extractNumbers(diagonalIndexes), 38) == null) {
			if (gameData.getGameWinnings()[2] > winAmount) {
				winMessage = "Bingo Lotto diagonaalide mänguga";
				winAmount = gameData.getGameWinnings()[2];
			}
		}
		if (checkWinnings(extractNumbers(cornerIndexes), 33) == null) {
			if (gameData.getGameWinnings()[3] > winAmount) {
				winMessage = "Bingo Lotto nurkademänguga";
				winAmount = gameData.getGameWinnings()[3];
			}
		} 
		
		return new GameResult(winMessage, winAmount, missingNumbers);
	}
	
	private List<Integer> extractNumbers(int[] indexes) {
		List<Integer> numbers = new ArrayList<Integer>();
		
		for (int i = 0; i < indexes.length; i++) {
			numbers.add(ticketNumbers.get(indexes[i]));
		}
		
		return numbers;
	}
	
	private List<Integer> checkWinnings(List<Integer> numbers, int interval) {
		for (int i = 0; i < interval; i++) {
			if (numbers.isEmpty()) return null;
			for (int j = 0; j < numbers.size(); j++) {
				if (gameData.getGameNumbers().get(i) == numbers.get(j)) {
					numbers.remove(j);
					break;
				}
			}
		}
		return numbers;
	}
}
