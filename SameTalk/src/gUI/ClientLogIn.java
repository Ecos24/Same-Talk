package gUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.UIManager;

public class ClientLogIn
{
	private ClientMain client;
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 450;
	private static final int frameheigth = 350;
	private Color bgColor = new Color(238, 238, 238);
	private boolean passFlag = true;
	
	private JFrame clientLogInframe;
	private JLabel bgLabel;
	private JTextField serverIp;
	private JTextPane serverIpTextPane;
	private JTextField serverPort;
	private JTextPane serverPortTextPane;
	private JTextPane userNameTextPane;
	private JTextField userName;
	private JTextPane passwordTextPane;
	private JPasswordField password;
	private JButton showHidePass;
	private JButton LogIn;

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
		clientLogInframe.getContentPane().add(bgLabel);
		clientLogInframe.getContentPane().add(serverIp);
		clientLogInframe.getContentPane().add(serverIpTextPane);
		clientLogInframe.getContentPane().add(serverPort);
		clientLogInframe.getContentPane().add(serverPortTextPane);
		clientLogInframe.getContentPane().add(LogIn);
		clientLogInframe.getContentPane().add(userNameTextPane);
		clientLogInframe.getContentPane().add(userName);
		clientLogInframe.getContentPane().add(passwordTextPane);
		clientLogInframe.getContentPane().add(password);
		clientLogInframe.getContentPane().add(showHidePass);

		clientLogInframe.getRootPane().setDefaultButton(LogIn);
	}
	
	private void initComponents()
	{
		// Background Label.
		bgLabel = new JLabel();
		bgLabel.setBounds(((frameLength/2)-30), 20, 60, 85);
		bgLabel.setIcon(new ImageIcon(new ImageIcon("images/sameTime.png").getImage()
				.getScaledInstance(bgLabel.getWidth(), bgLabel.getHeight(), Image.SCALE_SMOOTH)));	
		
		serverIpTextPane = new JTextPane();
		serverIpTextPane.setText("Server Add.");
		serverIpTextPane.setFocusable(false);
		serverIpTextPane.setEditable(false);
		serverIpTextPane.setBackground(UIManager.getColor("Button.background"));
		serverIpTextPane.setBounds(87, 120, 80, 20);
		serverIp = new JTextField();
		serverIp.setBounds(187, 120, 154, 20);
		
		serverPortTextPane = new JTextPane();
		serverPortTextPane.setText("Server Port");
		serverPortTextPane.setFocusable(false);
		serverPortTextPane.setEditable(false);
		serverPortTextPane.setBackground(UIManager.getColor("Button.background"));
		serverPortTextPane.setBounds(87, 152, 80, 20);
		serverPort = new JTextField();
		serverPort.setBounds(187, 152, 154, 20);
		
		userNameTextPane = new JTextPane();
		userNameTextPane.setBackground(bgColor);
		userNameTextPane.setBounds(87, 184, 80, 20);
		userNameTextPane.setText("User Id");
		userNameTextPane.setEditable(false);
		userNameTextPane.setFocusable(false);
		userName = new JTextField();
		userName.setBounds(187, 184, 154, 20);

		passwordTextPane = new JTextPane();
		passwordTextPane.setBackground(bgColor);
		passwordTextPane.setBounds(87, 219, 80, 20);
		passwordTextPane.setText("Password");
		passwordTextPane.setEditable(false);
		passwordTextPane.setFocusable(false);
		password = new JPasswordField();
		password.setBounds(187, 219, 154, 20);
		showHidePass = new JButton();
		showHidePass.setBounds(345, 219, 20, 20);
		showHidePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
				.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(), Image.SCALE_SMOOTH)));
		showHidePass.setToolTipText("Show Password");
		
		LogIn = new JButton("LogIn");
		LogIn.setBounds(((frameLength/2)-65), 268, 130, 20);
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
				int portNumber;
				String serverAddress;
				
				String userN = userName.getText();
				String pass = String.valueOf(password.getPassword());
				if( !serverIp.getText().equals("") && !serverPort.getText().equals("") 
						&& !userN.equals("") && !pass.equals("") )
				{
					final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
					Pattern ipPatter = Pattern.compile(ipv4Pattern);
					Matcher ipMatcher = ipPatter.matcher(serverIp.getText());
					// Validate Server Address.
					if( ipMatcher.matches() || serverIp.getText().equalsIgnoreCase("localhost") )
					{
						serverAddress = serverIp.getText();
						// Validate Port Number.
						try
						{
							portNumber = Integer.parseInt(serverPort.getText());
							if( portNumber<1024 || portNumber>65535 )
							{
								JOptionPane.showMessageDialog(clientLogInframe, "Port Number Should be in Range of 1024 to 65535.");
								return;
							}
						}
						catch(NumberFormatException e)
						{
							JOptionPane.showMessageDialog(clientLogInframe, "Invalid Port Number.");
							return;
						}
						
						// Validate User.
						User user = new User();
						user.setUserId(userN);
		                user.setPassword(pass);
		        		
		        		Calendar cal = Calendar.getInstance();
		        		user.setUniqueToken( user.getUserId()+cal.get(Calendar.YEAR)+
		        				cal.get(Calendar.WEEK_OF_YEAR)+cal.get(Calendar.DAY_OF_WEEK)+
		        				String.valueOf(new Random().nextInt()) );
		        		
		        		client = new ClientMain(serverAddress, user, portNumber);
		        		
		        		try
		                {
		        			User retrivedUser = client.authenticate(); 
		        			if( retrivedUser != null )
			                {
			                    System.out.println("User Authenticated");
			                    ClientLoggedInMain clientLoggedIn = new ClientLoggedInMain(retrivedUser, client);
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
		        		catch(ConnectException e)
		        		{
		        			JOptionPane.showMessageDialog(clientLogInframe, "No connection available on mentioned port.");
		        		}
		                catch(IOException e)
		                {
		                	JOptionPane.showMessageDialog(clientLogInframe, "User Can't Be Authenticated Internal Application Error!!");
		                	System.out.println("Exception --> "+e.getMessage());
						}
					}
					else
					{
						JOptionPane.showMessageDialog(clientLogInframe, "Invalid Server Address.");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(clientLogInframe, "Please Provide Complete Details.");
				}
			}
		});
		
		showHidePass.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( passFlag )
				{
					password.setEchoChar((char) 0);
					showHidePass.setIcon(new ImageIcon(new ImageIcon("images/hidePass.png").getImage()
							.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHidePass.setToolTipText("Hide Password");
					passFlag = false;
				}
				else
				{
					password.setEchoChar('*');
					showHidePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
							.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHidePass.setToolTipText("Show Password");
					passFlag = true;
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