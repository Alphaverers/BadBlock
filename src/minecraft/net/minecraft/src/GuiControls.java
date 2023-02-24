package net.minecraft.src;

public class GuiControls extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;

	public GuiControls(GuiScreen prevScreen, GameSettings gameSettings) {
		this.parentScreen = prevScreen;
		this.options = gameSettings;
	}

	public void initGui() {
		for(int i1 = 0; i1 < this.options.keyBindings.length; ++i1) {
			this.controlList.add(new GuiSmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getKeyBindingDescription(i1)));
		}

		this.controlList.add(new GuiButton(201, this.width / 2 - 100, this.height / 6 + 140, "Gamepad sensitivity: " + InputHandler.lookSens));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		for(int i2 = 0; i2 < this.options.keyBindings.length; ++i2) {
			((GuiButton)this.controlList.get(i2)).displayString = this.options.getKeyBindingDescription(i2);
		}

		if(guiButton1.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else if(guiButton1.id == 201) {
			++InputHandler.lookSens;
			if(InputHandler.lookSens == 11) {
				InputHandler.lookSens = 1;
			}

			guiButton1.displayString = "Gamepad sensitivity: " + InputHandler.lookSens;
		} else {
			this.buttonId = guiButton1.id;
			guiButton1.displayString = "> " + this.options.getKeyBindingDescription(guiButton1.id) + " <";
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, i2);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.getKeyBindingDescription(this.buttonId);
			this.buttonId = -1;
		} else {
			super.keyTyped(c1, i2);
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
