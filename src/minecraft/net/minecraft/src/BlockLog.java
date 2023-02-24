package net.minecraft.src;

import java.util.Random;

public class BlockLog extends Block {
	protected BlockLog(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public int quantityDropped(Random random1) {
		return 1;
	}

	public int idDropped(int i1, Random random2) {
		return Block.wood.blockID;
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? 21 : (i1 == 0 ? 21 : 20);
	}
}
