package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess {
	private MinecraftServer mcServer;

	public WorldManager(MinecraftServer minecraftServer1) {
		this.mcServer = minecraftServer1;
	}

	public void spawnParticle(String string1, double d2, double d4, double d6, double d8, double d10, double d12) {
	}

	public void obtainEntitySkin(Entity entity1) {
		this.mcServer.entityTracker.trackEntity(entity1);
	}

	public void releaseEntitySkin(Entity entity1) {
		this.mcServer.entityTracker.untrackEntity(entity1);
	}

	public void playSound(String name, double x, double y, double z, float volume, float pitch) {
	}

	public void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
	}

	public void updateAllRenderers() {
	}

	public void markBlockAndNeighborsNeedsUpdate(int i1, int i2, int i3) {
		this.mcServer.configManager.markBlockNeedsUpdate(i1, i2, i3);
	}

	public void playRecord(String name, int x, int y, int z) {
	}

	public void doNothingWithTileEntity(int x, int y, int z, TileEntity tileEntity) {
		this.mcServer.configManager.sentTileEntityToPlayer(x, y, z, tileEntity);
	}
}
