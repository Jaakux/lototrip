package com.martink.loto;

import com.martink.loto.R;
import com.martink.verification.VerifyTicket;
import com.martink.verification.VerifyTicketHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	private final String TAG = "ResultActivity";
	
	private final Activity activity = ResultActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		TextView gameResultText = (TextView)findViewById(R.id.gameResultText);
		TextView resultText = (TextView)findViewById(R.id.resultText);
		
		VerifyTicket verifyObject = VerifyTicketHolder.getVerifyTicket();
		
		if (verifyObject == null || verifyObject.getDataResult() == null) {
			gameResultText.setText("Loosimise andmed puuduvad\n");
			resultText.setText("Pileti kontrollimine ebaõnnestus");
		} else {
			gameResultText.setText(verifyObject.getDataResult());
			resultText.setText(verifyObject.getGameResultsValues());
		}
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.resultButton:
				Intent startMainActivity = new Intent(activity, PictureActivity.class);
				activity.startActivity(startMainActivity);
				activity.finish();
				Log.d(TAG, "back to start");
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
}
