package ChatCommands;

import java.util.ArrayList;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Config;
import sfibot.Server;

public class commandNotify extends ChatCommand{

	commandNotify(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) {
			/*	SPECIAL ARGS
			 * 		0 = Command
			 * 		1 = Who
			 * 		2 = How
			 * 		3 = What
			 */
			String[] specialArgs = input.split(" ", 4);
			TS3Api api = Server.getApi();
			
			//Determine targets.
			List<Client> matchingClients = new ArrayList<>();
			if (specialArgs[1].equals("*")) {
				matchingClients = api.getClients();
			}
			else if (specialArgs[1].equals("@")) {
				List<Client> online = api.getClients();
				
				for (Client client : online) {
					int[] serverGroups = client.getServerGroups();
					for (int group : serverGroups) {
						if (group == Config.getBotAdminGroup() || group == Config.getBotOwnerGroup()) {
							matchingClients.add(client);
						}
					}
				}
			}
			else {
				matchingClients = api.getClientsByName(specialArgs[1]);
			}

			//Determine method and send message.
			if (specialArgs[2].equals("text")) {
				for (Client client : matchingClients) {
					api.sendPrivateMessage(client.getId(), specialArgs[3]);
				}
				return 0;
			}
			else if (specialArgs[2].equals("poke")) {
				
				//Split and handle separately if necessary.
				if (specialArgs[3].length() > 100) {
					List<String> messagePart = new ArrayList<>();
					
					for (int i = 0; i < Math.ceil(specialArgs[3].length()/100.00); i++) {
						try {
							messagePart.add(specialArgs[3].substring((i*100)-1*i, ((i*100)+99-(i*1))));
						} catch (IndexOutOfBoundsException ioob) {
							messagePart.add(specialArgs[3].substring(i*100-1*i, specialArgs[3].length()-1));
						}
					}
					for (Client client : matchingClients) {
						for (String partialMessage : messagePart) {
							api.pokeClient(client.getId(), partialMessage);
						}
					}
					return 0;
				}
				else { 	//Don't split. Message is short.
					for (Client client : matchingClients) {
						api.pokeClient(client.getId(), specialArgs[3]);
					}
					return 0;
				}
			}
			else {
				return 3;
			}


		}
		else {
			return 4;
		}
	}

}
