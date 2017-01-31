package sfibot;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WelcomeMessage 
{
	private static JTextField field = null;
	
	//Guest Messages
	public static String getNewUserMessage()
	{
		return Config.getNewUserMessage();
	}
	public static void setNewUserMessage(String newUserMessage)
	{
		Config.setNewUserMessage(newUserMessage);
		Config.saveConfig();
	}
	
	//Non-Guest Messages
	public static String getNormalUserMessage()
	{
		return  Config.getNormalUserMessage();
	}
	public static void setNormalUserMessage(String normalUserMessage)
	{
		Config.setNormalUserMessage(normalUserMessage);
		Config.saveConfig();
	}
	
	//Bot Staff Messages
	public static String getBotStaffMessage()
	{
		return Config.getBotStaffMessage();
	}
	public static void setBotStaffMessage(String botStaffMessage)
	{
		Config.setBotStaffMessage(botStaffMessage);
		Config.saveConfig();
	}
	
	//Universal messages.
	public static String getUniversalWelcomeMessage()
	{
		return Config.getUniversalWelcomeMessage();
	}
	public static void setUniversalWelcomeMessage(String universalMessage)
	{
		Config.setUniversalWelcomeMessage(universalMessage);
		Config.saveConfig();
	}
	
	
	
	public static JPanel getWelcomeMessagePanel(WelcomeMessageTarget messageTarget)
	{
		JPanel panel = new JPanel();
		
		if (messageTarget == WelcomeMessageTarget.GUEST)
		{
			JLabel label = new JLabel ("Enter the new welcome message for guests: ");
			field = new JTextField (25);
			field.setText(Config.getNewUserMessage());
			panel.add(label);
			panel.add(field);
		}
		else if (messageTarget == WelcomeMessageTarget.NON_GUEST)
		{
			JLabel label = new JLabel ("Enter the new welcome message for non-guests: ");
			field = new JTextField (25);
			field.setText(Config.getNormalUserMessage());
			panel.add(label);
			panel.add(field);
		}
		else if (messageTarget == WelcomeMessageTarget.BOT_STAFF)
		{
			JLabel label = new JLabel ("Enter the new welcome message for bot staff: ");
			field = new JTextField (25);
			field.setText(Config.getBotStaffMessage());
			panel.add(label);
			panel.add(field);
		}
		else if (messageTarget == WelcomeMessageTarget.UNIVERSAL)
		{
			JLabel label = new JLabel ("Enter the new welcome message for all users: ");
			field = new JTextField (25);
			field.setText(Config.getUniversalWelcomeMessage());
			panel.add(label);
			panel.add(field);
		}
		
		return panel;
	}
	
	public static String getWelcomeMessage()
	{
		String message = field.getText();
		field.setText("");
		return message;
	}
	
	
	enum WelcomeMessageTarget
	{
		GUEST(0), NON_GUEST(1), BOT_STAFF(2), UNIVERSAL(3);
		
		private WelcomeMessageTarget(int value)
		{
		}
	}
	
	
}
