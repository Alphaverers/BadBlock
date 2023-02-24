package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

class ServerGuiCommandListener implements ActionListener {
	final JTextField textField;
	final ServerGUI mcServerGui;

	ServerGuiCommandListener(ServerGUI serverGUI1, JTextField jTextField2) {
		this.mcServerGui = serverGUI1;
		this.textField = jTextField2;
	}

	public void actionPerformed(ActionEvent actionEvent1) {
		String string2 = this.textField.getText().trim();
		if(string2.length() > 0) {
			ServerGUI.getMinecraftServer(this.mcServerGui).addCommand(string2, this.mcServerGui);
		}

		this.textField.setText("");
	}
}
