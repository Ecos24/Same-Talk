package clientHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTree;

public class CUtil
{
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
		String time = " "+ sdf.format(new Date()) + "\t" + msg;
		clientMessageBox.append(time);
	}
	
	/**
	 * Used to display Chat in given MessageBox.
	 * @param msg
	 * @param prGpMessageBox
	 */
	public void displayPrivateGroupChat(String msg)
	{
		String time = " "+ sdf.format(new Date()) + "\t" + msg;
		
		if( prGpMessageBox != null )
			prGpMessageBox.append(time);
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
}
