package net.minecraft.src;

public class GuiDownloadTerrain extends GuiScreen {
	private NetClientHandler netHandler;
	private int updateCounter = 0;

	public GuiDownloadTerrain(NetClientHandler netHandler) {
		this.netHandler = netHandler;
	}

	protected void keyTyped(char c1, int i2) {
	}

	public void initGui() {
		this.controlList.clear();
	}

	public void updateScreen() {
		++this.updateCounter;
		if(this.updateCounter % 20 == 0) {
			this.netHandler.addToSendQueue(new Packet0KeepAlive());
		}

		if(this.netHandler != null) {
			this.netHandler.processReadPackets();
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawBackground(0);
		this.drawCenteredString(this.fontRenderer, "Downloading terrain", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
