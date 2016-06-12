package com.martink.loto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public class Alerts {
	
	//Dialogs
	private static ProgressDialog progressDialog = null;
	private static AlertDialog alertDialog = null;
	
	public static synchronized void showProgressDialog(Activity activity, String title, String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setTitle(title);
			//progressDialog.setMessage(message);
			
			progressDialog.setCancelable(false); //Prevent from closing the dialog
			progressDialog.setCanceledOnTouchOutside(false);
			
			progressDialog.show();
		}
		
	}
	
	public static synchronized void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	public static synchronized void showAlertDialog(Activity activity, String title, String message) {
		if (alertDialog == null) {
			
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
			
			alertBuilder.setTitle(title);
			alertBuilder.setMessage(message);
			
			alertBuilder.setCancelable(false); //Prevent from closing the dialog
			
			alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { //Button for removing dialog
		        public void onClick(DialogInterface dialog, int which) { 
	            	dialog.cancel();
					alertDialog = null;
		        }
		    });
			
			alertDialog = alertBuilder.create();
			alertDialog.show();
		}
	}
}
