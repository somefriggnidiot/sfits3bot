package ChatCommands;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public abstract class ChatCommand {
		protected String commandName;
		protected CommandAccessLevel requiredAccessLevel;
		protected boolean isEnabled;
		
		ChatCommand(String commandName, boolean isEnabled, CommandAccessLevel requiredAccessLevel) {
			this.commandName = commandName;
			this.isEnabled = isEnabled;
			this.requiredAccessLevel = requiredAccessLevel;
		}
		
		String getName() {
			return commandName;
		}
		
		CommandAccessLevel getRequiredAccessLevel() {
			return requiredAccessLevel;
		}
		
		boolean isEnabled() {
			return isEnabled;
		}
		abstract int handle(ClientInfo invoker, CommandAccessLevel invokerAccessLevel, String input);
		
		public enum CommandAccessLevel {
			ALL(0), STAFF(1), OWNER(2);
			
			private Integer level;	
			
			CommandAccessLevel(Integer level) {
				this.level = level;
			}
			
			public static int getAccessLevel(CommandAccessLevel level) {
				return level.level;
			}
		}
}
