package net.minecraft.src;

public class ItemRedstone extends Item {
	public ItemRedstone(int i1) {
		super(i1);
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(i7 == 0) {
			--i5;
		}

		if(i7 == 1) {
			++i5;
		}

		if(i7 == 2) {
			--i6;
		}

		if(i7 == 3) {
			++i6;
		}

		if(i7 == 4) {
			--i4;
		}

		if(i7 == 5) {
			++i4;
		}

		if(world3.getBlockId(i4, i5, i6) != 0) {
			return false;
		} else {
			if(Block.redstoneWire.canPlaceBlockAt(world3, i4, i5, i6)) {
				--itemStack1.stackSize;
				world3.setBlockWithNotify(i4, i5, i6, Block.redstoneWire.blockID);
			}

			return true;
		}
	}
}
