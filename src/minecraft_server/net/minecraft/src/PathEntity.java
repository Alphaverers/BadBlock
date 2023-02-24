package net.minecraft.src;

public class PathEntity {
	private final PathPoint[] points;
	public final int pathLength;
	private int pathIndex;

	public PathEntity(PathPoint[] points) {
		this.points = points;
		this.pathLength = points.length;
	}

	public void incrementPathIndex() {
		++this.pathIndex;
	}

	public boolean isFinished() {
		return this.pathIndex >= this.points.length;
	}

	public Vec3D getPosition(Entity entity) {
		double d2 = (double)this.points[this.pathIndex].xCoord + (double)((int)(entity.width + 1.0F)) * 0.5D;
		double d4 = (double)this.points[this.pathIndex].yCoord;
		double d6 = (double)this.points[this.pathIndex].zCoord + (double)((int)(entity.width + 1.0F)) * 0.5D;
		return Vec3D.createVector(d2, d4, d6);
	}
}
