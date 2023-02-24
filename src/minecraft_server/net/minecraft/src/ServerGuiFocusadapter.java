package net.minecraft.src;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ServerGuiFocusadapter extends FocusAdapter {
	final ServerGUI mcServerGui;

	ServerGuiFocusadapter(ServerGUI serverGUI1) {
		this.mcServerGui = serverGUI1;
	}

	public void focusGained(FocusEvent focusEvent1) {
	}
}
