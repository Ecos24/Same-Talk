package comminication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import helper.ChatMessage;
import helper.Util;
import server.Server;

public class ServersClientThread extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream clientInputStream;
	private ObjectOutputStream clientOutputStream;
	
	// Unique id for Client.
	int clientId;
	// Clients name.
	String clientUsername;
	// Chat Message from client
	ChatMessage chatMessage;
	// Date of connection
	String date;
	
	public int getClientId()
	{
		return clientId;
	}
	public void setClientId(int id)
	{
		this.clientId = id;
	}
	public String getClientUserName()
	{
		return clientUsername;
	}
	public void setClientUserName(String userName)
	{
		this.clientUsername = userName;
	}

	public ServersClientThread(Socket clientSocket, int id)
	{
		super();
		this.clientSocket = clientSocket;
		this.clientId = id;
		
		// Creating both Data Stream
		try
		{
			// Creating Input Stream
			clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
			// Creating Output Stream
			clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			// Read the UserName
			System.out.println("Reading UserName");
			clientUsername = (String) clientInputStream.readUTF();
			Util.displayEvent(clientUsername + "just Connected");
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
					switch(chatMessage.getMsgTargetType())
					{
						case ChatMessage.MESSAGE_TARGET_BROADCAST:
							Server.broadCast(clientUsername + " : " + msg, clientId);
							break;
							
						case ChatMessage.MESSAGE_TARGET_GROUP:
							break;
							
						case ChatMessage.MESSAGE_TARGET_PERSONAL:
							break;

						default:
							System.out.println("Discarding Message as Type is not defined");
							break;
					}
					break;
				
				case ChatMessage.LOGOUT:
					Util.displayEvent(clientUsername + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
					
				case ChatMessage.WHOSETHERE:
					writeMsg("List of the users connected at " + Util.sdf.format(new Date()) + "\n");
					// Scan ArrayList for users connected
					for(int i = 0; i < Server.clientsList.size(); ++i)
					{
						ServersClientThread ct = Server.clientsList.get(i);
						writeMsg((i+1) + ") " + ct.clientUsername + " since " + ct.date);
					}
					break;

				default:
					break;
			}
		}
		// Remove myself from the arrayList containing the list of the connected Clients.
		Server.remove(clientId);
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
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+clientUsername+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientInputStream != null)
				clientInputStream.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+clientUsername+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientSocket != null)
				clientSocket.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+clientUsername+" Connection --> "+e.getMessage());
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
			clientOutputStream.flush();
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e)
		{
			Util.displayEvent("Error sending message to " + clientUsername);
			Util.displayEvent(e.getClass().getName()+" Exception --> "+e.getMessage());
		}
		return true;
	}
}