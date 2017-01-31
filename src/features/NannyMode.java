package features;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import sfibot.Config;
import sfibot.Server;


public class NannyMode {
	private static Boolean nannyMode = Config.getNannyModeFlag();
	private static List<Integer> restrictedChannels = new ArrayList<>();
	private static TS3Api api = Server.getApi();
	private static Properties chanFile = new Properties();
	private static InputStream cfIn = null;
	private static OutputStream cfOut = null;
	
	public static void enableNannyChannels() {
		if (!nannyMode) {
			nannyMode = true;

			//addRestrictedChannel(105058);	//Bar
//			addRestrictedChannel(125084);	//PS2 / Emerald NC, TR, VS
//			addRestrictedChannel(125085);
//			addRestrictedChannel(125083);
//			addRestrictedChannel(129546);	//RL Cas 1, 2, Comp 1
//			addRestrictedChannel(129544);
//			addRestrictedChannel(129545);
//			addRestrictedChannel(105205);	//Library
//			addRestrictedChannel(107773);	//Music Streaming
			
			Config.setNannyModeFlag(true);
			Config.saveConfig();
			
			for (int channelId : restrictedChannels) {
				api.addChannelPermission(channelId, "i_channel_needed_join_power", 5);
			}
		}
	}
	
	public static void disableNannyChannels() {
		if (nannyMode) {
			nannyMode = false;
			Config.setNannyModeFlag(false);
			Config.saveConfig();
			
			for (int channelId : restrictedChannels) {
				api.addChannelPermission(channelId, "i_channel_needed_join_power",  0);
			}
		}
	}
	
	public static List<String> getChannelList() {
		List<String> list = new ArrayList<>();
		
		for (Integer channelId : restrictedChannels) {
			list.add(api.getChannelInfo(channelId).getName());
		}
		
		return list;
	}
	
	public static void closeChannel(int channelId) {
		if (nannyMode) {
			api.addChannelPermission(channelId, "i_channel_needed_join_power", 5);
		}
		
		restrictedChannels.add(channelId);
		chanFile.setProperty("" + channelId, "1");
		saveChanFile();
	}
	
	public static void openChannel(int channelId) {
		if (nannyMode) {
			api.addChannelPermission(channelId, "i_channel_needed_join_power",  0);
		}
		
		restrictedChannels.removeIf( i -> {
			return i == channelId;
		});
		chanFile.setProperty("" + channelId, "0");
		saveChanFile();
	}
	
	public static Boolean isActive() {
		return nannyMode;
	}
	
	public static Boolean isRestricted(int channelId) {
		return chanFile.getProperty("" + channelId, "0").equals("1");
	}
	
	private static void init() {
		restrictedChannels.clear();
		
		nannyMode = Config.getNannyModeFlag();
		
		if (nannyMode) {
			for (Channel channel : api.getChannels()) {
				if (isRestricted(channel.getId())) {
					restrictedChannels.add(channel.getId());
					api.addChannelPermission(channel.getId(), "i_channel_needed_join_power", 5);
				}
			}
		}
		else {
			for (Channel channel : api.getChannels()) {
				if (isRestricted(channel.getId())) {
					restrictedChannels.add(channel.getId());
				}
			}
		}
	}
	
	public static void loadChanFile() {
		try {
			cfIn = new FileInputStream("nannychans.cfg");
			chanFile.load(cfIn);
			
			init();
			
		} catch (IOException iox) {
			System.out.println("An error was encountered while loading the nannychans file.");
		} finally {
			if (cfIn != null) {
				try {
					cfIn.close();
				} catch (IOException iox2) {
					iox2.printStackTrace();
				}
			}
		}
	}
	
	public static void saveChanFile() {
		try  {
			cfOut = new FileOutputStream("nannychans.cfg");
			chanFile.store(cfOut, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeChanFile() {
		try {
			cfIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
