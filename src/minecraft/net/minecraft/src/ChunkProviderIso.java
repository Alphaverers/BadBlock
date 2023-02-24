package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderIso implements IChunkProvider {
	private Chunk[] chunks = new Chunk[256];
	private World worldObj;
	private IChunkLoader chunkLoader;
	byte[] blocks = new byte[32768];

	public ChunkProviderIso(World worldObj, IChunkLoader chunkLoader) {
		this.worldObj = worldObj;
		this.chunkLoader = chunkLoader;
	}

	public boolean chunkExists(int i1, int i2) {
		int i3 = i1 & 15 | (i2 & 15) * 16;
		return this.chunks[i3] != null && this.chunks[i3].isAtLocation(i1, i2);
	}

	public Chunk provideChunk(int i1, int i2) {
		int i3 = i1 & 15 | (i2 & 15) * 16;

		try {
			if(!this.chunkExists(i1, i2)) {
				Chunk chunk4 = this.getChunkAt(i1, i2);
				if(chunk4 == null) {
					chunk4 = new Chunk(this.worldObj, this.blocks, i1, i2);
					chunk4.isChunkRendered = true;
					chunk4.neverSave = true;
				}

				this.chunks[i3] = chunk4;
			}

			return this.chunks[i3];
		} catch (Exception exception5) {
			exception5.printStackTrace();
			return null;
		}
	}

	private synchronized Chunk getChunkAt(int chunkX, int chunkZ) {
		try {
			return this.chunkLoader.loadChunk(this.worldObj, chunkX, chunkZ);
		} catch (IOException iOException4) {
			iOException4.printStackTrace();
			return null;
		}
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}
}
