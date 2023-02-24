package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;

public class PlayerManager {
	private List players = new ArrayList();
	private MCHashTable2 playerInstances = new MCHashTable2();
	private List playerInstancesToUpdate = new ArrayList();
	private MinecraftServer mcServer;

	public PlayerManager(MinecraftServer mcServer) {
		this.mcServer = mcServer;
	}

	public void updatePlayerInstances() throws IOException {
		for(int i1 = 0; i1 < this.playerInstancesToUpdate.size(); ++i1) {
			((PlayerInstance)this.playerInstancesToUpdate.get(i1)).onUpdate();
		}

		this.playerInstancesToUpdate.clear();
	}

	private PlayerInstance getPlayerInstance(int i1, int i2, boolean z3) {
		long j4 = (long)i1 + 2147483647L | (long)i2 + 2147483647L << 32;
		PlayerInstance playerInstance6 = (PlayerInstance)this.playerInstances.lookup(j4);
		if(playerInstance6 == null && z3) {
			playerInstance6 = new PlayerInstance(this, i1, i2);
			this.playerInstances.addKey(j4, playerInstance6);
		}

		return playerInstance6;
	}

	public void sendTileEntity(Packet packet, int x, int y, int z) {
		int i5 = x >> 4;
		int i6 = z >> 4;
		PlayerInstance playerInstance7 = this.getPlayerInstance(i5, i6, false);
		if(playerInstance7 != null) {
			playerInstance7.sendTileEntity(packet);
		}

	}

	public void markBlockNeedsUpdate(int x, int y, int z) {
		int i4 = x >> 4;
		int i5 = z >> 4;
		PlayerInstance playerInstance6 = this.getPlayerInstance(i4, i5, false);
		if(playerInstance6 != null) {
			playerInstance6.markBlockNeedsUpdate(x & 15, y, z & 15);
		}

	}

	public void addPlayer(EntityPlayerMP entityPlayerMP) {
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + entityPlayerMP.username + " joined the game."));
		int i2 = (int)entityPlayerMP.posX >> 4;
		int i3 = (int)entityPlayerMP.posZ >> 4;
		entityPlayerMP.managedPosX = entityPlayerMP.posX;
		entityPlayerMP.managedPosZ = entityPlayerMP.posZ;

		for(int i4 = i2 - 10; i4 <= i2 + 10; ++i4) {
			for(int i5 = i3 - 10; i5 <= i3 + 10; ++i5) {
				this.getPlayerInstance(i4, i5, true).addPlayer(entityPlayerMP);
			}
		}

		this.players.add(entityPlayerMP);
	}

	public void removePlayer(EntityPlayerMP entityPlayerMP) {
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + entityPlayerMP.username + " left the game."));
		int i2 = (int)entityPlayerMP.posX >> 4;
		int i3 = (int)entityPlayerMP.posZ >> 4;

		for(int i4 = i2 - 10; i4 <= i2 + 10; ++i4) {
			for(int i5 = i3 - 10; i5 <= i3 + 10; ++i5) {
				PlayerInstance playerInstance6 = this.getPlayerInstance(i4, i5, false);
				if(playerInstance6 != null) {
					playerInstance6.removePlayer(entityPlayerMP);
				}
			}
		}

		this.players.remove(entityPlayerMP);
	}

	private boolean a(int i1, int i2, int i3, int i4) {
		int i5 = i1 - i3;
		int i6 = i2 - i4;
		return i5 >= -10 && i5 <= 10 ? i6 >= -10 && i6 <= 10 : false;
	}

	public void updateMountedMovingPlayer(EntityPlayerMP entityPlayerMP) {
		int i2 = (int)entityPlayerMP.posX >> 4;
		int i3 = (int)entityPlayerMP.posZ >> 4;
		double d4 = entityPlayerMP.managedPosX - entityPlayerMP.posX;
		double d6 = entityPlayerMP.managedPosZ - entityPlayerMP.posZ;
		double d8 = d4 * d4 + d6 * d6;
		if(d8 >= 64.0D) {
			int i10 = (int)entityPlayerMP.managedPosX >> 4;
			int i11 = (int)entityPlayerMP.managedPosZ >> 4;
			int i12 = i2 - i10;
			int i13 = i3 - i11;
			if(i12 != 0 || i13 != 0) {
				for(int i14 = i2 - 10; i14 <= i2 + 10; ++i14) {
					for(int i15 = i3 - 10; i15 <= i3 + 10; ++i15) {
						if(!this.a(i14, i15, i10, i11)) {
							this.getPlayerInstance(i14, i15, true).addPlayer(entityPlayerMP);
						}

						if(!this.a(i14 - i12, i15 - i13, i2, i3)) {
							PlayerInstance playerInstance16 = this.getPlayerInstance(i14 - i12, i15 - i13, false);
							if(playerInstance16 != null) {
								playerInstance16.removePlayer(entityPlayerMP);
							}
						}
					}
				}

				entityPlayerMP.managedPosX = entityPlayerMP.posX;
				entityPlayerMP.managedPosZ = entityPlayerMP.posZ;
			}
		}
	}

	public int getMaxTrackingDistance() {
		return 144;
	}

	static MinecraftServer getMinecraftServer(PlayerManager playerManager) {
		return playerManager.mcServer;
	}

	static MCHashTable2 getPlayerInstances(PlayerManager playerManager) {
		return playerManager.playerInstances;
	}

	static List getPlayerInstancesToUpdate(PlayerManager playerManager) {
		return playerManager.playerInstancesToUpdate;
	}
}
