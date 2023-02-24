package net.minecraft.src;

public class ChunkPosition {
	public final int x;
	public final int y;
	public final int z;

	public ChunkPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean equals(Object object) {
		if(!(object instanceof ChunkPosition)) {
			return false;
		} else {
			ChunkPosition chunkPosition2 = (ChunkPosition)object;
			return chunkPosition2.x == this.x && chunkPosition2.y == this.y && chunkPosition2.z == this.z;
		}
	}

	public int hashCode() {
		return this.x * 8976890 + this.y * 981131 + this.z;
	}
}
