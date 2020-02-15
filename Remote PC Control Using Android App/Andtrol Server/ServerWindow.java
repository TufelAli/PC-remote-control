import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ServerWindow
{
	private RemoteDataServer server;
	private Thread sThread; 
	private String ipAddress;
	private JFrame window = new JFrame();
	private JLabel addressLabel = new JLabel("asdasdasdasd");
	private JLabel portLabel = new JLabel("PORT: ");
	private JTextArea[] forLocation = new JTextArea[3];
	private JLabel portTF = new JLabel();
	private JLabel serverMessages = new JLabel("Not Connected");
	private Image image;
	
	TrayIcon trayIcon;
    SystemTray tray;
	
	public ServerWindow() throws IOException
	{
		initializeWindow();
		
		if(SystemTray.isSupported())
		{
			tray=SystemTray.getSystemTray();
			PopupMenu popup=new PopupMenu();
			
			MenuItem openItem=new MenuItem("Open");
            openItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					tray.remove(trayIcon);
                    window.setVisible(true);
                    window.setExtendedState(JFrame.NORMAL);
                }
            });
			
            MenuItem exitItem=new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Exiting....");
                    System.exit(0);
                }
            });
			
            popup.add(openItem);
			popup.add(exitItem);
            trayIcon=new TrayIcon(image, "Andtrol Server", popup);
            trayIcon.setImageAutoSize(true);
                
            window.addWindowStateListener(new WindowStateListener() {
				public void windowStateChanged(WindowEvent e) {
					if(e.getNewState()==window.ICONIFIED){
						try {
							tray.add(trayIcon);
							window.setVisible(false);
							//System.out.println("added to SystemTray");
						} catch (AWTException ex) {
							System.out.println("unable to add to tray");
						}
					}
					if(e.getNewState()==7){
						try{
							tray.add(trayIcon);
							window.setVisible(false);
							//System.out.println("added to SystemTray");
						}catch(AWTException ex){
							System.out.println("unable to add to system tray");
						}
					}
					if(e.getNewState()==window.MAXIMIZED_BOTH){
						tray.remove(trayIcon);
						window.setVisible(true);
						System.out.println("Tray icon removed");
					}
					if(e.getNewState()==window.NORMAL){
						tray.remove(trayIcon);
						window.setVisible(true);
						System.out.println("Tray icon removed");
					}
				}
			});
		}
		else{
            System.out.println("system tray not supported");
        }
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setResizable(false);
	}
	
	public void initializeWindow()
	{
		try
		{
			server = new RemoteDataServer();
			window.setSize(180, 115);
			
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setTitle("Andtrol");
			Toolkit ico = Toolkit.getDefaultToolkit();
			image = ico.getImage("img/icon.png");
			window.setIconImage(image);
			
			Container c = window.getContentPane();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(layout.LEFT);
			c.setLayout(layout);
			
			try
			{
				InetAddress ip = InetAddress.getLocalHost();
				ipAddress = ip.getHostAddress();
				addressLabel.setText("IP Address: " + ipAddress);
			}
			catch(Exception e)
			{
				addressLabel.setText("IP Address Not Founded");
			}
			
					int x;
			for(x = 0; x < 3; x++)
			{
				forLocation[x] = new JTextArea("", 2, 2);
				forLocation[x].setEditable(false);
				forLocation[x].setBackground(window.getBackground());
			}
			c.add(addressLabel);
			c.add(portLabel);
			portTF.setText("5444");
			c.add(portTF);
			c.add(forLocation[0]);
			c.add(forLocation[1]);
			c.add(serverMessages);
			
			int port = Integer.parseInt(portTF.getText());
            runServer(port);
		}
		catch(Exception e)
		{
			addressLabel.setText("Unexpected exception occurs. Kindly contact admin");
		}
	}
	
	public void runServer(int port)
	{
		if(port <= 9999)
		{
			server.setPort(port);
			sThread = new Thread(server);
			sThread.start();
		}
		else
		{
			serverMessages.setText("The port Number must be less than 10000");
		}
	}
	
	public void closeServer()
	{
		serverMessages.setText("Disconnected");
		server.shutdown();
	}
	
	public static void main(String[] args) throws IOException
	{
		new ServerWindow();
	}
	
	public class RemoteDataServer implements Runnable
	{
		int PORT;
		private DatagramSocket server;
		private byte[] buf;
		private DatagramPacket dgp;
		private String message;
		private AutoBot bot;
		
		public RemoteDataServer(int port) throws IOException
		{
			PORT = port;
			buf = new byte[1000];
                        bot = new AutoBot();
			dgp = new DatagramPacket(buf, buf.length);
			serverMessages.setText("Not Connected");
		}
		
		public RemoteDataServer() throws IOException
		{
			buf = new byte[1000];
			bot = new AutoBot();
			dgp = new DatagramPacket(buf, buf.length);
			serverMessages.setText("Not Connected");
		}
		
		public String getIpAddress()
		{
			String returnStr;
			try{
					InetAddress ip = InetAddress.getLocalHost();
					returnStr = ip.getCanonicalHostName();
			}
			catch(Exception e){ returnStr = new String("Could Not Resolve Ip Address");}
			return returnStr;
		}
		
		public void setPort(int port)
		{
			PORT = port;
		}
		
		public void shutdown()
		{
			try{
                server.close();
				serverMessages.setText("Disconnected");}
			catch(Exception e){}
		}
		
		public void run()
		{
			boolean connected = false;
			boolean isUserConnected = false;
			
			try 
			{
                InetAddress ip = InetAddress.getLocalHost(); 
				serverMessages.setText("Waiting...");
				server = new DatagramSocket(PORT, ip);
				connected = true;
			}
			catch(BindException e)
			{
			serverMessages.setText("Port " + PORT + " is already in use."); 
			}
			catch(Exception e)
			{
				serverMessages.setText("Unable to connect");
			}
			
			while(connected)
			{
				
				try
				{ 
                    server.receive(dgp);
					message = new String(dgp.getData(), 0, dgp.getLength());
					
					if (message.equals("Connectivity..."))
					{
						if(!isUserConnected)
						{
							serverMessages.setText("Connecting...");
							server.send(dgp); 
							isUserConnected = true;
							
							if(SystemTray.isSupported())
							{
								tray.add(trayIcon);
								window.setVisible(false);
							}
						}
						else
						{
							//do nothing
						}
					}
					else if(message.equals("Connected"))
					{
						server.send(dgp); 
					}
					else if(message.equals("Close"))
					{
						
						serverMessages.setText("Controller has Disconnected.");
						isUserConnected = false;
						if(SystemTray.isSupported())
						{
							tray.remove(trayIcon);
							window.setVisible(true);
						}
					}
					else
					{
						serverMessages.setText("Connected to Controller");
						bot.handleMessage(message);
					}
				}
				catch(Exception e)
				{
					serverMessages.setText("Disconnected");
					connected = false;
					isUserConnected = false;
					
					if(SystemTray.isSupported())
					{
						tray.remove(trayIcon);
						window.setVisible(true);
					}
				}
			}
		}
	}
}
