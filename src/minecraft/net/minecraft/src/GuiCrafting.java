package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiCrafting extends GuiContainer {
	public CraftingInventoryWorkbenchCB craftingInventory = new CraftingInventoryWorkbenchCB();

	public GuiCrafting(InventoryPlayer inventoryPlayer1) {
		this.inventorySlots.add(new SlotCrafting(this, this.craftingInventory.craftMatrix, this.craftingInventory.craftResult, 0, 124, 35));

		int i2;
		int i3;
		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 3; ++i3) {
				this.inventorySlots.add(new SlotInventory(this, this.craftingInventory.craftMatrix, i3 + i2 * 3, 30 + i3 * 18, 17 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 9; ++i3) {
				this.inventorySlots.add(new SlotInventory(this, inventoryPlayer1, i3 + (i2 + 1) * 9, 8 + i3 * 18, 84 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 9; ++i2) {
			this.inventorySlots.add(new SlotInventory(this, inventoryPlayer1, i2, 8 + i2 * 18, 142));
		}

	}

	public void onGuiClosed() {
		super.onGuiClosed();
		this.craftingInventory.onCraftGuiClosed(this.mc.thePlayer);
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 28, 6, 0xFFFFFF);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 0xFFFFFF);
	}

	protected void drawGuiContainerBackgroundLayer(float f1) {
		int i2 = this.mc.renderEngine.getTexture("/gui/crafting.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i2);
		int i3 = (this.width - this.xSize) / 2;
		int i4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
	}
}
