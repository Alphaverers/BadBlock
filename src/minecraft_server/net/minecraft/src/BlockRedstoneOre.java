package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneOre extends Block {
	private boolean glowing;

	public BlockRedstoneOre(int id, int blockIndex, boolean glowing) {
		super(id, blockIndex, Material.rock);
		if(glowing) {
			this.setTickOnLoad(true);
		}

		this.glowing = glowing;
	}

	public int tickRate() {
		return 30;
	}

	public void onBlockClicked(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		this.glow(world1, i2, i3, i4);
		super.onBlockClicked(world1, i2, i3, i4, entityPlayer5);
	}

	public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
		this.glow(world1, i2, i3, i4);
		super.onEntityWalking(world1, i2, i3, i4, entity5);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		this.glow(world1, i2, i3, i4);
		return super.blockActivated(world1, i2, i3, i4, entityPlayer5);
	}

	private void glow(World world, int x, int y, int z) {
		this.sparkle(world, x, y, z);
		if(this.blockID == Block.oreRedstone.blockID) {
			world.setBlockWithNotify(x, y, z, Block.oreRedstoneGlowing.blockID);
		}

	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(this.blockID == Block.oreRedstoneGlowing.blockID) {
			world1.setBlockWithNotify(i2, i3, i4, Block.oreRedstone.blockID);
		}

	}

	public int idDropped(int i1, Random random2) {
		return Item.redstone.shiftedIndex;
	}

	public int quantityDropped(Random random1) {
		return 4 + random1.nextInt(2);
	}

	private void sparkle(World world, int x, int y, int z) {
		Random random5 = world.rand;
		double d6 = 0.0625D;

		for(int i8 = 0; i8 < 6; ++i8) {
			double d9 = (double)((float)x + random5.nextFloat());
			double d11 = (double)((float)y + random5.nextFloat());
			double d13 = (double)((float)z + random5.nextFloat());
			if(i8 == 0 && !world.isBlockNormalCube(x, y + 1, z)) {
				d11 = (double)(y + 1) + d6;
			}

			if(i8 == 1 && !world.isBlockNormalCube(x, y - 1, z)) {
				d11 = (double)(y + 0) - d6;
			}

			if(i8 == 2 && !world.isBlockNormalCube(x, y, z + 1)) {
				d13 = (double)(z + 1) + d6;
			}

			if(i8 == 3 && !world.isBlockNormalCube(x, y, z - 1)) {
				d13 = (double)(z + 0) - d6;
			}

			if(i8 == 4 && !world.isBlockNormalCube(x + 1, y, z)) {
				d9 = (double)(x + 1) + d6;
			}

			if(i8 == 5 && !world.isBlockNormalCube(x - 1, y, z)) {
				d9 = (double)(x + 0) - d6;
			}

			if(d9 < (double)x || d9 > (double)(x + 1) || d11 < 0.0D || d11 > (double)(y + 1) || d13 < (double)z || d13 > (double)(z + 1)) {
				world.spawnParticle("reddust", d9, d11, d13, 0.0D, 0.0D, 0.0D);
			}
		}

	}
}
