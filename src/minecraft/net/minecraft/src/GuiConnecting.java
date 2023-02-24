package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen {
	private NetClientHandler clientHandler;
	private boolean cancelled = false;

	public GuiConnecting(Minecraft minecraft1, String string2, int i3) {
		minecraft1.changeWorld1((World)null);
		(new ThreadConnectToServer(this, minecraft1, string2, i3)).start();
	}

	public void updateScreen() {
		if(this.clientHandler != null) {
			this.clientHandler.processReadPackets();
		}

	}

	protected void keyTyped(char c1, int i2) {
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			this.cancelled = true;
			if(this.clientHandler != null) {
				this.clientHandler.disconnect();
			}

			this.mc.displayGuiScreen(new GuiMainMenu());
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		if(this.clientHandler == null) {
			this.drawCenteredString(this.fontRenderer, "Connecting to the server...", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 0xFFFFFF);
		} else {
			this.drawCenteredString(this.fontRenderer, "Logging in...", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, this.clientHandler.loginProgress, this.width / 2, this.height / 2 - 10, 0xFFFFFF);
		}

		super.drawScreen(i1, i2, f3);
	}

	static NetClientHandler setNetClientHandler(GuiConnecting guiConnecting0, NetClientHandler netClientHandler1) {
		return guiConnecting0.clientHandler = netClientHandler1;
	}

	static boolean isCancelled(GuiConnecting guiConnecting0) {
		return guiConnecting0.cancelled;
	}

	static NetClientHandler getNetClientHandler(GuiConnecting guiConnecting0) {
		return guiConnecting0.clientHandler;
	}
}
