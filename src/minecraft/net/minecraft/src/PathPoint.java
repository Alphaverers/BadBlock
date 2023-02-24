package net.minecraft.src;

public class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	public final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst = false;

	public PathPoint(int x, int y, int z) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.hash = x | y << 10 | z << 20;
	}

	public float distanceTo(PathPoint pathPoint) {
		float f2 = (float)(pathPoint.xCoord - this.xCoord);
		float f3 = (float)(pathPoint.yCoord - this.yCoord);
		float f4 = (float)(pathPoint.zCoord - this.zCoord);
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public boolean equals(Object pathPoint) {
		return ((PathPoint)pathPoint).hash == this.hash;
	}

	public int hashCode() {
		return this.hash;
	}

	public boolean isAssigned() {
		return this.index >= 0;
	}

	public String toString() {
		return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
	}
}
