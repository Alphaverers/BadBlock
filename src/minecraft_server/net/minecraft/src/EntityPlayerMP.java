package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer {
	public NetServerHandler playerNetServerHandler;
	public MinecraftServer mcServer;
	public ItemInWorldManager theItemInWorldManager;
	public double managedPosX;
	public double managedPosZ;
	public List loadedChunks = new LinkedList();
	public Set loadChunks = new HashSet();
	public double managedPosY;

	public EntityPlayerMP(MinecraftServer minecraftServer, World world, String username, ItemInWorldManager itemManager) {
		super(world);
		int i5 = world.spawnX + this.rand.nextInt(20) - 10;
		int i6 = world.spawnZ + this.rand.nextInt(20) - 10;
		int i7 = world.getTopSolidOrLiquidBlock(i5, i6);
		this.setLocationAndAngles((double)i5 + 0.5D, (double)i7, (double)i6 + 0.5D, 0.0F, 0.0F);
		this.mcServer = minecraftServer;
		this.stepHeight = 0.0F;
		itemManager.thisPlayer = this;
		this.username = username;
		this.theItemInWorldManager = itemManager;
		this.yOffset = 0.0F;
	}

	public void onUpdate() {
	}

	public void onDeath(Entity entity1) {
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		return false;
	}

	public void heal(int health) {
	}

	public void onUpdateEntity() {
		super.onUpdate();
		ChunkCoordIntPair chunkCoordIntPair1 = null;
		double d2 = 0.0D;

		for(int i4 = 0; i4 < this.loadedChunks.size(); ++i4) {
			ChunkCoordIntPair chunkCoordIntPair5 = (ChunkCoordIntPair)this.loadedChunks.get(i4);
			double d6 = chunkCoordIntPair5.a(this);
			if(i4 == 0 || d6 < d2) {
				chunkCoordIntPair1 = chunkCoordIntPair5;
				d2 = chunkCoordIntPair5.a(this);
			}
		}

		if(chunkCoordIntPair1 != null) {
			boolean z8 = false;
			if(d2 < 1024.0D) {
				z8 = true;
			}

			if(this.playerNetServerHandler.getNumChunkDataPackets() < 2) {
				z8 = true;
			}

			if(z8) {
				this.loadedChunks.remove(chunkCoordIntPair1);
				this.playerNetServerHandler.sendPacket(new Packet51MapChunk(chunkCoordIntPair1.chunkXPos * 16, 0, chunkCoordIntPair1.chunkZPos * 16, 16, 128, 16, this.mcServer.worldMngr));
				List list9 = this.mcServer.worldMngr.getTileEntityList(chunkCoordIntPair1.chunkXPos * 16, 0, chunkCoordIntPair1.chunkZPos * 16, chunkCoordIntPair1.chunkXPos * 16 + 16, 128, chunkCoordIntPair1.chunkZPos * 16 + 16);

				for(int i10 = 0; i10 < list9.size(); ++i10) {
					TileEntity tileEntity7 = (TileEntity)list9.get(i10);
					this.playerNetServerHandler.sendPacket(new Packet59ComplexEntity(tileEntity7.xCoord, tileEntity7.yCoord, tileEntity7.zCoord, tileEntity7));
				}
			}
		}

	}

	public void onLivingUpdate() {
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.isJumping = false;
		super.onLivingUpdate();
	}

	public void onItemPickup(Entity entity1, int i2) {
		if(!entity1.isDead && entity1 instanceof EntityItem) {
			this.playerNetServerHandler.sendPacket(new Packet17AddToInventory(((EntityItem)entity1).item, i2));
			this.mcServer.entityTracker.sendPacketToTrackedPlayers(entity1, new Packet22Collect(entity1.entityID, this.entityID));
		}

		super.onItemPickup(entity1, i2);
	}

	public void swingItem() {
		if(!this.isSwinging) {
			this.swingProgressInt = -1;
			this.isSwinging = true;
			this.mcServer.entityTracker.sendPacketToTrackedPlayers(this, new Packet18ArmAnimation(this, 1));
		}

	}

	protected float getEyeHeight() {
		return 1.62F;
	}
}
