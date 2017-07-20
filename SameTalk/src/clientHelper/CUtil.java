package clientHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

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
		String time = sdf.format(new Date()) + ") " + msg;
		clientMessageBox.append(time+"\n");
	}
}
