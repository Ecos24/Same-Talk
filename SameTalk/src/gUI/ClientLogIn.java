package gUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import beanClasses.User;
import client.ClientMain;

public class ClientLogIn
{
	private ClientMain client;
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 450;
	private static final int frameheigth = 300;
	private Color bgColor = new Color(238, 238, 238);
	
	private JFrame clientLogInframe;
	private JButton LogIn;
	private JPasswordField password;
	private JTextField userName;
	private JTextPane userNameTextPane;
	private JTextPane passwordTextPane;
	private JLabel bgLabel;

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
					ClientLogIn window = new ClientLogIn();
					window.clientLogInframe.setVisible(true);
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
	public ClientLogIn()
	{
		initializeFrame();
		initComponents();
		initListeners();
		associateFrameComponents();
	}
	
	private void associateFrameComponents()
	{
		//frame.setJMenuBar(menuBar);
		clientLogInframe.getContentPane().add(LogIn);
		clientLogInframe.getContentPane().add(userName);
		clientLogInframe.getContentPane().add(password);
		clientLogInframe.getContentPane().add(userNameTextPane);
		clientLogInframe.getContentPane().add(passwordTextPane);
		clientLogInframe.getContentPane().add(bgLabel);		

		clientLogInframe.getRootPane().setDefaultButton(LogIn);
	}
	
	private void initComponents()
	{
		userNameTextPane = new JTextPane();
		userNameTextPane.setBackground(bgColor);
		userNameTextPane.setBounds(45, 120, 80, 20);
		userNameTextPane.setText("User Name");
		userNameTextPane.setEditable(false);
		userNameTextPane.setFocusable(false);
		userName = new JTextField();
		userName.setBounds(145, 120, 154, 20);

		passwordTextPane = new JTextPane();
		passwordTextPane.setBackground(bgColor);
		passwordTextPane.setBounds(45, 170, 80, 20);
		passwordTextPane.setText("Password");
		passwordTextPane.setEditable(false);
		passwordTextPane.setFocusable(false);
		password = new JPasswordField();
		password.setBounds(145, 170, 154, 20);
		
		LogIn = new JButton("LogIn");
		LogIn.setBounds(157, 218, 131, 20);
		
		// Background Label.
		bgLabel = new JLabel();
		bgLabel.setBounds(190, 20, 61, 85);
		bgLabel.setIcon(new ImageIcon("images/logo.png"));
	}
	

	/////////////////////////////////////////////////////////////////////////
	//////////////////////////Listeners//////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	private void initListeners()
	{
		LogIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String userN = userName.getText().toLowerCase();
				String pass = String.valueOf(password.getPassword());
				if( !userN.equals("") &&  !pass.equals("") )
				{
					User user = new User();
					user.setUserId(userN);
	                user.setPassword(pass);
	                
	        		int porNumber = 4501;
	        		String serverAddress = "localhost";
	        		if( user.getUserId().equals(null))
	        			user.setUserId("Anonymous");
	        		
	        		Calendar cal = Calendar.getInstance();
	        		user.setUniqueToken( user.getUserId()+cal.get(Calendar.YEAR)+
	        				cal.get(Calendar.WEEK_OF_YEAR)+cal.get(Calendar.DAY_OF_WEEK)+
	        				String.valueOf(new Random().nextInt()) );
	        		
	        		client = new ClientMain(serverAddress, user, porNumber);
	        		
	        		try
	                {
	        			if( client.authenticate() )
		                {
		                    System.out.println("User Authenticated");
		                    ClientLoggedInMain clientLoggedIn = new ClientLoggedInMain(user, client);
		                    clientLoggedIn.clientFrame.setVisible(true);
	                		clientLogInframe.dispose();
		                }
		                else
		                {
		                    System.out.println("User Invalid");
		                    JOptionPane.showMessageDialog(clientLogInframe, "Invalid UserName/Password");
		                }
	                }
	                catch(ClassNotFoundException e)
	                {
	                	JOptionPane.showMessageDialog(clientLogInframe, "User Can't Be Authenticated Internal Application Error!!");
	                	System.out.println("JDBC connector Class Not Found.");
					}
	                catch(IOException e)
	                {
	                	JOptionPane.showMessageDialog(clientLogInframe, "User Can't Be Authenticated Internal Application Error!!");
	                	System.out.println("Exception --> "+e.getMessage());
					}
				}
				else if( userN.equals("") )
				{
					JOptionPane.showMessageDialog(clientLogInframe, "Please provide UserName!!");
				}
				else if( pass.equals("") )
				{
					JOptionPane.showMessageDialog(clientLogInframe, "Please provide Password!!");
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		clientLogInframe = new JFrame("Same Talk LogIn");
		clientLogInframe.setBounds(framex, framey, frameLength, frameheigth);
		clientLogInframe.setBackground(bgColor);
		clientLogInframe.getContentPane().setLayout(null);
		clientLogInframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientLogInframe.setVisible(true);
		clientLogInframe.setResizable(false);
	}

}
