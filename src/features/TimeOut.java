package features;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.theholywaffle.teamspeak3.TS3Api;
import sfibot.Config;
import sfibot.Server;

public class TimeOut{
	 private Integer punishmentRemaining;
	 private Integer clientId;
	 private Integer clientDbId;
	
	 private final Integer timeoutChannel = Config.getTimeoutChannelId();
	 private final Integer timeoutGroup	= Config.getTimeoutGroupId();
	 private final TS3Api api = Server.getApi();
	 private final  ScheduledExecutorService punisher = Executors.newScheduledThreadPool(1);
	 
	private Runnable punishment = new Runnable() {
		public void run() {
			if (punishmentRemaining > 0) {
				punishmentRemaining--;
			}
			else {
				api.removeClientFromServerGroup(timeoutGroup, clientDbId);
				api.sendPrivateMessage(clientId, "You are no longer in time-out. Please adhere to the server and community rules in the future.");
				punisher.shutdown();
			}
		}
	};
	
	public TimeOut(Integer duration, Integer victimId) {
		clientId = victimId;
		clientDbId = api.getClientInfo(clientId).getDatabaseId();
		punishmentRemaining = duration;
		
		api.addClientToServerGroup(timeoutGroup, clientDbId);
		api.moveClient(clientId, timeoutChannel);
		
		punisher.scheduleAtFixedRate(punishment, 0, 1, TimeUnit.SECONDS);
	}
}
