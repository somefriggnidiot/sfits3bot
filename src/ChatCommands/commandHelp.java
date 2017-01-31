package ChatCommands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import sfibot.Server;

public class commandHelp extends ChatCommand {
	
	class Usage {
		public String syntax = null;
		public String description = null;
	}

	commandHelp(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
		super(commandName, isEnabled, requiredAccessLevel);
	}

	int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input) {
		//System.out.println("Inside the help handler."); 		//TODO DEBUG
		
		if (CommandAccessLevel.getAccessLevel(invokerAccessLevel) >= CommandAccessLevel.getAccessLevel(requiredAccessLevel)) {
			
			//System.out.println("Access granted. Setting variables."); 		//TODO DEBUG
			
			String[] specialArgs = input.split(" ", 2);
			
			//System.out.println("SpecialArgs set. Setting API."); 		//TODO DEBUG
			
			TS3Api api = Server.getApi();
			
			//System.out.println("API set. Setting info."); 		//TODO DEBUG
			System.out.println("Input=" + input); 	//TODO DEBUG
			System.out.println("Arg=" + specialArgs[1]); 	//TODO DEBUG

			//System.out.println("Info: " + info[1].syntax); 	//TODO DEBUG

			
			if (input.equals("help")) { 	//If there is no command being asked about...
				api.sendPrivateMessage(invoker.getId(), "This bot was created by somefriggnidiot for use by the Found in Action community TS3 server. "
						+ "If you need help on using a bot created by an idiot, you're a fucking dumbass.");
				return 0;
			}

			if (specialArgs[1].startsWith("admin")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!admin");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Sends a message to all connected staff members, alerting them to your need for assistance.");
			}
			else if (specialArgs[1].startsWith("botrename")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!botrename <new_name>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Renames the bot to the new name. May contain spaces. Must be between 3 and 30 characters.");
			}
			else if (specialArgs[1].startsWith("commands")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!commands");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Returns a list of all commands available for your rank.");
			}
			else if (specialArgs[1].startsWith("eventchannels")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!eventchannels <action> <game> <event>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Creates or deletes event channels for certain game events. Currently supports RL/GAT.");
			}
			else if (specialArgs[1].startsWith("help")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!help <command>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Returns usage information about the given command if the command exists.");
				api.sendPrivateMessage(invoker.getId(), "[b]Alt. Syntax #1: [/b]!help");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Returns basic information about the bot.");
			}
			else if (specialArgs[1].startsWith("nannymode")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!nannymode");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Displays the status of the NannyMode module. When active, NannyMode adds "
						+ "5 required join power to the selected channels.");
				api.sendPrivateMessage(invoker.getId(), "[b]Alt. Syntax #1: [/b]!nannymode <\"on\"/\"off\"/\"list\">");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Enables, disables the NannyMode module, or lists all affected channels.");
				api.sendPrivateMessage(invoker.getId(), "[b]Alt. Syntax #2: [/b]!nannymode <\"open\"/\"close\"> <channel>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Removes or adds a channel to the NannyMode list. Channel is added by first match; search may contain spaces.");
			}
			else if (specialArgs[1].startsWith("notify")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!notify <user> <\"poke\"/\"text\"> <message>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Uses the designated method to send the message to all clients whose names match the <user> parameter. "
						+ "The <user> parameter does not accept spaces.");
				api.sendPrivateMessage(invoker.getId(), "[b]Alt. Syntax #1: [/b]!notify <\"*\"/\"@\"> <\"poke\"/\"text\"> <message>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Uses the designated method to send the message to all clients (*) or all admins (@).");
			}
			else if (specialArgs[1].startsWith("renameroom")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!renameroom <new_name>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]If in an applicable channel, allows the user to change the channel's name at will.");
			}
			else if (specialArgs[1].startsWith("suspend")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!suspend <username>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Indefinitely places the user in TimeOut. <username> allows spaces, "
						+ "but the command succeeds only if exactly one matching user is found. Reversed by use of the \"!unsuspend\" command.");

			}
			else if (specialArgs[1].startsWith("timeout")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!timeout <duration_in_seconds> <username>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Places the user in TimeOut for the time specified in seconds. "
						+ "<username> allows spaces, but the command succeeds only if exactly one matching user is found.");
			}
			else if (specialArgs[1].startsWith("unsuspend")) {
				api.sendPrivateMessage(invoker.getId(), "[b]Syntax: [/b]!unsuspend <username>");
				api.sendPrivateMessage(invoker.getId(), "[b]About: [/b]Negates the suspension of a user. <username> allows spaces, but the "
						+ "command succeeds only if exactly one matching user is found.");
			}
			else {	//If we can't determine the command being asked about...
				return 3;
			}
			return 0;

		}
		else {
			return 4;
		}
	}
	
	Usage[] information(String command) {
		Usage uses[] = null;
		
		if (command.startsWith("admin")) {
			uses = new Usage[1];
			uses[0].syntax = "!admin";
			uses[0].description = "Sends a message to all connected staff members, alerting them to your need for assistance.";
		}
		else if (command.startsWith("botrename")) {
			uses = new Usage[1];
			uses[0].syntax = "!botrename <new_name>";
			uses[0].description = "Renames the bot to the new name. May contain spaces.";
		}
		else if (command.startsWith("commands")) {
			uses = new Usage[1];
			uses[0].syntax = "!commands";
			uses[0].description = "Returns a list of all commands available for your rank.";
		}
		else if (command.startsWith("eventchannels")) {
			uses = new Usage[1];
			uses[0].syntax = "!eventchannels <action> <game> <event>";
			uses[0].description = "Creates or deletes event channels for certain game events. Currently supports RL/GAT.";
		}
		else if (command.startsWith("help")) {
			uses = new Usage[2];
			uses[0].syntax = "!help";
			uses[0].description = "Returns basic information about the bot.";
			uses[1].syntax = "!help <command>";
			uses[1].description = "Returns usage information about the given command if the command exists.";
		}
		else if (command.startsWith("nannymode")) {
			uses = new Usage[3];
			uses[0].syntax = "!nannymode";
			uses[0].description = "Displays the status of the NannyMode module. When active, NannyMode adds 5 required join power to the selected channels.";
			uses[1].syntax = "!nannymode <\"on\"/\"off\"/\"list\">";
			uses[1].description = "Enables, disables the NannyMode module, or lists all affected channels.";
			uses[2].syntax = "!nannymode <\"open\"/\"close\"> <channel>";
			uses[2].description = "Removes or adds a channel to the NannyMode list. Channel is added by first match; search may contain spaces.";
		}
		else if (command.startsWith("notify")) {
			uses = new Usage[2];
			uses[0].syntax = "!notify <user> <\"poke\"/\"text\"> <message>";
			uses[0].description = "Uses the designated method to send the message to all clients whose names match the <user> parameter. The <user> parameter does not accept spaces.";
			uses[1].syntax = "!notify <\"*\"/\"@\"> <\"poke\"/\"text\"> <message>";
			uses[1].description = "Uses the designated method to send the message to all clients (*) or all admins (@).";
		}
		else if (command.startsWith("renameroom")) {
			uses = new Usage[1];
			uses[0].syntax = "!renameroom <new_name>";
			uses[0].description = "If in an applicable channel, allows the user to change the channel's name at will.";
		}
		else if (command.startsWith("suspend")) {
			uses = new Usage[1];
			uses[0].syntax = "!suspend <username>";
			uses[0].description = "Indefinitely places the user in TimeOut. <username> allows spaces, but the command succeeds only if exactly one matching user is found. Reversed by use of the \"!unsuspend\" command.";
		}
		else if (command.startsWith("timeout")) {
			uses = new Usage[1];
			uses[0].syntax = "!timeout <duration_in_seconds> <username>";
			uses[0].description = "Places the user in TimeOut for the time specified in seconds. <username> allows spaces, but the command succeeds only if exactly one matching user is found.";
		}
		else if (command.startsWith("unsuspend")) {
			uses = new Usage[1];
			uses[0].syntax = "!unsuspend <username>";
			uses[0].description = "Negates the suspension of a user. <username> allows spaces, but the command succeeds only if exactly one matching user is found.";
		}
		
		System.out.println("USES = " + uses.toString()); 	//TODO DEBUG
		return uses;
	}

}
