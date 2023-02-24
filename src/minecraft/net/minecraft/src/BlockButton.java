package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class BlockButton extends GuiButton {
	public int bID;
	public GuiScreen caller;

	public BlockButton(int i1, int i2, int i3, String string4, int i5, GuiScreen guiScreen6) {
		super(i1, i2, i3, 25, 20, string4);
		this.bID = i5;
		this.caller = guiScreen6;
	}

	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(this.visible) {
			FontRenderer fontRenderer4 = minecraft.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/gui/GUICREA.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			byte b5 = 1;
			boolean z6 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			if(!this.enabled) {
				b5 = 0;
			} else if(z6) {
				b5 = 2;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + b5 * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + b5 * 20, this.width / 2, this.height);
			if(!this.enabled) {
				this.drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
			} else if(z6) {
				this.drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
			} else {
				this.drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
			}

		}
	}
}
