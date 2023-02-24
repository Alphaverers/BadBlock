package net.minecraft.src;

import java.util.Random;

public class BlockGravel extends BlockSand {
	public BlockGravel(int i1, int i2) {
		super(i1, i2);
	}

	public int idDropped(int i1, Random random2) {
		return random2.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}
