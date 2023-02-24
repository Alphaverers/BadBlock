package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class World implements IBlockAccess {
	private List lightingToUpdate;
	public List loadedEntityList;
	private List unloadedEntityList;
	private TreeSet scheduledTickTreeSet;
	private Set scheduledTickSet;
	public List loadedTileEntityList;
	public long worldTime;
	public boolean snowCovered;
	private long skyColor;
	private long fogColor;
	private long cloudColor;
	public int skylightSubtracted;
	protected int updateLCG;
	protected int DIST_HASH_MAGIC;
	public boolean editingBlocks;
	public static float[] lightBrightnessTable = new float[16];
	private final long lockTimestamp;
	protected int autosavePeriod;
	public List playerEntities;
	public int difficultySetting;
	public Random rand;
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public boolean isNewWorld;
	protected List worldAccesses;
	private IChunkProvider chunkProvider;
	public File saveDirectory;
	public long randomSeed;
	private NBTTagCompound nbtCompoundPlayer;
	public long sizeOnDisk;
	public final String levelName;
	public boolean worldChunkLoadOverride;
	private ArrayList collidingBoundingBoxes;
	private Set positionsToUpdate;
	private int soundCounter;
	private List entitiesWithinAABBExcludingEntity;
	public boolean multiplayerWorld;

	public World(File worldFile, String levelName) {
		this(worldFile, levelName, (new Random()).nextLong());
	}

	public World(String levelName) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
		this.snowCovered = false;
		this.skyColor = 8961023L;
		this.fogColor = 12638463L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.playerEntities = new ArrayList();
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.randomSeed = 0L;
		this.sizeOnDisk = 0L;
		this.collidingBoundingBoxes = new ArrayList();
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.levelName = levelName;
		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		this.calculateInitialSkylight();
	}

	public World(File baseDir, String levelName, long randomSeed) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
		this.snowCovered = false;
		this.skyColor = 8961023L;
		this.fogColor = 12638463L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.playerEntities = new ArrayList();
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.randomSeed = 0L;
		this.sizeOnDisk = 0L;
		this.collidingBoundingBoxes = new ArrayList();
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.levelName = levelName;
		baseDir.mkdirs();
		this.saveDirectory = new File(baseDir, levelName);
		this.saveDirectory.mkdirs();

		File file5;
		try {
			file5 = new File(this.saveDirectory, "session.lock");
			DataOutputStream dataOutputStream6 = new DataOutputStream(new FileOutputStream(file5));

			try {
				dataOutputStream6.writeLong(this.lockTimestamp);
			} finally {
				dataOutputStream6.close();
			}
		} catch (IOException iOException13) {
			throw new RuntimeException("Failed to check session lock, aborting");
		}

		file5 = new File(this.saveDirectory, "level.dat");
		this.isNewWorld = !file5.exists();
		if(file5.exists()) {
			try {
				NBTTagCompound nBTTagCompound14 = CompressedStreamTools.readCompressed(new FileInputStream(file5));
				NBTTagCompound nBTTagCompound7 = nBTTagCompound14.getCompoundTag("Data");
				this.randomSeed = nBTTagCompound7.getLong("RandomSeed");
				this.spawnX = nBTTagCompound7.getInteger("SpawnX");
				this.spawnY = nBTTagCompound7.getInteger("SpawnY");
				this.spawnZ = nBTTagCompound7.getInteger("SpawnZ");
				this.worldTime = nBTTagCompound7.getLong("Time");
				this.sizeOnDisk = nBTTagCompound7.getLong("SizeOnDisk");
				this.snowCovered = nBTTagCompound7.getBoolean("SnowCovered");
				if(nBTTagCompound7.hasKey("Player")) {
					this.nbtCompoundPlayer = nBTTagCompound7.getCompoundTag("Player");
				}
			} catch (Exception exception11) {
				exception11.printStackTrace();
			}
		} else {
			this.snowCovered = this.rand.nextInt(4) == 0;
		}

		boolean z15 = false;
		if(this.randomSeed == 0L) {
			this.randomSeed = randomSeed;
			z15 = true;
		}

		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		if(z15) {
			this.worldChunkLoadOverride = true;
			this.spawnX = 0;
			this.spawnY = 64;

			for(this.spawnZ = 0; !this.getInitialSpawnLocation(this.spawnX, this.spawnZ); this.spawnZ += this.rand.nextInt(64) - this.rand.nextInt(64)) {
				this.spawnX += this.rand.nextInt(64) - this.rand.nextInt(64);
			}

			this.worldChunkLoadOverride = false;
		}

		this.calculateInitialSkylight();
	}

	protected IChunkProvider getChunkProvider(File saveDir) {
		return new ChunkProviderLoadOrGenerate(this, new ChunkLoader(saveDir, true), new ChunkProviderGenerate(this, this.randomSeed));
	}

	private boolean getInitialSpawnLocation(int x, int z) {
		int i3 = this.getFirstUncoveredBlock(x, z);
		return i3 == Block.sand.blockID;
	}

	private int getFirstUncoveredBlock(int x, int z) {
		int i3;
		for(i3 = 63; this.getBlockId(x, i3 + 1, z) != 0; ++i3) {
		}

		return this.getBlockId(x, i3, z);
	}

	public void saveWorld(boolean flag, IProgressUpdate progressUpdate) {
		if(this.chunkProvider.canSave()) {
			if(progressUpdate != null) {
				progressUpdate.displayProgressMessage("Saving level");
			}

			this.saveLevel();
			if(progressUpdate != null) {
				progressUpdate.resetProgressAndWorkingMessage("Saving chunks");
			}

			this.chunkProvider.saveChunks(flag, progressUpdate);
		}
	}

	private void saveLevel() {
		this.checkSessionLock();
		NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
		nBTTagCompound1.setLong("RandomSeed", this.randomSeed);
		nBTTagCompound1.setInteger("SpawnX", this.spawnX);
		nBTTagCompound1.setInteger("SpawnY", this.spawnY);
		nBTTagCompound1.setInteger("SpawnZ", this.spawnZ);
		nBTTagCompound1.setLong("Time", this.worldTime);
		nBTTagCompound1.setLong("SizeOnDisk", this.sizeOnDisk);
		nBTTagCompound1.setBoolean("SnowCovered", this.snowCovered);
		nBTTagCompound1.setLong("LastPlayed", System.currentTimeMillis());
		EntityPlayer entityPlayer2 = null;
		if(this.playerEntities.size() > 0) {
			entityPlayer2 = (EntityPlayer)this.playerEntities.get(0);
		}

		NBTTagCompound nBTTagCompound3;
		if(entityPlayer2 != null) {
			nBTTagCompound3 = new NBTTagCompound();
			entityPlayer2.writeToNBT(nBTTagCompound3);
			nBTTagCompound1.setCompoundTag("Player", nBTTagCompound3);
		}

		nBTTagCompound3 = new NBTTagCompound();
		nBTTagCompound3.setTag("Data", nBTTagCompound1);

		try {
			File file4 = new File(this.saveDirectory, "level.dat_new");
			File file5 = new File(this.saveDirectory, "level.dat_old");
			File file6 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(nBTTagCompound3, new FileOutputStream(file4));
			if(file5.exists()) {
				file5.delete();
			}

			file6.renameTo(file5);
			if(file6.exists()) {
				file6.delete();
			}

			file4.renameTo(file6);
			if(file4.exists()) {
				file4.delete();
			}
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	public int getBlockId(int i1, int i2, int i3) {
		return i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000 ? (i2 < 0 ? 0 : (i2 >= 128 ? 0 : this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).getBlockID(i1 & 15, i2, i3 & 15))) : 0;
	}

	public boolean blockExists(int x, int y, int z) {
		return y >= 0 && y < 128 ? this.chunkExists(x >> 4, z >> 4) : false;
	}

	public boolean checkChunksExist(int i1, int i2, int i3, int i4, int i5, int i6) {
		if(i5 >= 0 && i2 < 128) {
			i1 >>= 4;
			i2 >>= 4;
			i3 >>= 4;
			i4 >>= 4;
			i5 >>= 4;
			i6 >>= 4;

			for(int i7 = i1; i7 <= i4; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					if(!this.chunkExists(i7, i8)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean chunkExists(int x, int z) {
		return this.chunkProvider.chunkExists(x, z);
	}

	public Chunk getChunkFromBlockCoords(int x, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4);
	}

	public Chunk getChunkFromChunkCoords(int x, int z) {
		return this.chunkProvider.provideChunk(x, z);
	}

	public boolean setBlockAndMetadata(int x, int y, int z, int i4, int i5) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk chunk6 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				return chunk6.setBlockIDWithMetadata(x & 15, y, z & 15, i4, i5);
			}
		} else {
			return false;
		}
	}

	public boolean setBlock(int x, int y, int z, int i4) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				return chunk5.setBlockID(x & 15, y, z & 15, i4);
			}
		} else {
			return false;
		}
	}

	public Material getBlockMaterial(int x, int y, int z) {
		int i4 = this.getBlockId(x, y, z);
		return i4 == 0 ? Material.air : Block.blockList[i4].material;
	}

	public int getBlockMetadata(int i1, int i2, int i3) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return 0;
			} else if(i2 >= 128) {
				return 0;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk4.getBlockMetadata(i1, i2, i3);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int i1, int i2, int i3, int i4) {
		this.setBlockMetadata(i1, i2, i3, i4);
	}

	public boolean setBlockMetadata(int x, int y, int z, int i4) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				chunk5.setBlockMetadata(x, y, z, i4);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int x, int y, int z, int i4) {
		if(this.setBlock(x, y, z, i4)) {
			this.notifyBlockChange(x, y, z, i4);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int i4, int i5) {
		if(this.setBlockAndMetadata(x, y, z, i4, i5)) {
			this.notifyBlockChange(x, y, z, i4);
			return true;
		} else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int x, int y, int z) {
		for(int i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
			((IWorldAccess)this.worldAccesses.get(i4)).markBlockAndNeighborsNeedsUpdate(x, y, z);
		}

	}

	protected void notifyBlockChange(int x, int y, int z, int i4) {
		this.markBlockNeedsUpdate(x, y, z);
		this.notifyBlocksOfNeighborChange(x, y, z, i4);
	}

	public void markBlockAsNeedsUpdate(int x, int z, int y, int i4) {
		if(y > i4) {
			int i5 = i4;
			i4 = y;
			y = i5;
		}

		this.markBlocksDirty(x, y, z, x, i4, z);
	}

	public void markBlocksDirty(int i1, int i2, int i3, int i4, int i5, int i6) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).markBlockRangeNeedsUpdate(i1, i2, i3, i4, i5, i6);
		}

	}

	public void notifyBlocksOfNeighborChange(int x, int y, int z, int i4) {
		this.notifyBlockOfNeighborChange(x - 1, y, z, i4);
		this.notifyBlockOfNeighborChange(x + 1, y, z, i4);
		this.notifyBlockOfNeighborChange(x, y - 1, z, i4);
		this.notifyBlockOfNeighborChange(x, y + 1, z, i4);
		this.notifyBlockOfNeighborChange(x, y, z - 1, i4);
		this.notifyBlockOfNeighborChange(x, y, z + 1, i4);
	}

	private void notifyBlockOfNeighborChange(int x, int y, int z, int i4) {
		if(!this.editingBlocks && !this.multiplayerWorld) {
			Block block5 = Block.blockList[this.getBlockId(x, y, z)];
			if(block5 != null) {
				block5.onNeighborBlockChange(this, x, y, z, i4);
			}

		}
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
	}

	public int getBlockLightValue(int x, int y, int z) {
		return this.getBlockLightValue_do(x, y, z, true);
	}

	public int getBlockLightValue_do(int x, int y, int z, boolean z4) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int i5;
			if(z4) {
				i5 = this.getBlockId(x, y, z);
				if(i5 == Block.stairSingle.blockID || i5 == Block.tilledField.blockID) {
					int i6 = this.getBlockLightValue_do(x, y + 1, z, false);
					int i7 = this.getBlockLightValue_do(x + 1, y, z, false);
					int i8 = this.getBlockLightValue_do(x - 1, y, z, false);
					int i9 = this.getBlockLightValue_do(x, y, z + 1, false);
					int i10 = this.getBlockLightValue_do(x, y, z - 1, false);
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
				i5 = 15 - this.skylightSubtracted;
				if(i5 < 0) {
					i5 = 0;
				}

				return i5;
			} else {
				Chunk chunk11 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return chunk11.getBlockLightValue(x, y, z, this.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public boolean canExistingBlockSeeTheSky(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return true;
			} else if(!this.chunkExists(x >> 4, z >> 4)) {
				return false;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return chunk4.canBlockSeeTheSky(x, y, z);
			}
		} else {
			return false;
		}
	}

	public int getHeightValue(int blockX, int blockZ) {
		if(blockX >= -32000000 && blockZ >= -32000000 && blockX < 32000000 && blockZ <= 32000000) {
			if(!this.chunkExists(blockX >> 4, blockZ >> 4)) {
				return 0;
			} else {
				Chunk chunk3 = this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
				return chunk3.getHeightValue(blockX & 15, blockZ & 15);
			}
		} else {
			return 0;
		}
	}

	public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int i5) {
		if(this.blockExists(x, y, z)) {
			if(skyBlock == EnumSkyBlock.Sky) {
				if(this.canExistingBlockSeeTheSky(x, y, z)) {
					i5 = 15;
				}
			} else if(skyBlock == EnumSkyBlock.Block) {
				int i6 = this.getBlockId(x, y, z);
				if(Block.lightValue[i6] > i5) {
					i5 = Block.lightValue[i6];
				}
			}

			if(this.getSavedLightValue(skyBlock, x, y, z) != i5) {
				this.scheduleLightingUpdate(skyBlock, x, y, z, x, y, z);
			}

		}
	}

	public int getSavedLightValue(EnumSkyBlock skyBlock, int blockX, int blockY, int blockZ) {
		if(blockY >= 0 && blockY < 128 && blockX >= -32000000 && blockZ >= -32000000 && blockX < 32000000 && blockZ <= 32000000) {
			int i5 = blockX >> 4;
			int i6 = blockZ >> 4;
			if(!this.chunkExists(i5, i6)) {
				return 0;
			} else {
				Chunk chunk7 = this.getChunkFromChunkCoords(i5, i6);
				return chunk7.getSavedLightValue(skyBlock, blockX & 15, blockY, blockZ & 15);
			}
		} else {
			return skyBlock.defaultLightValue;
		}
	}

	public void setLightValue(EnumSkyBlock skyBlock, int blockX, int blockY, int blockZ, int i5) {
		if(blockX >= -32000000 && blockZ >= -32000000 && blockX < 32000000 && blockZ <= 32000000) {
			if(blockY >= 0) {
				if(blockY < 128) {
					if(this.chunkExists(blockX >> 4, blockZ >> 4)) {
						Chunk chunk6 = this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
						chunk6.setLightValue(skyBlock, blockX & 15, blockY, blockZ & 15, i5);

						for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
							((IWorldAccess)this.worldAccesses.get(i7)).markBlockAndNeighborsNeedsUpdate(blockX, blockY, blockZ);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int x, int y, int z) {
		return lightBrightnessTable[this.getBlockLightValue(x, y, z)];
	}

	public boolean isDayTime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D vector1, Vec3D vector2) {
		return this.rayTraceBlocks_do(vector1, vector2, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D vector1, Vec3D vector2, boolean z3) {
		if(!Double.isNaN(vector1.xCoord) && !Double.isNaN(vector1.yCoord) && !Double.isNaN(vector1.zCoord)) {
			if(!Double.isNaN(vector2.xCoord) && !Double.isNaN(vector2.yCoord) && !Double.isNaN(vector2.zCoord)) {
				int i4 = MathHelper.floor_double(vector2.xCoord);
				int i5 = MathHelper.floor_double(vector2.yCoord);
				int i6 = MathHelper.floor_double(vector2.zCoord);
				int i7 = MathHelper.floor_double(vector1.xCoord);
				int i8 = MathHelper.floor_double(vector1.yCoord);
				int i9 = MathHelper.floor_double(vector1.zCoord);
				int i10 = 20;

				while(i10-- >= 0) {
					if(Double.isNaN(vector1.xCoord) || Double.isNaN(vector1.yCoord) || Double.isNaN(vector1.zCoord)) {
						return null;
					}

					if(i7 == i4 && i8 == i5 && i9 == i6) {
						return null;
					}

					double d11 = 999.0D;
					double d13 = 999.0D;
					double d15 = 999.0D;
					if(i4 > i7) {
						d11 = (double)i7 + 1.0D;
					}

					if(i4 < i7) {
						d11 = (double)i7 + 0.0D;
					}

					if(i5 > i8) {
						d13 = (double)i8 + 1.0D;
					}

					if(i5 < i8) {
						d13 = (double)i8 + 0.0D;
					}

					if(i6 > i9) {
						d15 = (double)i9 + 1.0D;
					}

					if(i6 < i9) {
						d15 = (double)i9 + 0.0D;
					}

					double d17 = 999.0D;
					double d19 = 999.0D;
					double d21 = 999.0D;
					double d23 = vector2.xCoord - vector1.xCoord;
					double d25 = vector2.yCoord - vector1.yCoord;
					double d27 = vector2.zCoord - vector1.zCoord;
					if(d11 != 999.0D) {
						d17 = (d11 - vector1.xCoord) / d23;
					}

					if(d13 != 999.0D) {
						d19 = (d13 - vector1.yCoord) / d25;
					}

					if(d15 != 999.0D) {
						d21 = (d15 - vector1.zCoord) / d27;
					}

					boolean z29 = false;
					byte b35;
					if(d17 < d19 && d17 < d21) {
						if(i4 > i7) {
							b35 = 4;
						} else {
							b35 = 5;
						}

						vector1.xCoord = d11;
						vector1.yCoord += d25 * d17;
						vector1.zCoord += d27 * d17;
					} else if(d19 < d21) {
						if(i5 > i8) {
							b35 = 0;
						} else {
							b35 = 1;
						}

						vector1.xCoord += d23 * d19;
						vector1.yCoord = d13;
						vector1.zCoord += d27 * d19;
					} else {
						if(i6 > i9) {
							b35 = 2;
						} else {
							b35 = 3;
						}

						vector1.xCoord += d23 * d21;
						vector1.yCoord += d25 * d21;
						vector1.zCoord = d15;
					}

					Vec3D vec3D30 = Vec3D.createVector(vector1.xCoord, vector1.yCoord, vector1.zCoord);
					i7 = (int)(vec3D30.xCoord = (double)MathHelper.floor_double(vector1.xCoord));
					if(b35 == 5) {
						--i7;
						++vec3D30.xCoord;
					}

					i8 = (int)(vec3D30.yCoord = (double)MathHelper.floor_double(vector1.yCoord));
					if(b35 == 1) {
						--i8;
						++vec3D30.yCoord;
					}

					i9 = (int)(vec3D30.zCoord = (double)MathHelper.floor_double(vector1.zCoord));
					if(b35 == 3) {
						--i9;
						++vec3D30.zCoord;
					}

					int i31 = this.getBlockId(i7, i8, i9);
					int i32 = this.getBlockMetadata(i7, i8, i9);
					Block block33 = Block.blockList[i31];
					if(i31 > 0 && block33.canCollideCheck(i32, z3)) {
						MovingObjectPosition movingObjectPosition34 = block33.collisionRayTrace(this, i7, i8, i9, vector1, vector2);
						if(movingObjectPosition34 != null) {
							return movingObjectPosition34;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity entity, String soundName, float f3, float f4) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playSound(soundName, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, f3, f4);
		}

	}

	public void playSoundEffect(double x, double y, double z, String soundName, float f8, float f9) {
		for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
			((IWorldAccess)this.worldAccesses.get(i10)).playSound(soundName, x, y, z, f8, f9);
		}

	}

	public void playRecord(String recordName, int x, int y, int z) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playRecord(recordName, x, y, z);
		}

	}

	public void spawnParticle(String particleName, double x, double y, double z, double d8, double d10, double d12) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(particleName, x, y, z, d8, d10, d12);
		}

	}

	public boolean spawnEntityInWorld(Entity entity) {
		int i2 = MathHelper.floor_double(entity.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
		boolean z4 = false;
		if(entity instanceof EntityPlayer) {
			z4 = true;
		}

		if(!z4 && !this.chunkExists(i2, i3)) {
			return false;
		} else {
			if(entity instanceof EntityPlayer) {
				this.playerEntities.add((EntityPlayer)entity);
				System.out.println("Player count: " + this.playerEntities.size());
			}

			this.getChunkFromChunkCoords(i2, i3).removeEntityAtIndex(entity);
			this.loadedEntityList.add(entity);
			this.obtainEntitySkin(entity);
			return true;
		}
	}

	protected void obtainEntitySkin(Entity entity) {
		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).obtainEntitySkin(entity);
		}

	}

	protected void releaseEntitySkin(Entity entity) {
		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).releaseEntitySkin(entity);
		}

	}

	public void setEntityDead(Entity entity) {
		entity.setEntityDead();
		if(entity instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer)entity);
			System.out.println("Player count: " + this.playerEntities.size());
		}

	}

	public void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	public List getCollidingBoundingBoxes(Entity entity, AxisAlignedBB aabb) {
		this.collidingBoundingBoxes.clear();
		int i3 = MathHelper.floor_double(aabb.minX);
		int i4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i5 = MathHelper.floor_double(aabb.minY);
		int i6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i7 = MathHelper.floor_double(aabb.minZ);
		int i8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i7; i10 < i8; ++i10) {
				if(this.blockExists(i9, 64, i10)) {
					for(int i11 = i5 - 1; i11 < i6; ++i11) {
						Block block12 = Block.blockList[this.getBlockId(i9, i11, i10)];
						if(block12 != null) {
							block12.getCollidingBoundingBoxes(this, i9, i11, i10, aabb, this.collidingBoundingBoxes);
						}
					}
				}
			}
		}

		double d14 = 0.25D;
		List list15 = this.getEntitiesWithinAABBExcludingEntity(entity, aabb.expand(d14, d14, d14));

		for(int i16 = 0; i16 < list15.size(); ++i16) {
			AxisAlignedBB axisAlignedBB13 = ((Entity)list15.get(i16)).getBoundingBox();
			if(axisAlignedBB13 != null && axisAlignedBB13.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(axisAlignedBB13);
			}

			axisAlignedBB13 = entity.getCollisionBox((Entity)list15.get(i16));
			if(axisAlignedBB13 != null && axisAlignedBB13.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(axisAlignedBB13);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = 1.0F - (MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		return (int)(f3 * 11.0F);
	}

	public float getCelestialAngle(float f1) {
		int i2 = (int)(this.worldTime % 24000L);
		float f3 = ((float)i2 + f1) / 24000.0F - 0.25F;
		if(f3 < 0.0F) {
			++f3;
		}

		if(f3 > 1.0F) {
			--f3;
		}

		float f4 = f3;
		f3 = 1.0F - (float)((Math.cos((double)f3 * Math.PI) + 1.0D) / 2.0D);
		f3 = f4 + (f3 - f4) / 3.0F;
		return f3;
	}

	public int getTopSolidOrLiquidBlock(int x, int z) {
		Chunk chunk3 = this.getChunkFromBlockCoords(x, z);
		int i4 = 127;
		x &= 15;

		for(z &= 15; i4 > 0; --i4) {
			int i5 = chunk3.getBlockID(x, i4, z);
			if(i5 != 0 && (Block.blockList[i5].material.getIsSolid() || Block.blockList[i5].material.getIsLiquid())) {
				return i4 + 1;
			}
		}

		return -1;
	}

	public void scheduleBlockUpdate(int i1, int i2, int i3, int i4) {
		NextTickListEntry nextTickListEntry5 = new NextTickListEntry(i1, i2, i3, i4);
		byte b6 = 8;
		if(this.checkChunksExist(i1 - b6, i2 - b6, i3 - b6, i1 + b6, i2 + b6, i3 + b6)) {
			if(i4 > 0) {
				nextTickListEntry5.setScheduledTime((long)Block.blockList[i4].tickRate() + this.worldTime);
			}

			if(!this.scheduledTickSet.contains(nextTickListEntry5)) {
				this.scheduledTickSet.add(nextTickListEntry5);
				this.scheduledTickTreeSet.add(nextTickListEntry5);
			}
		}

	}

	public void updateEntities() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int i1;
		Entity entity2;
		int i3;
		int i4;
		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.unloadedEntityList.get(i1);
			i3 = entity2.chunkCoordX;
			i4 = entity2.chunkCoordZ;
			if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
				this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
			}
		}

		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(i1));
		}

		this.unloadedEntityList.clear();

		for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.loadedEntityList.get(i1);
			if(entity2.ridingEntity != null) {
				if(!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
					continue;
				}

				entity2.ridingEntity.riddenByEntity = null;
				entity2.ridingEntity = null;
			}

			if(!entity2.isDead) {
				this.updateEntity(entity2);
			}

			if(entity2.isDead) {
				i3 = entity2.chunkCoordX;
				i4 = entity2.chunkCoordZ;
				if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
					this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
				}

				this.loadedEntityList.remove(i1--);
				this.releaseEntitySkin(entity2);
			}
		}

		for(i1 = 0; i1 < this.loadedTileEntityList.size(); ++i1) {
			TileEntity tileEntity5 = (TileEntity)this.loadedTileEntityList.get(i1);
			tileEntity5.updateEntity();
		}

	}

	protected void updateEntity(Entity entity) {
		int i2 = MathHelper.floor_double(entity.posX);
		int i3 = MathHelper.floor_double(entity.posZ);
		byte b4 = 16;
		if(this.checkChunksExist(i2 - b4, 0, i3 - b4, i2 + b4, 128, i3 + b4)) {
			entity.lastTickPosX = entity.posX;
			entity.lastTickPosY = entity.posY;
			entity.lastTickPosZ = entity.posZ;
			entity.prevRotationYaw = entity.rotationYaw;
			entity.prevRotationPitch = entity.rotationPitch;
			if(entity.ridingEntity != null) {
				entity.updateRidden();
			} else {
				entity.onUpdate();
			}

			int i5 = MathHelper.floor_double(entity.posX / 16.0D);
			int i6 = MathHelper.floor_double(entity.posY / 16.0D);
			int i7 = MathHelper.floor_double(entity.posZ / 16.0D);
			if(!entity.addedToChunk || entity.chunkCoordX != i5 || entity.chunkCoordY != i6 || entity.chunkCoordZ != i7) {
				if(entity.addedToChunk && this.chunkExists(entity.chunkCoordX, entity.chunkCoordZ)) {
					this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
				}

				if(this.chunkExists(i5, i7)) {
					this.getChunkFromChunkCoords(i5, i7).removeEntityAtIndex(entity);
				} else {
					entity.addedToChunk = false;
					System.out.println("Removing entity because it\'s not in a chunk!!");
					entity.setEntityDead();
				}
			}

			if(entity.riddenByEntity != null) {
				if(!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
					this.updateEntity(entity.riddenByEntity);
				} else {
					entity.riddenByEntity.ridingEntity = null;
					entity.riddenByEntity = null;
				}
			}

			if(Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
				entity.posX = entity.lastTickPosX;
			}

			if(Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
				entity.posY = entity.lastTickPosY;
			}

			if(Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
				entity.posZ = entity.lastTickPosZ;
			}

			if(Double.isNaN((double)entity.rotationPitch) || Double.isInfinite((double)entity.rotationPitch)) {
				entity.rotationPitch = entity.prevRotationPitch;
			}

			if(Double.isNaN((double)entity.rotationYaw) || Double.isInfinite((double)entity.rotationYaw)) {
				entity.rotationYaw = entity.prevRotationYaw;
			}

		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB aabb) {
		List list2 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, aabb);

		for(int i3 = 0; i3 < list2.size(); ++i3) {
			Entity entity4 = (Entity)list2.get(i3);
			if(!entity4.isDead && entity4.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public boolean getIsAnyLiquid(AxisAlignedBB aabb) {
		int i2 = MathHelper.floor_double(aabb.minX);
		int i3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i4 = MathHelper.floor_double(aabb.minY);
		int i5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i6 = MathHelper.floor_double(aabb.minZ);
		int i7 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		if(aabb.minX < 0.0D) {
			--i2;
		}

		if(aabb.minY < 0.0D) {
			--i4;
		}

		if(aabb.minZ < 0.0D) {
			--i6;
		}

		for(int i8 = i2; i8 < i3; ++i8) {
			for(int i9 = i4; i9 < i5; ++i9) {
				for(int i10 = i6; i10 < i7; ++i10) {
					Block block11 = Block.blockList[this.getBlockId(i8, i9, i10)];
					if(block11 != null && block11.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB aabb) {
		int i2 = MathHelper.floor_double(aabb.minX);
		int i3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i4 = MathHelper.floor_double(aabb.minY);
		int i5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i6 = MathHelper.floor_double(aabb.minZ);
		int i7 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int i8 = i2; i8 < i3; ++i8) {
			for(int i9 = i4; i9 < i5; ++i9) {
				for(int i10 = i6; i10 < i7; ++i10) {
					int i11 = this.getBlockId(i8, i9, i10);
					if(i11 == Block.fire.blockID || i11 == Block.lavaMoving.blockID || i11 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean handleMaterialAcceleration(AxisAlignedBB aabb, Material material, Entity entity) {
		int i4 = MathHelper.floor_double(aabb.minX);
		int i5 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i6 = MathHelper.floor_double(aabb.minY);
		int i7 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i8 = MathHelper.floor_double(aabb.minZ);
		int i9 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		boolean z10 = false;
		Vec3D vec3D11 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

		for(int i12 = i4; i12 < i5; ++i12) {
			for(int i13 = i6; i13 < i7; ++i13) {
				for(int i14 = i8; i14 < i9; ++i14) {
					Block block15 = Block.blockList[this.getBlockId(i12, i13, i14)];
					if(block15 != null && block15.material == material) {
						double d16 = (double)((float)(i13 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(i12, i13, i14)));
						if((double)i7 >= d16) {
							z10 = true;
							block15.velocityToAddToEntity(this, i12, i13, i14, entity, vec3D11);
						}
					}
				}
			}
		}

		if(vec3D11.lengthVector() > 0.0D) {
			vec3D11 = vec3D11.normalize();
			double d18 = 0.004D;
			entity.motionX += vec3D11.xCoord * d18;
			entity.motionY += vec3D11.yCoord * d18;
			entity.motionZ += vec3D11.zCoord * d18;
		}

		return z10;
	}

	public boolean isMaterialInBB(AxisAlignedBB aabb, Material material) {
		int i3 = MathHelper.floor_double(aabb.minX);
		int i4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i5 = MathHelper.floor_double(aabb.minY);
		int i6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i7 = MathHelper.floor_double(aabb.minZ);
		int i8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i5; i10 < i6; ++i10) {
				for(int i11 = i7; i11 < i8; ++i11) {
					Block block12 = Block.blockList[this.getBlockId(i9, i10, i11)];
					if(block12 != null && block12.material == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB aabb, Material material) {
		int i3 = MathHelper.floor_double(aabb.minX);
		int i4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i5 = MathHelper.floor_double(aabb.minY);
		int i6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i7 = MathHelper.floor_double(aabb.minZ);
		int i8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i5; i10 < i6; ++i10) {
				for(int i11 = i7; i11 < i8; ++i11) {
					Block block12 = Block.blockList[this.getBlockId(i9, i10, i11)];
					if(block12 != null && block12.material == material) {
						int i13 = this.getBlockMetadata(i9, i10, i11);
						double d14 = (double)(i10 + 1);
						if(i13 < 8) {
							d14 = (double)(i10 + 1) - (double)i13 / 8.0D;
						}

						if(d14 >= aabb.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public void createExplosion(Entity entity, double x, double y, double z, float f8) {
		(new Explosion()).doExplosion(this, entity, x, y, z, f8);
	}

	public float getBlockDensity(Vec3D vector, AxisAlignedBB aabb) {
		double d3 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
		double d5 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
		double d7 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
		int i9 = 0;
		int i10 = 0;

		for(float f11 = 0.0F; f11 <= 1.0F; f11 = (float)((double)f11 + d3)) {
			for(float f12 = 0.0F; f12 <= 1.0F; f12 = (float)((double)f12 + d5)) {
				for(float f13 = 0.0F; f13 <= 1.0F; f13 = (float)((double)f13 + d7)) {
					double d14 = aabb.minX + (aabb.maxX - aabb.minX) * (double)f11;
					double d16 = aabb.minY + (aabb.maxY - aabb.minY) * (double)f12;
					double d18 = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)f13;
					if(this.rayTraceBlocks(Vec3D.createVector(d14, d16, d18), vector) == null) {
						++i9;
					}

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public TileEntity getBlockTileEntity(int x, int y, int z) {
		Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		return chunk4 != null ? chunk4.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
	}

	public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		if(chunk5 != null) {
			chunk5.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
		}

	}

	public void removeBlockTileEntity(int x, int y, int z) {
		Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
		if(chunk4 != null) {
			chunk4.removeChunkBlockTileEntity(x & 15, y, z & 15);
		}

	}

	public boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4 = Block.blockList[this.getBlockId(i1, i2, i3)];
		return block4 == null ? false : block4.isOpaqueCube();
	}

	public boolean updatingLighting() {
		int i1 = 1000;

		while(this.lightingToUpdate.size() > 0) {
			--i1;
			if(i1 <= 0) {
				return true;
			}

			((MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1)).updateLight(this);
		}

		return false;
	}

	public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int i2, int i3, int i4, int i5, int i6, int i7) {
		this.scheduleLightingUpdate_do(skyBlock, i2, i3, i4, i5, i6, i7, true);
	}

	public void scheduleLightingUpdate_do(EnumSkyBlock skyBlock, int i2, int i3, int i4, int i5, int i6, int i7, boolean z8) {
		int i9 = (i5 + i2) / 2;
		int i10 = (i7 + i4) / 2;
		if(this.blockExists(i9, 64, i10)) {
			int i11 = this.lightingToUpdate.size();
			if(z8) {
				int i12 = 4;
				if(i12 > i11) {
					i12 = i11;
				}

				for(int i13 = 0; i13 < i12; ++i13) {
					MetadataChunkBlock metadataChunkBlock14 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i13 - 1);
					if(metadataChunkBlock14.skyBlock == skyBlock && metadataChunkBlock14.getLightUpdated(i2, i3, i4, i5, i6, i7)) {
						return;
					}
				}
			}

			this.lightingToUpdate.add(new MetadataChunkBlock(skyBlock, i2, i3, i4, i5, i6, i7));
			if(this.lightingToUpdate.size() > 100000) {
				while(this.lightingToUpdate.size() > 50000) {
					this.updatingLighting();
				}
			}

		}
	}

	public void calculateInitialSkylight() {
		int i1 = this.calculateSkylightSubtracted(1.0F);
		if(i1 != this.skylightSubtracted) {
			this.skylightSubtracted = i1;
		}

	}

	public void tick() {
		this.chunkProvider.unload100OldestChunks();
		int i1 = this.calculateSkylightSubtracted(1.0F);
		if(i1 != this.skylightSubtracted) {
			this.skylightSubtracted = i1;

			for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
				((IWorldAccess)this.worldAccesses.get(i2)).updateAllRenderers();
			}
		}

		++this.worldTime;
		if(this.worldTime % (long)this.autosavePeriod == 0L) {
			this.saveWorld(false, (IProgressUpdate)null);
		}

		this.tickUpdates(false);
		this.updateBlocksAndPlayCaveSounds();
	}

	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();

		int i3;
		int i4;
		int i6;
		int i7;
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			EntityPlayer entityPlayer2 = (EntityPlayer)this.playerEntities.get(i1);
			i3 = MathHelper.floor_double(entityPlayer2.posX / 16.0D);
			i4 = MathHelper.floor_double(entityPlayer2.posZ / 16.0D);
			byte b5 = 9;

			for(i6 = -b5; i6 <= b5; ++i6) {
				for(i7 = -b5; i7 <= b5; ++i7) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(i6 + i3, i7 + i4));
				}
			}
		}

		if(this.soundCounter > 0) {
			--this.soundCounter;
		}

		Iterator iterator12 = this.positionsToUpdate.iterator();

		while(iterator12.hasNext()) {
			ChunkCoordIntPair chunkCoordIntPair13 = (ChunkCoordIntPair)iterator12.next();
			i3 = chunkCoordIntPair13.chunkXPos * 16;
			i4 = chunkCoordIntPair13.chunkZPos * 16;
			Chunk chunk14 = this.getChunkFromChunkCoords(chunkCoordIntPair13.chunkXPos, chunkCoordIntPair13.chunkZPos);
			int i8;
			int i9;
			int i10;
			if(this.soundCounter == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i6 = this.updateLCG >> 2;
				i7 = i6 & 15;
				i8 = i6 >> 8 & 15;
				i9 = i6 >> 16 & 127;
				i10 = chunk14.getBlockID(i7, i9, i8);
				i7 += i3;
				i8 += i4;
				if(i10 == 0 && this.getBlockLightValue(i7, i9, i8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, i7, i9, i8) <= 0) {
					EntityPlayer entityPlayer11 = this.getClosestPlayer((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, 8.0D);
					if(entityPlayer11 != null && entityPlayer11.getDistanceSq((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D) > 4.0D) {
						this.playSoundEffect((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			if(this.snowCovered && this.rand.nextInt(4) == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i6 = this.updateLCG >> 2;
				i7 = i6 & 15;
				i8 = i6 >> 8 & 15;
				i9 = this.getTopSolidOrLiquidBlock(i7 + i3, i8 + i4);
				if(i9 >= 0 && i9 < 128 && chunk14.getSavedLightValue(EnumSkyBlock.Block, i7, i9, i8) < 10) {
					i10 = chunk14.getBlockID(i7, i9 - 1, i8);
					if(chunk14.getBlockID(i7, i9, i8) == 0 && Block.snow.canPlaceBlockAt(this, i7 + i3, i9, i8 + i4)) {
						this.setBlockWithNotify(i7 + i3, i9, i8 + i4, Block.snow.blockID);
					}

					if(i10 == Block.waterStill.blockID && chunk14.getBlockMetadata(i7, i9 - 1, i8) == 0) {
						this.setBlockWithNotify(i7 + i3, i9 - 1, i8 + i4, Block.ice.blockID);
					}
				}
			}

			for(i6 = 0; i6 < 80; ++i6) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i7 = this.updateLCG >> 2;
				i8 = i7 & 15;
				i9 = i7 >> 8 & 15;
				i10 = i7 >> 16 & 127;
				byte b15 = chunk14.blocks[i8 << 11 | i9 << 7 | i10];
				if(Block.tickOnLoad[b15]) {
					Block.blockList[b15].updateTick(this, i8 + i3, i10, i9 + i4, this.rand);
				}
			}
		}

	}

	public boolean tickUpdates(boolean z1) {
		int i2 = this.scheduledTickTreeSet.size();
		if(i2 != this.scheduledTickSet.size()) {
			throw new IllegalStateException("TickNextTick list out of synch");
		} else {
			if(i2 > 1000) {
				i2 = 1000;
			}

			for(int i3 = 0; i3 < i2; ++i3) {
				NextTickListEntry nextTickListEntry4 = (NextTickListEntry)this.scheduledTickTreeSet.first();
				if(!z1 && nextTickListEntry4.scheduledTime > this.worldTime) {
					break;
				}

				this.scheduledTickTreeSet.remove(nextTickListEntry4);
				this.scheduledTickSet.remove(nextTickListEntry4);
				byte b5 = 8;
				if(this.checkChunksExist(nextTickListEntry4.xCoord - b5, nextTickListEntry4.yCoord - b5, nextTickListEntry4.zCoord - b5, nextTickListEntry4.xCoord + b5, nextTickListEntry4.yCoord + b5, nextTickListEntry4.zCoord + b5)) {
					int i6 = this.getBlockId(nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord);
					if(i6 == nextTickListEntry4.blockID && i6 > 0) {
						Block.blockList[i6].updateTick(this, nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB aabb) {
		this.entitiesWithinAABBExcludingEntity.clear();
		int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);

		for(int i7 = i3; i7 <= i4; ++i7) {
			for(int i8 = i5; i8 <= i6; ++i8) {
				if(this.chunkExists(i7, i8)) {
					this.getChunkFromChunkCoords(i7, i8).getEntitiesWithinAABBForEntity(entity, aabb, this.entitiesWithinAABBExcludingEntity);
				}
			}
		}

		return this.entitiesWithinAABBExcludingEntity;
	}

	public List getEntitiesWithinAABB(Class clazz, AxisAlignedBB aabb) {
		int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
		ArrayList arrayList7 = new ArrayList();

		for(int i8 = i3; i8 <= i4; ++i8) {
			for(int i9 = i5; i9 <= i6; ++i9) {
				if(this.chunkExists(i8, i9)) {
					this.getChunkFromChunkCoords(i8, i9).getEntitiesOfTypeWithinAAAB(clazz, aabb, arrayList7);
				}
			}
		}

		return arrayList7;
	}

	public void updateTileEntityChunkAndDoNothing(int x, int y, int z, TileEntity tileEntity) {
		if(this.blockExists(x, y, z)) {
			this.getChunkFromBlockCoords(x, z).setChunkModified();
		}

		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).doNothingWithTileEntity(x, y, z, tileEntity);
		}

	}

	public int countEntities(Class clazz) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			Entity entity4 = (Entity)this.loadedEntityList.get(i3);
			if(clazz.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public void addLoadedEntities(List list) {
		this.loadedEntityList.addAll(list);

		for(int i2 = 0; i2 < list.size(); ++i2) {
			this.obtainEntitySkin((Entity)list.get(i2));
		}

	}

	public void unloadEntities(List list) {
		this.unloadedEntityList.addAll(list);
	}

	public boolean canBlockBePlacedAt(int blockID, int x, int y, int z, boolean z5) {
		int i6 = this.getBlockId(x, y, z);
		Block block7 = Block.blockList[i6];
		Block block8 = Block.blockList[blockID];
		AxisAlignedBB axisAlignedBB9 = block8.getCollisionBoundingBoxFromPool(this, x, y, z);
		if(z5) {
			axisAlignedBB9 = null;
		}

		return axisAlignedBB9 != null && !this.checkIfAABBIsClear(axisAlignedBB9) ? false : (block7 != Block.waterMoving && block7 != Block.waterStill && block7 != Block.lavaMoving && block7 != Block.lavaStill && block7 != Block.fire && block7 != Block.snow ? blockID > 0 && block7 == null && block8.canPlaceBlockAt(this, x, y, z) : true);
	}

	public PathEntity getPathToEntity(Entity entity1, Entity entity2, float f3) {
		int i4 = MathHelper.floor_double(entity1.posX);
		int i5 = MathHelper.floor_double(entity1.posY);
		int i6 = MathHelper.floor_double(entity1.posZ);
		int i7 = (int)(f3 + 16.0F);
		int i8 = i4 - i7;
		int i9 = i5 - i7;
		int i10 = i6 - i7;
		int i11 = i4 + i7;
		int i12 = i5 + i7;
		int i13 = i6 + i7;
		ChunkCache chunkCache14 = new ChunkCache(this, i8, i9, i10, i11, i12, i13);
		return (new Pathfinder(chunkCache14)).createEntityPathTo(entity1, entity2, f3);
	}

	public PathEntity getEntityPathToXYZ(Entity entity, int x, int y, int z, float f5) {
		int i6 = MathHelper.floor_double(entity.posX);
		int i7 = MathHelper.floor_double(entity.posY);
		int i8 = MathHelper.floor_double(entity.posZ);
		int i9 = (int)(f5 + 8.0F);
		int i10 = i6 - i9;
		int i11 = i7 - i9;
		int i12 = i8 - i9;
		int i13 = i6 + i9;
		int i14 = i7 + i9;
		int i15 = i8 + i9;
		ChunkCache chunkCache16 = new ChunkCache(this, i10, i11, i12, i13, i14, i15);
		return (new Pathfinder(chunkCache16)).createEntityPathTo(entity, x, y, z, f5);
	}

	public boolean isBlockProvidingPowerTo(int x, int y, int z, int i4) {
		int i5 = this.getBlockId(x, y, z);
		return i5 == 0 ? false : Block.blockList[i5].isIndirectlyPoweringTo(this, x, y, z, i4);
	}

	public boolean isBlockGettingPowered(int i1, int i2, int i3) {
		return this.isBlockProvidingPowerTo(i1, i2 - 1, i3, 0) ? true : (this.isBlockProvidingPowerTo(i1, i2 + 1, i3, 1) ? true : (this.isBlockProvidingPowerTo(i1, i2, i3 - 1, 2) ? true : (this.isBlockProvidingPowerTo(i1, i2, i3 + 1, 3) ? true : (this.isBlockProvidingPowerTo(i1 - 1, i2, i3, 4) ? true : this.isBlockProvidingPowerTo(i1 + 1, i2, i3, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int x, int y, int z, int i4) {
		if(this.isBlockNormalCube(x, y, z)) {
			return this.isBlockGettingPowered(x, y, z);
		} else {
			int i5 = this.getBlockId(x, y, z);
			return i5 == 0 ? false : Block.blockList[i5].isPoweringTo(this, x, y, z, i4);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int x, int y, int z) {
		return this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity entity, double d2) {
		return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, d2);
	}

	public EntityPlayer getClosestPlayer(double d1, double d3, double d5, double d7) {
		double d9 = -1.0D;
		EntityPlayer entityPlayer11 = null;

		for(int i12 = 0; i12 < this.playerEntities.size(); ++i12) {
			EntityPlayer entityPlayer13 = (EntityPlayer)this.playerEntities.get(i12);
			double d14 = entityPlayer13.getDistanceSq(d1, d3, d5);
			if((d7 < 0.0D || d14 < d7 * d7) && (d9 == -1.0D || d14 < d9)) {
				d9 = d14;
				entityPlayer11 = entityPlayer13;
			}
		}

		return entityPlayer11;
	}

	public byte[] getChunkData(int i1, int i2, int i3, int x, int y, int z) {
		byte[] b7 = new byte[x * y * z * 5 / 2];
		int i8 = i1 >> 4;
		int i9 = i3 >> 4;
		int i10 = i1 + x - 1 >> 4;
		int i11 = i3 + z - 1 >> 4;
		int i12 = 0;
		int i13 = i2;
		int i14 = i2 + y;
		if(i2 < 0) {
			i13 = 0;
		}

		if(i14 > 128) {
			i14 = 128;
		}

		for(int i15 = i8; i15 <= i10; ++i15) {
			int i16 = i1 - i15 * 16;
			int i17 = i1 + x - i15 * 16;
			if(i16 < 0) {
				i16 = 0;
			}

			if(i17 > 16) {
				i17 = 16;
			}

			for(int i18 = i9; i18 <= i11; ++i18) {
				int i19 = i3 - i18 * 16;
				int i20 = i3 + z - i18 * 16;
				if(i19 < 0) {
					i19 = 0;
				}

				if(i20 > 16) {
					i20 = 16;
				}

				i12 = this.getChunkFromChunkCoords(i15, i18).setChunkData(b7, i16, i13, i19, i17, i14, i20, i12);
			}
		}

		return b7;
	}

	public void checkSessionLock() {
		try {
			File file1 = new File(this.saveDirectory, "session.lock");
			DataInputStream dataInputStream2 = new DataInputStream(new FileInputStream(file1));

			try {
				if(dataInputStream2.readLong() != this.lockTimestamp) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				dataInputStream2.close();
			}

		} catch (IOException iOException7) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	static {
		float f0 = 0.05F;

		for(int i1 = 0; i1 <= 15; ++i1) {
			float f2 = 1.0F - (float)i1 / 15.0F;
			lightBrightnessTable[i1] = (1.0F - f2) / (f2 * 3.0F + 1.0F) * (1.0F - f0) + f0;
		}

	}
}
