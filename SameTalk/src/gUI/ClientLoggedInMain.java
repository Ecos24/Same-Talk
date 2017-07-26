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
import javax.swing.tree.DefaultTreeModel;

import beanClasses.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import client.ClientListenerForServer;
import client.ClientMain;
import clientHelper.FileFunctions;
import helper.ChatMessage;
import helper.Util;

public class ClientLoggedInMain
{
	private final int framex = 100;
	private final int framey = 100;
	private final int frameLength = 600;
	private final int frameheigth = 400;
	private final Color bgColor = new Color(238, 238, 238);
	
	private ClientMain client;
	private User currentUser;
	private String targetAudience;
	
	public JFrame clientFrame;

	private JLabel sameTimeLogo;
	private JButton broadcastChat;
	private JButton personalChats;
	private JButton groupChat;
	private JButton logOutBtn;
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
		new ClientListenerForServer(client.getServerInputStream(), messageBox).start();
		
		broadcastChat.doClick();	
		readMessage.requestFocus();
	}
		
	private void initListeners()
	{
		broadcastChat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				targetAudience = ChatMessage.MESSAGE_TARGET_BROADCAST;
				
				DefaultTreeModel tree = (DefaultTreeModel) usersTree.getModel();
				DefaultMutableTreeNode userRoot = (DefaultMutableTreeNode) tree.getRoot();
				userRoot.removeAllChildren();
				tree.reload();
				DefaultMutableTreeNode managers = new DefaultMutableTreeNode("Managers");
				managers.add(new DefaultMutableTreeNode("Help"));
				managers.add(new DefaultMutableTreeNode("Help1"));
				managers.add(new DefaultMutableTreeNode("Help2"));
				tree.insertNodeInto(managers, userRoot, userRoot.getChildCount());
				
				Util.expandAllNodes(usersTree, 0, usersTree.getRowCount());
			}
		});
		groupChat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				targetAudience = ChatMessage.MESSAGE_TARGET_GROUP;
			}
		});
		personalChats.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				targetAudience = ChatMessage.MESSAGE_TARGET_PERSONAL;
			}
		});
		
		sendMessage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
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
		sameTimeLogo.setBounds(5, 5, 130, 60);
		sameTimeLogo.setIcon(new ImageIcon(
				new ImageIcon("images/sameTime.png").getImage()
				.getScaledInstance(sameTimeLogo.getWidth(), sameTimeLogo.getHeight(),
						Image.SCALE_SMOOTH)) );
		sameTimeLogo.setFocusable(false);
		
		broadcastChat = new JButton("Broadcast");
		broadcastChat.setBounds(150, 20, 110, 40);
		
		personalChats = new JButton("Personal");
		personalChats.setBounds(270, 20, 100, 40);
		
		groupChat = new JButton("Groups");
		groupChat.setBounds(380, 20, 90, 40);
		
		logOutBtn = new JButton("Logout");
		logOutBtn.setBounds(500, 20, 90, 40);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Users");
		usersTree = new JTree(root);
		usersTree.setFocusable(false);
		userTreeScrollPane = new JScrollPane(usersTree);
		userTreeScrollPane.setBounds(10, 70, 110, 310);
		
		messageBox = new JTextArea();
		messageBox.setEditable(false);
		messageBox.setFocusable(false);
		messageBox.setFont(new Font("Dialog", Font.PLAIN, 15));
		messageBox.setText("(Recieve Time) (Sent Time) (Username) : (Message)\n");
		messageBoxScrollPane = new JScrollPane(messageBox);
		messageBoxScrollPane.setBounds(130, 70, 460, 250);
		
		readMessage = new JTextField();
		readMessage.setFont(new Font("Dialog", Font.PLAIN, 15));
		readMessage.setBounds(130, 340, 260, 40);
		
		sendMessage = new JButton("Send");
		sendMessage.setBounds(400, 340, 70, 40);
		
		shareFile = new JButton("Share File");
		shareFile.setBounds(480, 340, 110, 40);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		clientFrame = new JFrame("Same Time");
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
}
