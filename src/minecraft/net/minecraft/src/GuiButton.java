package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
	public int width;
	public int height;
	public int xPosition;
	public int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;

	public GuiButton(int i1, int i2, int i3, String string4) {
		this(i1, i2, i3, 200, 20, string4);
	}

	protected GuiButton(int i1, int i2, int i3, int i4, int i5, String string6) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = i1;
		this.xPosition = i2;
		this.yPosition = i3;
		this.width = i4;
		this.height = i5;
		this.displayString = string6;
	}

	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(this.visible) {
			FontRenderer fontRenderer4 = minecraft.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/gui/gui.png"));
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

	public boolean mousePressed(int i1, int i2) {
		return this.enabled && i1 >= this.xPosition && i2 >= this.yPosition && i1 < this.xPosition + this.width && i2 < this.yPosition + this.height;
	}
}
