package com.paterson.Helpers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class IOHelper {

	public static Boolean getNewTarget()
	{
		File selectedFile = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
		chooser.setAcceptAllFileFilterUsed(false);
		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			selectedFile = chooser.getSelectedFile();
			System.out.println("FilePath selected: " + selectedFile.getPath());
			//AssetLoader.addTargetToList(selectedFile.getPath());
			//AssetLoader.updateTargetImage(selectedFile.getPath(), 0, 0, 60, 60, false);
			//IOHelper.copyFileToLocal(selectedFile.getPath());
			return true;
		}
		return false;
	}
	
	/**
	 * Not used any more 17/5
	 * @param filePath
	 */
	public static void copyFileToLocal(String filePath)
	{
		String relativePath = filePath.substring(Gdx.files.getExternalStoragePath().length());
		System.out.println("FilePath to use: " + relativePath);
		FileHandle selectedPicture = Gdx.files.external(relativePath);
		System.out.println("FilePath to copy from: " + selectedPicture.path());
		if (selectedPicture.exists())
		{
			AssetLoader.addTargetToList(relativePath);
		}
		else
		{
			System.out.println("File to copy from does not exist");
		}
		
	}
}
