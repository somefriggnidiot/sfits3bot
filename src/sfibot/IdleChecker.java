package sfibot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.theholywaffle.teamspeak3.api.CommandFuture;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class IdleChecker extends TimerTask
{
	private static IdleChecker checker;
	private static Timer checkerTimer;
	private static JTextField field;
	
	@Override
	public void run() 
	{
		CommandFuture<List<Client>> clients = Server.getApiAsync().getClients();
		
		clients.onSuccess(new CommandFuture.SuccessListener<List<Client>>() 
		{
			@Override
			public void handleSuccess(List<Client> clients) 
			{
				for (Client client : clients)
				{
					boolean ignore = false;
					
					for (int group : client.getServerGroups())
					{
						if (group == Config.getIdleIgnoreGroup())
						{
							ignore = true;
							break;
						}
						if (ignore)
						{
							break;
						}
					}
					if (!ignore && client.getIdleTime() / 60000 >= Config.getMaxIdleTimeMinutes() && client.getChannelId() != Config.getIdleChannelId() && client.getId() != Server.getMyClientId() && client.getChannelId() != Config.getIdleIgnoreChannelId())
					{
						System.out.println("\t" + client.getNickname() + " has been been moved for being idle for " + (client.getIdleTime() / 60000) + " minutes.");
						Server.getApi().moveClient(client.getId(), Config.getIdleChannelId());
						Server.getApi().sendPrivateMessage(client.getId(), "You have been moved for being idle longer than " + Config.getMaxIdleTimeMinutes() + " minutes.");
					}
				}
				return;
			}
		});
		return;
	}
	
	public static void start()
	{
		if (Gui.idleCheckerOn())
		{
			checker = new IdleChecker();
			checkerTimer = new Timer();
			checkerTimer.schedule(checker, 0, 1000);
		}
	}
	
	public static void stop()
	{
		checker.cancel();
		checkerTimer.cancel();
		checkerTimer.purge();
	}
	
	public static void updateIdleCheckerAttribute(IdleCheckerAttribute attribute)
	{
		int result = JOptionPane.showConfirmDialog(Gui.getFrame(), getIdleCheckerPopupPanel(attribute), "Update IdleChecker Attribute", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION)
		{
			if (attribute == IdleCheckerAttribute.IDLE_CHANNEL)
			{
				//Update idle channel.
				Config.setIdleChannelId(getInput());
				Config.saveConfig();
			}
			else if (attribute == IdleCheckerAttribute.IDLE_IGNORE_GROUP)
			{
				//Update idle ignore group.
				Config.setIdleIgnoreGroup(getInput());
				Config.saveConfig();
			}
			else if (attribute == IdleCheckerAttribute.MAX_IDLE_TIME)
			{
				//Update max idle time.
				Config.setMaxIdleTimeMinutes(getInput());
				Config.saveConfig();
			}
			else if (attribute == IdleCheckerAttribute.IDLE_IGNORE_CHANNEL)
			{
				//Update idle ignore channel.
				Config.setIdleIgnoreChannelId(getInput());
				Config.saveConfig();
			}
		}
	}
	
 	private static JPanel getIdleCheckerPopupPanel(IdleCheckerAttribute attribute)
	{
		JPanel panel = new JPanel();
		
		if (attribute == IdleCheckerAttribute.MAX_IDLE_TIME)
		{
			JLabel label = new JLabel ("Enter the desired maximum idle time: ");
			field = new JTextField (3);
			field.setText("" + Config.getMaxIdleTimeMinutes());
			panel.add(label);
			panel.add(field);
		}
		else if (attribute == IdleCheckerAttribute.IDLE_CHANNEL)
		{
			JLabel label = new JLabel ("Enter the ID of the channel idle users should be moved to: ");
			field = new JTextField (6);
			field.setText("" + Config.getIdleChannelId());
			panel.add(label);
			panel.add(field);
		}
		else if (attribute == IdleCheckerAttribute.IDLE_IGNORE_GROUP)
		{
			JLabel label = new JLabel ("Enter the ID of the group set to be exempt from the idle check: ");
			field = new JTextField (6);
			field.setText("" + Config.getIdleIgnoreGroup());
			panel.add(label);
			panel.add(field);
		}
		else if (attribute == IdleCheckerAttribute.IDLE_IGNORE_CHANNEL)
		{
			JLabel label = new JLabel ("Enter the Channel ID of the channel that will be skipped during the idle check: ");
			field = new JTextField(6);
			field.setText("" + Config.getIdleIgnoreChannelId());
			panel.add(label);
			panel.add(field);
		}
		
		return panel;
	}
 	
	public static String getInput()
	{
		String input = field.getText();
		field.setText("");
		return input;
	}
 	
	public static void printStatus()
	{
		if (Gui.idleCheckerOn())
		{
			System.out.println("\tIdle Checker: ");
		}
	}
	
 	enum IdleCheckerAttribute
 	{
 		MAX_IDLE_TIME(0), IDLE_CHANNEL(1), IDLE_IGNORE_GROUP(2), IDLE_IGNORE_CHANNEL(3);
 		
		private IdleCheckerAttribute(int value)
		{
		}
 	}
 	
 	
}
