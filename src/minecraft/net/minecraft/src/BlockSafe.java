package net.minecraft.src;

public class BlockSafe extends BlockMultiSided {
	protected BlockSafe(int i1, int i2, int i3, int i4) {
		super(i1, i2, i3, i4);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		ItemStack itemStack6 = entityPlayer5.inventory.getCurrentItem();
		if(itemStack6 == null) {
			InputHandler.mc.displayGuiScreen(new ScreenKeyInput(InputHandler.mc, 1));
		}

		return true;
	}
}
