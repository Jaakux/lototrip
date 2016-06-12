package com.martink.loto;

import com.martink.filtering.OcrResultFilterHolder;
import com.martink.loto.R;
import com.martink.verification.VerifyTicket;
import com.martink.verification.VerifyTicketHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FilterActivity extends Activity {
	
	private final String TAG = "FilterActivity";
	
	private final FilterActivity activity = FilterActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		TextView filterText1 = (TextView)findViewById(R.id.filterText1);
		TextView filterText2 = (TextView)findViewById(R.id.filterText2);
		
		String filterData1 = "Mängu tüüp: " + OcrResultFilterHolder.getOcrResultFilter().getGameTypeValue() + "\n"
				+ "Loosimise number: " + OcrResultFilterHolder.getOcrResultFilter().getGameNumber() + "\n";
		
		String filterData2 = OcrResultFilterHolder.getOcrResultFilter().getTicketNumberValues();
		
		filterText1.setText(filterData1);
		filterText2.setText(filterData2);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.filterAgreeButton:
				if (isInternetAvailable()) {
					Alerts.showProgressDialog(activity, "Pileti kontrollimine", "Palun oodake kuni teie piletit kontrollitakse...");
					
					VerifyTicket verifyTicket = new VerifyTicket(activity, OcrResultFilterHolder.getOcrResultFilter());
					verifyTicket.start();
				} else {
					this.runOnUiThread(new Runnable() {
						public void run() {
							Alerts.showAlertDialog(activity, "Viga!", "Telefonil puudub ühendus internetiga.\n\n"
									+ "Internetiühendus on vajalik numbrite kontrollimiseks.");
						}
					});
				}
				Log.d(TAG, "agreed");
				break;
			case R.id.filterDeclineButton:
				Intent startPictureActivity = new Intent(activity, PictureActivity.class);
				startPictureActivity.putExtra("filterDecline", "redo"); //Sends message to reopen camera
				activity.startActivity(startPictureActivity);
				activity.finish();
				Log.d(TAG, "disagreed");
				break;
		}
	}
	
	//-----------------------------------------BASE----------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*@Override
	public void onBackPressed() { //Stop user from leaving this activity
	    moveTaskToBack(true);
	}*/
	
	//--------------------------------------------DATA--------------------------------------
	
	public void passVerifyTicketObject(VerifyTicket verifyTicket) {
		VerifyTicketHolder.setVerifyTicket(verifyTicket);
		
		Intent startResultActivity = new Intent(activity, ResultActivity.class);
		activity.startActivity(startResultActivity);
		activity.finish();
	}
	
	public boolean isInternetAvailable() { //Check if device is connected to internet
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
