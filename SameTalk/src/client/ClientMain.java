package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

import helper.ChatMessage;

/**
 * This Class provides main Functionality for client communication.
 * @author ecos
 *
 */
public class ClientMain
{
	// For I/O
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStram;
	private Socket serveSocket;

	// Server Details
	private String server, userName;
	private int port;
	
	// MessageBox
	private JTextArea messageBox;

	public ClientMain(String server, String userName, int port, JTextArea messageBox)
	{
		this.server = server;
		this.userName = userName;
		this.port = port;
		this.messageBox = messageBox;
	}

	public boolean start()
	{
		// Try to connect to Server.
		try
		{
			serveSocket = new Socket(server, port);
		}
		// If Connection fails
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Error Connecting to server --> " + e.getMessage());
			return false;
		}

		displayCatchEvents("Connection accepted " + serveSocket.getInetAddress() + ":" + serveSocket.getPort());

		// Creating Both Data Streams
		try
		{
			serverOutputStram = new ObjectOutputStream(serveSocket.getOutputStream());
			serverInputStream = new ObjectInputStream(serveSocket.getInputStream());
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception occured while creating Streams --> " + e.getMessage());
			return false;
		}

		// Create the Thread to listen from server.
		new ClientListenerForServer(serverInputStream, messageBox).start();
		// Send our UserName to the server.
		try
		{
			serverOutputStram.writeUTF(userName);
			serverOutputStram.flush();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception doing LogIn --> " + e.getMessage());
			disconnect();
			return false;
		}

		// Success we inform the caller that it worked.
		return true;
	}

	/**
	 * To send message to Console.
	 */
	private void displayCatchEvents(String msg)
	{
		System.out.println(msg);
	}

	public void sendMessage(ChatMessage msg)
	{
		try
		{
			serverOutputStram.writeObject(msg);
			serverOutputStram.flush();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception writing to server --> " + e.getMessage());
		}
	}

	private void disconnect()
	{
		try
		{
			if(serverOutputStram != null)
				serverOutputStram.close();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName()+" Exception occured while closing OutputStream --> "+e.getCause());
		}
		try
		{
			if (serverInputStream != null)
				serverInputStream.close();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName()+" Exception occured while closing InputStream --> "+e.getCause());
		}
		try
		{
			if (serveSocket != null)
				serveSocket.close();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName()+" Exception occured while closing ServerSocket --> "+e.getCause());
		}
	}
}