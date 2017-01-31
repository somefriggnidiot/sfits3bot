package ChatCommands;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import ChatCommands.ChatCommand.CommandAccessLevel;
import sfibot.Config;
import sfibot.Gui;

public class Commands {
	//	private static List<? extends ChatCommand> commands = new ArrayList<>();
	private static CommandAccessLevel invokerAccessLevel = CommandAccessLevel.ALL;
	
	public static int handle(String input, ClientInfo invoker) {
		/**		If integer returned = certain values,
		 * 			5 = Blocked From Bot Commands
		 * 			4 = No Permission To Access
		 * 			3 = Bad Syntax
		 * 			2 = Command Does Not Exist
		 * 			1 = Unknown Error
		 * 			0 = No Error
		 * 		   -1 = FUBAR Error
		 */
		
		//Ensure invoker is reachable. 
		if (invoker != null) {
			
			//Then check for permission level.
			for (int serverGroup : invoker.getServerGroups()) {
				if (serverGroup == Config.getBotBlockedGroup()) {
					return 5;
				}
				if (serverGroup == Config.getBotAdminGroup()) {
					invokerAccessLevel = CommandAccessLevel.STAFF;
					break;
				}
				if (serverGroup == Config.getBotOwnerGroup()) {
					invokerAccessLevel = CommandAccessLevel.OWNER;
					break;
				}
			} 	//Proceed if invoker isn't bot-banned.
			

			//Convert input to lower and remove '!' for processing.
			String inputLower = input.toLowerCase().substring(1);
			String inputRaw = input.substring(1);
			
			
			//Determine command and send to that command's handler.
			/*	******************************
			 *  ***     COMMAND INDEX      ***
			 *  ******************************
			 *  
			 *  ******************************
			 *  ***	      OWNER ONLY       ***
			 *  ******************************
			 *  	botrename
			 *  	vip
			 *  
			 *  ******************************
			 *  ***	    STAFF COMMANDS     ***
			 *  ******************************
			 *  	eventchannels
			 *  	nannymode
			 *  	notify
			 *  	suspend
			 *  	timeout
			 *  	unsuspend
			 *  
			 *  ******************************
			 *  ***	    PUBILC COMMANDS    ***
			 *  ******************************
			 * 		admin
			 * 		commands
			 * 		renameroom
			 */
			
			if (!Gui.chatCommandsEnabled() && CommandAccessLevel.getAccessLevel(invokerAccessLevel) == CommandAccessLevel.getAccessLevel(CommandAccessLevel.OWNER)) {
				//Handle chat commands toggle.
				
				if (inputLower.startsWith("chatcommands")) {
					return -2;
					//return new commandChatCommands("chatcommands", true, CommandAccessLevel.OWNER).handle(invoker, invokerAccessLevel, inputText);
				}
				
				return 0;
			}
			
			
			if (inputLower.startsWith("admin")) {
				return new commandAdmin("admin", true, CommandAccessLevel.ALL).handle(invoker, invokerAccessLevel, inputLower);
			}
			else if (inputLower.startsWith("botrename")) {
				return new commandBotRename("botrename", true, CommandAccessLevel.OWNER).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else if (inputLower.startsWith("commands")) {
				return new commandCommands("commands", true, CommandAccessLevel.ALL).handle(invoker, invokerAccessLevel, inputLower);
			}
			else if (inputLower.startsWith("eventchannels")) {
				return new commandEventChannels("eventchannels", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputLower);
			}
			else if (inputLower.startsWith("help")) {
				return new commandHelp("help", true, CommandAccessLevel.ALL).handle(invoker, invokerAccessLevel, inputLower);
			}
			else if (inputLower.startsWith("nannymode")) {
				return new commandNannyMode("nannymode", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputLower);
			}
			else if (inputLower.startsWith("notify")) {
				return new commandNotify("notify", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else if (inputLower.startsWith("renameroom")) {
				return new commandRenameRoom("renameroom", true, CommandAccessLevel.ALL).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else if (inputLower.startsWith("suspend")) {
				return new commandSuspend("suspend", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else if (inputLower.startsWith("timeout")) {
				return new commandTimeOut("timeout", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else if (inputLower.startsWith("unsuspend")) {
				return new commandUnsuspend("unsuspend", true, CommandAccessLevel.STAFF).handle(invoker, invokerAccessLevel, inputRaw);
			}
			else {
				return 2;
			}
		}
		else {
			return 1;
		}
	}

//	public static boolean isCommand(String input) {
//		input = input.substring(1);
//		for (ChatCommand command : commands) {
//			if (input.startsWith(command.getName().toLowerCase())) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	public static void compileCommandList() {
//		
//	}
//	
//	public static int commandAdmin(ClientInfo invoker) {
//		adminInvoker = invoker.getId();
//		
//		Server.getApi().sendPrivateMessage(adminInvoker, "Online staff have been notified of your request.");
//		
//		CommandFuture<List<Client>> online = Server.getApiAsync().getClients();
//		
//		online.onSuccess(new CommandFuture.SuccessListener<List<Client>>() {
//			public void handleSuccess(List<Client> online) {
//				for (Client onlineClient : online) {
//					for (int group : onlineClient.getServerGroups()) {
//						if (group == Config.getBotAdminGroup() || group == Config.getBotOwnerGroup()) {
//							Server.getApi().sendPrivateMessage(onlineClient.getId(),invoker.getNickname() + " has requested the presence of an admin! Respond to this message with \"!grab @\" to bring them to your channel.");
//						}
//					}
//				}
//			}
//		});
//		
//		online.onFailure(new CommandFuture.FailureListener() {
//			@Override
//			public void handleFailure(QueryError error) {
//				return;
//			}
//		});
//		
//		return 0;
//	}
}
