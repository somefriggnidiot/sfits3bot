package sfibot;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
	private static int major, minor, release, build;
	
	public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Config.loadConfig();
            	Gui.createAndShowGUI();
            }
        });
	}
	
	public static String timeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return("[" + dateFormat.format(System.currentTimeMillis()) + "] ");
	}
	
	public static String getVersion(boolean withBuild) {
		String version;
		
		major = 1;
		
		minor = 10;
		
		/**	
		 * 		TODO:
		 * 			- Set focus to input box on pop-up launches.
		 * 			- Fix inability to reconnect after any sort of in-app D/C.
		 * 			- Ensure idle checker starts on connection if enabled on D/C.
		 * 			- Disable UI/Menu aspects based on connection status.
		 * 			- Create menu item to open changelog.
		 * 			- Add "!goto" and "!tome" to accompany "!admin" command.
		 */
		release = 4; 	
		
		build = 284;
		
		if (withBuild) {
			version = "v." + major + "." + minor + "." + release + "." + build;
		}
		else {
			version = "v." + major + "." + minor + "." + release;
		}
		
		return version;
	}
	
	public static JPanel getInfoPanel() {
		//TODO Create webpage and display that instead.
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel label;
		GridBagConstraints c = new GridBagConstraints();
		
		label = new JLabel("<html>"
				+ "This bot is currently in active development, and features are likely to break frequently and inconsitently.<br />"
				+ "For questions, suggestions, and feedback, send an email to <a href=\"mailto:idiot@somefriggnidiot.com\">idiot@somefriggnidiot.com</a><br />"
				+ "<br />"
				+ "Full Version ID: " + getVersion(true)
				+ "</html>");
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);
		
		return panel;
	}
	
	public static void confirmExit() {
		if (Gui.getTitle().contains("-")) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("The bot is still connected. Are you sure you wish to exit?");
			panel.add(label);
			int confirm = JOptionPane.showConfirmDialog(Gui.getFrame(), panel, "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if (confirm == JOptionPane.OK_OPTION) {
				Server.disconnect();
				System.exit(0);
			}
			else if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		else {
			System.exit(0);
		}
	}
}