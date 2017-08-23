package chatDataBase;

import java.io.File;

import javax.swing.JTextArea;

import beanClasses.ChatMessage;

public class ChatDataConnection
{
	// Main Directory.
	static String mainPath;
	static
	{
		File rootDirectoryForApp = new File(System.getProperty("user.home")+File.separator+"Same Talk");
		if( rootDirectoryForApp.isDirectory() )
		{
			// Do nothing as root exists.
		}
		else
		{
			rootDirectoryForApp.mkdir();
		}
		mainPath = rootDirectoryForApp.getAbsolutePath();
	}
	
	// Per User.
	public void openUserChatFolder(String userId, String userName)
	{
		mainPath = mainPath+File.separator+userName+"("+userId+")";
		File userFolder = new File(mainPath);
		if( userFolder.isDirectory() )
		{
			// Do nothing as root exists.
		}
		else
		{
			userFolder.mkdir();
		}
		mainPath = userFolder.getAbsolutePath();
	}
	
	// instance DataMembers.
	ThreadChatRead readingThread;
	private ThreadChatRead getReadingThread()
	{
		return readingThread;
	}
	private void setReadingThread(ThreadChatRead readingThread)
	{
		this.readingThread = readingThread;
	}

	// File to Write to File.
	public void writeChat( ChatMessage chatMessage )
	{
		
	}

	// Functions to Read File.
	public void readTargetUserFileFile(String clientId, String clientName, JTextArea messageArea)
	{
		String chatFilePath = mainPath+File.separator+clientName+"("+clientId+")";
		File clientChatFile = new File(chatFilePath);
		File[] clientChat = clientChatFile.listFiles();
		readingThread = new ThreadChatRead(clientChat,messageArea);
		this.setReadingThread(readingThread);
	}
	
	public void startReadingChat()
	{
		this.getReadingThread().start();
	}
}
