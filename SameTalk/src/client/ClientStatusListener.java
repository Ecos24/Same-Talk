package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import beanClasses.ClientStatus;
import clientHelper.CUtil;
import helper.DateWordFormatter;

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
		// Update the Tree.
		DefaultTreeModel tree = (DefaultTreeModel) usersTree.getModel();
		DefaultMutableTreeNode userRoot = (DefaultMutableTreeNode) tree.getRoot();
		userRoot.removeAllChildren();
		tree.reload();
		
		// Initialize IdName Mapper.
		CUtil.idNameMapping = new HashMap<>();
		
		LinkedHashMap<String,LinkedHashMap<String,ArrayList<ClientStatus>>> clientStatusMap =
				(LinkedHashMap<String,LinkedHashMap<String,ArrayList<ClientStatus>>>)obj;
		
		Iterator<String> deptkeys = clientStatusMap.keySet().iterator();
		while(deptkeys.hasNext())
		{
			String dept = deptkeys.next();
			DefaultMutableTreeNode deptNode = new DefaultMutableTreeNode(DateWordFormatter.capitalizeString(dept));
			LinkedHashMap<String,ArrayList<ClientStatus>> singleDeptMap = clientStatusMap.get(dept);
			Iterator<String> posKeys = singleDeptMap.keySet().iterator();
			while( posKeys.hasNext() )
			{
				String pos = posKeys.next();
				DefaultMutableTreeNode posNode = new DefaultMutableTreeNode(DateWordFormatter.capitalizeString(pos));
				ArrayList<ClientStatus> singlePosMap = singleDeptMap.get(pos);
				Iterator<ClientStatus> te = singlePosMap.iterator();
				while( te.hasNext() )
				{
					ClientStatus cste = new ClientStatus();
					cste = te.next();
					CUtil.idNameMapping.put(cste.getClientName(), cste.getClientId());
					posNode.add(new DefaultMutableTreeNode(DateWordFormatter.capitalizeString(cste.getClientName())));
				}
				deptNode.add(posNode);
			}
			tree.insertNodeInto(deptNode, userRoot, userRoot.getChildCount());
		}
		CUtil.expandAllNodes(usersTree, 0, usersTree.getRowCount());
	}
}