package clientHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileFunctions
{
	/**
	 * This function saves the given file path to project's directory and return's it's path.
	 * @param imgPath
	 * @param userName
	 * @return saved Image Path.
	 */
	@SuppressWarnings("deprecation")
	public static String saveFile(File sourceFile, String userName)
	{
		System.out.println(System.getProperty("user.home")+File.separator+"Downloads");
		File dstFolder = new File(System.getProperty("user.home")+File.separator+"Downloads");
		
		try
		{
			FileUtils.copyFileToDirectory(sourceFile, dstFolder);
			File source = new File(dstFolder.getAbsolutePath()+File.separator+sourceFile.getName());
			Date dt = new Date();
			dstFolder = new File(dstFolder.getAbsolutePath()+File.separator+userName+dt.getYear()+dt.getMonth()+dt.getDate()+dt.getHours()+dt.getMinutes()+dt.getSeconds()+getFileExtention(sourceFile.getAbsolutePath()));
			Path extra = Paths.get(dstFolder.getAbsolutePath()+File.separator+userName+getFileExtention(sourceFile.getAbsolutePath()));
			Files.deleteIfExists(extra);
			FileUtils.moveFile(source, dstFolder);
			dstFolder = new File(dstFolder.getAbsolutePath()+getFileName(sourceFile.getAbsolutePath()));
			return dstFolder.getAbsolutePath();
		}
		catch(IOException e)
		{
			System.out.println(e.getClass().getName()+" Exception occured while saving file --> "+e.getMessage() );
		}
		return null;
	}
	
	/**
	 * This function setup's the File Selector for user and return's the path of selected File.
	 * @return image path
	 */
	public static String selectFile()
	{
		JFileChooser browseFile = new JFileChooser();
		browseFile.setCurrentDirectory(new File(System.getProperty("user.home")) );
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Images", 
				"jpg", "png", "jpeg");
		browseFile.addChoosableFileFilter(imageFilter);
		int result = browseFile.showSaveDialog(null);
		if( result == JFileChooser.APPROVE_OPTION )
		{
			File selectedImage = browseFile.getSelectedFile();
			return selectedImage.getAbsolutePath();
		}
		else if( result == JFileChooser.CANCEL_OPTION )
		{
			System.out.println("No File Selected");
			return null;
		}
		return null;
	}
	
	/**
	 * This function gives the file name of the specified path.
	 * @param imagePath
	 * @return fileName
	 */
	public static String getFileName(String filePath)
	{
		int p = filePath.lastIndexOf(File.separator);
		return filePath.substring((p+1),filePath.length());
	}
	
	/**
	 * This function gives the file extention at the specified path.
	 * @param imagePath
	 * @return
	 */
	public static String getFileExtention(String filePath)
	{
		int i = filePath.lastIndexOf(".");
		int p = filePath.lastIndexOf("/");
		if( i > p )
		{
			return filePath.substring(i);
		}
		return null;
	}
}