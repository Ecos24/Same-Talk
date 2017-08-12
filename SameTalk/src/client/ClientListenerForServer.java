package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import beanClasses.ClientStatus;
import clientHelper.CUtil;
import clientHelper.FileFunctions;
import helper.ChatMessage;

public class ClientListenerForServer extends Thread
{
	// For I/O
	private ObjectInputStream serverInputStream;
	
	// For printing Messages.
	// Client's Frame reference.
	private JFrame clientFrame;
	
	// ClientUtil Reference.
	private CUtil utility;
	private ClientStatusListener statusListener = null;

	public ClientListenerForServer(ObjectInputStream serverInputStream, JFrame clientFrame ,
			CUtil cutil, JTree userTree)
	{
		super();
		this.clientFrame = clientFrame;
		this.serverInputStream = serverInputStream;
		this.statusListener = new ClientStatusListener(userTree);
		this.utility = cutil;
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
					ChatMessage chatMessage = (ChatMessage) obj; 
					if( chatMessage.isFileCheck() )
					{
						FileFunctions.saveFile(chatMessage.getFile(), chatMessage.getSendersUsername());
					}
					if( chatMessage.getMsgTargetType().equals(ChatMessage.MESSAGE_TARGET_BROADCAST) )
						utility.displayEvent(chatMessage.getMessage());
					else if( chatMessage.getMsgTargetType().equals(ChatMessage.MESSAGE_TARGET_GROUP) ||
							 chatMessage.getMsgTargetType().equals(ChatMessage.MESSAGE_TARGET_PERSONAL) )
					{
						utility.displayPrivateGroupChat(chatMessage.getMessage());
					}
				}
				else if( obj instanceof LinkedHashMap<?, ?> )
				{
					System.out.println("Found LinkedHashMap");
					@SuppressWarnings("unchecked")
					Iterator<LinkedHashMap<String, ArrayList<ClientStatus>>> itcsl = 
							((LinkedHashMap<String,LinkedHashMap<String,ArrayList<ClientStatus>>>) obj).values().iterator();
					if( itcsl.hasNext() )
					{
						Iterator<ArrayList<ClientStatus>> tl = itcsl.next().values().iterator();
						if( tl.hasNext() )
						{
							Iterator<ClientStatus> te = tl.next().iterator();
							if( te.hasNext() )
							{
								if( te.next() instanceof ClientStatus )
									statusListener.updateUserTree(obj);
							}
						}
						else
							continue;
					}
					else
						continue;
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
