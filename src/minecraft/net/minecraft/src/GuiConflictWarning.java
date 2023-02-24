package net.minecraft.src;

public class GuiConflictWarning extends GuiScreen {
	private int updateCounter = 0;

	public void updateScreen() {
		++this.updateCounter;
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Back to title screen"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 0) {
				this.mc.displayGuiScreen(new GuiMainMenu());
			}

		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Level save conflict", this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
		this.drawString(this.fontRenderer, "Minecraft detected a conflict in the level save data.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
		this.drawString(this.fontRenderer, "This could be caused by two copies of the game", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
		this.drawString(this.fontRenderer, "accessing the same level.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
		this.drawString(this.fontRenderer, "To prevent level corruption, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 45, 10526880);
		super.drawScreen(i1, i2, f3);
	}
}
