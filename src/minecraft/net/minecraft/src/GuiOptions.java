package net.minecraft.src;

public class GuiOptions extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Options";
	private GameSettings options;

	public GuiOptions(GuiScreen guiScreen1, GameSettings gameSettings2) {
		this.parentScreen = guiScreen1;
		this.options = gameSettings2;
	}

	public void initGui() {
		for(int i1 = 0; i1 < this.options.numberOfOptions; ++i1) {
			this.controlList.add(new GuiSmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getOptionDisplayString(i1)));
		}

		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id < 100) {
				this.options.setOptionValue(guiButton1.id, 1);
				guiButton1.displayString = this.options.getOptionDisplayString(guiButton1.id);
			}

			if(guiButton1.id == 100) {
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if(guiButton1.id == 200) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
