package ChatCommands;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import features.EventChannelGenerater;
import sfibot.Gui;

public class commandEventChannels extends ChatCommand
{
	commandEventChannels(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) 
	{
		super(commandName, isEnabled, requiredAccessLevel);
	}
	
	public int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) 
	{
//		System.out.println("Invoker Level: " + CommandAccessLevel.getAccessLevel(invokerAccessLevel) + "Required: " + CommandAccessLevel.getAccessLevel(requiredAccessLevel));
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel))
		{
			/* Special Args:
			 * 	0 = Command
			 *  1 = Create/Destroy
			 *  2 = Game Abbreviation
			 *  3 = Event Type Abbreviation
			 */
			
//			String[] specialArgs = new String[0];
//			String command = new String();
//			if (input.contains(" ")) {
//				String[] parts = input.split(" ");
//				command = parts[0];
//				
//				specialArgs = new String[parts.length - 1];
//				for (int i = 1; i < parts.length; i++) {
//					specialArgs[i - 1] = parts[i];
//				}
//			}
			
			String[] specialArgs = input.split(" ", 4);
			
			if (specialArgs[1].equals("create")) {
				if (specialArgs[2].equals("rl")) {
					if (specialArgs[3].equals("gat")) {
						EventChannelGenerater.createRlGaTourney();
						Gui.setEventChansRlGaTourney(true);
						return 0;
					}
				}
			}
			else if (specialArgs[1].equals("delete")) {
				if (specialArgs[2].equals("rl")) {
					if (specialArgs[3].equals("gat")) {
						EventChannelGenerater.deleteRlGaTourney();
						Gui.setEventChansRlGaTourney(false);
						return 0;
					}
				}
			}

			
			return 3;
		}
		else
		{
			return 4;
		}
		//return -2;
	}
}
