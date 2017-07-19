package comminication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import helper.ChatMessage;
import helper.Util;
import server.Server;

public class ClientThread extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream clientInputStream;
	private ObjectOutputStream clientOutputStream;
	
	// Unique id for Client.
	int id;
	// Clients name.
	String userName;
	// Chat Message from client
	ChatMessage chatMessage;
	// Date of connection
	String date;
	
	public int getUserId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ClientThread(Socket clientSocket, int id)
	{
		super();
		this.clientSocket = clientSocket;
		this.id = id;
		
		// Creating both Data Stream
		try
		{
			// Creating Input Stream
			clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
			// Creating Output Stream
			clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			// Read the UserName
			userName = (String) clientInputStream.readUTF();
			Util.displayEvent(userName + "just Connected");
		}
		catch(IOException e)
		{
			Util.displayEvent("Exception occured while creating Client Thread --> "+e.getMessage());
			return;
		}
		date = new Date().toString();
	}
	
	// What will run Forever.
	@Override
	public void run()
	{
		super.run();
		// To loop until LogOut.
		boolean keepGoing = true;
		while(keepGoing)
		{
			try
			{
				chatMessage = (ChatMessage) clientInputStream.readObject();
			}
			catch(ClassNotFoundException | IOException e)
			{
				if( e.getClass().getName().equals("java.io.EOFException") )
				{
					System.out.println("Client Closed the Application");
					close();
					break;
				}
				Util.displayEvent("Exception reading Stream --> "+e.getMessage());
				e.printStackTrace();
				break;
			}
			
			// Message
			String msg = chatMessage.getMessage();
			
			switch(chatMessage.getType())
			{
				case ChatMessage.MESSAGE:
					Server.broadCast(userName + " : " + msg);
					break;
				
				case ChatMessage.LOGOUT:
					Util.displayEvent(userName + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
					
				case ChatMessage.WHOSETHERE:
					writeMsg("List of the users connected at " + Util.sdf.format(new Date()) + "\n");
					// Scan ArrayList for users connected
					for(int i = 0; i < Server.clientsList.size(); ++i)
					{
						ClientThread ct = Server.clientsList.get(i);
						writeMsg((i+1) + ") " + ct.userName + " since " + ct.date);
					}
					break;

				default:
					break;
			}
		}
		// Remove myself from the arrayList containing the list of the connected Clients.
		Server.remove(id);
		close();
	}
	
	// try to close everything
	public void close()
	{
		// try to close the connection
		try
		{
			if(clientOutputStream != null)
				clientOutputStream.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+userName+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientInputStream != null)
				clientInputStream.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+userName+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientSocket != null)
				clientSocket.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+userName+" Connection --> "+e.getMessage());
			return;
		}
	}

	/*
	 * Write a String to the Client output stream
	 */
	public boolean writeMsg(String msg)
	{
		// If Client is still connected send the message to it
		if( !clientSocket.isConnected() )
		{
			close();
			return false;
		}
		// write the message to the stream
		try
		{
			clientOutputStream.writeObject(msg);
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e)
		{
			Util.displayEvent("Error sending message to " + userName);
			Util.displayEvent(e.getClass().getName()+" Exception --> "+e.getMessage());
		}
		return true;
	}
}