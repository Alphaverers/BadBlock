package net.minecraft.src;

import java.util.Random;

public class BlockGlowingFlower extends BlockGlowing {
	int renderMode = 1;

	protected BlockGlowingFlower(int i1, int i2, int i3) {
		super(i1, i2, i3);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return true;
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		ItemStack itemStack6 = entityPlayer5.inventory.getCurrentItem();
		if(itemStack6 == null) {
			return false;
		} else {
			if(itemStack6.itemID == 116) {
				world1.setBlockWithNotify(i2, i3, i4, 117);
				entityPlayer5.inventory.consumeInventoryItem(116);
				world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "ext.infuse", 1.0F, 1.0F);
			} else if(itemStack6.itemID == 266) {
				world1.setBlockWithNotify(i2, i3, i4, 118);
				entityPlayer5.inventory.consumeInventoryItem(266);
				world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "ext.infuse", 1.0F, 0.7F);
			} else if(itemStack6.itemID == 355) {
				world1.setBlockWithNotify(i2, i3, i4, 119);
				entityPlayer5.inventory.consumeInventoryItem(355);
				world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "ext.infuse", 1.0F, 0.3F);
			}

			return true;
		}
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
	}
}
