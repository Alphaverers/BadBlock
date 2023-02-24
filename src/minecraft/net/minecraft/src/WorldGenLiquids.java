package net.minecraft.src;

import java.util.Random;

public class WorldGenLiquids extends WorldGenerator {
	private int liquidBlockId;

	public WorldGenLiquids(int i1) {
		this.liquidBlockId = i1;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		if(world1.getBlockId(i3, i4 + 1, i5) != Block.stone.blockID) {
			return false;
		} else if(world1.getBlockId(i3, i4 - 1, i5) != Block.stone.blockID) {
			return false;
		} else if(world1.getBlockId(i3, i4, i5) != 0 && world1.getBlockId(i3, i4, i5) != Block.stone.blockID) {
			return false;
		} else {
			int i6 = 0;
			if(world1.getBlockId(i3 - 1, i4, i5) == Block.stone.blockID) {
				++i6;
			}

			if(world1.getBlockId(i3 + 1, i4, i5) == Block.stone.blockID) {
				++i6;
			}

			if(world1.getBlockId(i3, i4, i5 - 1) == Block.stone.blockID) {
				++i6;
			}

			if(world1.getBlockId(i3, i4, i5 + 1) == Block.stone.blockID) {
				++i6;
			}

			int i7 = 0;
			if(world1.getBlockId(i3 - 1, i4, i5) == 0) {
				++i7;
			}

			if(world1.getBlockId(i3 + 1, i4, i5) == 0) {
				++i7;
			}

			if(world1.getBlockId(i3, i4, i5 - 1) == 0) {
				++i7;
			}

			if(world1.getBlockId(i3, i4, i5 + 1) == 0) {
				++i7;
			}

			if(i6 == 3 && i7 == 1) {
				world1.setBlockWithNotify(i3, i4, i5, this.liquidBlockId);
			}

			return true;
		}
	}
}
