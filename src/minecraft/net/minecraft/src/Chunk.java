package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Chunk {
	public static boolean isLit;
	public byte[] blocks;
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray data;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	public int height;
	public final int xPosition;
	public final int zPosition;
	public Map chunkTileEntityMap;
	public List[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean neverSave;
	public boolean isChunkRendered;
	public boolean hasEntities;
	public long lastSaveTime;

	public Chunk(World worldObj, int xPosition, int zPosition) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.isChunkRendered = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.worldObj = worldObj;
		this.xPosition = xPosition;
		this.zPosition = zPosition;
		this.heightMap = new byte[256];

		for(int i4 = 0; i4 < this.entities.length; ++i4) {
			this.entities[i4] = new ArrayList();
		}

	}

	public Chunk(World worldObj, byte[] blocks, int xPosition, int zPosition) {
		this(worldObj, xPosition, zPosition);
		this.blocks = blocks;
		this.data = new NibbleArray(blocks.length);
		this.skylightMap = new NibbleArray(blocks.length);
		this.blocklightMap = new NibbleArray(blocks.length);
	}

	public boolean isAtLocation(int xPosition, int zPosition) {
		return xPosition == this.xPosition && zPosition == this.zPosition;
	}

	public int getHeightValue(int blockX, int blockZ) {
		return this.heightMap[blockZ << 4 | blockX] & 255;
	}

	public void doNothing() {
	}

	public void generateHeightMap() {
		int i1 = 127;

		for(int i2 = 0; i2 < 16; ++i2) {
			for(int i3 = 0; i3 < 16; ++i3) {
				int i4 = 127;

				for(int i5 = i2 << 11 | i3 << 7; i4 > 0 && Block.lightOpacity[this.blocks[i5 + i4 - 1]] == 0; --i4) {
				}

				this.heightMap[i3 << 4 | i2] = (byte)i4;
				if(i4 < i1) {
					i1 = i4;
				}
			}
		}

		this.height = i1;
		this.isModified = true;
	}

	public void generateSkylightMap() {
		int i1 = 127;

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.heightMap[i3 << 4 | i2] = -128;
				this.relightBlock(i2, 127, i3);
				if((this.heightMap[i3 << 4 | i2] & 255) < i1) {
					i1 = this.heightMap[i3 << 4 | i2] & 255;
				}
			}
		}

		this.height = i1;

		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.updateSkylight_do(i2, i3);
			}
		}

		this.isModified = true;
	}

	private void updateSkylight_do(int x, int z) {
		int i3 = this.getHeightValue(x, z);
		int i4 = this.xPosition * 16 + x;
		int i5 = this.zPosition * 16 + z;
		this.checkSkylightNeighborHeight(i4 - 1, i5, i3);
		this.checkSkylightNeighborHeight(i4 + 1, i5, i3);
		this.checkSkylightNeighborHeight(i4, i5 - 1, i3);
		this.checkSkylightNeighborHeight(i4, i5 + 1, i3);
	}

	private void checkSkylightNeighborHeight(int blockX, int blockZ, int blockY) {
		int i4 = this.worldObj.getHeightValue(blockX, blockZ);
		if(i4 > blockY) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, blockX, blockY, blockZ, blockX, i4, blockZ);
		} else if(i4 < blockY) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, blockX, i4, blockZ, blockX, blockY, blockZ);
		}

		this.isModified = true;
	}

	private void relightBlock(int x, int y, int z) {
		int i4 = this.heightMap[z << 4 | x] & 255;
		int i5 = i4;
		if(y > i4) {
			i5 = y;
		}

		for(int i6 = x << 11 | z << 7; i5 > 0 && Block.lightOpacity[this.blocks[i6 + i5 - 1]] == 0; --i5) {
		}

		if(i5 != i4) {
			this.worldObj.markBlocksDirtyVertical(x, z, i5, i4);
			this.heightMap[z << 4 | x] = (byte)i5;
			int i7;
			int i8;
			int i9;
			if(i5 < this.height) {
				this.height = i5;
			} else {
				i7 = 127;

				for(i8 = 0; i8 < 16; ++i8) {
					for(i9 = 0; i9 < 16; ++i9) {
						if((this.heightMap[i9 << 4 | i8] & 255) < i7) {
							i7 = this.heightMap[i9 << 4 | i8] & 255;
						}
					}
				}

				this.height = i7;
			}

			i7 = this.xPosition * 16 + x;
			i8 = this.zPosition * 16 + z;
			if(i5 < i4) {
				for(i9 = i5; i9 < i4; ++i9) {
					this.skylightMap.set(x, i9, z, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i7, i4, i8, i7, i5, i8);

				for(i9 = i4; i9 < i5; ++i9) {
					this.skylightMap.set(x, i9, z, 0);
				}
			}

			i9 = 15;

			int i10;
			for(i10 = i5; i5 > 0 && i9 > 0; this.skylightMap.set(x, i5, z, i9)) {
				--i5;
				int i11 = Block.lightOpacity[this.getBlockID(x, i5, z)];
				if(i11 == 0) {
					i11 = 1;
				}

				i9 -= i11;
				if(i9 < 0) {
					i9 = 0;
				}
			}

			while(i5 > 0 && Block.lightOpacity[this.getBlockID(x, i5 - 1, z)] == 0) {
				--i5;
			}

			if(i5 != i10) {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i7 - 1, i5, i8 - 1, i7 + 1, i10, i8 + 1);
			}

			this.isModified = true;
		}
	}

	public int getBlockID(int x, int y, int z) {
		return this.blocks[x << 11 | z << 7 | y];
	}

	public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int metadata) {
		byte b6 = (byte)id;
		int i7 = this.heightMap[z << 4 | x] & 255;
		int i8 = this.blocks[x << 11 | z << 7 | y] & 255;
		if(i8 == id && this.data.get(x, y, z) == metadata) {
			return false;
		} else {
			int i9 = this.xPosition * 16 + x;
			int i10 = this.zPosition * 16 + z;
			this.blocks[x << 11 | z << 7 | y] = b6;
			if(i8 != 0 && !this.worldObj.multiplayerWorld) {
				Block.blocksList[i8].onBlockRemoval(this.worldObj, i9, y, i10);
			}

			this.data.set(x, y, z, metadata);
			if(Block.lightOpacity[b6] != 0) {
				if(y >= i7) {
					this.relightBlock(x, y + 1, z);
				}
			} else if(y == i7 - 1) {
				this.relightBlock(x, y, z);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i9, y, i10, i9, y, i10);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i9, y, i10, i9, y, i10);
			this.updateSkylight_do(x, z);
			if(id != 0) {
				Block.blocksList[id].onBlockAdded(this.worldObj, i9, y, i10);
			}

			this.isModified = true;
			return true;
		}
	}

	public boolean setBlockID(int x, int y, int z, int id) {
		byte b5 = (byte)id;
		int i6 = this.heightMap[z << 4 | x] & 255;
		int i7 = this.blocks[x << 11 | z << 7 | y] & 255;
		if(i7 == id) {
			return false;
		} else {
			int i8 = this.xPosition * 16 + x;
			int i9 = this.zPosition * 16 + z;
			this.blocks[x << 11 | z << 7 | y] = b5;
			if(i7 != 0) {
				Block.blocksList[i7].onBlockRemoval(this.worldObj, i8, y, i9);
			}

			this.data.set(x, y, z, 0);
			if(Block.lightOpacity[b5] != 0) {
				if(y >= i6) {
					this.relightBlock(x, y + 1, z);
				}
			} else if(y == i6 - 1) {
				this.relightBlock(x, y, z);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i8, y, i9, i8, y, i9);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i8, y, i9, i8, y, i9);
			this.updateSkylight_do(x, z);
			if(id != 0 && !this.worldObj.multiplayerWorld) {
				Block.blocksList[id].onBlockAdded(this.worldObj, i8, y, i9);
			}

			this.isModified = true;
			return true;
		}
	}

	public int getBlockMetadata(int x, int y, int z) {
		return this.data.get(x, y, z);
	}

	public void setBlockMetadata(int x, int y, int z, int metadata) {
		this.isModified = true;
		this.data.set(x, y, z, metadata);
	}

	public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
		return skyBlock == EnumSkyBlock.Sky ? this.skylightMap.get(x, y, z) : (skyBlock == EnumSkyBlock.Block ? this.blocklightMap.get(x, y, z) : 0);
	}

	public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		this.isModified = true;
		if(skyBlock == EnumSkyBlock.Sky) {
			this.skylightMap.set(x, y, z, lightValue);
		} else {
			if(skyBlock != EnumSkyBlock.Block) {
				return;
			}

			this.blocklightMap.set(x, y, z, lightValue);
		}

	}

	public int getBlockLightValue(int x, int y, int z, int skyLightSubtracted) {
		int i5 = this.skylightMap.get(x, y, z);
		if(i5 > 0) {
			isLit = true;
		}

		i5 -= skyLightSubtracted;
		int i6 = this.blocklightMap.get(x, y, z);
		if(i6 > i5) {
			i5 = i6;
		}

		return i5;
	}

	public void addEntity(Entity entity) {
		if(!this.isChunkRendered) {
			this.hasEntities = true;
			int i2 = MathHelper.floor_double(entity.posX / 16.0D);
			int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
			if(i2 != this.xPosition || i3 != this.zPosition) {
				System.out.println("Wrong location! " + entity);
			}

			int i4 = MathHelper.floor_double(entity.posY / 16.0D);
			if(i4 < 0) {
				i4 = 0;
			}

			if(i4 >= this.entities.length) {
				i4 = this.entities.length - 1;
			}

			entity.addedToChunk = true;
			entity.chunkCoordX = this.xPosition;
			entity.chunkCoordY = i4;
			entity.chunkCoordZ = this.zPosition;
			this.entities[i4].add(entity);
		}
	}

	public void removeEntity(Entity entity) {
		this.removeEntityAtIndex(entity, entity.chunkCoordY);
	}

	public void removeEntityAtIndex(Entity entity, int index) {
		if(index < 0) {
			index = 0;
		}

		if(index >= this.entities.length) {
			index = this.entities.length - 1;
		}

		this.entities[index].remove(entity);
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return y >= (this.heightMap[z << 4 | x] & 255);
	}

	public TileEntity getChunkBlockTileEntity(int x, int y, int z) {
		ChunkPosition chunkPosition4 = new ChunkPosition(x, y, z);
		TileEntity tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		if(tileEntity5 == null) {
			int i6 = this.getBlockID(x, y, z);
			if(!Block.isBlockContainer[i6]) {
				return null;
			}

			BlockContainer blockContainer7 = (BlockContainer)Block.blocksList[i6];
			blockContainer7.onBlockAdded(this.worldObj, this.xPosition * 16 + x, y, this.zPosition * 16 + z);
			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		}

		return tileEntity5;
	}

	public void addTileEntity(TileEntity tileEntity) {
		int i2 = tileEntity.xCoord - this.xPosition * 16;
		int i3 = tileEntity.yCoord;
		int i4 = tileEntity.zCoord - this.zPosition * 16;
		this.setChunkBlockTileEntity(i2, i3, i4, tileEntity);
	}

	public void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		ChunkPosition chunkPosition5 = new ChunkPosition(x, y, z);
		tileEntity.worldObj = this.worldObj;
		tileEntity.xCoord = this.xPosition * 16 + x;
		tileEntity.yCoord = y;
		tileEntity.zCoord = this.zPosition * 16 + z;
		if(this.getBlockID(x, y, z) != 0 && Block.blocksList[this.getBlockID(x, y, z)] instanceof BlockContainer) {
			if(this.isChunkLoaded) {
				if(this.chunkTileEntityMap.get(chunkPosition5) != null) {
					this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.get(chunkPosition5));
				}

				this.worldObj.loadedTileEntityList.add(tileEntity);
			}

			this.chunkTileEntityMap.put(chunkPosition5, tileEntity);
		} else {
			System.out.println("Attempted to place a tile entity where there was no entity tile!");
		}
	}

	public void removeChunkBlockTileEntity(int x, int y, int z) {
		ChunkPosition chunkPosition4 = new ChunkPosition(x, y, z);
		if(this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.remove(chunkPosition4));
		}

	}

	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.loadedTileEntityList.addAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entities[i1]);
		}

	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		this.worldObj.loadedTileEntityList.removeAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.unloadEntities(this.entities[i1]);
		}

	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB aabb, List list) {
		int i4 = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List list7 = this.entities[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(entity9 != entity && entity9.boundingBox.intersectsWith(aabb)) {
					list.add(entity9);
				}
			}
		}

	}

	public void getEntitiesOfTypeWithinAAAB(Class entityClass, AxisAlignedBB aabb, List list) {
		int i4 = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List list7 = this.entities[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(entityClass.isAssignableFrom(entity9.getClass()) && entity9.boundingBox.intersectsWith(aabb)) {
					list.add(entity9);
				}
			}
		}

	}

	public boolean needsSaving(boolean z1) {
		return this.neverSave ? false : (this.hasEntities && this.worldObj.worldTime != this.lastSaveTime ? true : this.isModified);
	}

	public int setChunkData(byte[] blocks, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int size) {
		int i9;
		int i10;
		int i11;
		int i12;
		for(i9 = minX; i9 < maxX; ++i9) {
			for(i10 = minZ; i10 < maxZ; ++i10) {
				i11 = i9 << 11 | i10 << 7 | minY;
				i12 = maxY - minY;
				System.arraycopy(blocks, size, this.blocks, i11, i12);
				size += i12;
			}
		}

		this.generateHeightMap();

		for(i9 = minX; i9 < maxX; ++i9) {
			for(i10 = minZ; i10 < maxZ; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | minY) >> 1;
				i12 = (maxY - minY) / 2;
				System.arraycopy(blocks, size, this.data.data, i11, i12);
				size += i12;
			}
		}

		for(i9 = minX; i9 < maxX; ++i9) {
			for(i10 = minZ; i10 < maxZ; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | minY) >> 1;
				i12 = (maxY - minY) / 2;
				System.arraycopy(blocks, size, this.blocklightMap.data, i11, i12);
				size += i12;
			}
		}

		for(i9 = minX; i9 < maxX; ++i9) {
			for(i10 = minZ; i10 < maxZ; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | minY) >> 1;
				i12 = (maxY - minY) / 2;
				System.arraycopy(blocks, size, this.skylightMap.data, i11, i12);
				size += i12;
			}
		}

		return size;
	}

	public Random getRandomWithSeed(long j1) {
		return new Random(this.worldObj.randomSeed + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ j1);
	}
}
