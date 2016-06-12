package com.martink.verification;

import java.util.ArrayList;
import java.util.List;

import com.martink.bingo.BingoChecker;
import com.martink.bingo.BingoData;
import com.martink.viking.VikingChecker;
import com.martink.viking.VikingData;
import com.martink.loto.Alerts;
import com.martink.loto.FilterActivity;
import com.martink.results.GameResult;
import com.martink.eurojackpot.EurojackpotChecker;
import com.martink.eurojackpot.EurojackpotData;
import com.martink.filtering.OcrResultFilter;

public class VerifyTicket extends Thread {

	private FilterActivity filterActivity;
	
	private OcrResultFilter ocrResult;
	private List<GameResult> gameResults = new ArrayList<GameResult>();
	private String dataResult;
	
	public VerifyTicket(FilterActivity filterActivity, OcrResultFilter ocrResult){
		this.ocrResult = ocrResult;
		this.filterActivity = filterActivity;
	}
	
	public List<GameResult> getGameResults() {
		return gameResults;
	}
	
	public String getDataResult() {
		return dataResult;
	}
	
	private List<GameResult> ticketVerification() {
		List<GameResult> results = new ArrayList<GameResult>();
		
		if (ocrResult.getGameType() > 0 && ocrResult.getGameNumber() > 0 && !ocrResult.getTicketNumbers().isEmpty()) {
			switch (ocrResult.getGameType()) {
				case 1: //Viking
					VikingData vikingData = new VikingData(ocrResult.getGameNumber());
					dataResult = vikingData.toString();
					
					for (int i = 0; i < ocrResult.getTicketNumbers().size(); i++) {
						results.add(new VikingChecker(ocrResult.getTicketNumbers().get(i), ocrResult.getGameNumber(), vikingData).checkGame());
					}
					break;
				case 2: //Eurojackpot
					EurojackpotData eurojackpotData = new EurojackpotData(ocrResult.getGameNumber());
					dataResult = eurojackpotData.toString();
					
					for (int i = 0; i < ocrResult.getTicketNumbers().size(); i++) {
						results.add(new EurojackpotChecker(ocrResult.getTicketNumbers().get(i), ocrResult.getGameNumber(), eurojackpotData).checkGame());
					}
					break;
				case 3: //Bingo
					BingoData bingoData = new BingoData(ocrResult.getGameNumber());
					dataResult = bingoData.toString();
					
					for (int i = 0; i < ocrResult.getTicketNumbers().size(); i++) {
						results.add(new BingoChecker(ocrResult.getTicketNumbers().get(i), ocrResult.getGameNumber(), bingoData).checkGame());
					}
					break;
			}
		}
		return results;
	}
	
	public String getGameResultsValues() {
		String data = "";
		
		if (!gameResults.isEmpty()) {
			for (int i = 0; i < gameResults.size(); i++) {
				data += "\n" + (i + 1) + ". Mänguvälja tulemus:\n\n";
				data += gameResults.get(i).toString();
			}
		}
		
		return data;
	}
	
	@Override
	public void run() {
		gameResults = ticketVerification();
		
		Alerts.dismissProgressDialog(); //Dismiss dialog
		
		filterActivity.passVerifyTicketObject(this);
	}
}
