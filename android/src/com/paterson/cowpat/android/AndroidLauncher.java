package com.paterson.cowpat.android;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.paterson.Helpers.IOHelper;
import com.paterson.cowpat.CowPat;
import com.paterson.cowpat.android.helpers.AndroidPictureRetriever;

public class AndroidLauncher extends Game {
	private static int RESULT_LOAD_IMAGE = 1;
	private String selectedPicturePath = null;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new CowPatGame(new AndroidPictureRetriever(this)), config);
	}
	
	public void dispatchGetPictureIntent() {
	    Intent intent = new Intent();
	    intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       
        File photo = new File(Environment.getExternalStorageDirectory(),  "new_target.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(photo));
	    //takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	    if (intent.resolveActivity(getPackageManager()) != null) 
	    {
	    	startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     System.out.println("Returned results: " + resultCode);
	     if (resultCode == RESULT_OK) {
	    	 if (requestCode == RESULT_LOAD_IMAGE) {
	    		 //Uri selectedImageUri = imageUri;
	             selectedPicturePath = getRealPathFromURI(data.getData());
	             System.out.println("Returned path: " + selectedPicturePath);	 
	    		 if (selectedPicturePath != null) {
	            	 IOHelper.copyFileToLocal(selectedPicturePath);
	             }
	         }
         }
	}
	
	/**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String res = null;
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
               int column_index = cursor.getColumnIndexOrThrow(proj[0]);
               res = cursor.getString(column_index);
            }
            cursor.close();
            if (res == null)
            {
            	return uri.getPath();
            }
            else
            {
            	return res;
            }
    }
    
    public String getRealPathFromURI(Uri contentUri) {
    	Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
    	cursor.moveToFirst();
    	String document_id = cursor.getString(0);
    	document_id = document_id.substring(document_id.lastIndexOf(":")+1);
    	cursor.close();

    	cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    				null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
	   cursor.moveToFirst();
	   String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
	   cursor.close();

	   return path;
	}
}
