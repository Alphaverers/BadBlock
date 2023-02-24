package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiInventory extends GuiContainer {
	private CraftingInventoryPlayerCB craftingInventory;
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventory(IInventory iInventory1, ItemStack[] itemStack2) {
		this.allowUserInput = true;
		this.craftingInventory = new CraftingInventoryPlayerCB(itemStack2);
		this.inventorySlots.add(new SlotCrafting(this, this.craftingInventory.craftMatrix, this.craftingInventory.craftResult, 0, 144, 36));

		int i3;
		int i4;
		for(i3 = 0; i3 < 2; ++i3) {
			for(i4 = 0; i4 < 2; ++i4) {
				this.inventorySlots.add(new SlotInventory(this, this.craftingInventory.craftMatrix, i4 + i3 * 2, 88 + i4 * 18, 26 + i3 * 18));
			}
		}

		for(i3 = 0; i3 < 4; ++i3) {
			this.inventorySlots.add(new SlotArmor(this, this, iInventory1, iInventory1.getSizeInventory() - 1 - i3, 8, 8 + i3 * 18, i3));
		}

		for(i3 = 0; i3 < 3; ++i3) {
			for(i4 = 0; i4 < 9; ++i4) {
				this.inventorySlots.add(new SlotInventory(this, iInventory1, i4 + (i3 + 1) * 9, 8 + i4 * 18, 84 + i3 * 18));
			}
		}

		for(i3 = 0; i3 < 9; ++i3) {
			this.inventorySlots.add(new SlotInventory(this, iInventory1, i3, 8 + i3 * 18, 142));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 86, 16, 0xFFFFFF);
	}

	public void drawScreen(int i1, int i2, float f3) {
		super.drawScreen(i1, i2, f3);
		this.xSize_lo = (float)i1;
		this.ySize_lo = (float)i2;
	}

	protected void drawGuiContainerBackgroundLayer(float f1) {
		int i2 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i2);
		int i3 = (this.width - this.xSize) / 2;
		int i4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(i3 + 51), (float)(i4 + 75), 50.0F);
		float f5 = 30.0F;
		GL11.glScalef(-f5, f5, f5);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f6 = this.mc.thePlayer.renderYawOffset;
		float f7 = this.mc.thePlayer.rotationYaw;
		float f8 = this.mc.thePlayer.rotationPitch;
		float f9 = (float)(i3 + 51) - this.xSize_lo;
		float f10 = (float)(i4 + 75 - 50) - this.ySize_lo;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(f10 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(f9 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(f9 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(f10 / 40.0F))) * 20.0F;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		this.mc.thePlayer.renderYawOffset = f6;
		this.mc.thePlayer.rotationYaw = f7;
		this.mc.thePlayer.rotationPitch = f8;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
