package com.martink.viking;

import java.util.ArrayList;
import java.util.List;

import com.martink.results.GameResult;

public class VikingChecker {

	private List<Integer> ticketNumbers = new ArrayList<Integer>(); //6 numbers
	private VikingData gameData;
	
	public VikingChecker(List<Integer> ticketNumbers, int gameNumber, VikingData gameData) {
		this.ticketNumbers = ticketNumbers;
		this.gameData = gameData;
	}
	
	public GameResult checkGame() {
		if (gameData.getGameNumbers() == null) return new GameResult("error", 0, new ArrayList<Integer>()); //Failed to recieve right data
		
		List<Integer> resultAll = checkNumbers(gameData.getGameNumbers(),
				new ArrayList<Integer>(ticketNumbers));
		List<Integer> resultMain = checkNumbers(gameData.getMainNumbers(),
				new ArrayList<Integer>(ticketNumbers));
		List<Integer> resultExtra = checkNumbers(gameData.getExtraNumbers(),
				new ArrayList<Integer>(ticketNumbers));
		boolean resultLucky = checkLuckyNumber(new ArrayList<Integer>(ticketNumbers));
		
		String winMessage = "";
		double winAmount = 0;
		
		if (resultMain.size() == 0) {
			if (resultLucky) {
				winMessage = "Viking Lotto 6 põhinumbrit + õnnenumber võidugrupiga";
				winAmount = gameData.getGameWinnings()[0];
			}
			if (gameData.getGameWinnings()[0] > winAmount) {
				winMessage = "Viking Lotto 6 põhinumbrit võidugrupiga";
				winAmount = gameData.getGameWinnings()[0];
			}
		} else if (resultMain.size() == 1) {
			if (resultExtra.size() <= 5 && gameData.getGameWinnings()[1] > winAmount) {
				winMessage = "Viking Lotto 5 põhinumbrit + 1 lisanumber võidugrupiga";
				winAmount = gameData.getGameWinnings()[1];
			}
			if (gameData.getGameWinnings()[2] > winAmount) {
				winMessage = "Viking Lotto 5 põhinumbrit võidugrupiga";
				winAmount = gameData.getGameWinnings()[2];
			}
		} else if (resultMain.size() == 2) {
			if (gameData.getGameWinnings()[3] > winAmount) {
				winMessage = "Viking Lotto 4 põhinumbrit võidugrupiga";
				winAmount = gameData.getGameWinnings()[3];
			}
		} else if (resultMain.size() == 3) {
			if (gameData.getGameWinnings()[4] > winAmount) {
				winMessage = "Viking Lotto 3 põhinumbrit võidugrupiga";
				winAmount = gameData.getGameWinnings()[4];
			}
		} else if (resultMain.size() == 4) {
			if (resultExtra.size() <= 5 && gameData.getGameWinnings()[5] > winAmount) {
				winMessage = "Viking Lotto 2 põhinumbrit + 1 lisanumber võidugrupiga";
				winAmount = gameData.getGameWinnings()[5];
			}
		}
		
		return new GameResult(winMessage, winAmount, resultAll);
	}
	
	public List<Integer> checkNumbers(List<Integer> gameList, List<Integer> ticketList) {
		for (int i = 0; i < gameList.size(); i++) {
			for (int j = 0; j < ticketList.size(); j++) {
				if (gameList.get(i) == ticketList.get(j)) {
					ticketList.remove(j);
					break;
				}
			}
		}
		return ticketList;
	}
	
	public boolean checkLuckyNumber(List<Integer> ticketNumbers) {
		for (int i = 0; i < ticketNumbers.size(); i++) {
			if (ticketNumbers.get(i) == gameData.getLuckyNumber()) {
				return true;
			}
		}
		return false;
	}
}
