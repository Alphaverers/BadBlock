package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block {
	public BlockClay(int id, int blockIndex) {
		super(id, blockIndex, Material.clay);
	}

	public int idDropped(int i1, Random random2) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(Random random1) {
		return 4;
	}
}
