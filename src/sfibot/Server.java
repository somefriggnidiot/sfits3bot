package sfibot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.CommandFuture;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectingConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServerInfo;

import events.ClientJoinHandler;
import events.TextMessageHandler;
import features.ChannelNameChanger;
import features.NannyMode;

public class Server {
	private static TS3Query query = null;
	private static TS3Config tsConfig = null;
	private static Map<Integer, ClientInfo> clientsOnline = new HashMap<>();
	private static int clientId;
	
	/**	Server.connect()
	 * 		Called When:
	 * 			User selects the option to connect to the server.
	 * 		Conditions:
	 * 			All required fields (and those of its dependents) must already exist in Config.
	 * 		Effect:
	 * 			Configures and establishes connection to the server.
	 */
	public static void connect() {
		System.out.println(Main.timeStamp() + "Connecting to " + Config.getServerAddress() + " as " + Config.getServerBotName() + "...");

		//Create config object and set required attributes.
		tsConfig = new TS3Config();
		tsConfig.setHost(Config.getServerAddress());
		tsConfig.setDebugLevel(Level.OFF);
		if (Config.getServerSlowModeFlag()) {
			tsConfig.setFloodRate(FloodRate.DEFAULT);
		}
		else if (!Config.getServerSlowModeFlag()) {
			tsConfig.setFloodRate(FloodRate.UNLIMITED);
		}
		
		tsConfig.setConnectionHandler(new ReconnectingConnectionHandler() {
					@Override
					public void setUpQuery(TS3Query query) {
						configureQuery(query.getApi());
					}
				});
		
		//Create query object and connect.
		query = new TS3Query(tsConfig);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	query.connect();
        		System.out.println(Main.timeStamp() + "Connected!");
        		Gui.setTitle(Config.getServerBotName() + " - sFITs3 Bot (" + Main.getVersion(false) + ")");

        		addListeners(query.getApi());
        		
        		NannyMode.loadChanFile();
            }
        });
	}
	
	public static void disconnect() {
		query.exit();
		Gui.setTitle("sFITs3 Bot (" + Main.getVersion(false) + ")");
	}
	
	public static int getMyClientId() {
		return clientId;
	}
	
	/**	configQuery(TS3Api ts3Api)
	 * 		Called When:
	 * 			Bot must configure connection after each connect.
	 * 		Conditions:
	 * 			All required fields must exist in Config.
	 * 		Effect:
	 * 			Configures connection to the server.
	 * 				Logs bot into the Query.
	 * 				Selects specific server.
	 * 				Sets bot name.
	 * 				Registers events.
	 */
	private static void configureQuery(TS3Api ts3Api) {
		ts3Api.login(Config.getServerQueryLoginName(), Config.getServerQueryLoginPass());
		ts3Api.selectVirtualServerById(Config.getServerVirtualServerId());
		//ts3Api.selectVirtualServerByPort(Config.getServerQueryPort()); 	//WRONG!!!
		ts3Api.setNickname(Config.getServerBotName());
		
		ts3Api.registerAllEvents();
		clientId = ts3Api.whoAmI().getId();
		
		//Clear and repopulate online client list.
		clientsOnline = new HashMap<>();
		generateOnlineClients();
		printOnlineClients();
	}
	
	public static void addListeners(final TS3Api api) {
		api.addTS3Listeners(new TS3EventAdapter() {
			@Override
			public void onTextMessage(TextMessageEvent messageEvent) {
				new TextMessageHandler(messageEvent);
			}

			@Override
			public void onClientJoin(ClientJoinEvent newClient) {
				
				//Check to see if query/stat-checker. If so, ignore.
				if (newClient.getUniqueClientIdentifier().startsWith("ServerQuery")) {
					Server.addClient(newClient.getClientId(), new ClientInfo(-1, null));
					return;
				}
				
				//Set name and ID for later user.
				String tempName = newClient.getClientNickname();
				
				//Get info and add to online client list.
				CommandFuture<ClientInfo> clientInfo = Server.getApiAsync().getClientInfo(newClient.getClientId());
				CommandFuture<ChannelInfo> channel = Server.getApiAsync().getChannelInfo(newClient.getClientTargetId());
//				ClientInfo clientObject = null;
//				try {
//					clientObject = clientInfo.get();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				final ClientInfo client = clientObject;
				
				clientInfo.onSuccess(new CommandFuture.SuccessListener<ClientInfo>() {
					@Override
					public void handleSuccess(ClientInfo cli) {
						Server.addClient(cli.getId(), cli);
					}
				});
				
				//Log to console if client still connected and checked to be displayed.
				if (newClient != null && Gui.showServerActivity()) {
					System.out.println(Main.timeStamp() + tempName + " (" + newClient.getUniqueClientIdentifier() + ") connected to \"" + channel.getUninterruptibly().getName() + "\".");
				}
				
				
				//Send Welcome Messages
//				if (!WelcomeMessage.getUniversalWelcomeMessage().equals("")) {
//					Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getUniversalWelcomeMessage());
//				}

				//Send welcome message if new user and applicable. 
//				//System.out.println("TESTING TO SEE IF NEW USER");	//TODO DEBUG
//		        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//		            public void run() {
//		            	//System.out.println("TESTING TO SEE IF WELCOME MESSAGE EXISTS"); 	//TODO DEBUG
//						if (!WelcomeMessage.getNewUserMessage().equals(""))	{
//							//System.out.println("TEST PASSED: Message exists."); 	//TODO DEBUG
//							for (int csg : client.getServerGroups()) {
//								if (csg == Server.getApi().getServerInfo().getDefaultServerGroup()) {
//									//System.out.println("TEST PASSED: New user. SENDING MESSAGE"); 	//TODO DEBUG
//									Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getNewUserMessage());
//									break;
//								}
//							}
//						}
//		            }
//		        });
//
//				//Send welcome message if non-guest and applicable.
//		        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//		            public void run() {
//						if (!WelcomeMessage.getNormalUserMessage().equals("")) {
//							boolean guest = false;
//							for (int ccg : client.getServerGroups()) {
//								if (ccg == Server.getApi().getServerInfo().getDefaultServerGroup()) {
//									guest = true;
//									break;
//								}
//							}
//							if (!guest) {
//								Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getNormalUserMessage());
//							}
//						}
//		            }
//		        });
//
//				//Send welcome message if bot staff and applicable.
//		        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//		            public void run() {
//						if (!WelcomeMessage.getBotStaffMessage().equals("")) {
//							boolean staff = false;
//							for (int ccg : client.getServerGroups()) {
//								if (ccg == Config.getBotAdminGroup() || ccg == Config.getBotOwnerGroup()) {
//									staff = true;
//									break;
//								}
//							}
//							if (staff) {
//								Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getBotStaffMessage());
//							}
//						}
//		            }
//		        });
				new ClientJoinHandler(newClient);
			}

			@Override
			public void onClientLeave(ClientLeaveEvent leavingClient) {
				ClientInfo goneClient = clientsOnline.remove(leavingClient.getClientId());
				
				if (goneClient.getId() == -1 || goneClient.getNickname().equals("") || goneClient == null) {
					return;
				}
				
				//Log to console.
				if (Gui.showServerActivity() && (leavingClient.getInvokerName().equals("")/* || leavingClient.getInvokerName() == null*/)) {
					System.out.println(Main.timeStamp() + goneClient.getNickname() + " (" + goneClient.getUniqueIdentifier() + ") disconnected from the server.");
				}	//NEW (
				else if (Gui.showServerActivity() && !leavingClient.getInvokerName().equals("") /*&& leavingClient.getInvokerName() != null*/) {
					String reasonMessage = leavingClient.getReasonMessage();
					if (reasonMessage.equals("")/* || reasonMessage == null*/) {
						System.out.println(Main.timeStamp() + goneClient.getNickname() + " (" + goneClient.getUniqueIdentifier() + ") was kicked from the server by "
								+ leavingClient.getInvokerName() + " (" + leavingClient.getInvokerUniqueId() + ") without reason.");
						//TODO LOG THIS!
					}
					else {
						System.out.println(Main.timeStamp() + goneClient.getNickname() + " (" + goneClient.getUniqueIdentifier() + ") was kicked from the server by "
								+ leavingClient.getInvokerName() + " (" + leavingClient.getInvokerUniqueId() + ") with the following reason: \n" + leavingClient.getReasonMessage());
						//TODO And maybe log this, too.
					}
				}
				
				//Scan changer channels to see if they need reset.
				new ChannelNameChanger();
			}

			@Override
			public void onServerEdit(ServerEditedEvent e) {
				System.out.println(Main.timeStamp() + e.getInvokerName() + " (" + e.getInvokerUniqueId() + ") edited the server.");
			}

			@Override
			public void onChannelEdit(ChannelEditedEvent e) {
				System.out.println(Main.timeStamp() + e.getInvokerName() + " (" + e.getInvokerUniqueId() + ") edited \"" + query.getApi().getChannelInfo(e.getChannelId()).getName() + "\".");
			}

			@Override
			public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
				System.out.println(Main.timeStamp() + e.getInvokerName() + " (" + e.getInvokerUniqueId() + ") edited the description for \"" + query.getApi().getChannelInfo(e.getChannelId()).getName() + "\".");
			}

			@Override
			public void onClientMoved(ClientMovedEvent e) {
				if (Gui.showServerActivity()) {
					ClientInfo client = getApi().getClientInfo(e.getClientId());
					
					if (e.getInvokerName().equals("")/* || e.getInvokerName() == null*/) {
						System.out.println(Main.timeStamp() + client.getNickname() + " (" + client.getUniqueIdentifier()
						+ ") moved to \"" + getApi().getChannelInfo(e.getTargetChannelId()).getName() + "\".");
					}
					else {
						System.out.println(Main.timeStamp() + client.getNickname() + " (" + client.getUniqueIdentifier()
						+ ") was moved to \"" + getApi().getChannelInfo(e.getTargetChannelId()).getName() + "\" by " 
						+ e.getInvokerName() + " (" + e.getInvokerUniqueId() + ").");
					}
				}
				
				//Scan changer channels to see if they need reset.
				new ChannelNameChanger();
			}

			@Override
			public void onChannelCreate(ChannelCreateEvent e) {
				
			}

			@Override
			public void onChannelDeleted(ChannelDeletedEvent e) {
				
			}

			@Override
			public void onChannelMoved(ChannelMovedEvent e) {
				
			}

			@Override
			public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
				
			}

			@Override
			public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
				
			}
		});
	}
	
	public static TS3Api getApi() {
		return query.getApi();
	}
	
	public static void addClient(int clientId, ClientInfo client) {
		clientsOnline.put(clientId, client);
	}
	
	public static void printOnlineClients() {
		System.out.println(Main.timeStamp() + "Clients currently online: ");
		for (ClientInfo client : clientsOnline.values()) {
			String name = client.getNickname();
			while (name.length() < 41) {	//Even out names to (max length + 1) for formatting.
				name += " ";
			}
			System.out.println("\t" + name + "\t" + client.getUniqueIdentifier());
		}
	}

	public static void printChannels() {
		System.out.println(Main.timeStamp() + "Channels and IDs: ");
		for (Channel channel : getApi().getChannels()) {
			String name = channel.getName();
			while (name.length() < 41) {	//Even out names to (max length + 1) for formatting.
				name += " ";
			}
			System.out.println("\t" + name + "\t" + channel.getId());
		}
	}
	
	public static void printServerGroups() {
		System.out.println(Main.timeStamp() + "Server Groups and IDs: ");
		for (ServerGroup group : getApi().getServerGroups()) {
			String name = group.getName();
			while (name.length() < 36) { 	//Even out names to (max length + 1) for formatting.
				name += " ";
			}
			System.out.println("\t" + name + "\t" + group.getId());
		}
	}
	
	public static void printSnapshot(boolean activeChannelsOnly) {
		//TODO Change to Async
    	List<Channel> channels = getApi().getChannels();
    	Collection<ClientInfo> clients = getOnlineClients().values();
    	
    	if (!activeChannelsOnly) {
    		for (Channel channel : channels) {
    			System.out.println("\t" + channel.getName());
    			
    			if (getApi().getChannelInfo(channel.getId()).getSecondsEmpty() <= 0) {
    				for (ClientInfo client : clients) {
    					if (client.getChannelId() == channel.getId()) {
    						System.out.println("\t\t" + client.getNickname());
    					}
    				}
    			}
    		}
    	}
    	else if (activeChannelsOnly) {
    		for (Channel channel : getApi().getChannels()) {
    			if (getApi().getChannelInfo(channel.getId()).getSecondsEmpty() <= 0) {
    				System.out.println("\t" + channel.getName());
    				for (ClientInfo client :clients) {
    					if (client.getChannelId() == channel.getId()) {
    						System.out.println("\t\t" + client.getNickname());
    					}
    				}
    			}
    		}
    	}
	}
	
	static void generateOnlineClients() {
		clientsOnline.clear();
		List<Client> online = getApi().getClients();
		
		for (Client client : online) {
			clientsOnline.put(client.getId(), getApi().getClientInfo(client.getId()));
		}
	}
	
	public static Map<Integer, ClientInfo> getOnlineClients() {
		return clientsOnline;
	}
	
	public static TS3ApiAsync getApiAsync() {
		return query.getAsyncApi();
	}
	
	public static boolean isConnected() {
		try {
			getApi();
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
	
	public static void printStats() {
		VirtualServerInfo serverInfo = getApi().getServerInfo();
		//TODO
		System.out.println(serverInfo.getName() + "(" + serverInfo.getClientsOnline() + "/" + serverInfo.getMaxClients() + ")");
		System.out.println("Virtual Server Id: " + serverInfo.getId());
	}
}
