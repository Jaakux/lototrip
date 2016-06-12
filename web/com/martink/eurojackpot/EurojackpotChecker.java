package com.martink.eurojackpot;

import java.util.ArrayList;
import java.util.List;

import com.martink.results.GameResult;

public class EurojackpotChecker {

	private List<Integer> ticketNumbers = new ArrayList<Integer>(); //7 numbers
	private EurojackpotData gameData;
	
	public EurojackpotChecker(List<Integer> ticketNumbers, int gameNumber, EurojackpotData gameData) {
		this.ticketNumbers = ticketNumbers;
		this.gameData = gameData;
	}
	
	public GameResult checkGame() {
		if (gameData.getGameNumbers() == null) return new GameResult("error", 0, new ArrayList<Integer>()); //Failed to recieve right data
		
		List<Integer> resultAll = checkNumbers(gameData.getGameNumbers(),
				new ArrayList<Integer>(ticketNumbers));
		List<Integer> resultMain = checkNumbers(gameData.getMainNumbers(),
				new ArrayList<Integer>(ticketNumbers.subList(0, 5)));
		List<Integer> resultExtra = checkNumbers(gameData.getExtraNumbers(),
				new ArrayList<Integer>(ticketNumbers.subList(5, 7)));
		
		String biggestWin = "";
		double winAmount = 0;
		
		if (resultMain.size() == 0) {
			if (resultExtra.size() == 0 && gameData.getGameWinnings()[0] > winAmount) {
				biggestWin = "Eurojackpoti 5+2 võidutasandiga";
				winAmount = gameData.getGameWinnings()[0];
			} else if (resultExtra.size() == 1 && gameData.getGameWinnings()[1] > winAmount) {
				biggestWin = "Eurojackpoti 5+1 võidutasandiga";
				winAmount = gameData.getGameWinnings()[1];
			}
			if (gameData.getGameWinnings()[2] > winAmount) {
				biggestWin = "Eurojackpoti 5+0 võidutasandiga";
				winAmount = gameData.getGameWinnings()[2];
			}
		} else if (resultMain.size() == 1) {
			if (resultExtra.size() == 0 && gameData.getGameWinnings()[3] > winAmount) {
				biggestWin = "Eurojackpoti 4+2 võidutasandiga";
				winAmount = gameData.getGameWinnings()[3];
			} else if (resultExtra.size() == 1 && gameData.getGameWinnings()[4] > winAmount) {
				biggestWin = "Eurojackpoti 4+1 võidutasandiga";
				winAmount = gameData.getGameWinnings()[4];
			}
			if (gameData.getGameWinnings()[5] > winAmount) {
				biggestWin = "Eurojackpoti 4+0 võidutasandiga";
				winAmount = gameData.getGameWinnings()[5];
			}
		} else if (resultMain.size() == 2) {
			if (resultExtra.size() == 0 && gameData.getGameWinnings()[6] > winAmount) {
				biggestWin = "Eurojackpoti 3+2 võidutasandiga";
				winAmount = gameData.getGameWinnings()[6];
			} else if (resultExtra.size() == 1 && gameData.getGameWinnings()[7] > winAmount) {
				biggestWin = "Eurojackpoti 3+1 võidutasandiga";
				winAmount = gameData.getGameWinnings()[7];
			}
			if (gameData.getGameWinnings()[8] > winAmount) {
				biggestWin = "Eurojackpoti 3+0 võidutasandiga";
				winAmount = gameData.getGameWinnings()[8];
			}
		} else if (resultMain.size() == 3) {
			if (resultExtra.size() == 0 && gameData.getGameWinnings()[9] > winAmount) {
				biggestWin = "Eurojackpoti 2+2 võidutasandiga";
				winAmount = gameData.getGameWinnings()[9];
			} else if (resultExtra.size() == 1 && gameData.getGameWinnings()[10] > winAmount) {
				biggestWin = "Eurojackpoti 2+1 võidutasandiga";
				winAmount = gameData.getGameWinnings()[10];
			}
		} else if (resultMain.size() == 4) {
			if (resultExtra.size() == 0 && gameData.getGameWinnings()[11] > winAmount) {
				biggestWin = "Eurojackpoti 1+2 võidutasandiga";
				winAmount = gameData.getGameWinnings()[11];
			}
		}
		
		return new GameResult(biggestWin, winAmount, resultAll);
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
}
