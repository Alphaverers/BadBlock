package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GuiStatsListener implements ActionListener {
	final GuiStatsComponent component;

	GuiStatsListener(GuiStatsComponent component) {
		this.component = component;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		GuiStatsComponent.update(this.component);
	}
}
