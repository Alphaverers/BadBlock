package net.minecraft.src;

public class ChunkCache implements IBlockAccess {
	private int chunkX;
	private int chunkZ;
	private Chunk[][] chunkArray;
	private World worldObj;

	public ChunkCache(World worldObj, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.worldObj = worldObj;
		this.chunkX = minX >> 4;
		this.chunkZ = minZ >> 4;
		int i8 = maxX >> 4;
		int i9 = maxZ >> 4;
		this.chunkArray = new Chunk[i8 - this.chunkX + 1][i9 - this.chunkZ + 1];

		for(int i10 = this.chunkX; i10 <= i8; ++i10) {
			for(int i11 = this.chunkZ; i11 <= i9; ++i11) {
				this.chunkArray[i10 - this.chunkX][i11 - this.chunkZ] = worldObj.getChunkFromChunkCoords(i10, i11);
			}
		}

	}

	public int getBlockId(int x, int y, int z) {
		if(y < 0) {
			return 0;
		} else if(y >= 128) {
			return 0;
		} else {
			int i4 = (x >> 4) - this.chunkX;
			int i5 = (z >> 4) - this.chunkZ;
			return this.chunkArray[i4][i5].getBlockID(x & 15, y, z & 15);
		}
	}

	public TileEntity getBlockTileEntity(int i1, int i2, int i3) {
		int i4 = (i1 >> 4) - this.chunkX;
		int i5 = (i3 >> 4) - this.chunkZ;
		return this.chunkArray[i4][i5].getChunkBlockTileEntity(i1 & 15, i2, i3 & 15);
	}

	public float getBrightness(int x, int y, int z) {
		return World.lightBrightnessTable[this.getLightValue(x, y, z)];
	}

	public int getLightValue(int x, int y, int z) {
		return this.getLightValueExt(x, y, z, true);
	}

	public int getLightValueExt(int x, int y, int z, boolean z4) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int i5;
			int i6;
			if(z4) {
				i5 = this.getBlockId(x, y, z);
				if(i5 == Block.stairSingle.blockID || i5 == Block.tilledField.blockID) {
					i6 = this.getLightValueExt(x, y + 1, z, false);
					int i7 = this.getLightValueExt(x + 1, y, z, false);
					int i8 = this.getLightValueExt(x - 1, y, z, false);
					int i9 = this.getLightValueExt(x, y, z + 1, false);
					int i10 = this.getLightValueExt(x, y, z - 1, false);
					if(i7 > i6) {
						i6 = i7;
					}

					if(i8 > i6) {
						i6 = i8;
					}

					if(i9 > i6) {
						i6 = i9;
					}

					if(i10 > i6) {
						i6 = i10;
					}

					return i6;
				}
			}

			if(y < 0) {
				return 0;
			} else if(y >= 128) {
				i5 = 15 - this.worldObj.skylightSubtracted;
				if(i5 < 0) {
					i5 = 0;
				}

				return i5;
			} else {
				i5 = (x >> 4) - this.chunkX;
				i6 = (z >> 4) - this.chunkZ;
				return this.chunkArray[i5][i6].getBlockLightValue(x & 15, y, z & 15, this.worldObj.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public int getBlockMetadata(int x, int y, int z) {
		if(y < 0) {
			return 0;
		} else if(y >= 128) {
			return 0;
		} else {
			int i4 = (x >> 4) - this.chunkX;
			int i5 = (z >> 4) - this.chunkZ;
			return this.chunkArray[i4][i5].getBlockMetadata(x & 15, y, z & 15);
		}
	}

	public Material getBlockMaterial(int i1, int i2, int i3) {
		int i4 = this.getBlockId(i1, i2, i3);
		return i4 == 0 ? Material.air : Block.blocksList[i4].material;
	}

	public boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4 = Block.blocksList[this.getBlockId(i1, i2, i3)];
		return block4 == null ? false : block4.isOpaqueCube();
	}
}
