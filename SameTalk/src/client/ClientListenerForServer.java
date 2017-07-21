package client;

import java.io.IOException;
import java.io.ObjectInputStream;

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
	
	// ClientUtil Reference.
	private CUtil utility;

	public ClientListenerForServer(ObjectInputStream serverInputStream, JTextArea messageBox)
	{
		super();
		this.clientMessageBox = messageBox;
		this.serverInputStream = serverInputStream;
		this.utility = new CUtil(clientMessageBox);
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				ChatMessage chatMessage = (ChatMessage) serverInputStream.readObject();
				if( chatMessage.isFileCheck() )
				{
					FileFunctions.saveFile(chatMessage.getFile(), chatMessage.getSendersUsername());
				}
				utility.displayEvent(chatMessage.getMessage());
			}
			catch( ClassNotFoundException | IOException e )
			{
				displayCatchEvents(e.getClass().getName()+" Server has closed Connection --> "+e.getMessage());
				break;
			}
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
