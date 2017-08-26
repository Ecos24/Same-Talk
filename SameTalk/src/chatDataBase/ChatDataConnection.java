package chatDataBase;

import java.io.File;
import java.io.IOException;

import javax.swing.JTextArea;

import beanClasses.ChatMessage;
import clientHelper.CUtil;
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
	private String currentId;
	private ThreadChatRead readingThread;
	
	// Constructor.
	public ChatDataConnection(String currentId)
	{
		super();
		this.currentId = currentId;
	}
	
	private ThreadChatRead getReadingThread()
	{
		return readingThread;
	}
	private void setReadingThread(ThreadChatRead readingThread)
	{
		this.readingThread = readingThread;
	}

	// File to Write to File.
	public void writeChatIntermediator( ChatMessage chatMessage ) throws IOException
	{
		String filePath;
		switch(chatMessage.getMsgTargetType())
		{
			case ChatMessage.MESSAGE_TARGET_BROADCAST:
				filePath = mainPath+File.separator+"BroadCast";
				File dirBroadcast = new File(filePath);
				if( !dirBroadcast.exists() )
				{
					dirBroadcast.mkdir();
					ChatUtil.createCSS(filePath);
				}
				try
				{
					writeChat(filePath, chatMessage.getMessage(), chatMessage.getSendersUsername(),
							chatMessage.getReceivedTime(),
							CUtil.getGeneratorCode(chatMessage.getSendersId(), currentId), "BroadCast");
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
				{
					dirGroup.mkdirs();
					ChatUtil.createCSS(filePath);
				}
				try
				{
					writeChat(filePath, chatMessage.getMessage(), chatMessage.getSendersUsername(),
							chatMessage.getReceivedTime(),
							CUtil.getGeneratorCode(chatMessage.getSendersId(), currentId),"Group");
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
				{
					dirPersonal.mkdirs();
					ChatUtil.createCSS(filePath);
				}
				try
				{
					String name = CUtil.getKeyForValue(chatMessage.getMsgTarget());
					writeChatPersonal(filePath, chatMessage.getMessage(), chatMessage.getReceivedTime(),
							CUtil.getGeneratorCode(chatMessage.getSendersId(), currentId), name);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Switch case not found while trying to write chat.");
		}
	}
	
	// Main Function to Write Chat to File.
	private void writeChat(String filePath, String msg, String senderName, String recTime, int generator,
			String chatName) throws IOException
	{
		File target = new File(filePath);
		File[] all = target.listFiles();
		if( all.length > 1 )
		{
			for(File file : all)
			{
				if( file.getName().equalsIgnoreCase(DateWordFormatter.getCurrentDate()) )
				{
					ChatUtil.updateHTML(file.getAbsolutePath(), senderName, recTime, msg, generator);
					return;
				}
			}
		}
		else
		{
			filePath += File.separator+DateWordFormatter.getCurrentDate();
			ChatUtil.writeRawHTML(filePath, "Chat with :- "+chatName);
			ChatUtil.updateHTML(filePath, senderName, recTime, msg, generator);
		}
	}
	private void writeChatPersonal(String filePath, String msg, String recTime, int generator,
			String chatName) throws IOException
	{
		File target = new File(filePath);
		File[] all = target.listFiles();
		if( all.length > 1 )
		{
			for(File file : all)
			{
				if( file.getName().equalsIgnoreCase(DateWordFormatter.getCurrentDate()) )
				{
					ChatUtil.updateHTMLPersonal(file.getAbsolutePath(), msg, recTime, generator);
					return;
				}
			}
		}
		else
		{
			filePath += File.separator+DateWordFormatter.getCurrentDate();
			ChatUtil.writeRawHTML(filePath, "Chat with :- "+chatName);
			ChatUtil.updateHTMLPersonal(filePath, msg, recTime, generator);
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
