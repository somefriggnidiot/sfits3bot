package ChatCommands;

import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import features.TimeOut;
import sfibot.Main;
import sfibot.Server;

public class commandTimeOut extends ChatCommand{

	commandTimeOut(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}
	
	public int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) { 	//Check for clearance.
			String[] specialArgs = input.split(" ", 3);
			
			TS3Api api = Server.getApi();
			
			List<Client> matchingClients = api.getClientsByName(specialArgs[2]);
			
			if (matchingClients.size() > 1) {
				api.sendPrivateMessage(invoker.getId(), "Too many connected users matched the given search phrase. Please be more specific.");
				return 1;
			}
			else if (matchingClients.isEmpty()) {
				api.sendPrivateMessage(invoker.getId(), "No connected users matched the given search phrase.");
				return 1;
			}
			else if (matchingClients.size() == 1) {
				System.out.println(Main.timeStamp() + "Punishing " + matchingClients.get(0).getNickname() + " for " + specialArgs[1] + " seconds.");
				try {
					new TimeOut(Integer.parseInt(specialArgs[1]), matchingClients.get(0).getId());
				}
				catch (NumberFormatException e) {
					return 3;
				}
			}

		}
		else {
			return 4;
		}
		
		

		
		return -1;
	}

	
	
}
