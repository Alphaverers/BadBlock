package net.minecraft.src;

public class ItemSign extends Item {
	public ItemSign(int i1) {
		super(i1);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(i7 == 0) {
			return false;
		} else if(!world3.getBlockMaterial(i4, i5, i6).isSolid()) {
			return false;
		} else {
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

			if(!Block.signStanding.canPlaceBlockAt(world3, i4, i5, i6)) {
				return false;
			} else {
				if(i7 == 1) {
					world3.setBlockAndMetadataWithNotify(i4, i5, i6, Block.signStanding.blockID, MathHelper.floor_double((double)((entityPlayer2.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
				} else {
					world3.setBlockAndMetadataWithNotify(i4, i5, i6, Block.signWall.blockID, i7);
				}

				--itemStack1.stackSize;
				entityPlayer2.displayGUIEditSign((TileEntitySign)world3.getBlockTileEntity(i4, i5, i6));
				return true;
			}
		}
	}
}
