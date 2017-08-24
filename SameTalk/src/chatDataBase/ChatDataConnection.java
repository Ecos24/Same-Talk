package chatDataBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTextArea;

import beanClasses.ChatMessage;
import helper.DateWordFormatter;

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
			System.out.println("Main directory already present :- "+rootDirectoryForApp.getAbsolutePath());
		}
		else
		{
			rootDirectoryForApp.mkdir();
			System.out.println("Main directory Made :- "+rootDirectoryForApp.getAbsolutePath());
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
	public void writeChatIntermediator( ChatMessage chatMessage )
	{
		String filePath;
		switch(chatMessage.getMsgTargetType())
		{
			case ChatMessage.MESSAGE_TARGET_BROADCAST:
				filePath = mainPath+File.separator+"BroadCast";
				File dirBroadcast = new File(filePath);
				if( !dirBroadcast.exists() )
					dirBroadcast.mkdir();
				try
				{
					writeChat(filePath, chatMessage.getMessage());
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
			case ChatMessage.MESSAGE_TARGET_GROUP:
				filePath = mainPath+File.separator+"Group"
							+File.separator+chatMessage.getMsgTarget();
				File dirGroup = new File(filePath);
				if( !dirGroup.exists() )
					dirGroup.mkdirs();
				try
				{
					writeChat(filePath, chatMessage.getMessage());
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
			case ChatMessage.MESSAGE_TARGET_PERSONAL:
				filePath = mainPath+File.separator+"Personal"
							+File.separator+chatMessage.getMsgTarget();
				File dirPersonal = new File(filePath);
				if( !dirPersonal.exists() )
					dirPersonal.mkdirs();
				try
				{
					writeChat(filePath, chatMessage.getMessage());
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
		}
	}
	
	// Main Function to Write Chat to File.
	private void writeChat(String filePath, String msg) throws IOException
	{
		File target = new File(filePath);
		File[] all = target.listFiles();
		if( all.length > 0 )
		{
			for(File file : all)
			{
				if( file.getName().equalsIgnoreCase(DateWordFormatter.getCurrentDate()) )
				{
					FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("  "+msg);
					bw.close();
					fw.close();
					return;
				}
			}
		}
		else
		{
			filePath += File.separator+DateWordFormatter.getCurrentDate();
			File newFile = new File(filePath);
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("(Received Time) (Sent Time) (Username) : (Message)\n");
			bw.write("  "+msg);
			bw.close();
			fw.close();
		}
	}

	// Functions to Read File.
	public void readTargetUserFileFile(String targetType, String clientName, String clientId,
			JTextArea messageArea)
	{
		String chatFilePath = "";
		switch(targetType)
		{
			case ChatMessage.MESSAGE_TARGET_BROADCAST:
				chatFilePath = mainPath+File.separator+"BroadCast"
										+File.separator+clientId;
				break;
			case ChatMessage.MESSAGE_TARGET_GROUP:
				chatFilePath = mainPath+File.separator+"Group"
								+File.separator+clientId;
				break;
			case ChatMessage.MESSAGE_TARGET_PERSONAL:
				chatFilePath = mainPath+File.separator+"Personal"
								+File.separator+clientId;
				break;
		}
		System.out.println(chatFilePath);
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
