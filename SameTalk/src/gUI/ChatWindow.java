package gUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import beanClasses.User;
import client.ClientListenerForServer;
import client.ClientMain;
import clientHelper.CUtil;
import clientHelper.FileFunctions;
import helper.ChatMessage;
import helper.WordUtil;

public class ChatWindow
{
	private final int framex = 100;
	private final int framey = 100;
	private final int frameLength = 700;
	private final int frameheigth = 400;
	private final Color bgColor = new Color(238, 238, 238);
	
	private ClientMain client;
	private ClientListenerForServer clistenerForServer;
	private CUtil cutil;
	private User currentUser;
	private String targetAudience;
	private String selected;
	
	public JFrame clientChatFrame;
	
	private JScrollPane messageBoxScrollPane;
	private JTextArea messageBox;
	private JTextField readMessage;
	private JButton sendMessage;
	private JButton shareFile;
	
	
	/**
	 * Create the application.
	 */
	public ChatWindow( String targetAudience, String selectedNode, ClientListenerForServer clistenerForServer,
			User currentUser, ClientMain client, CUtil cutil)
	{
		// Initialize References.
		this.selected = selectedNode;
		this.clistenerForServer = clistenerForServer;
		this.cutil = cutil;
		this.currentUser = currentUser;
		this.client = client;
		this.targetAudience = targetAudience;
		
		// Setup Frame.
		initComponents();
		initializeFrame();
		associateFrameComponents();
		initListeners();
		
		// Set the MessageBox to be used.
		cutil.setPrGpMessageBox(messageBox);
	}
	
	private void initListeners()
	{
		// For Sending Message.
		sendMessage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Test the connection
				checkServerListener();

				String msg = readMessage.getText();
				if(!msg.equals(""))
				{
					// Check Message.
					readMessage.setText(null);
					ChatMessage chat = new ChatMessage(currentUser.getUserId(), ChatMessage.MESSAGE, msg);
					chat.setMsgTargetType(targetAudience);
					chat.setMsgTarget(ClientLoggedInMain.idNameMapping.get(selected));
					client.sendMessage(chat);
				}
			}
		});

		// BroadCast File.
		shareFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Test the connection
				checkServerListener();

				String path = FileFunctions.selectFile();
				int ans = JOptionPane.showConfirmDialog(clientChatFrame, "Do you really want's to share File -" + path);
				if(ans == 0)
				{
					ChatMessage chat = new ChatMessage(currentUser.getUserId(), ChatMessage.MESSAGE,
							FileFunctions.getFileName(path));
					chat.setFileCheck(true);
					chat.setFile(new File(path));
					chat.setMsgTargetType(targetAudience);
					chat.setMsgTarget(selected);
					client.sendMessage(chat);
				}
				else if(ans == 1)
				{
					shareFile.doClick();
				}
				else if(ans == 2)
					return;
			}
		});
		
		messageBoxScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{	
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});
	}
	
	private void associateFrameComponents()
	{
		clientChatFrame.getContentPane().add(messageBoxScrollPane);
		clientChatFrame.getContentPane().add(readMessage);
		clientChatFrame.getContentPane().add(sendMessage);
		clientChatFrame.getContentPane().add(shareFile);
		
		clientChatFrame.getRootPane().setDefaultButton(sendMessage);
	}
	
	private void initComponents()
	{
		messageBox = new JTextArea();
		messageBox.setEditable(false);
		messageBox.setFocusable(false);
		messageBox.setFont(new Font("Dialog", Font.PLAIN, 15));
		messageBox.setText("(Recieve Time) (Sent Time) (Username) : (Message)\n");
		messageBoxScrollPane = new JScrollPane(messageBox);
		messageBoxScrollPane.setBounds(10, 10, 680, 320);
		
		readMessage = new JTextField();
		readMessage.setFont(new Font("Dialog", Font.PLAIN, 15));
		readMessage.setBounds(10, 340, 480, 40);
		
		sendMessage = new JButton("Send");
		sendMessage.setBounds(500, 340, 70, 40);
		
		shareFile = new JButton("Share File");
		shareFile.setBounds(580, 340, 110, 40);
	}
	
	private void initializeFrame()
	{
		clientChatFrame = new JFrame(WordUtil.capitalizeString(targetAudience) + " : " + WordUtil.capitalizeString(selected));
		clientChatFrame.setBounds(framex, framey, frameLength, frameheigth);
		clientChatFrame.setBackground(bgColor);
		clientChatFrame.getContentPane().setLayout(null);
		clientChatFrame.setResizable(false);
		clientChatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		clientChatFrame.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				// Set the previously set Message Box to null.
				cutil.setPrGpMessageBox(null);
				super.windowClosing(e);
			}
		});
	}
	
	private void checkServerListener()
	{
		if( !clistenerForServer.isAlive() )
		{
			JOptionPane.showMessageDialog(clientChatFrame, "Server has Shutdown exiting the application!!!");
			System.exit(1);
		}
	}
}
