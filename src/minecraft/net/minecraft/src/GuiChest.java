package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiChest extends GuiContainer {
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int inventoryRows = 0;

	public GuiChest(IInventory iInventory1, IInventory iInventory2) {
		this.upperChestInventory = iInventory1;
		this.lowerChestInventory = iInventory2;
		this.allowUserInput = false;
		short s3 = 222;
		int i4 = s3 - 108;
		this.inventoryRows = iInventory2.getSizeInventory() / 9;
		this.ySize = i4 + this.inventoryRows * 18;
		int i5 = (this.inventoryRows - 4) * 18;

		int i6;
		int i7;
		for(i6 = 0; i6 < this.inventoryRows; ++i6) {
			for(i7 = 0; i7 < 9; ++i7) {
				this.inventorySlots.add(new SlotInventory(this, iInventory2, i7 + i6 * 9, 8 + i7 * 18, 18 + i6 * 18));
			}
		}

		for(i6 = 0; i6 < 3; ++i6) {
			for(i7 = 0; i7 < 9; ++i7) {
				this.inventorySlots.add(new SlotInventory(this, iInventory1, i7 + (i6 + 1) * 9, 8 + i7 * 18, 103 + i6 * 18 + i5));
			}
		}

		for(i6 = 0; i6 < 9; ++i6) {
			this.inventorySlots.add(new SlotInventory(this, iInventory1, i6, 8 + i6 * 18, 161 + i5));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 0xFFFFFF);
		this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 0xFFFFFF);
	}

	protected void drawGuiContainerBackgroundLayer(float f1) {
		int i2 = this.mc.renderEngine.getTexture("/gui/container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i2);
		int i3 = (this.width - this.xSize) / 2;
		int i4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(i3, i4 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}
