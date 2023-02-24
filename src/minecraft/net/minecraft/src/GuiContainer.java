package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen {
	private static RenderItem itemRenderer = new RenderItem();
	protected int xSize = 176;
	protected int ySize = 166;
	protected List inventorySlots = new ArrayList();

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		int i4 = (this.width - this.xSize) / 2;
		int i5 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer(f3);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)i4, (float)i5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		for(int i6 = 0; i6 < this.inventorySlots.size(); ++i6) {
			SlotInventory slotInventory7 = (SlotInventory)this.inventorySlots.get(i6);
			this.drawSlotInventory(slotInventory7);
			if(slotInventory7.getIsMouseOverSlot(i1, i2)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int i8 = slotInventory7.xDisplayPosition;
				int i9 = slotInventory7.yDisplayPosition;
				this.drawGradientRect(i8, i9, i8 + 16, i9 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		InventoryPlayer inventoryPlayer10 = this.mc.thePlayer.inventory;
		if(inventoryPlayer10.draggedItemStack != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryPlayer10.draggedItemStack, i1 - i4 - 8, i2 - i5 - 8);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryPlayer10.draggedItemStack, i1 - i4 - 8, i2 - i5 - 8);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer(float f1);

	private void drawSlotInventory(SlotInventory slotInventory) {
		IInventory iInventory2 = slotInventory.inventory;
		int i3 = slotInventory.slotIndex;
		int i4 = slotInventory.xDisplayPosition;
		int i5 = slotInventory.yDisplayPosition;
		ItemStack itemStack6 = iInventory2.getStackInSlot(i3);
		if(itemStack6 == null) {
			int i7 = slotInventory.getBackgroundIconIndex();
			if(i7 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
				this.drawTexturedModalRect(i4, i5, i7 % 16 * 16, i7 / 16 * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				return;
			}
		}

		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemStack6, i4, i5);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemStack6, i4, i5);
	}

	private Slot getSlotAtPosition(int i1, int i2) {
		for(int i3 = 0; i3 < this.inventorySlots.size(); ++i3) {
			SlotInventory slotInventory4 = (SlotInventory)this.inventorySlots.get(i3);
			if(slotInventory4.getIsMouseOverSlot(i1, i2)) {
				return slotInventory4;
			}
		}

		return null;
	}

	protected void mouseClicked(int i1, int i2, int i3) {
		if(i3 == 0 || i3 == 1) {
			Slot slot4 = this.getSlotAtPosition(i1, i2);
			InventoryPlayer inventoryPlayer5 = this.mc.thePlayer.inventory;
			int i7;
			if(slot4 != null) {
				ItemStack itemStack6 = slot4.getStack();
				if(itemStack6 != null || inventoryPlayer5.draggedItemStack != null) {
					if(itemStack6 != null && inventoryPlayer5.draggedItemStack == null) {
						i7 = i3 == 0 ? itemStack6.stackSize : (itemStack6.stackSize + 1) / 2;
						inventoryPlayer5.draggedItemStack = slot4.inventory.decrStackSize(slot4.slotIndex, i7);
						if(itemStack6.stackSize == 0) {
							slot4.putStack((ItemStack)null);
						}

						slot4.onPickupFromSlot();
					} else if(itemStack6 == null && inventoryPlayer5.draggedItemStack != null && slot4.isItemValid(inventoryPlayer5.draggedItemStack)) {
						i7 = i3 == 0 ? inventoryPlayer5.draggedItemStack.stackSize : 1;
						if(i7 > slot4.inventory.getInventoryStackLimit()) {
							i7 = slot4.inventory.getInventoryStackLimit();
						}

						slot4.putStack(inventoryPlayer5.draggedItemStack.splitStack(i7));
						if(inventoryPlayer5.draggedItemStack.stackSize == 0) {
							inventoryPlayer5.draggedItemStack = null;
						}
					} else if(itemStack6 != null && inventoryPlayer5.draggedItemStack != null) {
						if(slot4.isItemValid(inventoryPlayer5.draggedItemStack)) {
							if(itemStack6.itemID != inventoryPlayer5.draggedItemStack.itemID) {
								if(inventoryPlayer5.draggedItemStack.stackSize <= slot4.inventory.getInventoryStackLimit()) {
									slot4.putStack(inventoryPlayer5.draggedItemStack);
									inventoryPlayer5.draggedItemStack = itemStack6;
								}
							} else if(itemStack6.itemID == inventoryPlayer5.draggedItemStack.itemID) {
								if(i3 == 0) {
									i7 = inventoryPlayer5.draggedItemStack.stackSize;
									if(i7 > slot4.inventory.getInventoryStackLimit() - itemStack6.stackSize) {
										i7 = slot4.inventory.getInventoryStackLimit() - itemStack6.stackSize;
									}

									if(i7 > inventoryPlayer5.draggedItemStack.getMaxStackSize() - itemStack6.stackSize) {
										i7 = inventoryPlayer5.draggedItemStack.getMaxStackSize() - itemStack6.stackSize;
									}

									inventoryPlayer5.draggedItemStack.splitStack(i7);
									if(inventoryPlayer5.draggedItemStack.stackSize == 0) {
										inventoryPlayer5.draggedItemStack = null;
									}

									itemStack6.stackSize += i7;
								} else if(i3 == 1) {
									i7 = 1;
									if(i7 > slot4.inventory.getInventoryStackLimit() - itemStack6.stackSize) {
										i7 = slot4.inventory.getInventoryStackLimit() - itemStack6.stackSize;
									}

									if(i7 > inventoryPlayer5.draggedItemStack.getMaxStackSize() - itemStack6.stackSize) {
										i7 = inventoryPlayer5.draggedItemStack.getMaxStackSize() - itemStack6.stackSize;
									}

									inventoryPlayer5.draggedItemStack.splitStack(i7);
									if(inventoryPlayer5.draggedItemStack.stackSize == 0) {
										inventoryPlayer5.draggedItemStack = null;
									}

									itemStack6.stackSize += i7;
								}
							}
						} else if(itemStack6.itemID == inventoryPlayer5.draggedItemStack.itemID && inventoryPlayer5.draggedItemStack.getMaxStackSize() > 1) {
							i7 = itemStack6.stackSize;
							if(i7 > 0 && i7 + inventoryPlayer5.draggedItemStack.stackSize <= inventoryPlayer5.draggedItemStack.getMaxStackSize()) {
								inventoryPlayer5.draggedItemStack.stackSize += i7;
								itemStack6.splitStack(i7);
								if(itemStack6.stackSize == 0) {
									slot4.putStack((ItemStack)null);
								}

								slot4.onPickupFromSlot();
							}
						}
					}
				}

				slot4.onSlotChanged();
			} else if(inventoryPlayer5.draggedItemStack != null) {
				int i9 = (this.width - this.xSize) / 2;
				i7 = (this.height - this.ySize) / 2;
				if(i1 < i9 || i2 < i7 || i1 >= i9 + this.xSize || i2 >= i7 + this.xSize) {
					EntityPlayerSP entityPlayerSP8 = this.mc.thePlayer;
					if(i3 == 0) {
						entityPlayerSP8.dropPlayerItem(inventoryPlayer5.draggedItemStack);
						inventoryPlayer5.draggedItemStack = null;
					}

					if(i3 == 1) {
						entityPlayerSP8.dropPlayerItem(inventoryPlayer5.draggedItemStack.splitStack(1));
						if(inventoryPlayer5.draggedItemStack.stackSize == 0) {
							inventoryPlayer5.draggedItemStack = null;
						}
					}
				}
			}
		}

	}

	protected void mouseMovedOrUp(int i1, int i2, int i3) {
		if(i3 == 0) {
			;
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == 1 || i2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void onGuiClosed() {
		InventoryPlayer inventoryPlayer1 = this.mc.thePlayer.inventory;
		if(inventoryPlayer1.draggedItemStack != null) {
			this.mc.thePlayer.dropPlayerItem(inventoryPlayer1.draggedItemStack);
			inventoryPlayer1.draggedItemStack = null;
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
