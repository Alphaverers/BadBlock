package net.minecraft.src;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator {
	private int plantBlockId;

	public WorldGenFlowers(int plantBlockID) {
		this.plantBlockId = plantBlockID;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		for(int i6 = 0; i6 < 64; ++i6) {
			int i7 = i3 + random2.nextInt(8) - random2.nextInt(8);
			int i8 = i4 + random2.nextInt(4) - random2.nextInt(4);
			int i9 = i5 + random2.nextInt(8) - random2.nextInt(8);
			if(world1.getBlockId(i7, i8, i9) == 0 && ((BlockFlower)Block.blockList[this.plantBlockId]).canBlockStay(world1, i7, i8, i9)) {
				world1.setBlock(i7, i8, i9, this.plantBlockId);
			}
		}

		return true;
	}
}
