package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntityTrackerEntry {
	public Entity trackedEntity;
	public int trackingDistanceThreshold;
	public int updateFrequency;
	public int encodedPosX;
	public int encodedPosY;
	public int encodedPosZ;
	public int encodedRotationYaw;
	public int encodedRotationPitch;
	public int updateCounter = 0;
	private double lastTrackedEntityPosX;
	private double lastTrackedEntityPosY;
	private double lastTrackedEntityPosZ;
	private boolean firstUpdateDone = false;
	public boolean playerEntitiesUpdated = false;
	public Set trackedPlayers = new HashSet();

	public EntityTrackerEntry(Entity entity1, int i2, int i3) {
		this.trackedEntity = entity1;
		this.trackingDistanceThreshold = i2;
		this.updateFrequency = i3;
		this.encodedPosX = MathHelper.floor_double(entity1.posX * 32.0D);
		this.encodedPosY = MathHelper.floor_double(entity1.posY * 32.0D);
		this.encodedPosZ = MathHelper.floor_double(entity1.posZ * 32.0D);
		this.encodedRotationYaw = MathHelper.floor_float(entity1.rotationYaw * 256.0F / 360.0F);
		this.encodedRotationPitch = MathHelper.floor_float(entity1.rotationPitch * 256.0F / 360.0F);
	}

	public boolean equals(Object object1) {
		return object1 instanceof EntityTrackerEntry ? ((EntityTrackerEntry)object1).trackedEntity.entityID == this.trackedEntity.entityID : false;
	}

	public int hashCode() {
		return this.trackedEntity.entityID;
	}

	public void updatePlayerList(List list1) {
		this.playerEntitiesUpdated = false;
		if(!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D) {
			this.updatePlayerEntities(list1);
			this.lastTrackedEntityPosX = this.trackedEntity.posX;
			this.lastTrackedEntityPosY = this.trackedEntity.posY;
			this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
			this.firstUpdateDone = true;
			this.playerEntitiesUpdated = true;
		}

		if(this.updateCounter++ % this.updateFrequency == 0) {
			int i2 = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
			int i3 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
			int i4 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
			int i5 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
			int i6 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
			boolean z7 = i2 != this.encodedPosX || i3 != this.encodedPosY || i4 != this.encodedPosZ;
			boolean z8 = i5 != this.encodedRotationYaw || i6 != this.encodedRotationPitch;
			int i9 = i2 - this.encodedPosX;
			int i10 = i3 - this.encodedPosY;
			int i11 = i4 - this.encodedPosZ;
			Object object12 = null;
			if(i9 >= -128 && i9 < 128 && i10 >= -128 && i10 < 128 && i11 >= -128 && i11 < 128) {
				if(z7 && z8) {
					object12 = new Packet33RelEntityMoveLook(this.trackedEntity.entityID, (byte)i9, (byte)i10, (byte)i11, (byte)i5, (byte)i6);
				} else if(z7) {
					object12 = new Packet31RelEntityMove(this.trackedEntity.entityID, (byte)i9, (byte)i10, (byte)i11);
				} else if(z8) {
					object12 = new Packet32EntityLook(this.trackedEntity.entityID, (byte)i5, (byte)i6);
				} else {
					object12 = new Packet30Entity(this.trackedEntity.entityID);
				}
			} else {
				object12 = new Packet34EntityTeleport(this.trackedEntity.entityID, i2, i3, i4, (byte)i5, (byte)i6);
			}

			if(object12 != null) {
				this.sendPacketToTrackedPlayers((Packet)object12);
			}

			this.encodedPosX = i2;
			this.encodedPosY = i3;
			this.encodedPosZ = i4;
			this.encodedRotationYaw = i5;
			this.encodedRotationPitch = i6;
		}

	}

	public void sendPacketToTrackedPlayers(Packet packet1) {
		Iterator iterator2 = this.trackedPlayers.iterator();

		while(iterator2.hasNext()) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)iterator2.next();
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet1);
		}

	}

	public void removeFromTrackedPlayers() {
		this.sendPacketToTrackedPlayers(new Packet29DestroyEntity(this.trackedEntity.entityID));
	}

	public void updatePlayerEntity(EntityPlayerMP entityPlayerMP1) {
		if(entityPlayerMP1 != this.trackedEntity) {
			double d2 = entityPlayerMP1.posX - (double)(this.encodedPosX / 32);
			double d4 = entityPlayerMP1.posZ - (double)(this.encodedPosZ / 32);
			if(d2 >= (double)(-this.trackingDistanceThreshold) && d2 <= (double)this.trackingDistanceThreshold && d4 >= (double)(-this.trackingDistanceThreshold) && d4 <= (double)this.trackingDistanceThreshold) {
				if(!this.trackedPlayers.contains(entityPlayerMP1)) {
					this.trackedPlayers.add(entityPlayerMP1);
					entityPlayerMP1.playerNetServerHandler.sendPacket(this.getSpawnPacket());
				}
			} else if(this.trackedPlayers.contains(entityPlayerMP1)) {
				this.trackedPlayers.remove(entityPlayerMP1);
				entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(this.trackedEntity.entityID));
			}

		}
	}

	public void updatePlayerEntities(List list1) {
		for(int i2 = 0; i2 < list1.size(); ++i2) {
			this.updatePlayerEntity((EntityPlayerMP)list1.get(i2));
		}

	}

	private Packet getSpawnPacket() {
		if(this.trackedEntity instanceof EntityItem) {
			EntityItem entityItem3 = (EntityItem)this.trackedEntity;
			Packet21PickupSpawn packet21PickupSpawn2 = new Packet21PickupSpawn(entityItem3);
			entityItem3.posX = (double)packet21PickupSpawn2.xPosition / 32.0D;
			entityItem3.posY = (double)packet21PickupSpawn2.yPosition / 32.0D;
			entityItem3.posZ = (double)packet21PickupSpawn2.zPosition / 32.0D;
			entityItem3.motionX = (double)packet21PickupSpawn2.rotation / 128.0D;
			entityItem3.motionY = (double)packet21PickupSpawn2.pitch / 128.0D;
			entityItem3.motionZ = (double)packet21PickupSpawn2.roll / 128.0D;
			return packet21PickupSpawn2;
		} else if(this.trackedEntity instanceof EntityPlayerMP) {
			return new Packet20NamedEntitySpawn((EntityPlayer)this.trackedEntity);
		} else {
			if(this.trackedEntity instanceof EntityMinecart) {
				EntityMinecart entityMinecart1 = (EntityMinecart)this.trackedEntity;
				if(entityMinecart1.minecartType == 0) {
					return new Packet23VehicleSpawn(this.trackedEntity, 10);
				}

				if(entityMinecart1.minecartType == 1) {
					return new Packet23VehicleSpawn(this.trackedEntity, 11);
				}

				if(entityMinecart1.minecartType == 2) {
					return new Packet23VehicleSpawn(this.trackedEntity, 12);
				}
			}

			if(this.trackedEntity instanceof EntityBoat) {
				return new Packet23VehicleSpawn(this.trackedEntity, 1);
			} else if(this.trackedEntity instanceof IAnimals) {
				return new Packet24MobSpawn((EntityLiving)this.trackedEntity);
			} else {
				throw new IllegalArgumentException("Don\'t know how to add " + this.trackedEntity.getClass() + "!");
			}
		}
	}
}
