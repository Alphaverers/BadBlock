package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.input.Keyboard;

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
	public Object fontRenderer;
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
	public long milestone;
	public boolean exclFrailMode;
	public boolean checkedInputManager;
	public boolean hasInputManager;
	public boolean bossfightInProgress;
	public MobGiant bossRef;
	public String bossname;

	public boolean CanUseCheats() {
		if(!this.checkedInputManager) {
			try {
				Class.forName("InputHandler");
				this.hasInputManager = true;
			} catch (ClassNotFoundException classNotFoundException2) {
				this.hasInputManager = false;
			}

			this.checkedInputManager = true;
		}

		return this.hasInputManager ? InputHandler.cheatsEnabled : false;
	}

	public void CueSpawnBossFrom(int i1, int i2) {
		if(!this.multiplayerWorld && !this.bossfightInProgress) {
			int i3 = i1 + 64 * ((new Random()).nextInt(3) - 1);
			int i4 = i2 + 64 * ((new Random()).nextInt(3) - 1);
			if(i3 == i1 && i4 == i2) {
				i4 += 64;
			}

			String string5 = "Giant of " + GuiIngame.Namegen2(this.randomSeed, i3 / 32, i4 / 32);
			this.bossname = string5;
			this.bossRef = InputHandler.SpawnGiant((double)i3, 100.0D, (double)i4);
			System.out.println("spawned at " + i3 + ", " + i4 + ", health: " + this.bossRef.health);
			this.bossfightInProgress = true;
		}

	}

	public static NBTTagCompound getLevelData(File file, String world) {
		File file2 = new File(new File(file, "saves"), world);
		if(!file2.exists()) {
			return null;
		} else {
			File file3 = new File(file2, "level.dat");
			if(file3.exists()) {
				try {
					return ClassX.a((InputStream)(new FileInputStream(file3))).getCompoundTag("Data");
				} catch (Exception exception5) {
					exception5.printStackTrace();
				}
			}

			return null;
		}
	}

	public static void deleteWorld(File file, String world) {
		File file2 = new File(new File(file, "saves"), world);
		if(file2.exists()) {
			deleteWorldFiles(file2.listFiles());
			file2.delete();
		}
	}

	private static void deleteWorldFiles(File[] files) {
		for(int i1 = 0; i1 < files.length; ++i1) {
			if(files[i1].isDirectory()) {
				deleteWorldFiles(files[i1].listFiles());
			}

			files[i1].delete();
		}

	}

	public World(File file, String name) {
		this(file, name, (new Random()).nextLong());
	}

	public World(String name) {
		this.milestone = 0L;
		this.exclFrailMode = true;
		this.checkedInputManager = false;
		this.hasInputManager = false;
		this.bossfightInProgress = false;
		this.bossRef = null;
		this.bossname = "";
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
		this.levelName = name;
		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		this.calculateInitialSkylight();
	}

	public World(File file, String name, long seed) {
		this.milestone = 0L;
		this.exclFrailMode = true;
		this.checkedInputManager = false;
		this.hasInputManager = false;
		this.bossfightInProgress = false;
		this.bossRef = null;
		this.bossname = "";
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
		this.levelName = name;
		file.mkdirs();
		(this.saveDirectory = new File(file, name)).mkdirs();

		try {
			DataOutputStream dataOutputStream5 = new DataOutputStream(new FileOutputStream(new File(this.saveDirectory, "session.lock")));

			try {
				dataOutputStream5.writeLong(this.lockTimestamp);
			} finally {
				dataOutputStream5.close();
			}
		} catch (IOException iOException12) {
			throw new RuntimeException("Failed to check session lock, aborting");
		}

		File file13 = new File(this.saveDirectory, "level.dat");
		this.isNewWorld = !file13.exists();
		if(file13.exists()) {
			try {
				NBTTagCompound nBTTagCompound6 = ClassX.a((InputStream)(new FileInputStream(file13))).getCompoundTag("Data");
				this.randomSeed = nBTTagCompound6.getLong("RandomSeed");
				this.spawnX = nBTTagCompound6.getInteger("SpawnX");
				this.spawnY = nBTTagCompound6.getInteger("SpawnY");
				this.spawnZ = nBTTagCompound6.getInteger("SpawnZ");
				this.worldTime = nBTTagCompound6.getLong("Time");
				this.sizeOnDisk = nBTTagCompound6.getLong("SizeOnDisk");
				this.snowCovered = nBTTagCompound6.getBoolean("SnowCovered");
				if(nBTTagCompound6.hasKey("Player")) {
					this.nbtCompoundPlayer = nBTTagCompound6.getCompoundTag("Player");
				}

				this.milestone = nBTTagCompound6.getLong("Milestones");
				this.exclFrailMode = nBTTagCompound6.getBoolean("ExclusivelyFrail");
				System.out.println("Current milestone: " + this.milestone);
			} catch (Exception exception10) {
				exception10.printStackTrace();
			}
		} else {
			this.snowCovered = this.rand.nextInt(4) == 0;
		}

		boolean z14 = false;
		if(this.randomSeed == 0L) {
			this.randomSeed = seed;
			z14 = true;
		}

		this.chunkProvider = this.getChunkProvider(this.saveDirectory);
		if(z14) {
			this.worldChunkLoadOverride = true;
			this.spawnX = 0;
			this.spawnY = 64;

			for(this.spawnZ = 0; !this.findSpawn(this.spawnX, this.spawnZ); this.spawnZ += this.rand.nextInt(64) - this.rand.nextInt(64)) {
				this.spawnX += this.rand.nextInt(64) - this.rand.nextInt(64);
			}

			this.worldChunkLoadOverride = false;
		}

		this.calculateInitialSkylight();
	}

	protected IChunkProvider getChunkProvider(File file) {
		return new ChunkProviderLoadOrGenerate(this, new ChunkLoader(file, true), new ChunkProviderGenerate(this, this.randomSeed));
	}

	public void setSpawnLocation() {
		if(this.spawnY <= 0) {
			this.spawnY = 64;
		}

		while(this.getFirstUncoveredBlock(this.spawnX, this.spawnZ) == 0) {
			this.spawnX += this.rand.nextInt(8) - this.rand.nextInt(8);
			this.spawnZ += this.rand.nextInt(8) - this.rand.nextInt(8);
		}

	}

	private boolean findSpawn(int x, int z) {
		return this.getFirstUncoveredBlock(x, z) == Block.sand.blockID;
	}

	private int getFirstUncoveredBlock(int x, int z) {
		int i3;
		for(i3 = 63; this.getBlockId(x, i3 + 1, z) != 0; ++i3) {
		}

		return this.getBlockId(x, i3, z);
	}

	public void spawnPlayerWithLoadedChunks(EntityPlayer entityPlayer) {
		try {
			if(this.nbtCompoundPlayer != null) {
				entityPlayer.readFromNBT(this.nbtCompoundPlayer);
				this.nbtCompoundPlayer = null;
			}

			this.spawnEntityInWorld(entityPlayer);
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

	}

	public void saveWorld(boolean z1, IProgressUpdate iProgressUpdate2) {
		if(this.chunkProvider.canSave()) {
			if(iProgressUpdate2 != null) {
				iProgressUpdate2.displayProgressMessage("Saving level");
			}

			this.saveLevel();
			if(iProgressUpdate2 != null) {
				iProgressUpdate2.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(z1, iProgressUpdate2);
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
		nBTTagCompound1.setLong("Milestones", this.milestone);
		nBTTagCompound1.setBoolean("ExclusivelyFrail", this.exclFrailMode);
		Entity entity2 = null;
		if(this.playerEntities.size() > 0) {
			entity2 = (Entity)this.playerEntities.get(0);
		}

		NBTTagCompound nBTTagCompound3;
		if(entity2 != null) {
			nBTTagCompound3 = new NBTTagCompound();
			entity2.writeToNBT(nBTTagCompound3);
			nBTTagCompound1.setCompoundTag("Player", nBTTagCompound3);
		}

		nBTTagCompound3 = new NBTTagCompound();
		nBTTagCompound3.setTag("Data", nBTTagCompound1);

		try {
			File file4 = new File(this.saveDirectory, "level.dat_new");
			File file5 = new File(this.saveDirectory, "level.dat_old");
			File file6 = new File(this.saveDirectory, "level.dat");
			ClassX.a(nBTTagCompound3, (OutputStream)(new FileOutputStream(file4)));
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

	public boolean saveWorld(int i1) {
		if(!this.chunkProvider.canSave()) {
			return true;
		} else {
			if(i1 == 0) {
				this.saveLevel();
			}

			return this.chunkProvider.saveChunks(false, (IProgressUpdate)null);
		}
	}

	public int getBlockId(int x, int y, int z) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (y < 0 ? 0 : (y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15))) : 0;
	}

	public boolean blockExists(int x, int y, int z) {
		return y >= 0 && y < 128 && this.chunkExists(x >> 4, z >> 4);
	}

	public boolean checkChunksExist(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(maxY >= 0 && minY < 128) {
			minX >>= 4;
			minY >>= 4;
			minZ >>= 4;
			maxX >>= 4;
			maxY >>= 4;
			maxZ >>= 4;

			for(int i7 = minX; i7 <= maxX; ++i7) {
				for(int i8 = minZ; i8 <= maxZ; ++i8) {
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

	private boolean chunkExists(int xPos, int zPos) {
		return this.chunkProvider.chunkExists(xPos, zPos);
	}

	public Chunk getChunkFromBlockCoords(int xPos, int zPos) {
		return this.getChunkFromChunkCoords(xPos >> 4, zPos >> 4);
	}

	public Chunk getChunkFromChunkCoords(int xPos, int zPos) {
		return this.chunkProvider.provideChunk(xPos, zPos);
	}

	public boolean setBlockAndMetadata(int x, int y, int z, int id, int metadata) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 && y >= 0 && y < 128 && this.getChunkFromChunkCoords(x >> 4, z >> 4).setBlockIDWithMetadata(x & 15, y, z & 15, id, metadata);
	}

	public boolean setBlock(int x, int y, int z, int id) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 && y >= 0 && y < 128 && this.getChunkFromChunkCoords(x >> 4, z >> 4).setBlockID(x & 15, y, z & 15, id);
	}

	public Material getBlockMaterial(int i1, int i2, int i3) {
		int i4 = this.getBlockId(i1, i2, i3);
		return i4 == 0 ? Material.air : Block.blocksList[i4].material;
	}

	public int getBlockMetadata(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return 0;
			} else if(y >= 128) {
				return 0;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return chunk4.getBlockMetadata(x, y, z);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int x, int y, int z, int metadata) {
		this.setBlockMetadata(x, y, z, metadata);
	}

	public boolean setBlockMetadata(int x, int y, int z, int metadata) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				chunk5.setBlockMetadata(x, y, z, metadata);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int x, int y, int z, int id) {
		if(this.setBlock(x, y, z, id)) {
			this.notifyBlockChange(x, y, z, id);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int id, int metadata) {
		return this.bStage2(x, y, z, id, metadata);
	}

	public boolean bStage2(int i1, int i2, int i3, int i4, int i5) {
		if(this.setBlockAndMetadata(i1, i2, i3, i4, i5)) {
			this.notifyBlockChange(i1, i2, i3, i4);
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

	protected void notifyBlockChange(int x, int y, int z, int id) {
		this.markBlockNeedsUpdate(x, y, z);
		this.notifyBlocksOfNeighborChange(x, y, z, id);
	}

	public void markBlocksDirtyVertical(int x, int z, int minY, int maxY) {
		if(minY > maxY) {
			int i5 = maxY;
			maxY = minY;
			minY = i5;
		}

		this.markBlocksDirty(x, minY, z, x, maxY, z);
	}

	public void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).markBlockRangeNeedsUpdate(minX, minY, minZ, maxX, maxY, maxZ);
		}

	}

	public void notifyBlocksOfNeighborChange(int x, int y, int z, int id) {
		this.notifyBlockOfNeighborChange(x - 1, y, z, id);
		this.notifyBlockOfNeighborChange(x + 1, y, z, id);
		this.notifyBlockOfNeighborChange(x, y - 1, z, id);
		this.notifyBlockOfNeighborChange(x, y + 1, z, id);
		this.notifyBlockOfNeighborChange(x, y, z - 1, id);
		this.notifyBlockOfNeighborChange(x, y, z + 1, id);
	}

	private void notifyBlockOfNeighborChange(int x, int y, int z, int id) {
		if(!this.editingBlocks && !this.multiplayerWorld) {
			Block block5 = Block.blocksList[this.getBlockId(x, y, z)];
			if(block5 != null) {
				block5.onNeighborBlockChange(this, x, y, z, id);
			}

		}
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
	}

	public int getBlockLightValue(int x, int y, int z) {
		return this.getBlockLightValue_do(x, y, z, true);
	}

	public int getBlockLightValue_do(int x, int y, int z, boolean update) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int i5;
			if(update) {
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
		return blockX >= -32000000 && blockZ >= -32000000 && blockX < 32000000 && blockZ <= 32000000 ? (!this.chunkExists(blockX >> 4, blockZ >> 4) ? 0 : this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4).getHeightValue(blockX & 15, blockZ & 15)) : 0;
	}

	public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		this.aStage3(skyBlock, x, y, z, lightValue);
	}

	public void aStage3(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		if(this.blockExists(i2, i3, i4)) {
			if(enumSkyBlock1 == EnumSkyBlock.Sky) {
				if(this.canExistingBlockSeeTheSky(i2, i3, i4)) {
					i5 = 15;
				}
			} else if(enumSkyBlock1 == EnumSkyBlock.Block) {
				int i6 = this.getBlockId(i2, i3, i4);
				if(Block.lightValue[i6] > i5) {
					i5 = Block.lightValue[i6];
				}
			}

			if(this.getSavedLightValue(enumSkyBlock1, i2, i3, i4) != i5) {
				this.scheduleLightingUpdate(enumSkyBlock1, i2, i3, i4, i2, i3, i4);
			}

		}
	}

	public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
		if(y >= 0 && y < 128 && x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int i5 = x >> 4;
			int i6 = z >> 4;
			return !this.chunkExists(i5, i6) ? 0 : this.getChunkFromChunkCoords(i5, i6).getSavedLightValue(skyBlock, x & 15, y, z & 15);
		} else {
			return skyBlock.defaultLightValue;
		}
	}

	public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y >= 0) {
				if(y < 128) {
					if(this.chunkExists(x >> 4, z >> 4)) {
						this.getChunkFromChunkCoords(x >> 4, z >> 4).setLightValue(skyBlock, x & 15, y, z & 15, lightValue);

						for(int i6 = 0; i6 < this.worldAccesses.size(); ++i6) {
							((IWorldAccess)this.worldAccesses.get(i6)).markBlockAndNeighborsNeedsUpdate(x, y, z);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int x, int y, int z) {
		return lightBrightnessTable[this.getBlockLightValue(x, y, z)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D vec3D1, Vec3D vec3D2) {
		return this.rayTraceBlocks_do(vec3D1, vec3D2, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D vec3D1, Vec3D vec3D2, boolean z3) {
		if(!Double.isNaN(vec3D1.xCoord) && !Double.isNaN(vec3D1.yCoord) && !Double.isNaN(vec3D1.zCoord)) {
			if(!Double.isNaN(vec3D2.xCoord) && !Double.isNaN(vec3D2.yCoord) && !Double.isNaN(vec3D2.zCoord)) {
				int i4 = MathHelper.floor_double(vec3D2.xCoord);
				int i5 = MathHelper.floor_double(vec3D2.yCoord);
				int i6 = MathHelper.floor_double(vec3D2.zCoord);
				int i7 = MathHelper.floor_double(vec3D1.xCoord);
				int i8 = MathHelper.floor_double(vec3D1.yCoord);
				int i9 = MathHelper.floor_double(vec3D1.zCoord);
				int i10 = 20;

				while(i10-- >= 0) {
					if(Double.isNaN(vec3D1.xCoord) || Double.isNaN(vec3D1.yCoord) || Double.isNaN(vec3D1.zCoord)) {
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
					double d23 = vec3D2.xCoord - vec3D1.xCoord;
					double d25 = vec3D2.yCoord - vec3D1.yCoord;
					double d27 = vec3D2.zCoord - vec3D1.zCoord;
					if(d11 != 999.0D) {
						d17 = (d11 - vec3D1.xCoord) / d23;
					}

					if(d13 != 999.0D) {
						d19 = (d13 - vec3D1.yCoord) / d25;
					}

					if(d15 != 999.0D) {
						d21 = (d15 - vec3D1.zCoord) / d27;
					}

					byte b29;
					if(d17 < d19 && d17 < d21) {
						if(i4 > i7) {
							b29 = 4;
						} else {
							b29 = 5;
						}

						vec3D1.xCoord = d11;
						vec3D1.yCoord += d25 * d17;
						vec3D1.zCoord += d27 * d17;
					} else if(d19 < d21) {
						if(i5 > i8) {
							b29 = 0;
						} else {
							b29 = 1;
						}

						vec3D1.xCoord += d23 * d19;
						vec3D1.yCoord = d13;
						vec3D1.zCoord += d27 * d19;
					} else {
						if(i6 > i9) {
							b29 = 2;
						} else {
							b29 = 3;
						}

						vec3D1.xCoord += d23 * d21;
						vec3D1.yCoord += d25 * d21;
						vec3D1.zCoord = d15;
					}

					Vec3D vec3D30;
					Vec3D vec3D31 = vec3D30 = Vec3D.createVector(vec3D1.xCoord, vec3D1.yCoord, vec3D1.zCoord);
					double d32 = (double)MathHelper.floor_double(vec3D1.xCoord);
					vec3D30.xCoord = d32;
					i7 = (int)d32;
					if(b29 == 5) {
						--i7;
						++vec3D31.xCoord;
					}

					double d35 = (double)MathHelper.floor_double(vec3D1.yCoord);
					vec3D31.yCoord = d35;
					i8 = (int)d35;
					if(b29 == 1) {
						--i8;
						++vec3D31.yCoord;
					}

					double d38 = (double)MathHelper.floor_double(vec3D1.zCoord);
					vec3D31.zCoord = d38;
					i9 = (int)d38;
					if(b29 == 3) {
						--i9;
						++vec3D31.zCoord;
					}

					int i40 = this.getBlockId(i7, i8, i9);
					int i41 = this.getBlockMetadata(i7, i8, i9);
					Block block42 = Block.blocksList[i40];
					if(i40 > 0 && block42.canCollideCheck(i41, z3)) {
						MovingObjectPosition movingObjectPosition43 = block42.collisionRayTrace(this, i7, i8, i9, vec3D1, vec3D2);
						if(movingObjectPosition43 != null) {
							return movingObjectPosition43;
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

	public void playSoundAtEntity(Entity entity, String sound, float volume, float pitch) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playSound(sound, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, volume, pitch);
		}

	}

	public void playSoundEffect(double posX, double posY, double posZ, String sound, float volume, float pitch) {
		for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
			((IWorldAccess)this.worldAccesses.get(i10)).playSound(sound, posX, posY, posZ, volume, pitch);
		}

	}

	public void playRecord(String record, int x, int y, int z) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playRecord(record, x, y, z);
		}

	}

	public void spawnParticle(String particle, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(particle, posX, posY, posZ, motionX, motionY, motionZ);
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
				this.playerEntities.add(entity);
				System.out.println("Player count: " + this.playerEntities.size());
			}

			this.getChunkFromChunkCoords(i2, i3).addEntity(entity);
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
			this.playerEntities.remove(entity);
			System.out.println("Player count: " + this.playerEntities.size());
		}

	}

	public void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	public void removeWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.remove(worldAccess);
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
						Block block12 = Block.blocksList[this.getBlockId(i9, i11, i10)];
						if(block12 != null) {
							block12.getCollidingBoundingBoxes(this, i9, i11, i10, aabb, this.collidingBoundingBoxes);
						}
					}
				}
			}
		}

		List list15 = this.getEntitiesWithinAABBExcludingEntity(entity, aabb.expand(0.25D, 0.25D, 0.25D));

		for(int i16 = 0; i16 < list15.size(); ++i16) {
			AxisAlignedBB axisAlignedBB13 = ((Entity)list15.get(i16)).getBoundingBox();
			if(axisAlignedBB13 != null && axisAlignedBB13.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(axisAlignedBB13);
			}

			AxisAlignedBB axisAlignedBB14 = entity.getCollisionBox((Entity)list15.get(i16));
			if(axisAlignedBB14 != null && axisAlignedBB14.intersectsWith(aabb)) {
				this.collidingBoundingBoxes.add(axisAlignedBB14);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float renderPartialTick) {
		float f2 = 1.0F - (MathHelper.cos(this.getCelestialAngle(renderPartialTick) * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return (int)(f2 * 11.0F);
	}

	public Vec3D getSkyColor(float renderPartialTick) {
		float f2 = MathHelper.cos(this.getCelestialAngle(renderPartialTick) * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return Vec3D.createVector((double)((float)(this.skyColor >> 16 & 255L) / 255.0F * f2), (double)((float)(this.skyColor >> 8 & 255L) / 255.0F * f2), (double)((float)(this.skyColor & 255L) / 255.0F * f2));
	}

	public float getCelestialAngle(float renderPartialTick) {
		float f2 = ((float)((int)(this.worldTime % 24000L)) + renderPartialTick) / 24000.0F - 0.25F;
		if(f2 < 0.0F) {
			++f2;
		}

		if(f2 > 1.0F) {
			--f2;
		}

		return f2 + (1.0F - (float)((Math.cos((double)f2 * Math.PI) + 1.0D) / 2.0D) - f2) / 3.0F;
	}

	public Vec3D getCloudColor(float renderPartialTick) {
		float f2 = MathHelper.cos(this.getCelestialAngle(renderPartialTick) * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return Vec3D.createVector((double)((float)(this.cloudColor >> 16 & 255L) / 255.0F * (f2 * 0.9F + 0.1F)), (double)((float)(this.cloudColor >> 8 & 255L) / 255.0F * (f2 * 0.9F + 0.1F)), (double)((float)(this.cloudColor & 255L) / 255.0F * (f2 * 0.85F + 0.15F)));
	}

	public Vec3D getFogColor(float renderPartialTick) {
		float f2 = MathHelper.cos(this.getCelestialAngle(renderPartialTick) * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return Vec3D.createVector((double)((float)(this.fogColor >> 16 & 255L) / 255.0F * (f2 * 0.94F + 0.06F)), (double)((float)(this.fogColor >> 8 & 255L) / 255.0F * (f2 * 0.94F + 0.06F)), (double)((float)(this.fogColor & 255L) / 255.0F * (f2 * 0.91F + 0.09F)));
	}

	public int getTopSolidOrLiquidBlock(int x, int z) {
		Chunk chunk3 = this.getChunkFromBlockCoords(x, z);
		int i4 = 127;
		x &= 15;

		for(z &= 15; i4 > 0; --i4) {
			int i5 = chunk3.getBlockID(x, i4, z);
			if(i5 != 0 && (Block.blocksList[i5].material.getIsSolid() || Block.blocksList[i5].material.getIsLiquid())) {
				return i4 + 1;
			}
		}

		return -1;
	}

	public int getPrecipitationHeight(int x, int z) {
		return this.eStage2(x, z);
	}

	public int eStage2(int i1, int i2) {
		return this.getChunkFromBlockCoords(i1, i2).getHeightValue(i1 & 15, i2 & 15);
	}

	public float getStarBrightness(float renderPartialTick) {
		float f2 = 1.0F - (MathHelper.cos(this.getCelestialAngle(renderPartialTick) * (float)Math.PI * 2.0F) * 2.0F + 0.75F);
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return f2 * f2 * 0.5F;
	}

	public void scheduleBlockUpdate(int x, int y, int z, int id) {
		NextTickListEntry nextTickListEntry5 = new NextTickListEntry(x, y, z, id);
		if(this.checkChunksExist(x - 8, y - 8, z - 8, x + 8, y + 8, z + 8)) {
			if(id > 0) {
				nextTickListEntry5.setScheduledTime((long)Block.blocksList[id].tickRate() + this.worldTime);
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
			((TileEntity)this.loadedTileEntityList.get(i1)).updateEntity();
		}

	}

	protected void updateEntity(Entity entity) {
		int i2 = MathHelper.floor_double(entity.posX);
		int i3 = MathHelper.floor_double(entity.posZ);
		if(this.checkChunksExist(i2 - 16, 0, i3 - 16, i2 + 16, 128, i3 + 16)) {
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
					this.getChunkFromChunkCoords(i5, i7).addEntity(entity);
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
					Block block11 = Block.blocksList[this.getBlockId(i8, i9, i10)];
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
					Block block15 = Block.blocksList[this.getBlockId(i12, i13, i14)];
					if(block15 != null && block15.material == material && (double)i7 >= (double)((float)(i13 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(i12, i13, i14)))) {
						z10 = true;
						block15.velocityToAddToEntity(this, i12, i13, i14, entity, vec3D11);
					}
				}
			}
		}

		if(vec3D11.lengthVector() > 0.0D) {
			Vec3D vec3D16 = vec3D11.normalize();
			entity.motionX += vec3D16.xCoord * 0.004D;
			entity.motionY += vec3D16.yCoord * 0.004D;
			entity.motionZ += vec3D16.zCoord * 0.004D;
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
					Block block12 = Block.blocksList[this.getBlockId(i9, i10, i11)];
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
					Block block12 = Block.blocksList[this.getBlockId(i9, i10, i11)];
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

	public void createExplosion(Entity entity1, double d2, double d4, double d6, float f8) {
		(new Explosion()).doExplosion(this, entity1, d2, d4, d6, f8);
	}

	public float getBlockDensity(Vec3D vector, AxisAlignedBB aabb) {
		double d3 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
		double d5 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
		double d7 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
		int i9 = 0;
		int i10 = 0;

		for(float f11 = 0.0F; f11 <= 1.0F; f11 += (float)d3) {
			for(float f12 = 0.0F; f12 <= 1.0F; f12 += (float)d5) {
				for(float f13 = 0.0F; f13 <= 1.0F; f13 += (float)d7) {
					if(this.rayTraceBlocks(Vec3D.createVector(aabb.minX + (aabb.maxX - aabb.minX) * (double)f11, aabb.minY + (aabb.maxY - aabb.minY) * (double)f12, aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)f13), vector) == null) {
						++i9;
					}

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public void extinguishFire(int x, int y, int z, int side) {
		if(side == 0) {
			--y;
		}

		if(side == 1) {
			++y;
		}

		if(side == 2) {
			--z;
		}

		if(side == 3) {
			++z;
		}

		if(side == 4) {
			--x;
		}

		if(side == 5) {
			++x;
		}

		if(this.getBlockId(x, y, z) == Block.fire.blockID) {
			this.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
			this.setBlockWithNotify(x, y, z, 0);
		}

	}

	public Entity createDebugPlayer(Class playerClass) {
		return null;
	}

	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public TileEntity getBlockTileEntity(int i1, int i2, int i3) {
		Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
		return chunk4 != null ? chunk4.getChunkBlockTileEntity(i1 & 15, i2, i3 & 15) : null;
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
		Block block4 = Block.blocksList[this.getBlockId(i1, i2, i3)];
		return block4 != null && block4.isOpaqueCube();
	}

	public void saveWorldIndirectly(IProgressUpdate iProgressUpdate1) {
		this.saveWorld(true, iProgressUpdate1);
	}

	public boolean updatingLighting() {
		return this.e2stage2();
	}

	public boolean e2stage2() {
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

	public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.scheduleLightingUpdate_do(skyBlock, minX, minY, minZ, maxX, maxY, maxZ, true);
	}

	public void scheduleLightingUpdate_do(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean update) {
		if(this.blockExists((maxX + minX) / 2, 64, (maxZ + minZ) / 2)) {
			int i9 = this.lightingToUpdate.size();
			if(update) {
				int i10 = 4;
				if(i10 > i9) {
					i10 = i9;
				}

				for(int i11 = 0; i11 < i10; ++i11) {
					MetadataChunkBlock metadataChunkBlock12 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i11 - 1);
					if(metadataChunkBlock12.skyBlock == skyBlock && metadataChunkBlock12.getLightUpdated(minX, minY, minZ, maxX, maxY, maxZ)) {
						return;
					}
				}
			}

			this.lightingToUpdate.add(new MetadataChunkBlock(skyBlock, minX, minY, minZ, maxX, maxY, maxZ));
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

		if(this.bossfightInProgress && this.bossRef.isDead) {
			this.bossfightInProgress = false;
			this.bossRef = null;
		}

		if(this.exclFrailMode && InputHandler.mc.options.difficulty != 4) {
			this.exclFrailMode = false;
			System.out.println("World has been changed from Frail mode");
		}

		if(this.CanUseCheats() && Keyboard.isKeyDown(Keyboard.KEY_MULTIPLY)) {
			this.worldTime += 12L;
		} else if(!this.CanUseCheats() || !Keyboard.isKeyDown(Keyboard.KEY_DECIMAL)) {
			++this.worldTime;
			if(this.worldTime % 23000L == 0L) {
				++this.milestone;
				System.out.println("Milestone " + this.milestone + (this.exclFrailMode ? "*" : "") + " reached.");
				if(this.exclFrailMode && this.milestone == 10L) {
					GuiIngame.uqKey = ScreenKeyInput.playerIndex() + ": " + ScreenKeyInput.calcString((long)ScreenKeyInput.playerIndex() << 56, ~((int)(this.milestone - 2L - (long)ScreenKeyInput.playerIndex())));
					System.out.println(GuiIngame.uqKey);
				}
			}
		}

		if(this.worldTime % (long)this.autosavePeriod == 0L) {
			this.saveWorld(false, (IProgressUpdate)null);
		}

		this.tickUpdates(false);
		this.updateBlocksAndPlayCaveSounds();
	}

	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();

		int i4;
		int i7;
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			EntityPlayer entityPlayer2 = (EntityPlayer)this.playerEntities.get(i1);
			int i3 = MathHelper.floor_double(entityPlayer2.posX / 16.0D);
			i4 = MathHelper.floor_double(entityPlayer2.posZ / 16.0D);
			byte b5 = 9;

			for(int i6 = -b5; i6 <= b5; ++i6) {
				for(i7 = -b5; i7 <= b5; ++i7) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(i6 + i3, i7 + i4));
				}
			}
		}

		if(this.soundCounter > 0) {
			--this.soundCounter;
		}

		Iterator iterator15 = this.positionsToUpdate.iterator();

		while(iterator15.hasNext()) {
			Object object16 = iterator15.next();
			ChunkCoordIntPair chunkCoordIntPair17 = (ChunkCoordIntPair)object16;
			i4 = chunkCoordIntPair17.chunkXPos * 16;
			int i18 = chunkCoordIntPair17.chunkZPos * 16;
			Chunk chunk19 = this.getChunkFromChunkCoords(chunkCoordIntPair17.chunkXPos, chunkCoordIntPair17.chunkZPos);
			int i8;
			int i9;
			int i10;
			int i11;
			if(this.soundCounter == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i7 = this.updateLCG >> 2;
				i8 = i7 & 15;
				i9 = i7 >> 8 & 15;
				i10 = i7 >> 16 & 127;
				i11 = chunk19.getBlockID(i8, i10, i9);
				int i12 = i8 + i4;
				int i13 = i9 + i18;
				if(i11 == 0 && this.getBlockLightValue(i12, i10, i13) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, i12, i10, i13) <= 0) {
					EntityPlayer entityPlayer14 = this.getClosestPlayer((double)i12 + 0.5D, (double)i10 + 0.5D, (double)i13 + 0.5D, 8.0D);
					if(entityPlayer14 != null && entityPlayer14.getDistanceSq((double)i12 + 0.5D, (double)i10 + 0.5D, (double)i13 + 0.5D) > 4.0D) {
						this.playSoundEffect((double)i12 + 0.5D, (double)i10 + 0.5D, (double)i13 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			if(this.snowCovered && this.rand.nextInt(4) == 0) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i7 = this.updateLCG >> 2;
				i8 = i7 & 15;
				i9 = i7 >> 8 & 15;
				i10 = this.getTopSolidOrLiquidBlock(i8 + i4, i9 + i18);
				if(i10 >= 0 && i10 < 128 && chunk19.getSavedLightValue(EnumSkyBlock.Block, i8, i10, i9) < 10) {
					i11 = chunk19.getBlockID(i8, i10 - 1, i9);
					if(chunk19.getBlockID(i8, i10, i9) == 0 && Block.snow.canPlaceBlockAt(this, i8 + i4, i10, i9 + i18)) {
						this.setBlockWithNotify(i8 + i4, i10, i9 + i18, Block.snow.blockID);
					}

					if(i11 == Block.waterStill.blockID && chunk19.getBlockMetadata(i8, i10 - 1, i9) == 0) {
						this.setBlockWithNotify(i8 + i4, i10 - 1, i9 + i18, Block.ice.blockID);
					}
				}
			}

			for(i7 = 0; i7 < 80; ++i7) {
				this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
				i8 = this.updateLCG >> 2;
				i9 = i8 & 15;
				i10 = i8 >> 8 & 15;
				i11 = i8 >> 16 & 127;
				byte b20 = chunk19.blocks[i9 << 11 | i10 << 7 | i11];
				if(Block.tickOnLoad[b20]) {
					Block.blocksList[b20].updateTick(this, i9 + i4, i11, i10 + i18, this.rand);
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
				if(this.checkChunksExist(nextTickListEntry4.xCoord - 8, nextTickListEntry4.yCoord - 8, nextTickListEntry4.zCoord - 8, nextTickListEntry4.xCoord + 8, nextTickListEntry4.yCoord + 8, nextTickListEntry4.zCoord + 8)) {
					int i6 = this.getBlockId(nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord);
					if(i6 == nextTickListEntry4.blockID && i6 > 0) {
						Block.blocksList[i6].updateTick(this, nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public void randomDisplayUpdates(int posX, int posY, int posZ) {
		Random random5 = new Random();

		for(int i6 = 0; i6 < 1000; ++i6) {
			int i7 = posX + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i8 = posY + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i9 = posZ + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i10 = this.getBlockId(i7, i8, i9);
			if(i10 > 0) {
				Block.blocksList[i10].randomDisplayTick(this, i7, i8, i9, random5);
			}
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

	public List getEntitiesWithinAABB(Class entityClass, AxisAlignedBB aabb) {
		int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
		ArrayList arrayList7 = new ArrayList();

		for(int i8 = i3; i8 <= i4; ++i8) {
			for(int i9 = i5; i9 <= i6; ++i9) {
				if(this.chunkExists(i8, i9)) {
					this.getChunkFromChunkCoords(i8, i9).getEntitiesOfTypeWithinAAAB(entityClass, aabb, arrayList7);
				}
			}
		}

		return arrayList7;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void updateTileEntityChunkAndDoNothing(int x, int y, int z, TileEntity tileEntity) {
		if(this.blockExists(x, y, z)) {
			this.getChunkFromBlockCoords(x, z).setChunkModified();
		}

		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).doNothingWithTileEntity(x, y, z, tileEntity);
		}

	}

	public int countEntities(Class entityClass) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			if(entityClass.isAssignableFrom(((Entity)this.loadedEntityList.get(i3)).getClass())) {
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

	public void dropOldChunks() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

	public boolean canBlockBePlacedAt(int id, int x, int y, int z, boolean ignoreBB) {
		Block block6 = Block.blocksList[this.getBlockId(x, y, z)];
		Block block7 = Block.blocksList[id];
		AxisAlignedBB axisAlignedBB8 = block7.getCollisionBoundingBoxFromPool(this, x, y, z);
		if(ignoreBB) {
			axisAlignedBB8 = null;
		}

		return (axisAlignedBB8 == null || this.checkIfAABBIsClear(axisAlignedBB8)) && (block6 == Block.waterMoving || block6 == Block.waterStill || block6 == Block.lavaMoving || block6 == Block.lavaStill || block6 == Block.fire || block6 == Block.snow || id > 0 && block6 == null && block7.canPlaceBlockAt(this, x, y, z));
	}

	public PathEntity getPathToEntity(Entity entity1, Entity entity2, float f3) {
		int i4 = MathHelper.floor_double(entity1.posX);
		int i5 = MathHelper.floor_double(entity1.posY);
		int i6 = MathHelper.floor_double(entity1.posZ);
		int i7 = (int)(f3 + 16.0F);
		return (new Pathfinder(new ChunkCache(this, i4 - i7, i5 - i7, i6 - i7, i4 + i7, i5 + i7, i6 + i7))).createEntityPathTo(entity1, entity2, f3);
	}

	public PathEntity getEntityPathToXYZ(Entity entity, int i2, int i3, int i4, float f5) {
		int i6 = MathHelper.floor_double(entity.posX);
		int i7 = MathHelper.floor_double(entity.posY);
		int i8 = MathHelper.floor_double(entity.posZ);
		int i9 = (int)(f5 + 8.0F);
		return (new Pathfinder(new ChunkCache(this, i6 - i9, i7 - i9, i8 - i9, i6 + i9, i7 + i9, i8 + i9))).createEntityPathTo(entity, i2, i3, i4, f5);
	}

	public boolean isBlockProvidingPowerTo(int x, int y, int z, int side) {
		int i5 = this.getBlockId(x, y, z);
		return i5 != 0 && Block.blocksList[i5].isIndirectlyPoweringTo(this, x, y, z, side);
	}

	public boolean isBlockGettingPowered(int x, int y, int z) {
		return this.isBlockProvidingPowerTo(x, y - 1, z, 0) || this.isBlockProvidingPowerTo(x, y + 1, z, 1) || this.isBlockProvidingPowerTo(x, y, z - 1, 2) || this.isBlockProvidingPowerTo(x, y, z + 1, 3) || this.isBlockProvidingPowerTo(x - 1, y, z, 4) || this.isBlockProvidingPowerTo(x + 1, y, z, 5);
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int x, int y, int z, int side) {
		if(this.isBlockNormalCube(x, y, z)) {
			return this.isBlockGettingPowered(x, y, z);
		} else {
			int i5 = this.getBlockId(x, y, z);
			return i5 != 0 && Block.blocksList[i5].isPoweringTo(this, x, y, z, side);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int x, int y, int z) {
		return this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) || this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) || this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5);
	}

	public EntityPlayer getClosestPlayerToEntity(Entity entity, double distance) {
		return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, distance);
	}

	public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance) {
		double d9 = -1.0D;
		EntityPlayer entityPlayer11 = null;

		for(int i12 = 0; i12 < this.playerEntities.size(); ++i12) {
			EntityPlayer entityPlayer13 = (EntityPlayer)this.playerEntities.get(i12);
			double d14 = entityPlayer13.getDistanceSq(posX, posY, posZ);
			if((distance < 0.0D || d14 < distance * distance) && (d9 == -1.0D || d14 < d9)) {
				d9 = d14;
				entityPlayer11 = entityPlayer13;
			}
		}

		return entityPlayer11;
	}

	public void setChunkData(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, byte[] blocks) {
		int i8 = minX >> 4;
		int i9 = minZ >> 4;
		int i10 = minX + maxX - 1 >> 4;
		int i11 = minZ + maxZ - 1 >> 4;
		int i12 = 0;
		int i13 = minY;
		int i14 = minY + maxY;
		if(minY < 0) {
			i13 = 0;
		}

		if(i14 > 128) {
			i14 = 128;
		}

		for(int i15 = i8; i15 <= i10; ++i15) {
			int i16 = minX - i15 * 16;
			int i17 = minX + maxX - i15 * 16;
			if(i16 < 0) {
				i16 = 0;
			}

			if(i17 > 16) {
				i17 = 16;
			}

			for(int i18 = i9; i18 <= i11; ++i18) {
				int i19 = minZ - i18 * 16;
				int i20 = minZ + maxZ - i18 * 16;
				if(i19 < 0) {
					i19 = 0;
				}

				if(i20 > 16) {
					i20 = 16;
				}

				i12 = this.getChunkFromChunkCoords(i15, i18).setChunkData(blocks, i16, i13, i19, i17, i14, i20, i12);
				this.markBlocksDirty(i15 * 16 + i16, i13, i18 * 16 + i19, i15 * 16 + i17, i14, i18 * 16 + i20);
			}
		}

	}

	public void sendQuittingDisconnectingPacket() {
	}

	public void checkSessionLock() {
		try {
			DataInputStream dataInputStream1 = new DataInputStream(new FileInputStream(new File(this.saveDirectory, "session.lock")));

			try {
				if(dataInputStream1.readLong() != this.lockTimestamp) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				dataInputStream1.close();
			}

		} catch (IOException iOException6) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public void setWorldTime(long time) {
		this.worldTime = time;
	}

	public void joinEntityInSurroundings(Entity entity) {
		int i2 = MathHelper.floor_double(entity.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
		byte b4 = 2;

		for(int i5 = i2 - b4; i5 <= i2 + b4; ++i5) {
			for(int i6 = i3 - b4; i6 <= i3 + b4; ++i6) {
				this.getChunkFromChunkCoords(i5, i6);
			}
		}

		if(!this.loadedEntityList.contains(entity)) {
			System.out.println("REINSERTING PLAYER!");
			this.loadedEntityList.add(entity);
		}

	}

	static {
		for(int i1 = 0; i1 <= 15; ++i1) {
			float f2 = 1.0F - (float)i1 / 15.0F;
			lightBrightnessTable[i1] = (1.0F - f2) / (f2 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

	}
}
