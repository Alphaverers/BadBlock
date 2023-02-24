package net.minecraft.src;

import java.util.Random;

public class MapGenBase {
	protected int range = 8;
	protected Random rand = new Random();

	public void generate(ChunkProviderGenerate chunkProviderGenerate1, World world2, int i3, int i4, byte[] b5) {
		int i6 = this.range;
		this.rand.setSeed(world2.randomSeed);
		long j7 = this.rand.nextLong() / 2L * 2L + 1L;
		long j9 = this.rand.nextLong() / 2L * 2L + 1L;

		for(int i11 = i3 - i6; i11 <= i3 + i6; ++i11) {
			for(int i12 = i4 - i6; i12 <= i4 + i6; ++i12) {
				this.rand.setSeed((long)i11 * j7 + (long)i12 * j9 ^ world2.randomSeed);
				this.recursiveGenerate(world2, i11, i12, i3, i4, b5);
			}
		}

	}

	protected void recursiveGenerate(World world1, int i2, int i3, int i4, int i5, byte[] b6) {
	}
}
