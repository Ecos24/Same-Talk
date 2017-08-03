package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import clientHelper.CUtil;
import clientHelper.FileFunctions;
import helper.ChatMessage;

public class ClientListenerForServer extends Thread
{
	// For I/O
	private ObjectInputStream serverInputStream;
	
	// For printing Messages.
	private JTextArea clientMessageBox;
	// Client's Frame reference.
	private JFrame clientFrame;
	
	// ClientUtil Reference.
	private CUtil utility;

	public ClientListenerForServer(ObjectInputStream serverInputStream, JFrame clientFrame , JTextArea messageBox)
	{
		super();
		this.clientFrame = clientFrame;
		this.clientMessageBox = messageBox;
		this.serverInputStream = serverInputStream;
		this.utility = new CUtil(clientMessageBox);
	}
	
	@Override
	public void run()
	{
		Object obj;
		while(true)
		{
			obj = null;
			try
			{
				System.out.println("Reading an OBject from inputStream.");
				obj = serverInputStream.readObject();
				System.out.println("Got an Object");
				if( obj instanceof ChatMessage )
				{
					System.out.println("Object received is chat.");
					ChatMessage chatMessage = (ChatMessage) obj; 
					if( chatMessage.isFileCheck() )
					{
						FileFunctions.saveFile(chatMessage.getFile(), chatMessage.getSendersUsername());
					}
					System.out.println("Displaying Chat received");
					utility.displayEvent(chatMessage.getMessage());
				}
				else
				{
					System.out.println("Object not a chat.");
				}
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("Exception in ClientsServerListener :- " + e.getClass().getName() + " --> " + e.getMessage() );
				continue;
			}
			catch( IOException e )
			{
				displayCatchEvents(e.getClass().getName()+" Server has closed Connection --> "+e.getMessage());
				break;
			}
		}
		if( clientFrame.isActive() )
		{
			JOptionPane.showMessageDialog(null, "Server has Shutdown exiting the application!!!");
			System.exit(1);
		}
	}
	
	/**
	 * To send message to Console.
	 */
	private void displayCatchEvents(String msg)
	{
		System.out.println(msg);
	}
}
