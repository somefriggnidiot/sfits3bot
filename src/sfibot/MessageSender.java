package sfibot;

import java.util.List;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class MessageSender 
{
	private static JTextField field = null;
	private static Map<Integer, JCheckBox> clientBoxes = getClientCheckboxList();
	
	MessageSender(TextMessageTargetMode target)
	{	
		if (target == TextMessageTargetMode.CLIENT)
		{
			//Popup of client list. Selects targets.
			int result = JOptionPane.showConfirmDialog(Gui.getFrame(), getClientListPanel(clientBoxes), "Select Target Clients", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
			{
				return;
			}
			//Reset list of targets.
			List<Integer> clientIds = new ArrayList<>();
			
			//If user selects "Okay" from client list, add clients to target list.
			if (result == JOptionPane.OK_OPTION)
			{
				for (int client : clientBoxes.keySet())
				{
					//If client is target, add to target list and reset box.
					if (clientBoxes.get(client).isSelected())
					{
						clientIds.add(client);
						clientBoxes.get(client).setSelected(false);
					}
				}
			}
			
			//Popup of message to send.
			if (JOptionPane.showConfirmDialog(Gui.getFrame(), getMessageSenderPanel(target), "Enter Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION)
			{
				//If "Okay" isn't selected, do nothing. At all.
				return;
			};
			
			//Send message to each target, clear message once done.
			for (Integer client : clientIds)
			{
				Server.getApi().sendPrivateMessage(client, getMessage());
			}
			resetMessage();
			return;
		}
		else
		{
			//Popup of message to send.
			if (JOptionPane.showConfirmDialog(Gui.getFrame(), getMessageSenderPanel(target), "Enter Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION)
			{
				//If "Okay" isn't selected, do nothing. At all.
				return;
			};
			Server.getApi().sendTextMessage(target, -1, getMessage());
			resetMessage();
		}
	}
	
	private static String getMessage()
	{
		return field.getText();
	}
	
	private static void resetMessage()
	{
		field.setText("");
	}
	
 	private static JPanel getMessageSenderPanel(TextMessageTargetMode target)
	{
		JPanel panel = new JPanel();
		
		if (target == TextMessageTargetMode.SERVER)
		{
			JLabel label = new JLabel ("Enter a message to send to the server: ");
			field = new JTextField (25);
			panel.add(label);
			panel.add(field);
		}
		else if (target == TextMessageTargetMode.CHANNEL)
		{
			JLabel label = new JLabel ("Enter a message to send to the current channel: ");
			field = new JTextField (25);
			panel.add(label);
			panel.add(field);
		}
		else if (target == TextMessageTargetMode.CLIENT)
		{
			JLabel label = new JLabel ("Enter a message to send to the selected client(s): ");
			field = new JTextField (25);
			panel.add(label);
			panel.add(field);
		}

		
		return panel;
	}
	
	private static Map<Integer, JCheckBox> getClientCheckboxList()
	{
		Map<Integer, JCheckBox> mappedList = new HashMap<Integer, JCheckBox>();
		
		for (Client client : Server.getApi().getClients())
		{
			JCheckBox clientBox = new JCheckBox("" + client.getId() + "-" + client.getNickname());
			mappedList.put(client.getId(), clientBox);
		}
		
		return mappedList;
	}
	
	private static JPanel getClientListPanel(Map<Integer, JCheckBox> clientList)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		int row = 0;
		
		for(int clientId : clientList.keySet())
		{
			JCheckBox clientBox = clientList.get(clientId);
			c.gridy = row++;
			panel.add(clientBox, c);
		}
		
		return panel;
	}
}
