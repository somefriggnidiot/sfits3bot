package features;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.Codec;

import sfibot.Server;

public class EventChannelGenerater {
	private static TS3Api api = Server.getApi();
	private static Integer rlGaTourneyParent;
	
	public static void createRlGaTourney() {
		
		Map<ChannelProperty, String> chanParams = new HashMap<>();
		
		//Create params for top channel.
		chanParams.put(ChannelProperty.CHANNEL_CODEC, String.valueOf(Codec.OPUS_VOICE.getIndex()));
		chanParams.put(ChannelProperty.CHANNEL_CODEC_QUALITY, "9");
		chanParams.put(ChannelProperty.CHANNEL_MAXCLIENTS, "0");
		chanParams.put(ChannelProperty.CHANNEL_FLAG_MAXFAMILYCLIENTS_UNLIMITED, "1");
		chanParams.put(ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT, "1");
		
		//Create top channel and clear params.
		api.createChannel("Gentleman's Agreement Tournament", chanParams);
		chanParams.clear();
		
		//Get channel ID of top channel for future reference.
		rlGaTourneyParent = api.getChannelByNameExact("Gentleman's Agreement Tournament", false).getId();

		chanParams.put(ChannelProperty.CPID, String.valueOf(rlGaTourneyParent));
		chanParams.put(ChannelProperty.CHANNEL_CODEC, String.valueOf(Codec.OPUS_VOICE.getIndex()));
		chanParams.put(ChannelProperty.CHANNEL_CODEC_QUALITY, "9");
		chanParams.put(ChannelProperty.CHANNEL_MAXCLIENTS, "-1");
		chanParams.put(ChannelProperty.CHANNEL_FLAG_MAXFAMILYCLIENTS_UNLIMITED, "1");
		chanParams.put(ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT, "1");
		
		api.createChannel("GA Tournament Lobby", chanParams);
		api.createChannel("GA Tournament Waiting Room", chanParams);
		
		chanParams.put(ChannelProperty.CHANNEL_PASSWORD, "applejacks");
		
		api.createChannel("GAT Casters Corner", chanParams);
		api.createChannel("GAT Team 1", chanParams);
		api.createChannel("GAT Team 2", chanParams);
	}
	
	public static void deleteRlGaTourney() {
		api.deleteChannel(rlGaTourneyParent);
		rlGaTourneyParent = 0;
	}
}
