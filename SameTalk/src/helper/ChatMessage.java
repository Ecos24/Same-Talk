package helper;

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
	public static final int WHOSETHERE = 0, MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String message;

	// Constructor
	public ChatMessage(int type, String message)
	{
		super();
		this.type = type;
		this.message = message;
	}

	// Getters
	public int getType()
	{
		return type;
	}
	public String getMessage()
	{
		return message;
	}
}
