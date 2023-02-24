package net.minecraft.src;

public class ItemHoe extends Item {
	public ItemHoe(int id, int strength) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		int i8 = world3.getBlockId(i4, i5, i6);
		Material material9 = world3.getBlockMaterial(i4, i5 + 1, i6);
		if((material9.isSolid() || i8 != Block.grass.blockID) && i8 != Block.dirt.blockID) {
			return false;
		} else {
			Block block10 = Block.tilledField;
			world3.playSoundEffect((double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F), block10.stepSound.getStepSound(), (block10.stepSound.getVolume() + 1.0F) / 2.0F, block10.stepSound.getPitch() * 0.8F);
			world3.setBlockWithNotify(i4, i5, i6, block10.blockID);
			itemStack1.damageItem(1);
			if(world3.rand.nextInt(8) == 0 && i8 == Block.grass.blockID) {
				byte b11 = 1;

				for(int i12 = 0; i12 < b11; ++i12) {
					float f13 = 0.7F;
					float f14 = world3.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
					float f15 = 1.2F;
					float f16 = world3.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
					EntityItem entityItem17 = new EntityItem(world3, (double)((float)i4 + f14), (double)((float)i5 + f15), (double)((float)i6 + f16), new ItemStack(Item.seeds));
					entityItem17.delayBeforeCanPickup = 10;
					world3.spawnEntityInWorld(entityItem17);
				}
			}

			return true;
		}
	}

	public boolean isFull3D() {
		return true;
	}
}
