package events;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import ChatCommands.Commands;
import sfibot.Gui;
import sfibot.Main;
import sfibot.Server;

public class TextMessageHandler {
	private  TextMessageEvent messageEvent;
	public TextMessageHandler(TextMessageEvent event) {
		messageEvent = event;
		
		logToConsole(messageEvent);
		
		if(messageEvent.getMessage().startsWith("!")) {
			handleChatCommand();
		}
	}
	
	private void logToConsole(TextMessageEvent messageEvent) {
		//Display chat if enabled.
		if (Gui.showChatOn()) {
			//For messages not sent directly to the bot.
			if (messageEvent.getTargetMode() != TextMessageTargetMode.CLIENT) {
				//Format and forward to console.
				System.out.println(Main.timeStamp() + " [MESSAGE] " + messageEvent.getInvokerName() + " (" + messageEvent.getInvokerUniqueId() 
				+ ") to " + messageEvent.getTargetMode().toString().toLowerCase() + ": " + messageEvent.getMessage());
			}
			//For client messages sent to the bot.
			else if (messageEvent.getInvokerId() != Server.getMyClientId()) {
				//Format and forward to console.
				System.out.println(Main.timeStamp() + " [MESSAGE] " + messageEvent.getInvokerName() + " (" + messageEvent.getInvokerUniqueId() 
				+ ") to bot: " + messageEvent.getMessage());
			}
			//For client messages sent from the bot.
			else {
				System.out.println(Main.timeStamp() + " [MESSAGE] " + messageEvent.getInvokerName() + " (" + messageEvent.getInvokerUniqueId() 
				+ "): " + messageEvent.getMessage());
			}
		}
	}
	
	private void handleChatCommand() {
		//TODO: Handle command if enabled.
		/**	Command Handler returns integer. Store integer.
		 * 		If integer returned = certain values,
		 * 			5 = Blocked From Bot Commands
		 * 			4 = No Permission To Access
		 * 			3 = Bad Syntax
		 * 			2 = Command Does Not Exist
		 * 			1 = Unknown Error
		 * 			0 = No Error
		 * 		   -1 = FUBAR Error
		 * 		   -2 = Command Not Implemented
		 */
		if (messageEvent.getMessage().startsWith("!") && Gui.chatCommandsEnabled()) {		//TODO Remove chatcommand check and handle in Commands
			int returnValue = Commands.handle(messageEvent.getMessage(), Server.getApi().getClientInfo(messageEvent.getInvokerId()));
			
			//React based on returnvalue.
			switch(returnValue) {
			case -2:
				Server.getApi().sendPrivateMessage(messageEvent.getInvokerId(), "The command you are trying to use exists, but has not been implemented. Tell Idiot to get his ass in gear.");
			case 0:
				return;
			case 1:
				System.out.println("\tSomething went wrong while trying to handle the command. \n\t\tThe invoker may have been null.\n\t\tToo many or too few results may have been found.");
				return;
			case 2:
				Server.getApi().sendPrivateMessage(messageEvent.getInvokerId(), "[b]Command does not exist.[/b] Please consult someone who knows their shit.");
				return;
			case 3:
				Server.getApi().sendPrivateMessage(messageEvent.getInvokerId(), "[b]Invalid syntax.[/b] Please consult someone who knows their shit or use \"!help <command>\" to learn some shit.");
				return;
			case 4:
				Server.getApi().sendPrivateMessage(messageEvent.getInvokerId(), "[b]You do not have access to this command![/b]");
				return;
			case 5:
				System.out.println(Main.timeStamp() + "[ALERT] "+ messageEvent.getInvokerName() + " (" + messageEvent.getInvokerUniqueId() 
				+ ") tried to use a bot command as a blocked user. ");
				System.out.println("\tMessage: " + messageEvent.getMessage());
				Server.getApi().sendPrivateMessage(messageEvent.getInvokerId(), "[color=red][b]You are not allowed to use bot commands! Your attempt has been logged.[/b][/color]");
				return;
			}
		}
	}
}
