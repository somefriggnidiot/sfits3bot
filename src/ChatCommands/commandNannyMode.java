package ChatCommands;

import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import features.NannyMode;
import sfibot.Main;
import sfibot.Server;

public class commandNannyMode extends ChatCommand {

	commandNannyMode(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) {
			TS3Api api = Server.getApi();
			
			if (input.contains(" ")) {
				String[] specialArgs = input.split(" ", 3);
				
				if (specialArgs[1].equals("on") || specialArgs[1].equals("enable")) {
					NannyMode.enableNannyChannels();
					api.sendPrivateMessage(invoker.getId(), "NannyMode has been enabled.");
					System.out.println(Main.timeStamp() + "NannyMode has been enabled.");
				}
				else if (specialArgs[1].equals("off") || specialArgs[1].equals("disable")) {
					NannyMode.disableNannyChannels();
					api.sendPrivateMessage(invoker.getId(), "NannyMode has been disabled.");
					System.out.println(Main.timeStamp() + "NannyMode has been disabled.");
				}
				else if (specialArgs[1].equals("close")) {
					List<Channel> channels = api.getChannelsByName(specialArgs[2]);
					
					if (channels.size() != 1) {
						return 1;
					}
					else {
						NannyMode.closeChannel(channels.get(0).getId());
						api.sendPrivateMessage(invoker.getId(), channels.get(0).getName() + " has been added to the nanny list.");
						System.out.println(Main.timeStamp() + channels.get(0).getName() + " has been added to the nanny list.");
					}
				}
				else if (specialArgs[1].equals("open")) {
					List<Channel> channels = api.getChannelsByName(specialArgs[2]);
					
					if (channels.size() != 1) {
						return 1;
					}
					else {
						NannyMode.openChannel(channels.get(0).getId());
						api.sendPrivateMessage(invoker.getId(), channels.get(0).getName() + " has been removed from the nanny list.");
						System.out.println(Main.timeStamp() + channels.get(0).getName() + " has been removed from the nanny list.");
					}
				}
				else if (specialArgs[1].startsWith("list")) {
					List<String> channels = NannyMode.getChannelList();
					String list = null;
					for (int i = 0; i < channels.size(); i++) {
						if (i == 0) {
							list += channels.get(i);
							list = list.substring(4);
						}
						else {
							list += channels.get(i);
						}
						

						if (i < channels.size() - 1) {
							list += ", ";
						}
					}
					
					if (list.length() >= 998) {
						list = list.substring(0, 998);
						api.sendPrivateMessage(invoker.getId(), "There are currently [b]" + channels.size() + "[/b] restricted channels.");
						api.sendPrivateMessage(invoker.getId(), "Restricted Channels: " + list);
						api.sendPrivateMessage(invoker.getId(), "Due to message length restrictions, not all channels were listed.");
					}
					else {
						api.sendPrivateMessage(invoker.getId(), "There are currently [b]" + channels.size() + "[/b] restricted channels.");
						api.sendPrivateMessage(invoker.getId(), "[b]Restricted Channels: [/b]" + list);
					}
				}
			}
			else if (input.equals("nannymode")) {
				String status = NannyMode.isActive() ? "active" : "inactive";
				
				api.sendPrivateMessage(invoker.getId(), "NannyMode is currently " + status);
			}
			
			return 0;
		}
		else {
			return 4;
		}
	}
	
	

}
