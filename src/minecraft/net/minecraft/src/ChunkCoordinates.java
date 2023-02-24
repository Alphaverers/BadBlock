package net.minecraft.src;

final class ChunkCoordinates {
	public final int posX;
	public final int posZ;

	public ChunkCoordinates(int posX, int posZ) {
		this.posX = posX;
		this.posZ = posZ;
	}

	public boolean equals(Object chunkCoordinates) {
		if(!(chunkCoordinates instanceof ChunkCoordinates)) {
			return false;
		} else {
			ChunkCoordinates chunkCoordinates2 = (ChunkCoordinates)chunkCoordinates;
			return this.posX == chunkCoordinates2.posX && this.posZ == chunkCoordinates2.posZ;
		}
	}

	public int hashCode() {
		return this.posX << 16 ^ this.posZ;
	}
}
