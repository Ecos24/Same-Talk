package beanClasses;

import java.io.File;
import java.io.Serializable;

/**
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server.
 * When talking from a Java Client to a Java Server it is lot easier to pass Java objects, no
 * need to count bytes or to wait for a line feed at the end of the frame
 * @author ecos
 */
public class ChatMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int MESSAGE = 1, LOGOUT = 2;
	public static final String MESSAGE_TARGET_PERSONAL = "personal", 
			MESSAGE_TARGET_BROADCAST = "broadcast", MESSAGE_TARGET_GROUP = "group"; 
	private int type;
	private String sendersUsername, sendersId;
	private String message, msgTarget, msgTargetType;
	private String receivedTime, sentTime;
	private File file;
	private boolean fileCheck;

	// Constructor
	public ChatMessage(String sendersUsername, String sendersId, int type, String message, String sentTime)
	{
		super();
		this.type = type;
		this.message = message;
		this.sendersUsername = sendersUsername;
		this.sendersId = sendersId;
		this.sentTime = sentTime;
	}

	// Getters
	public int getType()
	{
		return type;
	}
	public String getSendersUsername()
	{
		return sendersUsername;
	}
	public String getSendersId()
	{
		return sendersId;
	}
	public String getMessage()
	{
		return message;
	}
	public String getMsgTarget()
	{
		return msgTarget;
	}
	public String getMsgTargetType()
	{
		return msgTargetType;
	}
	public File getFile()
	{
		return file;
	}
	public boolean isFileCheck()
	{
		return fileCheck;
	}
	public String getReceivedTime()
	{
		return receivedTime;
	}
	public String getSentTime()
	{
		return sentTime;
	}

	// Setters
	public void setFileCheck(boolean fileCheck)
	{
		this.fileCheck = fileCheck;
	}
	public void setReceivedTime(String receivedTime)
	{
		this.receivedTime = receivedTime;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public void setFile(File file)
	{
		this.file = file;
	}
	public void setMsgTarget(String msgTarget)
	{
		this.msgTarget = msgTarget;
	}
	public void setMsgTargetType(String msgTargetType)
	{
		this.msgTargetType = msgTargetType;
	}
}
