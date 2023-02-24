package net.minecraft.src;

import java.util.Random;

public class BlockMobSpawner extends BlockContainer {
	protected BlockMobSpawner(int id, int blockIndex) {
		super(id, blockIndex, Material.rock);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityMobSpawner();
	}

	public int idDropped(int i1, Random random2) {
		return 0;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public boolean isOpaqueCube() {
		return false;
	}
}
