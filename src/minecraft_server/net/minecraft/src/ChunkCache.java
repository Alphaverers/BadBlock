package net.minecraft.src;

public class ChunkCache implements IBlockAccess {
	private int chunkX;
	private int chunkZ;
	private Chunk[][] chunkArray;
	private World worldObj;

	public ChunkCache(World world, int x, int i3, int z, int i5, int i6, int i7) {
		this.worldObj = world;
		this.chunkX = x >> 4;
		this.chunkZ = z >> 4;
		int i8 = i5 >> 4;
		int i9 = i7 >> 4;
		this.chunkArray = new Chunk[i8 - this.chunkX + 1][i9 - this.chunkZ + 1];

		for(int i10 = this.chunkX; i10 <= i8; ++i10) {
			for(int i11 = this.chunkZ; i11 <= i9; ++i11) {
				this.chunkArray[i10 - this.chunkX][i11 - this.chunkZ] = world.getChunkFromChunkCoords(i10, i11);
			}
		}

	}

	public int getBlockId(int i1, int i2, int i3) {
		if(i2 < 0) {
			return 0;
		} else if(i2 >= 128) {
			return 0;
		} else {
			int i4 = (i1 >> 4) - this.chunkX;
			int i5 = (i3 >> 4) - this.chunkZ;
			return this.chunkArray[i4][i5].getBlockID(i1 & 15, i2, i3 & 15);
		}
	}

	public int getBlockMetadata(int i1, int i2, int i3) {
		if(i2 < 0) {
			return 0;
		} else if(i2 >= 128) {
			return 0;
		} else {
			int i4 = (i1 >> 4) - this.chunkX;
			int i5 = (i3 >> 4) - this.chunkZ;
			return this.chunkArray[i4][i5].getBlockMetadata(i1 & 15, i2, i3 & 15);
		}
	}

	public Material getBlockMaterial(int x, int y, int z) {
		int i4 = this.getBlockId(x, y, z);
		return i4 == 0 ? Material.air : Block.blockList[i4].material;
	}

	public boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4 = Block.blockList[this.getBlockId(i1, i2, i3)];
		return block4 == null ? false : block4.isOpaqueCube();
	}
}
