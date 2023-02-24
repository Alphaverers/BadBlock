package net.minecraft.src;

public class SwordFlameberge extends ItemSword {
	int[] v2s = new int[]{3, 0, 3, 1, 2, 2, 1, 3, 0, 3, -1, 3, -2, 2, -3, 1, -3, 0, -3, -1, -2, -2, -1, -3, 0, -3, 1, -3, 2, -2, 3, -1};

	public SwordFlameberge(int i1) {
		super(i1, 2);
	}

	public boolean BlockIDFirable(int i1) {
		return i1 == 0 || i1 == 78;
	}

	public void SetBlockFire(World world1, int i2, int i3, int i4, int i5) {
		if(i5 != 6) {
			int i6 = world1.getBlockId(i2, i3, i4);
			world1.getBlockId(i2, i3 + 1, i4);
			int i8 = world1.getBlockId(i2, i3 - 1, i4);
			if(this.BlockIDFirable(i6)) {
				if(!this.BlockIDFirable(i8)) {
					world1.setBlockWithNotify(i2, i3, i4, Block.fire.blockID);
				} else {
					this.SetBlockFire(world1, i2, i3 - 1, i4, i5 + 1);
				}
			} else {
				this.SetBlockFire(world1, i2, i3 + 1, i4, i5 + 1);
			}

		}
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		int i4 = (int)InputHandler.mc.thePlayer.posX;
		int i5 = (int)InputHandler.mc.thePlayer.posY;
		int i6 = (int)InputHandler.mc.thePlayer.posZ;

		for(int i7 = 0; i7 != this.v2s.length / 2; ++i7) {
			this.SetBlockFire(world2, i4 + this.v2s[i7 * 2], i5, i6 + this.v2s[i7 * 2 + 1], 0);
		}

		return itemStack1;
	}
}
