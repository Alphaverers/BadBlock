package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChunkProviderServer implements IChunkProvider {
	private Set droppedChunksSet = new HashSet();
	private Chunk chunk;
	private IChunkProvider serverChunkProvider;
	private IChunkLoader serverChunkLoader;
	private Map id2ChunkMap = new HashMap();
	private List loadedChunks = new ArrayList();
	private WorldServer worldObj;

	public ChunkProviderServer(WorldServer worldServer, IChunkLoader chunkLoader, IChunkProvider chunkProvider) {
		this.chunk = new Chunk(worldServer, new byte[32768], 0, 0);
		this.chunk.isChunkRendered = true;
		this.chunk.neverSave = true;
		this.worldObj = worldServer;
		this.serverChunkLoader = chunkLoader;
		this.serverChunkProvider = chunkProvider;
	}

	public boolean chunkExists(int x, int z) {
		ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(x, z);
		return this.id2ChunkMap.containsKey(chunkCoordinates3);
	}

	public void dropChunk(int i1, int i2) {
		int i3 = i1 * 16 + 8 - this.worldObj.spawnX;
		int i4 = i2 * 16 + 8 - this.worldObj.spawnZ;
		byte b5 = 20;
		if(i3 < -b5 || i3 > b5 || i4 < -b5 || i4 > b5) {
			this.droppedChunksSet.add(new ChunkCoordinates(i1, i2));
		}

	}

	public Chunk loadChunk(int x, int z) {
		ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(x, z);
		this.droppedChunksSet.remove(new ChunkCoordinates(x, z));
		Chunk chunk4 = (Chunk)this.id2ChunkMap.get(chunkCoordinates3);
		if(chunk4 == null) {
			chunk4 = this.loadAndSaveChunk(x, z);
			if(chunk4 == null) {
				if(this.serverChunkProvider == null) {
					chunk4 = this.chunk;
				} else {
					chunk4 = this.serverChunkProvider.provideChunk(x, z);
				}
			}

			this.id2ChunkMap.put(chunkCoordinates3, chunk4);
			this.loadedChunks.add(chunk4);
			if(chunk4 != null) {
				chunk4.onChunkLoad();
			}

			if(!chunk4.isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
				this.populate(this, x, z);
			}

			if(this.chunkExists(x - 1, z) && !this.provideChunk(x - 1, z).isTerrainPopulated && this.chunkExists(x - 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x - 1, z)) {
				this.populate(this, x - 1, z);
			}

			if(this.chunkExists(x, z - 1) && !this.provideChunk(x, z - 1).isTerrainPopulated && this.chunkExists(x + 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x + 1, z)) {
				this.populate(this, x, z - 1);
			}

			if(this.chunkExists(x - 1, z - 1) && !this.provideChunk(x - 1, z - 1).isTerrainPopulated && this.chunkExists(x - 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x - 1, z)) {
				this.populate(this, x - 1, z - 1);
			}
		}

		return chunk4;
	}

	public Chunk provideChunk(int i1, int i2) {
		ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(i1, i2);
		Chunk chunk4 = (Chunk)this.id2ChunkMap.get(chunkCoordinates3);
		return chunk4 == null ? (this.worldObj.worldChunkLoadOverride ? this.loadChunk(i1, i2) : this.chunk) : chunk4;
	}

	private Chunk loadAndSaveChunk(int x, int z) {
		if(this.serverChunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk3 = this.serverChunkLoader.loadChunk(this.worldObj, x, z);
				if(chunk3 != null) {
					chunk3.lastSaveTime = this.worldObj.worldTime;
				}

				return chunk3;
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return null;
			}
		}
	}

	private void saveExtraChunkData(Chunk chunk) {
		if(this.serverChunkLoader != null) {
			try {
				this.serverChunkLoader.saveExtraChunkData(this.worldObj, chunk);
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}

		}
	}

	private void saveChunk(Chunk chunk) {
		if(this.serverChunkLoader != null) {
			try {
				chunk.lastSaveTime = this.worldObj.worldTime;
				this.serverChunkLoader.saveChunk(this.worldObj, chunk);
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4 = this.provideChunk(i2, i3);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.serverChunkProvider != null) {
				this.serverChunkProvider.populate(iChunkProvider1, i2, i3);
				chunk4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean flag, IProgressUpdate progressUpdate) {
		int i3 = 0;

		for(int i4 = 0; i4 < this.loadedChunks.size(); ++i4) {
			Chunk chunk5 = (Chunk)this.loadedChunks.get(i4);
			if(flag && !chunk5.neverSave) {
				this.saveExtraChunkData(chunk5);
			}

			if(chunk5.needsSaving(flag)) {
				this.saveChunk(chunk5);
				chunk5.isModified = false;
				++i3;
				if(i3 == 2 && !flag) {
					return false;
				}
			}
		}

		if(flag) {
			if(this.serverChunkLoader == null) {
				return true;
			}

			this.serverChunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		if(!this.worldObj.levelSaving) {
			for(int i1 = 0; i1 < 16; ++i1) {
				if(!this.droppedChunksSet.isEmpty()) {
					ChunkCoordinates chunkCoordinates2 = (ChunkCoordinates)this.droppedChunksSet.iterator().next();
					Chunk chunk3 = this.provideChunk(chunkCoordinates2.posX, chunkCoordinates2.posZ);
					chunk3.onChunkUnload();
					this.saveChunk(chunk3);
					this.saveExtraChunkData(chunk3);
					this.droppedChunksSet.remove(chunkCoordinates2);
					this.id2ChunkMap.remove(chunkCoordinates2);
					this.loadedChunks.remove(chunk3);
				}
			}

			if(this.serverChunkLoader != null) {
				this.serverChunkLoader.chunkTick();
			}
		}

		return this.serverChunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return !this.worldObj.levelSaving;
	}
}
