package com.martink.tess;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class TessImage {
	
	private final ContentResolver contentResolver;
	private final Uri uri;
	
	private String name;
	private String mimeType;
	private String extension;
	private int orientation;
	
	private String path;
	
	public TessImage(ContentResolver contentResolver, Uri uri) {
		this.contentResolver = contentResolver;
		this.uri = uri;
		
		//File name
	    Cursor returnCursor = contentResolver.query(uri, null, null, null, null);
	    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
	    returnCursor.moveToFirst();
	    name = returnCursor.getString(nameIndex);
	    
	    //File type
	    mimeType = contentResolver.getType(uri);
	    
	    //File extension
	    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
	    
	    //File path
	    path = uri.getPath();
	    
	    //File orientation
	    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
	    
        Cursor orientationCursor = contentResolver.query(uri, orientationColumn, null, null, null);
        if (orientationCursor != null && orientationCursor.moveToFirst()) {
            orientation = orientationCursor.getInt(orientationCursor.getColumnIndex(orientationColumn[0]));
        }  
        
        Log.d("TessImage", "Picture name: " + name);
        Log.d("TessImage", "Picture mime type: " + mimeType);
        Log.d("TessImage", "Picture extension: " + extension);
        Log.d("TessImage", "Picture rotation: " + String.valueOf(orientation));
	}

	public String getName() {
		return name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExtension() {
		return extension;
	}

	public String getPath() {
		return path;
	}
	
	public InputStream getInputStream() throws IOException {
		return contentResolver.openInputStream(uri);
	}
	
	public Bitmap getBitmap() {
        InputStream input = null;
        Bitmap bitmap = null;
        
        try {
        	input = getInputStream();
        	
        	BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            
            bitmap = BitmapFactory.decodeStream(input, null, options);
            
            bitmap = getResizedBitmap(bitmap, 1600); //Resize bitmap
            
            bitmap = rotateOnNeed(bitmap); //Rotate the bitmap
            
		} catch (IOException e) {
			//TODO
		} finally {
			try {
				if (input != null) input.close();
			} catch (IOException e) {
					//TODO
			}
		}

        return bitmap;
	}
	
	public Bitmap rotateOnNeed(Bitmap bitmap) {
		if (orientation != 0) {
			
		    int width = bitmap.getWidth();
		    int height = bitmap.getHeight();

		    Matrix matrix = new Matrix();
		    matrix.postRotate(orientation);

		    // Rotating Bitmap & convert to ARGB_8888, required by tess
		    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		}
		
		return bitmap;
	}
	
	public Bitmap getResizedBitmap(Bitmap bitmap, int size) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float ratio = (float)width / (float)height;
		
		if (ratio > 0) {
			width = size;
			height = (int)(width / ratio);
		} else {
			height = size;
			width = (int)(height * ratio);
		}
		
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}
}
