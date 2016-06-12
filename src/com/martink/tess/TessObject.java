package com.martink.tess;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.martink.loto.Alerts;
import com.martink.loto.PictureActivity;

public class TessObject extends Thread {

	private String result = "";
	private Bitmap bitmap;
	private PictureActivity main;
	
	private final String LANG_LOC = "/storage/emulated/0/Tesseract"; //Traineddata location
	private final String LANG = "eng"; //Traineddata language
	
	public TessObject(PictureActivity main, TessImage image) {
		this.main = main;
		this.bitmap = image.getBitmap();
	}
	
	@Override
	public void run() {
		TessBaseAPI baseApi = new TessBaseAPI();
		
		baseApi.init(LANG_LOC, LANG);
		baseApi.setImage(bitmap);
		
		result = baseApi.getUTF8Text();
		baseApi.end(); //Closes tesseract
		
		Alerts.dismissProgressDialog(); //Dismiss progress dialog
		
		main.passOcrResult(result); //Pass result to main
	}
	
	public String getResult() {
		return result;
	}
}
