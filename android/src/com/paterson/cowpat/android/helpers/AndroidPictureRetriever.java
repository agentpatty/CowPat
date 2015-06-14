package com.paterson.cowpat.andriod.helpers;


import android.app.Activity;

import com.paterson.Helpers.IPictureRetriever;
import com.paterson.cowpat.android.AndroidLauncher;

public class AndroidPictureRetriever extends Activity implements IPictureRetriever  {

	private String selectedPicturePath = null;
	private Boolean wasCancelled = false;
	
	private AndroidLauncher launcher;
	
	public AndroidPictureRetriever(AndroidLauncher launcher)
	{
		super();
		this.launcher = launcher;
	}
	
	
	@Override
	public void selectPicture() {
		launcher.dispatchGetPictureIntent();
	}
	

	@Override
	public String getPicturePath() {
		return selectedPicturePath;
	}


	@Override
	public Boolean wasCancelled() {
		return this.wasCancelled;
	}
	
}
