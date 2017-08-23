package chatDataBase;

import java.io.File;

import javax.swing.JTextArea;

public class ThreadChatRead extends Thread
{
	private File[] clientFile;
	private JTextArea messageArea;
	
	public ThreadChatRead(File[] clientFile, JTextArea messageArea)
	{
		super();
		this.clientFile = clientFile;
		this.messageArea = messageArea;
	}	
	
	@Override
	public void run()
	{
		
	}
}
