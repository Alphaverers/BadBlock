package net.minecraft.src;

public class GuiYesNo extends GuiScreen {
	private GuiScreen parentScreen;
	private String message1;
	private String message2;
	private int worldNumber;

	public GuiYesNo(GuiScreen guiScreen1, String string2, String string3, int i4) {
		this.parentScreen = guiScreen1;
		this.message1 = string2;
		this.message2 = string3;
		this.worldNumber = i4;
	}

	public void initGui() {
		this.controlList.add(new GuiSmallButton(0, this.width / 2 - 155 + 0, this.height / 6 + 96, "Yes"));
		this.controlList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, "No"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		this.parentScreen.deleteWorld(guiButton1.id == 0, this.worldNumber);
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 70, 0xFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.message2, this.width / 2, 90, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
