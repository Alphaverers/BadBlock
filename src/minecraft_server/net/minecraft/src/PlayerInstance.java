package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PlayerInstance {
	private List players;
	private int chunkX;
	private int chunkZ;
	private ChunkCoordIntPair currentChunk;
	private short[] blocksToUpdate;
	private int numBlocksToUpdate;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private int minZ;
	private int maxZ;
	final PlayerManager playerManager;

	public PlayerInstance(PlayerManager playerManager, int x, int z) {
		this.playerManager = playerManager;
		this.players = new ArrayList();
		this.blocksToUpdate = new short[10];
		this.numBlocksToUpdate = 0;
		this.chunkX = x;
		this.chunkZ = z;
		this.currentChunk = new ChunkCoordIntPair(x, z);
		PlayerManager.getMinecraftServer(playerManager).worldMngr.chunkProviderServer.loadChunk(x, z);
	}

	public void addPlayer(EntityPlayerMP entityPlayerMP) {
		if(this.players.contains(entityPlayerMP)) {
			throw new IllegalStateException("Failed to add player. " + entityPlayerMP + " already is in chunk " + this.chunkX + ", " + this.chunkZ);
		} else {
			entityPlayerMP.loadChunks.add(this.currentChunk);
			entityPlayerMP.playerNetServerHandler.sendPacket(new Packet50PreChunk(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos, true));
			this.players.add(entityPlayerMP);
			entityPlayerMP.loadedChunks.add(this.currentChunk);
		}
	}

	public void removePlayer(EntityPlayerMP entityPlayerMP) {
		if(!this.players.contains(entityPlayerMP)) {
			(new IllegalStateException("Failed to remove player. " + entityPlayerMP + " isn\'t in chunk " + this.chunkX + ", " + this.chunkZ)).printStackTrace();
		} else {
			this.players.remove(entityPlayerMP);
			if(this.players.size() == 0) {
				long j2 = (long)this.chunkX + 2147483647L | (long)this.chunkZ + 2147483647L << 32;
				PlayerManager.getPlayerInstances(this.playerManager).removeObject(j2);
				if(this.numBlocksToUpdate > 0) {
					PlayerManager.getPlayerInstancesToUpdate(this.playerManager).remove(this);
				}

				PlayerManager.getMinecraftServer(this.playerManager).worldMngr.chunkProviderServer.dropChunk(this.chunkX, this.chunkZ);
			}

			entityPlayerMP.loadedChunks.remove(this.currentChunk);
			if(entityPlayerMP.loadChunks.contains(this.currentChunk)) {
				entityPlayerMP.playerNetServerHandler.sendPacket(new Packet50PreChunk(this.chunkX, this.chunkZ, false));
			}

		}
	}

	public void markBlockNeedsUpdate(int x, int y, int z) {
		if(this.numBlocksToUpdate == 0) {
			PlayerManager.getPlayerInstancesToUpdate(this.playerManager).add(this);
			this.minX = this.maxX = x;
			this.minY = this.maxY = y;
			this.minZ = this.maxZ = z;
		}

		if(this.minX > x) {
			this.minX = x;
		}

		if(this.maxX < x) {
			this.maxX = x;
		}

		if(this.minY > y) {
			this.minY = y;
		}

		if(this.maxY < y) {
			this.maxY = y;
		}

		if(this.minZ > z) {
			this.minZ = z;
		}

		if(this.maxZ < z) {
			this.maxZ = z;
		}

		if(this.numBlocksToUpdate < 10) {
			short s4 = (short)(x << 12 | z << 8 | y);

			for(int i5 = 0; i5 < this.numBlocksToUpdate; ++i5) {
				if(this.blocksToUpdate[i5] == s4) {
					return;
				}
			}

			this.blocksToUpdate[this.numBlocksToUpdate++] = s4;
		}

	}

	public void sendTileEntity(Packet packet) {
		for(int i2 = 0; i2 < this.players.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.players.get(i2);
			if(entityPlayerMP3.loadChunks.contains(this.currentChunk)) {
				entityPlayerMP3.playerNetServerHandler.sendPacket(packet);
			}
		}

	}

	public void onUpdate() throws IOException {
		if(this.numBlocksToUpdate != 0) {
			int i1;
			int i2;
			int i3;
			if(this.numBlocksToUpdate == 1) {
				i1 = this.chunkX * 16 + this.minX;
				i2 = this.minY;
				i3 = this.chunkZ * 16 + this.minZ;
				this.sendTileEntity(new Packet53BlockChange(i1, i2, i3, PlayerManager.getMinecraftServer(this.playerManager).worldMngr));
				if(Block.isBlockContainer[PlayerManager.getMinecraftServer(this.playerManager).worldMngr.getBlockId(i1, i2, i3)]) {
					this.sendTileEntity(new Packet59ComplexEntity(i1, i2, i3, PlayerManager.getMinecraftServer(this.playerManager).worldMngr.getBlockTileEntity(i1, i2, i3)));
				}
			} else {
				int i4;
				if(this.numBlocksToUpdate == 10) {
					this.minY = this.minY / 2 * 2;
					this.maxY = (this.maxY / 2 + 1) * 2;
					i1 = this.minX + this.chunkX * 16;
					i2 = this.minY;
					i3 = this.minZ + this.chunkZ * 16;
					i4 = this.maxX - this.minX + 1;
					int i5 = this.maxY - this.minY + 2;
					int i6 = this.maxZ - this.minZ + 1;
					this.sendTileEntity(new Packet51MapChunk(i1, i2, i3, i4, i5, i6, PlayerManager.getMinecraftServer(this.playerManager).worldMngr));
					List list7 = PlayerManager.getMinecraftServer(this.playerManager).worldMngr.getTileEntityList(i1, i2, i3, i1 + i4, i2 + i5, i3 + i6);

					for(int i8 = 0; i8 < list7.size(); ++i8) {
						TileEntity tileEntity9 = (TileEntity)list7.get(i8);
						this.sendTileEntity(new Packet59ComplexEntity(tileEntity9.xCoord, tileEntity9.yCoord, tileEntity9.zCoord, tileEntity9));
					}
				} else {
					this.sendTileEntity(new Packet52MultiBlockChange(this.chunkX, this.chunkZ, this.blocksToUpdate, this.numBlocksToUpdate, PlayerManager.getMinecraftServer(this.playerManager).worldMngr));

					for(i1 = 0; i1 < this.numBlocksToUpdate; ++i1) {
						i2 = this.chunkX * 16 + (this.numBlocksToUpdate >> 12 & 15);
						i3 = this.numBlocksToUpdate & 255;
						i4 = this.chunkZ * 16 + (this.numBlocksToUpdate >> 8 & 15);
						if(Block.isBlockContainer[PlayerManager.getMinecraftServer(this.playerManager).worldMngr.getBlockId(i2, i3, i4)]) {
							this.sendTileEntity(new Packet59ComplexEntity(i2, i3, i4, PlayerManager.getMinecraftServer(this.playerManager).worldMngr.getBlockTileEntity(i2, i3, i4)));
						}
					}
				}
			}

			this.numBlocksToUpdate = 0;
		}
	}
}
