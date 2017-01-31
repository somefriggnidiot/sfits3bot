package ChatCommands;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class commandVip extends ChatCommand{
	commandVip(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel)  {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) { 	//Check for clearance.
			/* ARGS
			 * 	0 = Command
			 *  1 = Set/Info
			 *  2 = Date/User
			 *  3 = User
			 */
			String[] specialArgs = input.split("", 3);
			
			if (specialArgs[1].equals("set")) {
				return -2;
			}
			
			return 0;
		}
		else { 	//No clearance.
			return 4;
		}
	}

}
