package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class ChunkLoader implements IChunkLoader {
	private File saveDir;
	private boolean createIfNecessary;

	public ChunkLoader(File saveDir, boolean createIfNecessary) {
		this.saveDir = saveDir;
		this.createIfNecessary = createIfNecessary;
	}

	private File chunkFileForXZ(int x, int z) {
		String string3 = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
		String string4 = Integer.toString(x & 63, 36);
		String string5 = Integer.toString(z & 63, 36);
		File file6 = new File(this.saveDir, string4);
		if(!file6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			file6.mkdir();
		}

		file6 = new File(file6, string5);
		if(!file6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			file6.mkdir();
		}

		file6 = new File(file6, string3);
		return !file6.exists() && !this.createIfNecessary ? null : file6;
	}

	public Chunk loadChunk(World world, int x, int z) {
		File file4 = this.chunkFileForXZ(x, z);
		if(file4 != null && file4.exists()) {
			try {
				FileInputStream fileInputStream5 = new FileInputStream(file4);
				NBTTagCompound nBTTagCompound6 = CompressedStreamTools.readCompressed(fileInputStream5);
				if(!nBTTagCompound6.hasKey("Level")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
					return null;
				}

				if(!nBTTagCompound6.getCompoundTag("Level").hasKey("Blocks")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
					return null;
				}

				Chunk chunk7 = loadChunkIntoWorldFromCompound(world, nBTTagCompound6.getCompoundTag("Level"));
				if(!chunk7.isAtLocation(x, z)) {
					System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk7.xPosition + ", " + chunk7.zPosition + ")");
					nBTTagCompound6.setInteger("xPos", x);
					nBTTagCompound6.setInteger("zPos", z);
					chunk7 = loadChunkIntoWorldFromCompound(world, nBTTagCompound6.getCompoundTag("Level"));
				}

				return chunk7;
			} catch (Exception exception8) {
				exception8.printStackTrace();
			}
		}

		return null;
	}

	public void saveChunk(World world1, Chunk chunk2) {
		world1.checkSessionLock();
		File file3 = this.chunkFileForXZ(chunk2.xPosition, chunk2.zPosition);
		if(file3.exists()) {
			world1.sizeOnDisk -= file3.length();
		}

		try {
			File file4 = new File(this.saveDir, "tmp_chunk.dat");
			FileOutputStream fileOutputStream5 = new FileOutputStream(file4);
			NBTTagCompound nBTTagCompound6 = new NBTTagCompound();
			NBTTagCompound nBTTagCompound7 = new NBTTagCompound();
			nBTTagCompound6.setTag("Level", nBTTagCompound7);
			this.storeChunkInCompound(chunk2, world1, nBTTagCompound7);
			CompressedStreamTools.writeCompressed(nBTTagCompound6, fileOutputStream5);
			fileOutputStream5.close();
			if(file3.exists()) {
				file3.delete();
			}

			file4.renameTo(file3);
			world1.sizeOnDisk += file3.length();
		} catch (Exception exception8) {
			exception8.printStackTrace();
		}

	}

	public void storeChunkInCompound(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
		world.checkSessionLock();
		nbttagcompound.setInteger("xPos", chunk.xPosition);
		nbttagcompound.setInteger("zPos", chunk.zPosition);
		nbttagcompound.setLong("LastUpdate", world.worldTime);
		nbttagcompound.setByteArray("Blocks", chunk.blocks);
		nbttagcompound.setByteArray("Data", chunk.data.data);
		nbttagcompound.setByteArray("SkyLight", chunk.skylightMap.data);
		nbttagcompound.setByteArray("BlockLight", chunk.blocklightMap.data);
		nbttagcompound.setByteArray("HeightMap", chunk.heightMap);
		nbttagcompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated);
		chunk.hasEntities = false;
		NBTTagList nBTTagList4 = new NBTTagList();

		Iterator iterator6;
		NBTTagCompound nBTTagCompound8;
		for(int i5 = 0; i5 < chunk.entities.length; ++i5) {
			iterator6 = chunk.entities[i5].iterator();

			while(iterator6.hasNext()) {
				Entity entity7 = (Entity)iterator6.next();
				chunk.hasEntities = true;
				nBTTagCompound8 = new NBTTagCompound();
				if(entity7.addEntityID(nBTTagCompound8)) {
					nBTTagList4.setTag(nBTTagCompound8);
				}
			}
		}

		nbttagcompound.setTag("Entities", nBTTagList4);
		NBTTagList nBTTagList9 = new NBTTagList();
		iterator6 = chunk.chunkTileEntityMap.values().iterator();

		while(iterator6.hasNext()) {
			TileEntity tileEntity10 = (TileEntity)iterator6.next();
			nBTTagCompound8 = new NBTTagCompound();
			tileEntity10.writeToNBT(nBTTagCompound8);
			nBTTagList9.setTag(nBTTagCompound8);
		}

		nbttagcompound.setTag("TileEntities", nBTTagList9);
	}

	public static Chunk loadChunkIntoWorldFromCompound(World world, NBTTagCompound nbttagcompound) {
		int i2 = nbttagcompound.getInteger("xPos");
		int i3 = nbttagcompound.getInteger("zPos");
		Chunk chunk4 = new Chunk(world, i2, i3);
		chunk4.blocks = nbttagcompound.getByteArray("Blocks");
		chunk4.data = new NibbleArray(nbttagcompound.getByteArray("Data"));
		chunk4.skylightMap = new NibbleArray(nbttagcompound.getByteArray("SkyLight"));
		chunk4.blocklightMap = new NibbleArray(nbttagcompound.getByteArray("BlockLight"));
		chunk4.heightMap = nbttagcompound.getByteArray("HeightMap");
		chunk4.isTerrainPopulated = nbttagcompound.getBoolean("TerrainPopulated");
		if(!chunk4.data.isValid()) {
			chunk4.data = new NibbleArray(chunk4.blocks.length);
		}

		if(chunk4.heightMap == null || !chunk4.skylightMap.isValid()) {
			chunk4.heightMap = new byte[256];
			chunk4.skylightMap = new NibbleArray(chunk4.blocks.length);
			chunk4.generateHeightMap();
		}

		if(!chunk4.blocklightMap.isValid()) {
			chunk4.blocklightMap = new NibbleArray(chunk4.blocks.length);
			chunk4.doNothing();
		}

		NBTTagList nBTTagList5 = nbttagcompound.getTagList("Entities");
		if(nBTTagList5 != null) {
			for(int i6 = 0; i6 < nBTTagList5.tagCount(); ++i6) {
				NBTTagCompound nBTTagCompound7 = (NBTTagCompound)nBTTagList5.entities(i6);
				Entity entity8 = EntityList.createEntityFromNBT(nBTTagCompound7, world);
				chunk4.hasEntities = true;
				if(entity8 != null) {
					chunk4.removeEntityAtIndex(entity8);
				}
			}
		}

		NBTTagList nBTTagList10 = nbttagcompound.getTagList("TileEntities");
		if(nBTTagList10 != null) {
			for(int i11 = 0; i11 < nBTTagList10.tagCount(); ++i11) {
				NBTTagCompound nBTTagCompound12 = (NBTTagCompound)nBTTagList10.entities(i11);
				TileEntity tileEntity9 = TileEntity.createAndLoadEntity(nBTTagCompound12);
				if(tileEntity9 != null) {
					chunk4.addTileEntity(tileEntity9);
				}
			}
		}

		return chunk4;
	}

	public void chunkTick() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World world1, Chunk chunk2) {
	}
}
