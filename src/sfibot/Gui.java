package sfibot;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;

import features.EventChannelGenerater;
import sfibot.IdleChecker.IdleCheckerAttribute;
import sfibot.WelcomeMessage.WelcomeMessageTarget;

@SuppressWarnings("serial")
public class Gui extends JPanel {
	
	private static JFrame frame = new JFrame();
    private static JTextArea console = new JTextArea();
    private static JCheckBoxMenuItem slowModeBox = null;
	private static JCheckBoxMenuItem idleCheckerBox = null;
	private static JCheckBoxMenuItem serverActivityBox = null;
	private static JCheckBoxMenuItem chatDisplayBox = null;
	private static JCheckBoxMenuItem chatCommandsBox = null;
	private static JCheckBoxMenuItem eventChansRlGaTourney = null;
	
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //File Menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
	        menuItem = new JMenuItem("Clear Console", KeyEvent.VK_C);
	        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
	        menuItem.addActionListener(new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent e) {
		    			console.setText("");
		        	}
				});
	        menu.add(menuItem);
	//        menu.addSeparator();
	//        menuItem = new JMenuItem("Load Config", KeyEvent.VK_L);
	//        menuItem.addActionListener(new ActionListener()
	//			{
	//	    	@Override
	//	    	public void actionPerformed(ActionEvent e)
	//	        	{
	//	    			//Config.load();
	//	        	}
	//			});
	//        menu.add(menuItem);
	//        menuItem = new JMenuItem("Save Config", KeyEvent.VK_S);
	//        menuItem.addActionListener(new ActionListener()
	//			{
	//	    	@Override
	//	    	public void actionPerformed(ActionEvent e)
	//	        	{
	//	    			//Config.save();
	//	        	}
	//			});
	//        menu.add(menuItem);
        
        menu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new ActionListener() {
	        	@Override
	            public void actionPerformed(ActionEvent e) {
	        		Main.confirmExit();
	            }
	        });
        menu.add(menuItem);

        //Server Menu
        menu = new JMenu("Server");
        menu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menu);
        menuItem = new JMenuItem("Edit Connection Info", KeyEvent.VK_E);
	        menuItem.addActionListener(new ActionListener() {
		        @Override
	        	public void actionPerformed(ActionEvent e) {
        			int option = JOptionPane.showConfirmDialog(frame, ConnectionConfig.createServerInfoPanel(), "Connection Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        			if (option == JOptionPane.OK_OPTION) {
        				ConnectionConfig.updateConfig();
        				Config.saveConfig();
        				if (Server.isConnected()) {
        					ConnectionConfig.updateNickname();
        				}
        				System.out.println(Main.timeStamp() + "Connection information updated.\n\tChanges will take effect next time the bot connects.");
        			}
        			else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
        				return;
        			}
		        }
	    	});
	        menu.add(menuItem);
	        menu.addSeparator();
	        menuItem = new JMenuItem("Connect", KeyEvent.VK_C);
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
					if (!Server.isConnected()) {
						Server.connect();
					}
					else {
						System.out.println(Main.timeStamp() + "Bot is already connected to a server!");
					}
	        	}
	        });
	        menu.add(menuItem);
	        menuItem = new JMenuItem("Disconnect", KeyEvent.VK_D);
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		if (Server.isConnected()) {
	        			Server.disconnect();
	        			System.out.println(Main.timeStamp() + "Bot disconnected from console.");
	        		}
	        		else {
	        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
	        		}
	        	}
	        });
	        menu.add(menuItem);
	        menu.addSeparator();
	        menuItem = new JMenuItem("List Clients", KeyEvent.VK_L);
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		if (Server.isConnected()) {
	        			Server.printOnlineClients();
	        		}
	        		else {
	        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
	        		}
	        	}
    		});
	        menu.add(menuItem);
	        menuItem = new JMenuItem("Recompile Client List");
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		if (Server.isConnected()) {
	        			Server.generateOnlineClients();
	        		}
	        		else {
	        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
	        		}
	        	}
	        });
	        menu.add(menuItem);
	        menu.addSeparator();
	        menuItem = new JMenuItem("List Channels", KeyEvent.VK_H);
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		if (Server.isConnected()) {
		        		Server.printChannels();
	        		}
	        		else {
	        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
	        		}
	        	}
    		});
	        menu.add(menuItem);
	        menuItem = new JMenuItem("List Server Groups", KeyEvent.VK_G);
	        menuItem.addActionListener(new ActionListener() {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		if (Server.isConnected()) {
		        		Server.printServerGroups();
	        		}
	        		else {
	        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
	        		}
	        	}
    		});
	        menu.add(menuItem);
	        submenu = new JMenu("Print Server Snapshot");
	        submenu.setMnemonic(KeyEvent.VK_S);
	        menu.add(submenu);
	        	menuItem = new JMenuItem("With Empty Channels", KeyEvent.VK_W);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (Server.isConnected()) {
			        		Server.printSnapshot(false);
		        		}
		        		else {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        		}
		        	}
	    		});
		        submenu.add(menuItem);
	        	menuItem = new JMenuItem("Without Empty Channels", KeyEvent.VK_O);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (Server.isConnected()) {
			        		Server.printSnapshot(true);
		        		}
		        		else {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        		}
		        	}
	    		});
		        submenu.add(menuItem);
        
        //Modules Menu
        menu = new JMenu("Modules");
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);
	        submenu = new JMenu("Idle-Checker");
	        submenu.setMnemonic(KeyEvent.VK_I);
	        menu.add(submenu);
		        idleCheckerBox = new JCheckBoxMenuItem ("Enabled");
		        idleCheckerBox.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (idleCheckerBox.isSelected() && Server.isConnected()) {
		        			IdleChecker.start();
		        			System.out.println(Main.timeStamp() + "IdleChecker started. Users will be moved after being idle for " + Config.getMaxIdleTimeMinutes() + " minutes.");
		        		}
		        		else if (!idleCheckerBox.isSelected() && Server.isConnected()) {
		        			IdleChecker.stop();
		        			System.out.println(Main.timeStamp() + "IdleChecker stopped.");
		        		}
		        		else if (!Server.isConnected()) {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        			idleCheckerBox.setSelected(false);
		        		}
		        	}
	    		});
		        submenu.add(idleCheckerBox);
		        submenu.addSeparator();
		        menuItem = new JMenuItem("Set Max Idle Time", KeyEvent.VK_T);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		IdleChecker.updateIdleCheckerAttribute(IdleCheckerAttribute.MAX_IDLE_TIME);
		        	}
	    		});
		        submenu.add(menuItem);
		        menuItem = new JMenuItem("Set Idle Destination Channel", KeyEvent.VK_D);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		IdleChecker.updateIdleCheckerAttribute(IdleCheckerAttribute.IDLE_CHANNEL);
		        	}
	    		});
		        submenu.add(menuItem);
		        menuItem = new JMenuItem("Set Idle Ignore Group", KeyEvent.VK_G);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		IdleChecker.updateIdleCheckerAttribute(IdleCheckerAttribute.IDLE_IGNORE_GROUP);
		        	}
	    		});
		        submenu.add(menuItem);
		        menuItem = new JMenuItem("Set Idle Ignore Channel", KeyEvent.VK_C);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		IdleChecker.updateIdleCheckerAttribute(IdleCheckerAttribute.IDLE_IGNORE_CHANNEL);
		        	}
	    		});
		        menuItem.setEnabled(true);
		        submenu.add(menuItem);
	        submenu = new JMenu("Server Activity");
	        submenu.setMnemonic(KeyEvent.VK_A);
	        menu.add(submenu);
		        JMenu subSubMenu = new JMenu("Display");
		        subSubMenu.setMnemonic(KeyEvent.VK_D);
		        submenu.add(subSubMenu);
			        serverActivityBox = new JCheckBoxMenuItem ("Activity");
			        serverActivityBox.setSelected(Config.getServerActivityFlag());
			        serverActivityBox.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
			        		
			        		if (serverActivityBox.isSelected()) {
			        			Config.setServerActivityFlag(true);
			        			System.out.println(Main.timeStamp() + "Server activity display enabled.");
			        			Config.saveConfig();
			        		}
			        		if (!serverActivityBox.isSelected()) {
			        			Config.setServerActivityFlag(false);
			        			System.out.println(Main.timeStamp() + "Server activity display disabled.");
			        			Config.saveConfig();
			        		}
			        	}
	        		});
			        subSubMenu.add(serverActivityBox);
			        chatDisplayBox = new JCheckBoxMenuItem ("Chat");
			        chatDisplayBox.setSelected(Config.getChatDisplayFlag());
			        chatDisplayBox.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
			        		if (chatDisplayBox.isSelected()) {
			        			Config.setChatDisplayFlag(true);
			        			System.out.println(Main.timeStamp() + "Chat activity display enabled.");
			        			Config.saveConfig();
			        		}
			        		if (!chatDisplayBox.isSelected()) {
			        			Config.setChatDisplayFlag(false);
			        			System.out.println(Main.timeStamp() + "Chat activity display disabled.");
			        			Config.saveConfig();
			        		}
			        	}
	        		});
			        subSubMenu.add(chatDisplayBox);
			    subSubMenu = new JMenu("Functionality");
			    subSubMenu.setMnemonic(KeyEvent.VK_F);
			    submenu.add(subSubMenu);
			    	chatCommandsBox = new JCheckBoxMenuItem ("Enable Chat Commands");
			    	chatCommandsBox.setSelected(Config.getChatCommandsFlag());
			    	chatCommandsBox.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
			        		if (chatCommandsBox.isSelected()) {
			        			Config.setChatCommandsFlag(true);
			        			System.out.println(Main.timeStamp() + "Chat commands enabled.");
			        			Config.saveConfig();
			        		}
			        		if (!chatCommandsBox.isSelected()) {
			        			Config.setChatCommandsFlag(false);
			        			System.out.println(Main.timeStamp() + "Chat commands disabled.");
			        			Config.saveConfig();
			        		}
			        	}
	        		});
			    	subSubMenu.add(chatCommandsBox);
			submenu = new JMenu("Messaging");
			submenu.setMnemonic(KeyEvent.VK_M);
			menu.add(submenu);
		        menuItem = new JMenuItem("Send Server Message", KeyEvent.VK_S);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (Server.isConnected()) {
							new MessageSender(TextMessageTargetMode.SERVER);	        		
						}
		        		else {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        		}
		        	}
	        	});
		        submenu.add(menuItem);
		        menuItem = new JMenuItem("Send Channel Message", KeyEvent.VK_C);
		        menuItem.addActionListener(new ActionListener() {
		        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (Server.isConnected()) {
							new MessageSender(TextMessageTargetMode.CHANNEL);	        		
						}
		        		else {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        		}
		        	}
	        	});
		        submenu.add(menuItem);
		        menuItem = new JMenuItem("Send Private Message", KeyEvent.VK_P);
		        menuItem.addActionListener(new ActionListener() {
	        	@Override
		        	public void actionPerformed(ActionEvent e) {
		        		if (Server.isConnected()) {
							new MessageSender(TextMessageTargetMode.CLIENT);
		        		}
		        		else {
		        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
		        		}
		        	}
	        	});
		        submenu.add(menuItem);
		        submenu.addSeparator();
		        subSubMenu = new JMenu("Set Welcome Message For");
		        submenu.add(subSubMenu);
			        menuItem = new JMenuItem("Guest", KeyEvent.VK_G);
			        menuItem.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
		        			int confirm = JOptionPane.showConfirmDialog(frame, WelcomeMessage.getWelcomeMessagePanel(WelcomeMessageTarget.GUEST), "Guest Welcome Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        			if (confirm == JOptionPane.OK_OPTION) {
		        				WelcomeMessage.setNewUserMessage(WelcomeMessage.getWelcomeMessage());
		        				Config.saveConfig();
		        				System.out.println(Main.timeStamp() + "Welcome message for guests has been updated.");
		        			}
			        	}
		        	});
			        subSubMenu.add(menuItem);
			        menuItem = new JMenuItem("Non-Guest", KeyEvent.VK_N);
			        menuItem.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
		        			int confirm = JOptionPane.showConfirmDialog(frame, WelcomeMessage.getWelcomeMessagePanel(WelcomeMessageTarget.NON_GUEST), "Non-Guest Welcome Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        			if (confirm == JOptionPane.OK_OPTION) {
		        				WelcomeMessage.setNormalUserMessage(WelcomeMessage.getWelcomeMessage());
		        				Config.saveConfig();
		        				System.out.println(Main.timeStamp() + "Welcome message for non-guests has been updated.");
		        			}
			        	}
		        	});
			        subSubMenu.add(menuItem);
			        menuItem = new JMenuItem("Bot Staff", KeyEvent.VK_S);
			        menuItem.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
		        			int confirm = JOptionPane.showConfirmDialog(frame, WelcomeMessage.getWelcomeMessagePanel(WelcomeMessageTarget.BOT_STAFF), "Bot Staff Welcome Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        			if (confirm == JOptionPane.OK_OPTION) {
		        				WelcomeMessage.setBotStaffMessage(WelcomeMessage.getWelcomeMessage());
		        				Config.saveConfig();
		        				System.out.println(Main.timeStamp() + "Welcome message for bot staff has been updated.");
		        			}
			        	}
		        	});
			        subSubMenu.add(menuItem);
			        menuItem = new JMenuItem("All Users", KeyEvent.VK_U);
			        menuItem.addActionListener(new ActionListener() {
			        	@Override
			        	public void actionPerformed(ActionEvent e) {
		        			int confirm = JOptionPane.showConfirmDialog(frame, WelcomeMessage.getWelcomeMessagePanel(WelcomeMessageTarget.UNIVERSAL), "Universal Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        			if (confirm == JOptionPane.OK_OPTION) {
		        				WelcomeMessage.setUniversalWelcomeMessage(WelcomeMessage.getWelcomeMessage());
		        				Config.saveConfig();
		        				System.out.println(Main.timeStamp() + "Universal welcome message has been updated.");
		        			}
			        	}
		        	});
			        subSubMenu.add(menuItem);
			        
					submenu = new JMenu("Event Channels");
					submenu.setMnemonic(KeyEvent.VK_C);
					menu.add(submenu);
						eventChansRlGaTourney = new JCheckBoxMenuItem("RL GA Tournament");
						eventChansRlGaTourney.addActionListener(new ActionListener() {
				        	@Override
				        	public void actionPerformed(ActionEvent e) {
				        		if (Server.isConnected()) {
									//new EventChannelGenerater();
									if (eventChansRlGaTourney.isSelected()) {
										EventChannelGenerater.createRlGaTourney();
									}
									else if (!eventChansRlGaTourney.isSelected()) {
										EventChannelGenerater.deleteRlGaTourney();
									}
								}
				        		else {
				        			System.out.println(Main.timeStamp() + "Bot is not currently connected to a server.");
				        		}
				        	}
			        	});
				        submenu.add(eventChansRlGaTourney);
			        
        //Help
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);
        menuItem = new JMenuItem("Information", KeyEvent.VK_E);
        menuItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		JOptionPane.showOptionDialog(frame, Main.getInfoPanel(), ("SFI Bot " + Main.getVersion(true) + " Information"), JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
	    	}
    	});
        menu.add(menuItem);
        
        return menuBar;
    }
	
    private Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        console = new JTextArea(20, 70);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
		
		PrintStream printStream = new PrintStream(new CustomOutputStream(console));
		System.setOut(printStream);
		//TODO : Allow only if debug on. 
		//System.setErr(printStream);
        JScrollPane scrollPane = new JScrollPane(console);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new SmartScroller(scrollPane));

        contentPane.add (scrollPane, BorderLayout.CENTER);

        return contentPane;
    }

	public static void createAndShowGUI() {
        //Create and set up the window.
    	frame = new JFrame("sFITs3 Bot (" + Main.getVersion(false) + ")");
    	try {
			frame.setIconImage(Toolkit.getDefaultToolkit().createImage(new URL("http://somefriggnidiot.com/favicon.ico")));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//frame.setDefaultCloseOperation(Main.confirmExit());
    	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	frame.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent we) {
    			Main.confirmExit();
    		}
		});
    	frame.setResizable(true);
    	frame.setPreferredSize(new Dimension(800,400));
    	frame.setMinimumSize(new Dimension(800,400));
        
        Gui gui = new Gui();
        frame.setJMenuBar(gui.createMenuBar());
        frame.setContentPane(gui.createContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        //TODO Tutorial if bot staff groups not set.
	}

	public static boolean slowModeOn() {
		return slowModeBox.isSelected();
	}
	
	public static boolean idleCheckerOn() {
		return idleCheckerBox.isSelected();
	}
	
	public static boolean showServerActivity() {
		return serverActivityBox.isSelected();
	}
	
	public static boolean showChatOn() {
		return chatDisplayBox.isSelected();
	}
	
	public static boolean chatCommandsEnabled() {
		return chatCommandsBox.isSelected();
	}
	
	public static void setTitle(String newTitle) {
		frame.setTitle(newTitle);
	}	
	
	public static String getTitle() {
		return frame.getTitle();
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static void setEventChansRlGaTourney(boolean on) {
		eventChansRlGaTourney.setSelected(on);
	}

}
