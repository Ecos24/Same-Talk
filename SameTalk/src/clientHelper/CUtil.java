package clientHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTree;

import chatDataBase.ChatUtil;

public class CUtil
{
	public static HashMap<String, String> idNameMapping;
	public static String getKeyForValue(String value)
	{
		if( idNameMapping != null )
		{
			Iterable<String> keySet = idNameMapping.keySet();
			for(String key : keySet)
			{
				if( idNameMapping.get(key).equalsIgnoreCase(value) )
					return key;
			}
		}
		return null;
	}
	// To display time in hh:mm:ss
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
	private JTextArea clientMessageBox;
	private JTextArea prGpMessageBox;
	public void setPrGpMessageBox(JTextArea prGpMessageBox)
	{
		this.prGpMessageBox = prGpMessageBox;
	}

	public CUtil(JTextArea messageBox)
	{
		super();
		this.clientMessageBox = messageBox;
	}
	
	/**
	 * Display an event (not a message) to the console or the GUI
	 * 
	 * @param msg
	 */
	public void displayEvent(String msg)
	{
		clientMessageBox.append("  "+msg+"\n");
	}
	
	/**
	 * Used to display Chat in given MessageBox.
	 * @param msg
	 * @param prGpMessageBox
	 */
	public void displayPrivateGroupChat(String msg)
	{
		if( prGpMessageBox != null )
			prGpMessageBox.append("  "+msg+"\n");
	}
	
	public static void expandAllNodes(JTree tree, int startingIndex, int rowCount)
	{
	    for(int i=startingIndex;i<rowCount;++i)
	    {
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount)
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	}

	public static int getGeneratorCode(String sendersId, String receiversId)
	{
		if( sendersId.equalsIgnoreCase(receiversId) )
			return ChatUtil.ORIGINATED_ME;
		else
			return ChatUtil.ORIGINATED_OTHER;
	}
}
