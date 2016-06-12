package com.martink.filtering;

public class OcrResultFilterHolder {

	private static OcrResultFilter ocrResultFilter;
	
	public static synchronized OcrResultFilter getOcrResultFilter() {
		return ocrResultFilter;
	}
	
	public static synchronized void setOcrResultFilter(OcrResultFilter newHolder) {
		ocrResultFilter = newHolder;
	}
}
