package com.martink.loto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpeningActivity extends Activity {

	private Activity activity = OpeningActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opening);
		
		int secondsDelayed = 2;
		
        new Handler().postDelayed(new Runnable() { //SPLASH to be delayed
                public void run() {
	            	Intent startPictureActivity = new Intent(activity, PictureActivity.class);
	    			activity.startActivity(startPictureActivity);
	    			activity.finish();
                }
        }, secondsDelayed * 1000);
	}
}
