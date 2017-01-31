package ChatCommands;

import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Config;
import sfibot.Main;
import sfibot.Server;

public class commandSuspend extends ChatCommand{

	commandSuspend(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}
	
	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) {
			String[] specialArgs = input.split(" ", 2);
			TS3Api api = Server.getApi();
			
			List<Client> matchingClients = api.getClientsByName(specialArgs[1]);
			
			if (matchingClients.size() > 1) {
				api.sendPrivateMessage(invoker.getId(), "Too many connected users matched the given search phrase. Please be more specific.");
				return 1;
			}
			else if (matchingClients.isEmpty()) {
				api.sendPrivateMessage(invoker.getId(), "No connected users matched the given search phrase.");
				return 1;
			}
			else if (matchingClients.size() == 1) {
				Client match = matchingClients.get(0);
				System.out.println(Main.timeStamp() + "Suspending " + match.getNickname() + ".");
				api.addClientToServerGroup(Config.getSuspendedGroupId(), api.getDatabaseClientByUId(match.getUniqueIdentifier()).getDatabaseId());
				api.moveClient(match.getId(), Config.getSuspendedChannelId());
				return 0;
			}
			
			return 1;
		}
		else {
			return 4;
		}
	}
}
