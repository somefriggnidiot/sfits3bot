package ChatCommands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Server;

public class commandCommands extends ChatCommand{

	commandCommands(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) {
			TS3Api api = Server.getApi();

			api.sendPrivateMessage(invoker.getId(), "[b]You have access to the following supported commands... [/b]");
			
			if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(CommandAccessLevel.OWNER)) {
				api.sendPrivateMessage(invoker.getId(), "[b]Owner-Level Commands:[/b]");
				api.sendPrivateMessage(invoker.getId(), "botrename");
				//Send owner-only commands.
			}
			if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(CommandAccessLevel.STAFF)) {
				api.sendPrivateMessage(invoker.getId(), "[b]Staff-Level Commands:[/b]");
				api.sendPrivateMessage(invoker.getId(), "eventchannels, nannymode, suspend, timeout, unsuspend");
				//Send staff-only commands.
			}
			if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(CommandAccessLevel.ALL)) {
				api.sendPrivateMessage(invoker.getId(), "[b]Public Commands:[/b]");
				api.sendPrivateMessage(invoker.getId(), "admin, commands, help, renameroom");
				//Send public commands.
			}
			api.sendPrivateMessage(invoker.getId(), "[i]Use \"!help <command>\" to receive information on how to use a command.[/i]");
			
			return 0;
		}
		else {
			return 4;
		}
	}

}
