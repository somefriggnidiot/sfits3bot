package events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

import sfibot.Config;
import sfibot.Main;
import sfibot.Server;
import sfibot.WelcomeMessage;

public class ClientJoinHandler {
	ClientInfo clientInfo = null;
	Client client = null;
	TS3Api api = Server.getApi();
	public ClientJoinHandler(ClientJoinEvent e) {
		clientInfo = Server.getApi().getClientInfo(e.getClientId());
		client = Server.getApi().getClientByUId(clientInfo.getUniqueIdentifier());
		
		checkPermissions(client.getUniqueIdentifier());
		checkBirthday(client.getUniqueIdentifier());
		handleSuspended();
		logToConsole(e);
		sendWelcomeMessages();
	};
	
	private void checkPermissions(String clientUid) {
		String userGroup = "";
		try {
			String webUid = URLEncoder.encode(clientUid, "UTF-8");
			String urlWithId = "http://www.foundinaction.com/ts3botuidchecker.php?uid=" + webUid;
			URL url = new URL(urlWithId);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;

			while ((inputLine = br.readLine()) != null) {
				userGroup = inputLine;
			}
			br.close();
			
			assignGroups(userGroup);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	
	private void checkBirthday(String clientUid) {
		String ifBirthday = "";
		try {
			String webUid = URLEncoder.encode(clientUid, "UTF-8");
			String urlWithId = "http://www.foundinaction.com/ts3botbirthdaychecker.php?uid=" + webUid;
			URL url = new URL(urlWithId);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;

			while ((inputLine = br.readLine()) != null) {
				ifBirthday = inputLine;
			}
			br.close();
			
			giveCake(ifBirthday);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void giveCake(String ifBirthday) {
		Integer birthdayValue = Integer.parseInt(ifBirthday);
		if (birthdayValue != null && birthdayValue == 0 && !isGroupMember(client, Config.birthdayGroupId)) {
			api.addClientToServerGroup(Config.birthdayGroupId, clientInfo.getDatabaseId());
		}
		else {
			if (isGroupMember(client, Config.birthdayGroupId)) {
				api.removeClientFromServerGroup(Config.birthdayGroupId, clientInfo.getDatabaseId());
			}
		}
	}
	
	private void assignGroups(String userGroup) {
		if (userGroup != "") { 	//Check for empty.
			//System.out.println("User Group:" + userGroup); //DEBUG: Print group. TODO
			
			if (userGroup.startsWith("registered")) { //For users who default to Registered. 
				/* 	ASSIGN TO
				 * 		registeredGroupId
				 * 	ASSIGN TO EXTRA
				 * 		none
				 * 	REMOVE FROM
				 * 		communityMemberGroupId
				 * 		divisionManagerGroupId
				 * 		communityManagerGroupId
				 * 		seniorCommunityManagerGroupId
				 * 		founderGroupId
				 */
				//Assign to registered group.
				if (!isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().addClientToServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				
				//Remove from other groups.
				if (isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
			}
			else if (userGroup.startsWith("community member")) { //For users who default to Community Member
				/* 	ASSIGN TO
				 * 		communityMemberGroupId
				 * 	ASSIGN TO EXTRA
				 * 		none
				 * 	REMOVE FROM
				 * 		registeredGroupId
				 * 		divisionManagerGroupId
				 * 		communityManagerGroupId
				 * 		seniorCommunityManagerGroupId
				 * 		founderGroupId
				 */
				//Assign to community member group.
				if (!isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				
				//Remove from other groups.
				if (isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
			}
			else if (userGroup.startsWith("division manager")) { //For users who default to Division Manager
				/* 	ASSIGN TO
				 * 		divisionManagerGroupId
				 * 	ASSIGN TO EXTRA
				 * 		communityMemberGroupId
				 * 	REMOVE FROM
				 * 		registeredGroupId
				 * 		communityManagerGroupId
				 * 		seniorCommunityManagerGroupId
				 * 		founderGroupId
				 */
				//Assign to division manager group.
				if (!isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().addClientToServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				//Set extra groups.
				if (!isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				//Remove from other groups.
				if (isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
			}
			else if (userGroup.startsWith("community manager")) { //For users who default to a CM role.
				/* 	ASSIGN TO
				 * 		communityManagerGroupId
				 * 	ASSIGN TO EXTRA
				 * 		communityMemberGroupId
				 * 	REMOVE FROM
				 * 		registeredGroupId
				 * 		divisionManagerGroupId
				 * 		seniorCommunityManagerGroupId
				 * 		founderGroupId
				 */
				//Assign to community manager group.
				if (!isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				//Set extra groups.
				if (!isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				//Remove from other groups.
				if (isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
			}
			else if (userGroup.startsWith("senior community manager")) { //For users who default to a SCM role.
				/* 	ASSIGN TO
				 * 		seniorCommunityManagerGroupId
				 * 	ASSIGN TO EXTRA
				 * 		communityMemberGroupId
				 * 	REMOVE FROM
				 * 		registeredGroupId
				 * 		divisionManagerGroupId
				 * 		communityManagerGroupId
				 * 		founderGroupId
				 */
				//Assign to community manager group.
				if (!isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().addClientToServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
				//Set extra groups.
				if (!isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				//Remove from other groups.
				if (isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
			}
			else if (userGroup.startsWith("founder")) { //For users who default to a founder role.
				/* 	ASSIGN TO
				 * 		founderGroupId
				 * 	ASSIGN TO EXTRA
				 * 		communityMemberGroupId
				 * 	REMOVE FROM
				 * 		registeredGroupId
				 * 		divisionManagerGroupId
				 * 		communityManagerGroupId
				 * 		seniorCommunityManagerGroupId
				 */
				//Assign to founder group.
				if (!isGroupMember(client, Config.founderGroupId)) {
					Server.getApi().addClientToServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
				}
				//Set extra groups.
				if (!isGroupMember(client, Config.communityMemberGroupId)) {
					Server.getApi().addClientToServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
				}
				//Remove from other groups.
				if (isGroupMember(client, Config.registeredGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.divisionManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.communityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
				}
				if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
					Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
				}
			}
		} else {
			System.out.println(Main.timeStamp() + client.getNickname() + " is not registered on the forums."); //TODO Debug: User not found on forums.
			//Remove from groups.
			if (isGroupMember(client, Config.registeredGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.registeredGroupId, clientInfo.getDatabaseId());
			}
			if (isGroupMember(client, Config.communityMemberGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.communityMemberGroupId, clientInfo.getDatabaseId());
			}
			if (isGroupMember(client, Config.divisionManagerGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.divisionManagerGroupId, clientInfo.getDatabaseId());
			}
			if (isGroupMember(client, Config.communityManagerGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.communityManagerGroupId, clientInfo.getDatabaseId());
			}
			if (isGroupMember(client, Config.seniorCommunityManagerGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.seniorCommunityManagerGroupId, clientInfo.getDatabaseId());
			}
			if (isGroupMember(client, Config.founderGroupId)) {
				Server.getApi().removeClientFromServerGroup(Config.founderGroupId, clientInfo.getDatabaseId());
			}
		}
	};
	
	private boolean isGroupMember(Client client, Integer groupId) {
		List<ServerGroup> serverGroups = Server.getApi().getServerGroupsByClient(client);
		for (ServerGroup serverGroup : serverGroups)
		{
			if (serverGroup.getId() == groupId) {
				return true;
			}
		}
		return false;
	};
	
	public void sendWelcomeMessages() {
		//Send universal welcome message if applicable.
		if (!WelcomeMessage.getUniversalWelcomeMessage().equals(""))
		{
			Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getUniversalWelcomeMessage());
		}

		//Send welcome message if new user and applicable. 
		//System.out.println("TESTING TO SEE IF NEW USER");	//TODO DEBUG
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	//System.out.println("TESTING TO SEE IF WELCOME MESSAGE EXISTS"); 	//TODO DEBUG
				if (!WelcomeMessage.getNewUserMessage().equals(""))	
				{
					//System.out.println("TEST PASSED: Message exists."); 	//TODO DEBUG
					for (int csg : client.getServerGroups())
					{
						if (csg == Server.getApi().getServerInfo().getDefaultServerGroup())
						{
							//System.out.println("TEST PASSED: New user. SENDING MESSAGE"); 	//TODO DEBUG
							Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getNewUserMessage());
							break;
						}
					}
				}
            }
        });

		//Send welcome message if non-guest and applicable.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
				if (!WelcomeMessage.getNormalUserMessage().equals(""))
				{
					boolean guest = false;
					for (int ccg : client.getServerGroups())
					{
						if (ccg == Server.getApi().getServerInfo().getDefaultServerGroup())
						{
							guest = true;
							break;
						}
					}
					if (!guest)
					{
						Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getNormalUserMessage());
					}
				}
            }
        });

		//Send welcome message if bot staff and applicable.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
				if (!WelcomeMessage.getBotStaffMessage().equals(""))
				{
					boolean staff = false;
					for (int ccg : client.getServerGroups())
					{
						if (ccg == Config.getBotAdminGroup() || ccg == Config.getBotOwnerGroup())
						{
							staff = true;
							break;
						}
					}
					if (staff)
					{
						Server.getApi().sendPrivateMessage(client.getId(), WelcomeMessage.getBotStaffMessage());
					}
				}
            }
        });
	}
	
	public void handleSuspended() {
		if (isGroupMember(client, Config.getSuspendedGroupId())) {
			api.moveClient(client.getId(), Config.getSuspendedChannelId());
			api.sendPrivateMessage(client.getId(), "Your account is currently suspended from this server. Please reply with \"!admin\" to notify a staff member.");
		}
	}
	
	public void logToConsole(ClientJoinEvent newClient) {
		//Log to console if client still connected and checked to be displayed.
		//TODO Minimize delay. Currently 2sec.
//		if (newClient != null && Config.getServerActivityFlag())
//		{
//			String channel = Server.getApi().getChannelInfo(newClient.getClientTargetId()).getName();
//			System.out.println(Main.timeStamp() + client.getNickname() + " (" + newClient.getUniqueClientIdentifier() + ") connected to \"" + channel + "\".");
//		}
	}
}
