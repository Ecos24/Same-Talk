package client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import beanClasses.ClientStatus;

public class ClientStatusListener
{
	private JTree usersTree;

	public ClientStatusListener(JTree usersTree)
	{
		super();
		this.usersTree = usersTree;
	}
	
	@SuppressWarnings("unchecked")
	public void updateUserTree(Object obj)
	{
		System.out.println("Updating Users Tree.");
		Iterator<LinkedHashMap<String, ArrayList<ClientStatus>>> itcsl = 
				((LinkedHashMap<String,LinkedHashMap<String,ArrayList<ClientStatus>>>)obj).values().iterator();
		while(itcsl.hasNext())
		{
			Iterator<ArrayList<ClientStatus>> tl = itcsl.next().values().iterator();
			while( tl.hasNext() )
			{
				Iterator<ClientStatus> te = tl.next().iterator();
				while( te.hasNext() )
				{
					ClientStatus cs = te.next();
					System.out.println(cs.getClientId()+" "+cs.getClientStatus());
				}
			}
		}
		
		// Update the Tree.
		DefaultTreeModel tree = (DefaultTreeModel) usersTree.getModel();
		DefaultMutableTreeNode userRoot = (DefaultMutableTreeNode) tree.getRoot();
		userRoot.removeAllChildren();
		tree.reload();
		
		DefaultMutableTreeNode managers = new DefaultMutableTreeNode("Managers");
		managers.add(new DefaultMutableTreeNode("Help"));
		managers.add(new DefaultMutableTreeNode("Help1"));
		managers.add(new DefaultMutableTreeNode("Help2"));
		tree.insertNodeInto(managers, userRoot, userRoot.getChildCount()); 
	}
}