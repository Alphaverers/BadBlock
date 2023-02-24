package net.minecraft.src;

import java.util.Random;

public class OnWaterGen extends WorldGenerator {
	private int a;

	public OnWaterGen(int i1) {
		this.a = i1;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		for(int i6 = 0; i6 < 64; ++i6) {
			int i7 = i3 + random2.nextInt(8) - random2.nextInt(8);
			int i8 = i4 + random2.nextInt(4) - random2.nextInt(4);
			int i9 = i5 + random2.nextInt(8) - random2.nextInt(8);
			int i10 = world1.getBlockId(i7, i8 - 1, i9);
			boolean z11 = i10 == 9 || i10 == 79;
			if(world1.getBlockId(i7, i8, i9) == 0 && z11) {
				world1.setBlock(i7, i8, i9, this.a);
			}
		}

		return true;
	}
}
