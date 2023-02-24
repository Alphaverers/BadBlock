package net.minecraft.src;

import java.util.Random;

public class BlockStone extends Block {
	public BlockStone(int id, int tex) {
		super(id, tex, Material.rock);
	}

	public int idDropped(int i1, Random random2) {
		return Block.cobblestone.blockID;
	}
}
