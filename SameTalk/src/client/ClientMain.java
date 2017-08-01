package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import beanClasses.User;
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
	public ObjectInputStream getServerInputStream()
	{
		return serverInputStream;
	}
	private ObjectOutputStream serverOutputStram;
	private Socket serveSocket;

	// Server Details
	private String server;
	private User currentUser;
	private int port;

	public ClientMain(String server, User client, int port)
	{
		this.server = server;
		this.currentUser = client;
		this.port = port;
	}
	
	public boolean authenticate() throws IOException, ClassNotFoundException
	{
		// Try to connect to Server.
		try
		{
			serveSocket = new Socket(server, port);

			displayCatchEvents("Connection accepted " + serveSocket.getInetAddress() + ":" + serveSocket.getPort());
		}
		// If Connection fails
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception making connection while LogIn --> " + e.getMessage());
			disconnect();
			throw e;
		}

		// Creating Both Data Streams
		try
		{
			serverOutputStram = new ObjectOutputStream(serveSocket.getOutputStream());
			serverInputStream = new ObjectInputStream(serveSocket.getInputStream());
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception while opening Stream while LogIn --> " + e.getMessage());
			disconnect();
			throw e;
		}
		
		// Send our User Details to the server that needs to be Authenticated.
		try
		{
			serverOutputStram.writeObject(currentUser);
			serverOutputStram.flush();
		}
		catch(IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception writing user Object while LogIn --> " + e.getMessage());
			disconnect();
			throw e;
		}
		
		if( !serveSocket.isConnected() )
		{
			displayCatchEvents("Connection Closed from Server Side might be due to UnAuthenticity of User.");
			return false;
		}
		// Retrieve authentication Token from server.
		User retrievedUser;
		try
		{
			retrievedUser = (User) serverInputStream.readObject();
		}
		catch(ClassNotFoundException | IOException e)
		{
			displayCatchEvents(e.getClass().getName() + " Exception reading Object doing LogIn --> " + e.getMessage());
			disconnect();
			return false;
		}
		
		if( retrievedUser != null )
		{
			if( currentUser.getUniqueToken().equals(retrievedUser.getUniqueToken()) )
			{
				// Success we inform the caller that it worked.
				System.out.println("Client successfully authenticated.");
				
				return true;
			}
		}
		
		disconnect();
		return false;
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