package features;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;

import sfibot.Server;

public class ChannelNameChanger {
	Map<Integer, String> changerChannelDefaultNames = new HashMap<>();
	ChannelInfo channelToCheck;
	Integer changerChannelParent = 129473;
	
	//Constructor used for resetting channels.
	public ChannelNameChanger() {
		init();
		//Check each Changer Channel for empty.
		for(Integer channelId : changerChannelDefaultNames.keySet()) {
			//If channel's empty, check for need to rename.
			if (Server.getApi().getChannelInfo(channelId).getSecondsEmpty() >= 0) { 
				String defaultName = changerChannelDefaultNames.get(channelId);
				//Only rename if channel's not already default name.
				if (!Server.getApi().getChannelInfo(channelId).getName().equals(defaultName)) {
					changeChannelName(channelId, defaultName);
				}
			}
		} //End loop.
	}
	
	public ChannelNameChanger(int channelIdToCheck, String message) {
		init(channelIdToCheck);
		
		//System.out.println("Checking to see if we rename channel."); //TODO Debug: Seeing if called.
		if (isChangerChannel(channelToCheck)) {
			//System.out.println("Renaming channel to new name."); //TODO Debug: Seeing if we rename.
			String[] args = message.split(" ", 2);
			if (args[0].equals("!renameroom") || args[0].equals("renameroom")) {
				changeChannelName(channelIdToCheck, args[1]);
			}
		}
	}
	
	public void changeChannelName(int channelId, String newChanName) {
		Map<ChannelProperty, String> properties = new HashMap<>();
		properties.put(ChannelProperty.CHANNEL_NAME, newChanName);
		
		Server.getApi().editChannel(channelId, properties);
	}
	
	private boolean isChangerChannel(ChannelInfo channelToCheck) {
		//System.out.println("Channel Parent: " + channelToCheck.getParentChannelId() + "Changer Parent: " + changerChannelParent); 	//TODO DEBUG
		if (channelToCheck.getParentChannelId()  == changerChannelParent) {
			//System.out.println("User is in a changeable channel."); //TODO Debug: See if conditions pass.
			return true;
		}
		//System.out.println("Not in a changeable channel."); 	//TODO DEBUG
		return false;
	}

	public void init(int channelIdToCheck) {
		setChangerChannels();
		
		channelToCheck = Server.getApi().getChannelInfo(channelIdToCheck);
	}
	
	public void init() {
		setChangerChannels();
	}
	
	public void setChangerChannels() {
		changerChannelDefaultNames.put(129474, "General Gaming Lobby 1");
		changerChannelDefaultNames.put(129475, "General Gaming Lobby 2");
		changerChannelDefaultNames.put(129476, "General Gaming Lobby 3");
	}
}
