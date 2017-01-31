package ChatCommands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Config;
import sfibot.ConnectionConfig;
import sfibot.Gui;
import sfibot.Main;
import sfibot.Server;

public class commandBotRename extends ChatCommand {
	commandBotRename(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel)  {
		super(commandName, isEnabled, requiredAccessLevel);
	}
	
	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input)  {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) { 	//Check for clearance.
			
			//Ensure there are spaces.
			if (!input.startsWith("botrename ")) {
				return 3;
			}
			
			//Split the input so we have the command and the parameter.
			String[] specialArgs = input.split(" ", 2);
			
			//Check that there is input after the space and that the length is at least 3.
			if (specialArgs[1].equals("") || specialArgs[1].length() <= 2) {
				return 3;
			}
			
			//We're in the clear. Grab the API from the server for later use.
			TS3Api api = Server.getApi();	
			
			//If the given name was too long, let the user know.
			if (specialArgs[1].length() > 30) {
				api.sendPrivateMessage(invoker.getId(), "The name you requested is longer than 30 characters. Anything after the 30th character will be cut off.");
				api.setNickname(specialArgs[1].substring(0, 30));
			}
			else {
				api.setNickname(specialArgs[1]);
			}
			
			//Store bot's current name after changing.
			String botName = api.getClientInfo(Server.getMyClientId()).getNickname();
			
			//If change held, output. Else, unknown error.
			if (botName.equals(specialArgs[1]) || botName.equals(specialArgs[1].substring(0, 30))) {
				ConnectionConfig.setNickname(specialArgs[1]);
				System.out.println(Main.timeStamp() + "Bot renamed to " + Config.getServerBotName() + " by " + invoker.getNickname() + " (" + invoker.getUniqueIdentifier() + ").");
				api.sendPrivateMessage(invoker.getId(), "The bot's name has been successfully changed!");
				Gui.setTitle(Config.getServerBotName() + " - sFITs3 Bot (" + Main.getVersion(false) + ")");
				
				return 0;
			}
			return 1; 	//Unknown error.
		}
		else {	//No clearance.
			return 4;
		}
	}
}
