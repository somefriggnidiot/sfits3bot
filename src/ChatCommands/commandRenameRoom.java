package ChatCommands;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import features.ChannelNameChanger;

public class commandRenameRoom extends ChatCommand {

	commandRenameRoom(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		
		new ChannelNameChanger(invoker.getChannelId(), input);
		
		return 0;
	}
}
