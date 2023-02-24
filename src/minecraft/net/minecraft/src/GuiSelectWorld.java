package net.minecraft.src;

import java.io.File;

import net.minecraft.client.Minecraft;

public class GuiSelectWorld extends GuiScreen {
	protected GuiScreen parentScreen;
	protected String screenTitle = "Select world";
	private boolean selected = false;

	public GuiSelectWorld(GuiScreen guiScreen1) {
		this.parentScreen = guiScreen1;
	}

	public void initGui() {
		File file1 = Minecraft.getMinecraftDir();

		for(int i2 = 0; i2 < 5; ++i2) {
			NBTTagCompound nBTTagCompound3 = World.getLevelData(file1, "World" + (i2 + 1));
			if(nBTTagCompound3 == null) {
				this.controlList.add(new GuiButton(i2, this.width / 2 - 100, this.height / 6 + 24 * i2, "- empty -"));
			} else {
				String string4 = "World " + (i2 + 1);
				long j5 = nBTTagCompound3.getLong("SizeOnDisk");
				string4 = string4 + " (" + (float)(j5 / 1024L * 100L / 1024L) / 100.0F + " MB)";
				this.controlList.add(new GuiButton(i2, this.width / 2 - 100, this.height / 6 + 24 * i2, string4));
			}
		}

		this.initButtons();
	}

	protected String getSaveName(int i1) {
		File file2 = Minecraft.getMinecraftDir();
		return World.getLevelData(file2, "World" + i1) != null ? "World" + i1 : null;
	}

	public void initButtons() {
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Delete world..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id < 5) {
				this.selectWorld(guiButton1.id + 1);
			} else if(guiButton1.id == 5) {
				this.mc.displayGuiScreen(new GuiDeleteWorld(this));
			} else if(guiButton1.id == 6) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void selectWorld(int i1) {
		this.mc.displayGuiScreen((GuiScreen)null);
		if(!this.selected) {
			this.selected = true;
			this.mc.playerController = new PlayerControllerSP(this.mc);
			this.mc.startWorld("World" + i1);
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
