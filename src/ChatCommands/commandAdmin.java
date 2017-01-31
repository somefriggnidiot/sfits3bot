package ChatCommands;

import java.util.Map;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Config;
import sfibot.Server;

public class commandAdmin extends ChatCommand {

	commandAdmin(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}
	
	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) { 	//Check for clearance.
			
			//Grab Async API & create a counter.
			TS3ApiAsync apiAsync = Server.getApiAsync();
			Integer usersNotified = 0;
			
			//Retrieve list of online clients.
			Map<Integer, ClientInfo> online = Server.getOnlineClients();
			
			for (ClientInfo clientInfo : online.values()) { 											//For every online client...
				int[] serverGroups = clientInfo.getServerGroups(); 											//...Grab their server groups...
				for (int serverGroup : serverGroups) { 															//...And for each server group...
					if (serverGroup == Config.getBotAdminGroup() || serverGroup == Config.getBotOwnerGroup()) { 	//...See if they're a staff member.
						apiAsync.sendPrivateMessage(clientInfo.getId(), invoker.getNickname() + " has requested the presence of an admin.");
						
						//Increment "notified" counter and break if 5 staff have been notified.
						if (++usersNotified >= 5) {
							break;
						}
					}
				}
			}
			
			//Let user know how many staff have been notified.
			apiAsync.sendPrivateMessage(invoker.getId(), usersNotified + " online staff members have been notified of your request.");
			
			return 0;
		}
		else { 	//No clearance.
			return 4;
		}
	}
}
