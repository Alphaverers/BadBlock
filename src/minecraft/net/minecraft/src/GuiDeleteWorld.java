package net.minecraft.src;

import java.io.File;

import net.minecraft.client.Minecraft;

public class GuiDeleteWorld extends GuiSelectWorld {
	public GuiDeleteWorld(GuiScreen guiScreen1) {
		super(guiScreen1);
		this.screenTitle = "Delete world";
	}

	public void initButtons() {
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	public void selectWorld(int i1) {
		String string2 = this.getSaveName(i1);
		if(string2 != null) {
			this.mc.displayGuiScreen(new GuiYesNo(this, "Are you sure you want to delete this world?", "\'" + string2 + "\' will be lost forever!", i1));
		}

	}

	public void deleteWorld(boolean z1, int i2) {
		if(z1) {
			File file3 = Minecraft.getMinecraftDir();
			World.deleteWorld(file3, this.getSaveName(i2));
		}

		this.mc.displayGuiScreen(this.parentScreen);
	}
}
