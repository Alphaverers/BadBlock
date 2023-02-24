package net.minecraft.src;

import java.util.Random;

public class BlockGlass extends BlockBreakable {
	public BlockGlass(int i1, int i2, Material material3, boolean z4) {
		super(i1, i2, material3, z4);
	}

	public int quantityDropped(Random random1) {
		return 0;
	}
}
