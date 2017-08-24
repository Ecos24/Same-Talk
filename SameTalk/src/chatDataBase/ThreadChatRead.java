package chatDataBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
		if( clientFile != null )
		{
			orderFiles();
			for(File file : clientFile)
			{
				try
				{
					BufferedReader br = new BufferedReader(new InputStreamReader
							(new ReverseFileInputStream(file)));
					while(true)
					{
						String r = br.readLine();
						if( r == null )
							break;
						System.out.println(r);
					}
					br.close();
					
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void orderFiles()
	{
		
	}
}
