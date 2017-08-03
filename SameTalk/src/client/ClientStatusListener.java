package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import beanClasses.ClientStatus;

public class ClientStatusListener extends Thread
{
	public boolean keepGoing = true;
	
	private ObjectInputStream inputStream;
	private JTree usersTree;
	
	private ArrayList<ClientStatus> clientStatusList;

	public ClientStatusListener(ObjectInputStream inputStream, JTree usersTree)
	{
		super();
		this.inputStream = inputStream;
		this.usersTree = usersTree;
	}	
	

	@Override
	public void run()
	{
		super.run();
		
		while( keepGoing )
		{
			try
			{
				Object obj = inputStream.readObject();
				if( obj instanceof ArrayList<?> )
				{
					ArrayList<?> al = (ArrayList<?>) obj;
					if( al.size() > 0 )
					{
						if( al.get(0) instanceof ClientStatus )
						{
							// Correct Object received.
							clientStatusList = (ArrayList<ClientStatus>) obj;
						}
						else
							continue;
					}
					else
						continue;
				}
				else
					continue;
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IOException e)
			{
				System.out.println("Exception " + e.getClass().getName() + " occured in " +
						this.getClass().getName() + " --> " +e.getMessage() );
				e.printStackTrace();
				
				continue;
			}
			
			// Update the Tree.
			DefaultTreeModel tree = (DefaultTreeModel) usersTree.getModel();
			DefaultMutableTreeNode userRoot = (DefaultMutableTreeNode) tree.getRoot();
			userRoot.removeAllChildren();
			tree.reload();
			/*DefaultMutableTreeNode managers = new DefaultMutableTreeNode("Managers");
			managers.add(new DefaultMutableTreeNode("Help"));
			managers.add(new DefaultMutableTreeNode("Help1"));
			managers.add(new DefaultMutableTreeNode("Help2"));
			tree.insertNodeInto(managers, userRoot, userRoot.getChildCount());*/
			for(ClientStatus clientStatus : clientStatusList)
			{
				System.out.println(clientStatus.getClientId() + " --> " + clientStatus.getClientStatus());
			}
		}
	}
}