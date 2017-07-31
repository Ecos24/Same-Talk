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
