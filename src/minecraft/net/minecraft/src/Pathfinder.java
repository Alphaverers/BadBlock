package net.minecraft.src;

public class Pathfinder {
	private IBlockAccess worldMap;
	private Path path = new Path();
	private MCHashTable pointMap = new MCHashTable();
	private PathPoint[] pathOptions = new PathPoint[32];

	public Pathfinder(IBlockAccess blockAccess) {
		this.worldMap = blockAccess;
	}

	public PathEntity createEntityPathTo(Entity fromEntity, Entity toEntity, float distance) {
		return this.createEntityPathTo(fromEntity, toEntity.posX, toEntity.boundingBox.minY, toEntity.posZ, distance);
	}

	public PathEntity createEntityPathTo(Entity fromEntity, int toX, int toY, int toZ, float distance) {
		return this.createEntityPathTo(fromEntity, (double)((float)toX + 0.5F), (double)((float)toY + 0.5F), (double)((float)toZ + 0.5F), distance);
	}

	private PathEntity createEntityPathTo(Entity fromEntity, double toX, double toY, double toZ, float distance) {
		this.path.clearPath();
		this.pointMap.clearMap();
		PathPoint pathPoint9 = this.openPoint(MathHelper.floor_double(fromEntity.boundingBox.minX), MathHelper.floor_double(fromEntity.boundingBox.minY), MathHelper.floor_double(fromEntity.boundingBox.minZ));
		PathPoint pathPoint10 = this.openPoint(MathHelper.floor_double(toX - (double)(fromEntity.width / 2.0F)), MathHelper.floor_double(toY), MathHelper.floor_double(toZ - (double)(fromEntity.width / 2.0F)));
		PathPoint pathPoint11 = new PathPoint(MathHelper.floor_float(fromEntity.width + 1.0F), MathHelper.floor_float(fromEntity.height + 1.0F), MathHelper.floor_float(fromEntity.width + 1.0F));
		PathEntity pathEntity12 = this.addToPath(fromEntity, pathPoint9, pathPoint10, pathPoint11, distance);
		return pathEntity12;
	}

	private PathEntity addToPath(Entity entity, PathPoint pathPoint1, PathPoint pathPoint2, PathPoint pathPoint3, float distance) {
		pathPoint1.totalPathDistance = 0.0F;
		pathPoint1.distanceToNext = pathPoint1.distanceTo(pathPoint2);
		pathPoint1.distanceToTarget = pathPoint1.distanceToNext;
		this.path.clearPath();
		this.path.addPoint(pathPoint1);
		PathPoint pathPoint6 = pathPoint1;

		while(!this.path.isPathEmpty()) {
			PathPoint pathPoint7 = this.path.dequeue();
			if(pathPoint7.hash == pathPoint2.hash) {
				return this.createEntityPath(pathPoint1, pathPoint2);
			}

			if(pathPoint7.distanceTo(pathPoint2) < pathPoint6.distanceTo(pathPoint2)) {
				pathPoint6 = pathPoint7;
			}

			pathPoint7.isFirst = true;
			int i8 = this.findPathOptions(entity, pathPoint7, pathPoint3, pathPoint2, distance);

			for(int i9 = 0; i9 < i8; ++i9) {
				PathPoint pathPoint10 = this.pathOptions[i9];
				float f11 = pathPoint7.totalPathDistance + pathPoint7.distanceTo(pathPoint10);
				if(!pathPoint10.isAssigned() || f11 < pathPoint10.totalPathDistance) {
					pathPoint10.previous = pathPoint7;
					pathPoint10.totalPathDistance = f11;
					pathPoint10.distanceToNext = pathPoint10.distanceTo(pathPoint2);
					if(pathPoint10.isAssigned()) {
						this.path.changeDistance(pathPoint10, pathPoint10.totalPathDistance + pathPoint10.distanceToNext);
					} else {
						pathPoint10.distanceToTarget = pathPoint10.totalPathDistance + pathPoint10.distanceToNext;
						this.path.addPoint(pathPoint10);
					}
				}
			}
		}

		if(pathPoint6 == pathPoint1) {
			return null;
		} else {
			return this.createEntityPath(pathPoint1, pathPoint6);
		}
	}

	private int findPathOptions(Entity entity, PathPoint pathPoint1, PathPoint pathPoint2, PathPoint pathPoint3, float distance) {
		int i6 = 0;
		byte b7 = 0;
		if(this.getVerticalOffset(entity, pathPoint1.xCoord, pathPoint1.yCoord + 1, pathPoint1.zCoord, pathPoint2) > 0) {
			b7 = 1;
		}

		PathPoint pathPoint8 = this.getSafePoint(entity, pathPoint1.xCoord, pathPoint1.yCoord, pathPoint1.zCoord + 1, pathPoint2, b7);
		PathPoint pathPoint9 = this.getSafePoint(entity, pathPoint1.xCoord - 1, pathPoint1.yCoord, pathPoint1.zCoord, pathPoint2, b7);
		PathPoint pathPoint10 = this.getSafePoint(entity, pathPoint1.xCoord + 1, pathPoint1.yCoord, pathPoint1.zCoord, pathPoint2, b7);
		PathPoint pathPoint11 = this.getSafePoint(entity, pathPoint1.xCoord, pathPoint1.yCoord, pathPoint1.zCoord - 1, pathPoint2, b7);
		if(pathPoint8 != null && !pathPoint8.isFirst && pathPoint8.distanceTo(pathPoint3) < distance) {
			this.pathOptions[i6++] = pathPoint8;
		}

		if(pathPoint9 != null && !pathPoint9.isFirst && pathPoint9.distanceTo(pathPoint3) < distance) {
			this.pathOptions[i6++] = pathPoint9;
		}

		if(pathPoint10 != null && !pathPoint10.isFirst && pathPoint10.distanceTo(pathPoint3) < distance) {
			this.pathOptions[i6++] = pathPoint10;
		}

		if(pathPoint11 != null && !pathPoint11.isFirst && pathPoint11.distanceTo(pathPoint3) < distance) {
			this.pathOptions[i6++] = pathPoint11;
		}

		return i6;
	}

	private PathPoint getSafePoint(Entity entity1, int i2, int i3, int i4, PathPoint pathPoint5, int i6) {
		PathPoint pathPoint7 = null;
		if(this.getVerticalOffset(entity1, i2, i3, i4, pathPoint5) > 0) {
			pathPoint7 = this.openPoint(i2, i3, i4);
		}

		if(pathPoint7 == null && this.getVerticalOffset(entity1, i2, i3 + i6, i4, pathPoint5) > 0) {
			pathPoint7 = this.openPoint(i2, i3 + i6, i4);
			i3 += i6;
		}

		if(pathPoint7 != null) {
			int i8 = 0;

			int i10;
			for(boolean z9 = false; i3 > 0 && (i10 = this.getVerticalOffset(entity1, i2, i3 - 1, i4, pathPoint5)) > 0; --i3) {
				if(i10 < 0) {
					return null;
				}

				++i8;
				if(i8 >= 4) {
					return null;
				}
			}

			if(i3 > 0) {
				pathPoint7 = this.openPoint(i2, i3, i4);
			}
		}

		return pathPoint7;
	}

	private final PathPoint openPoint(int i1, int i2, int i3) {
		int i4 = i1 | i2 << 10 | i3 << 20;
		PathPoint pathPoint5 = (PathPoint)this.pointMap.lookup(i4);
		if(pathPoint5 == null) {
			pathPoint5 = new PathPoint(i1, i2, i3);
			this.pointMap.addKey(i4, pathPoint5);
		}

		return pathPoint5;
	}

	private int getVerticalOffset(Entity entity1, int i2, int i3, int i4, PathPoint pathPoint5) {
		for(int i6 = i2; i6 < i2 + pathPoint5.xCoord; ++i6) {
			for(int i7 = i3; i7 < i3 + pathPoint5.yCoord; ++i7) {
				for(int i8 = i4; i8 < i4 + pathPoint5.zCoord; ++i8) {
					Material material9 = this.worldMap.getBlockMaterial(i2, i3, i4);
					if(material9.getIsSolid()) {
						return 0;
					}

					if(material9 == Material.water || material9 == Material.lava) {
						return -1;
					}
				}
			}
		}

		return 1;
	}

	private PathEntity createEntityPath(PathPoint fromPathPoint, PathPoint toPathPoint) {
		int i3 = 1;

		PathPoint pathPoint4;
		for(pathPoint4 = toPathPoint; pathPoint4.previous != null; pathPoint4 = pathPoint4.previous) {
			++i3;
		}

		PathPoint[] pathPoint5 = new PathPoint[i3];
		pathPoint4 = toPathPoint;
		--i3;

		for(pathPoint5[i3] = toPathPoint; pathPoint4.previous != null; pathPoint5[i3] = pathPoint4) {
			pathPoint4 = pathPoint4.previous;
			--i3;
		}

		return new PathEntity(pathPoint5);
	}
}
