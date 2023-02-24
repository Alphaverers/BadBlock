package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiGameOver extends GuiScreen {
	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Respawn"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Title menu"));
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected void keyTyped(char c1, int i2) {
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			;
		}

		if(guiButton1.id == 1) {
			this.mc.respawn();
			this.mc.displayGuiScreen((GuiScreen)null);
		}

		if(guiButton1.id == 2) {
			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		this.drawCenteredString(this.fontRenderer, this.mc.options.difficulty == 4 ? "Shattered" : "Game over!", this.width / 2 / 2, 30, 0xFFFFFF);
		GL11.glPopMatrix();
		this.drawCenteredString(this.fontRenderer, "Milestone: " + this.mc.theWorld.milestone + (this.mc.theWorld.exclFrailMode ? "*" : ""), this.width / 2, 100, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
