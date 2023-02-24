package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.server.MinecraftServer;

public class EntityTracker {
	private Set trackedEntitySet = new HashSet();
	private MCHashTable trackedEntityHashTable = new MCHashTable();
	private MinecraftServer mcServer;
	private int maxTrackingDistanceThreshold;

	public EntityTracker(MinecraftServer minecraftServer) {
		this.mcServer = minecraftServer;
		this.maxTrackingDistanceThreshold = minecraftServer.configManager.getMaxTrackingDistance();
	}

	public void trackEntity(Entity entity1) {
		if(entity1 instanceof EntityPlayerMP) {
			this.trackEntity(entity1, 512, 2);
			EntityPlayerMP entityPlayerMP2 = (EntityPlayerMP)entity1;
			Iterator iterator3 = this.trackedEntitySet.iterator();

			while(iterator3.hasNext()) {
				EntityTrackerEntry entityTrackerEntry4 = (EntityTrackerEntry)iterator3.next();
				if(entityTrackerEntry4.trackedEntity != entityPlayerMP2) {
					entityTrackerEntry4.updatePlayerEntity(entityPlayerMP2);
				}
			}
		} else if(entity1 instanceof EntityItem) {
			this.trackEntity(entity1, 64, 20);
		} else if(entity1 instanceof EntityMinecart) {
			this.trackEntity(entity1, 160, 4);
		} else if(entity1 instanceof IAnimals) {
			this.trackEntity(entity1, 160, 2);
		}

	}

	public void trackEntity(Entity entity1, int i2, int i3) {
		if(i2 > this.maxTrackingDistanceThreshold) {
			i2 = this.maxTrackingDistanceThreshold;
		}

		if(this.trackedEntityHashTable.containsItem(entity1.entityID)) {
			throw new IllegalStateException("Entity is already tracked!");
		} else {
			EntityTrackerEntry entityTrackerEntry4 = new EntityTrackerEntry(entity1, i2, i3);
			this.trackedEntitySet.add(entityTrackerEntry4);
			this.trackedEntityHashTable.addKey(entity1.entityID, entityTrackerEntry4);
			entityTrackerEntry4.updatePlayerEntities(this.mcServer.worldMngr.playerEntities);
		}
	}

	public void untrackEntity(Entity entity1) {
		EntityTrackerEntry entityTrackerEntry2 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(entity1.entityID);
		if(entityTrackerEntry2 != null) {
			this.trackedEntitySet.remove(entityTrackerEntry2);
			entityTrackerEntry2.removeFromTrackedPlayers();
		}

	}

	public void updateTrackedEntities() {
		ArrayList arrayList1 = new ArrayList();
		Iterator iterator2 = this.trackedEntitySet.iterator();

		while(iterator2.hasNext()) {
			EntityTrackerEntry entityTrackerEntry3 = (EntityTrackerEntry)iterator2.next();
			entityTrackerEntry3.updatePlayerList(this.mcServer.worldMngr.playerEntities);
			if(entityTrackerEntry3.playerEntitiesUpdated && entityTrackerEntry3.trackedEntity instanceof EntityPlayerMP) {
				arrayList1.add((EntityPlayerMP)entityTrackerEntry3.trackedEntity);
			}
		}

		for(int i6 = 0; i6 < arrayList1.size(); ++i6) {
			EntityPlayerMP entityPlayerMP7 = (EntityPlayerMP)arrayList1.get(i6);
			Iterator iterator4 = this.trackedEntitySet.iterator();

			while(iterator4.hasNext()) {
				EntityTrackerEntry entityTrackerEntry5 = (EntityTrackerEntry)iterator4.next();
				if(entityTrackerEntry5.trackedEntity != entityPlayerMP7) {
					entityTrackerEntry5.updatePlayerEntity(entityPlayerMP7);
				}
			}
		}

	}

	public void sendPacketToTrackedPlayers(Entity entity1, Packet packet2) {
		EntityTrackerEntry entityTrackerEntry3 = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entity1.entityID);
		if(entityTrackerEntry3 != null) {
			entityTrackerEntry3.sendPacketToTrackedPlayers(packet2);
		}

	}
}
