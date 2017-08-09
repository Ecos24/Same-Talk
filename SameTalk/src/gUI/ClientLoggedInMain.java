package gUI;

//(DefaultMutableTreeNode) usersTree.getLastSelectedPathComponent().toString()

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import beanClasses.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import client.ClientListenerForServer;
import client.ClientMain;
import clientHelper.CUtil;
import clientHelper.FileFunctions;
import helper.ChatMessage;

public class ClientLoggedInMain
{
	private final int framex = 100;
	private final int framey = 100;
	private final int frameLength = 1000;
	private final int frameheigth = 610;
	private final Color bgColor = new Color(238, 238, 238);
	
	private ClientMain client;
	private ClientListenerForServer clistenerForServer;
	private User currentUser;
	private String targetAudience;
	
	public JFrame clientFrame;

	private JLabel sameTimeLogo;
	private JButton broadcastChat;
	private JButton personalChats;
	private JButton groupChat;
	private JButton logOutBtn;
	private JLabel currentChatStatus;
	private JScrollPane userTreeScrollPane;
	private JTree usersTree;
	private JScrollPane messageBoxScrollPane;
	private JTextArea messageBox;
	private JTextField readMessage;
	private JButton sendMessage;
	private JButton shareFile;

	/**
	 * Create the application.
	 */
	public ClientLoggedInMain(User user, ClientMain client)
	{
		// Initialize References.
		this.currentUser = user;
		this.client = client;
		
		initComponents();
		initializeFrame();
		associateFrameComponents();
		initListeners();

		// Create the Thread to listen from server.
		clistenerForServer = new ClientListenerForServer(client.getServerInputStream(), clientFrame,
				messageBox, usersTree);
		clistenerForServer.start();
		
		broadcastChat.doClick();	
		readMessage.requestFocus();
	}
		
	private void initListeners()
	{
		broadcastChat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Test the connection
				checkServerListener();
				
				targetAudience = ChatMessage.MESSAGE_TARGET_BROADCAST;
				currentChatStatus.setText("BroadCasting");
				usersTree.setEnabled(false);
				usersTree.setFocusable(false);
				
				CUtil.expandAllNodes(usersTree, 0, usersTree.getRowCount());
			}
		});
		groupChat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Test the connection
				checkServerListener();
				
				targetAudience = ChatMessage.MESSAGE_TARGET_GROUP;
				currentChatStatus.setText("Group Chat");
				usersTree.setEnabled(true);
				usersTree.setFocusable(true);
			}
		});
		personalChats.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Test the connection
				checkServerListener();
				
				targetAudience = ChatMessage.MESSAGE_TARGET_PERSONAL;
				currentChatStatus.setText("Personal Chat");
				usersTree.setEnabled(true);
				usersTree.setFocusable(true);
			}
		});
		
		sendMessage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Test the connection
				checkServerListener();
				
				String msg = readMessage.getText();
				if( !msg.equals("") )
				{
					if( msg.equalsIgnoreCase("WHOSETHERE"))
					{
						readMessage.setText(null);
						client.sendMessage(new ChatMessage(currentUser.getUserId(), ChatMessage.WHOSETHERE, ""));
					}
					else
					{
						// Check Message.
						readMessage.setText(null);
						ChatMessage chat = new ChatMessage(currentUser.getUserId(), ChatMessage.MESSAGE, msg);
						chat.setMsgTargetType(targetAudience);
						if( targetAudience.equals(ChatMessage.MESSAGE_TARGET_PERSONAL) )
							chat.setMsgTarget( ((DefaultMutableTreeNode)usersTree
									.getLastSelectedPathComponent()).toString() );
						client.sendMessage(chat);
					}
				}
			}
		});
		
		shareFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Test the connection
				checkServerListener();
				
				String path = FileFunctions.selectFile();
				int ans = JOptionPane.showConfirmDialog(clientFrame, "Do you really want's to share File -"+path);
				if( ans == 0 )
				{
					ChatMessage chat = new ChatMessage(currentUser.getUserId(), ChatMessage.MESSAGE, FileFunctions.getFileName(path));
					chat.setFileCheck(true);
					chat.setFile(new File(path));
					chat.setMsgTargetType(targetAudience);
					if( targetAudience.equals(ChatMessage.MESSAGE_TARGET_PERSONAL) )
						chat.setMsgTarget( ((DefaultMutableTreeNode)usersTree
								.getLastSelectedPathComponent()).toString() );
					client.sendMessage(chat);
					
				}
				else if( ans == 1 )
				{
					shareFile.doClick();
				}
				else if( ans == 2)
					return;
			}
		});

		logOutBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Test the connection
				checkServerListener();
				
				try
				{
					client.sendMessage(new ChatMessage(currentUser.getUserId(), ChatMessage.LOGOUT, ""));
				}
				catch( NullPointerException e )
				{
					System.out.println("User was not LoggedIn in First Place Quiting the Application.");
					clientFrame.dispose();
				}
				clientFrame.dispose();
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
		clientFrame.getContentPane().add(sameTimeLogo);
		clientFrame.getContentPane().add(broadcastChat);
		clientFrame.getContentPane().add(personalChats);
		clientFrame.getContentPane().add(groupChat);
		clientFrame.getContentPane().add(logOutBtn);
		clientFrame.getContentPane().add(currentChatStatus);
		clientFrame.getContentPane().add(userTreeScrollPane);
		clientFrame.getContentPane().add(messageBoxScrollPane);
		clientFrame.getContentPane().add(readMessage);
		clientFrame.getContentPane().add(sendMessage);
		clientFrame.getContentPane().add(shareFile);
		
		clientFrame.getRootPane().setDefaultButton(sendMessage);
	}
	
	private void initComponents()
	{
		sameTimeLogo = new JLabel();
		sameTimeLogo.setBounds(5, 5, 205, 65);
		sameTimeLogo.setIcon(new ImageIcon(
				new ImageIcon("images/sameTime.png").getImage()
				.getScaledInstance(sameTimeLogo.getWidth(), sameTimeLogo.getHeight(),
						Image.SCALE_SMOOTH)) );
		sameTimeLogo.setFocusable(false);
		
		broadcastChat = new JButton("Broadcast");
		broadcastChat.setBounds(220, 20, 110, 40);
		
		personalChats = new JButton("Personal");
		personalChats.setBounds(340, 20, 100, 40);
		
		groupChat = new JButton("Groups");
		groupChat.setBounds(460, 20, 90, 40);
		
		logOutBtn = new JButton("Logout");
		logOutBtn.setBounds(890, 20, 90, 40);
		
		currentChatStatus = new JLabel();
		currentChatStatus.setFont(new Font("Dialog", Font.BOLD, 17));
		currentChatStatus.setBounds(10, 80, 200, 40);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Users");
		usersTree = new JTree(root);
		usersTree.setFocusable(false);
		userTreeScrollPane = new JScrollPane(usersTree);
		userTreeScrollPane.setBounds(10, 130, 200, 460);
		
		messageBox = new JTextArea();
		messageBox.setEditable(false);
		messageBox.setFocusable(false);
		messageBox.setFont(new Font("Dialog", Font.PLAIN, 15));
		messageBox.setText("(Recieve Time) (Sent Time) (Username) : (Message)\n");
		messageBoxScrollPane = new JScrollPane(messageBox);
		messageBoxScrollPane.setBounds(220, 80, 760, 450);
		
		readMessage = new JTextField();
		readMessage.setFont(new Font("Dialog", Font.PLAIN, 15));
		readMessage.setBounds(220, 550, 560, 40);
		
		sendMessage = new JButton("Send");
		sendMessage.setBounds(790, 550, 70, 40);
		
		shareFile = new JButton("Share File");
		shareFile.setBounds(870, 550, 110, 40);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		clientFrame = new JFrame("Same Time: Welcome "+this.currentUser.getUserId());
		clientFrame.setBounds(framex, framey, frameLength, frameheigth);
		clientFrame.setBackground(bgColor);
		clientFrame.getContentPane().setLayout(null);
		clientFrame.setResizable(false);
		clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		clientFrame.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				// TODO Auto-generated method stub
				logOutBtn.doClick();
				super.windowClosing(e);
			}
		});
	}
	
	private void checkServerListener()
	{
		if( !clistenerForServer.isAlive() )
		{
			JOptionPane.showMessageDialog(clientFrame, "Server has Shutdown exiting the application!!!");
			System.exit(1);
		}
	}
}
