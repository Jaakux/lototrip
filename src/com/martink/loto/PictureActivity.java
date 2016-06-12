package com.martink.loto;

import com.martink.filtering.OcrResultFilter;
import com.martink.filtering.OcrResultFilterHolder;
import com.martink.loto.R;
import com.martink.tess.TessImage;
import com.martink.tess.TessObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PictureActivity extends Activity {
	
	private static final String TAG = "PictureActivity";
	
	private final PictureActivity activity = PictureActivity.this;
	
	private final int CAMERA_CAPTURE_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("filterDecline")) { //If exists redo picture
			String keyValue = intent.getStringExtra("filterDecline");
			if (keyValue.equals("redo")) { //Redo call exists
				Button camera = (Button)findViewById(R.id.cameraButton);
				camera.performClick();
			}
		}
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.cameraButton:
				/*passOcrResult("Loosimise number 186\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP\n"
						+ "A: 4 15 17 45 46 8 9 PP");*/
				/*passOcrResult("Loosimise number 1167\n"
				 * 		+ "A: 25 33 40 44 5 20 PP=";*/
				/*passOcrResult("Loosimise number 1054\n"
						+ "| 9  | 21 | 31 | 52 | 73\n"
						+ "| 8  | 22 | 42 | 53 | 68\n"
						+ "| 15 | 26 | 44 | 48 | 61\n"
						+ "| 11 | 23 | 38 | 55 | 70\n"
						+ "| 6  | 29 | 35 | 47 | 67");*/
				
			    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Start taking picture with camera
			    
			    if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //If possible
			        startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE); //Open camera
			    }
			    Log.d(TAG, "Camera opened");
				break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_CAPTURE_IMAGE) { //Camera activity result
				resolveActivityResultUri(data.getData());
			}
		}
	}

	public void resolveActivityResultUri(final Uri uri) {
		TessImage image = new TessImage(getContentResolver(), uri);
		
		Alerts.showProgressDialog(activity, "Pildi töötlemine", "Palun oodake kuni teie pilti töödeldakse...");
		
		TessObject tess = new TessObject(activity, image);
		tess.start();
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
	
	//--------------------------------------------INTERFACE--------------------------------------

	
	//--------------------------------------------DATA--------------------------------------
	
	public void passOcrResult(String result) {
		Log.d(TAG, result);
		
		OcrResultFilter filter = new OcrResultFilter(result);
		
		if (filter.getGameType() != 0) {
			OcrResultFilterHolder.setOcrResultFilter(filter); //Set migratable ocrResultFilter
			
			Intent startFilterActivity = new Intent(activity, FilterActivity.class);
			activity.startActivity(startFilterActivity);
			activity.finish();
		} else { //Call thread wrapper to display error in UI thread
			this.runOnUiThread(new Runnable() {
				public void run() {
					Alerts.showAlertDialog(activity, "Viga!", "Pildilt ei õnnestunud kätte saada vajalikke andmeid.\n\n"
							+ "Palun veenduge et pilt: oleks hästi valgustatud, õiget pidi ning sisaldaks vajalikke andmeid (Loosimise number, Mängunumbrid).");
				}
			});
		}
	}
}
