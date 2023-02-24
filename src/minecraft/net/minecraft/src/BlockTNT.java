package net.minecraft.src;

import java.util.Random;

public class BlockTNT extends Block {
	public BlockTNT(int id, int tex) {
		super(id, tex, Material.tnt);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 0 ? this.blockIndexInTexture + 2 : (i1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(i5 > 0 && Block.blocksList[i5].canProvidePower() && world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
			this.onBlockDestroyedByPlayer(world1, i2, i3, i4, 0);
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public void onBlockDestroyedByExplosion(World world1, int i2, int i3, int i4) {
		EntityTNTPrimed entityTNTPrimed5 = new EntityTNTPrimed(world1, (float)i2 + 0.5F, (float)i3 + 0.5F, (float)i4 + 0.5F);
		entityTNTPrimed5.fuse = world1.rand.nextInt(entityTNTPrimed5.fuse / 4) + entityTNTPrimed5.fuse / 8;
		world1.spawnEntityInWorld(entityTNTPrimed5);
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		EntityTNTPrimed entityTNTPrimed6 = new EntityTNTPrimed(world1, (float)i2 + 0.5F, (float)i3 + 0.5F, (float)i4 + 0.5F);
		world1.spawnEntityInWorld(entityTNTPrimed6);
		world1.playSoundAtEntity(entityTNTPrimed6, "random.fuse", 1.0F, 1.0F);
	}
}
