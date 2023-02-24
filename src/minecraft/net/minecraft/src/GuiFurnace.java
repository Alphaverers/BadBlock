package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiFurnace extends GuiContainer {
	private TileEntityFurnace furnaceInventory;

	public GuiFurnace(InventoryPlayer inventoryPlayer1, TileEntityFurnace tileEntityFurnace2) {
		this.furnaceInventory = tileEntityFurnace2;
		this.inventorySlots.add(new SlotInventory(this, tileEntityFurnace2, 0, 56, 17));
		this.inventorySlots.add(new SlotInventory(this, tileEntityFurnace2, 1, 56, 53));
		this.inventorySlots.add(new SlotInventory(this, tileEntityFurnace2, 2, 116, 35));

		int i3;
		for(i3 = 0; i3 < 3; ++i3) {
			for(int i4 = 0; i4 < 9; ++i4) {
				this.inventorySlots.add(new SlotInventory(this, inventoryPlayer1, i4 + (i3 + 1) * 9, 8 + i4 * 18, 84 + i3 * 18));
			}
		}

		for(i3 = 0; i3 < 9; ++i3) {
			this.inventorySlots.add(new SlotInventory(this, inventoryPlayer1, i3, 8 + i3 * 18, 142));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Furnace", 60, 6, 0xFFFFFF);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 0xFFFFFF);
	}

	protected void drawGuiContainerBackgroundLayer(float f1) {
		int i2 = this.mc.renderEngine.getTexture("/gui/furnace.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i2);
		int i3 = (this.width - this.xSize) / 2;
		int i4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
		int i5;
		if(this.furnaceInventory.isBurning()) {
			i5 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(i3 + 56, i4 + 36 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
		}

		i5 = this.furnaceInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(i3 + 79, i4 + 34, 176, 14, i5 + 1, 16);
	}
}
