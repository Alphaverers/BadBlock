package net.minecraft.src;

import java.util.Comparator;

public class EntitySorter implements Comparator {
	private Entity comparedEntity;

	public EntitySorter(Entity entity1) {
		this.comparedEntity = entity1;
	}

	public int sortByDistanceToEntity(WorldRenderer worldRenderer1, WorldRenderer worldRenderer2) {
		return worldRenderer1.distanceToEntitySquared(this.comparedEntity) < worldRenderer2.distanceToEntitySquared(this.comparedEntity) ? -1 : 1;
	}

	public int compare(Object object1, Object object2) {
		return this.sortByDistanceToEntity((WorldRenderer)object1, (WorldRenderer)object2);
	}
}
