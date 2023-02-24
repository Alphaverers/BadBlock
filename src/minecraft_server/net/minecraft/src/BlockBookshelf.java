package net.minecraft.src;

import java.util.Random;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 <= 1 ? 4 : this.blockIndexInTexture;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}
}
