package net.minecraft.src;

public class ScreenConfirmQuit extends GuiScreen {
	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 62, "Save and Quit"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Go back"));
	}

	protected void keyTyped(char c1, int i2) {
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			;
		}

		if(guiButton1.id == 1) {
			if(this.mc.isMultiplayerWorld()) {
				this.mc.theWorld.sendQuittingDisconnectingPacket();
			}

			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

		if(guiButton1.id == 2) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawGradientRect(0, 0, this.width, this.height, -13619152, -14671840);
		this.drawCenteredString(this.fontRenderer, "Quitting the game will end the current boss fight. Are you sure?", this.width / 2, 30, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}
