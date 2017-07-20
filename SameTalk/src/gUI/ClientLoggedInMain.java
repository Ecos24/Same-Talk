package gUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;

import client.ClientMain;
import helper.ChatMessage;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class ClientLoggedInMain
{
	private final int framex = 100;
	private final int framey = 100;
	private final int frameLength = 600;
	private final int frameheigth = 400;
	private final Color bgColor = new Color(238, 238, 238);;
	
	private ClientMain client;
	
	private JFrame clientFrame;

	private JLabel sameTimeLogo;
	private JButton logOutBtn;
	private JTree usersTree;
	private JTextArea messageBox;
	private JTextField readMessage;
	private JButton sendMessage;
	private JButton shareFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ClientLoggedInMain window = new ClientLoggedInMain();
					window.clientFrame.setVisible(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientLoggedInMain()
	{
		initComponents();
		initClient();
		initializeFrame();
		associateFrameComponents();
		initListeners();
	}
	
	private void initClient()
	{
		int porNumber = 4501;
		String serverAddress = "192.168.31.91";
		String userName = "Anonymous";
		
		client = new ClientMain(serverAddress, userName, porNumber, messageBox);
		if( !client.start() )
			return;
	}
	
	private void initListeners()
	{
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
						client.sendMessage(new ChatMessage(ChatMessage.WHOSETHERE, ""));
					}
					else
					{
						readMessage.setText(null);
						client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
					}
				}
			}
		});
		
		shareFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});

		logOutBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
				clientFrame.dispose();
			}
		});
	}
	
	private void associateFrameComponents()
	{
		clientFrame.getContentPane().add(sameTimeLogo);
		clientFrame.getContentPane().add(usersTree);
		clientFrame.getContentPane().add(messageBox);
		clientFrame.getContentPane().add(readMessage);
		clientFrame.getContentPane().add(sendMessage);
		clientFrame.getContentPane().add(shareFile);
		clientFrame.getContentPane().add(logOutBtn);
		
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
		
		logOutBtn = new JButton("Logout");
		logOutBtn.setBounds(500, 20, 90, 40);
		
		usersTree = new JTree();
		//usersTree.setFont(new Font("Dialog", Font.PLAIN, 15));
		usersTree.setBounds(10, 70, 110, 310);
		
		messageBox = new JTextArea();
		messageBox.setEditable(false);
		messageBox.setFocusable(false);
		messageBox.setFont(new Font("Dialog", Font.PLAIN, 15));
		messageBox.setBounds(130, 70, 460, 250);
		
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
