package net.minecraft.src;

public class ChunkCoordIntPair {
	public int chunkXPos;
	public int chunkZPos;

	public ChunkCoordIntPair(int chunkXPos, int chunkZPos) {
		this.chunkXPos = chunkXPos;
		this.chunkZPos = chunkZPos;
	}

	public int hashCode() {
		return this.chunkXPos << 8 | this.chunkZPos;
	}

	public boolean equals(Object object1) {
		ChunkCoordIntPair chunkCoordIntPair2 = (ChunkCoordIntPair)object1;
		return chunkCoordIntPair2.chunkXPos == this.chunkXPos && chunkCoordIntPair2.chunkZPos == this.chunkZPos;
	}

	public double a(Entity entity) {
		double d2 = (double)(this.chunkXPos * 16 + 8);
		double d4 = (double)(this.chunkZPos * 16 + 8);
		double d6 = d2 - entity.posX;
		double d8 = d4 - entity.posZ;
		return d6 * d6 + d8 * d8;
	}
}
