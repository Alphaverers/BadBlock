package net.minecraft.src;

public class Path {
	private PathPoint[] pathPoints = new PathPoint[1024];
	private int count = 0;

	public PathPoint addPoint(PathPoint pathPoint) {
		if(pathPoint.index >= 0) {
			throw new IllegalStateException("OW KNOWS!");
		} else {
			if(this.count == this.pathPoints.length) {
				PathPoint[] pathPoint2 = new PathPoint[this.count << 1];
				System.arraycopy(this.pathPoints, 0, pathPoint2, 0, this.count);
				this.pathPoints = pathPoint2;
			}

			this.pathPoints[this.count] = pathPoint;
			pathPoint.index = this.count;
			this.sortBack(this.count++);
			return pathPoint;
		}
	}

	public void clearPath() {
		this.count = 0;
	}

	public PathPoint dequeue() {
		PathPoint pathPoint1 = this.pathPoints[0];
		this.pathPoints[0] = this.pathPoints[--this.count];
		this.pathPoints[this.count] = null;
		if(this.count > 0) {
			this.sortForward(0);
		}

		pathPoint1.index = -1;
		return pathPoint1;
	}

	public void changeDistance(PathPoint pathPoint, float distance) {
		float f3 = pathPoint.distanceToTarget;
		pathPoint.distanceToTarget = distance;
		if(distance < f3) {
			this.sortBack(pathPoint.index);
		} else {
			this.sortForward(pathPoint.index);
		}

	}

	private void sortBack(int index) {
		PathPoint pathPoint2 = this.pathPoints[index];

		int i4;
		for(float f3 = pathPoint2.distanceToTarget; index > 0; index = i4) {
			i4 = index - 1 >> 1;
			PathPoint pathPoint5 = this.pathPoints[i4];
			if(f3 >= pathPoint5.distanceToTarget) {
				break;
			}

			this.pathPoints[index] = pathPoint5;
			pathPoint5.index = index;
		}

		this.pathPoints[index] = pathPoint2;
		pathPoint2.index = index;
	}

	private void sortForward(int index) {
		PathPoint pathPoint2 = this.pathPoints[index];
		float f3 = pathPoint2.distanceToTarget;

		while(true) {
			int i4 = 1 + (index << 1);
			int i5 = i4 + 1;
			if(i4 >= this.count) {
				break;
			}

			PathPoint pathPoint6 = this.pathPoints[i4];
			float f7 = pathPoint6.distanceToTarget;
			PathPoint pathPoint8;
			float f9;
			if(i5 >= this.count) {
				pathPoint8 = null;
				f9 = Float.POSITIVE_INFINITY;
			} else {
				pathPoint8 = this.pathPoints[i5];
				f9 = pathPoint8.distanceToTarget;
			}

			if(f7 < f9) {
				if(f7 >= f3) {
					break;
				}

				this.pathPoints[index] = pathPoint6;
				pathPoint6.index = index;
				index = i4;
			} else {
				if(f9 >= f3) {
					break;
				}

				this.pathPoints[index] = pathPoint8;
				pathPoint8.index = index;
				index = i5;
			}
		}

		this.pathPoints[index] = pathPoint2;
		pathPoint2.index = index;
	}

	public boolean isPathEmpty() {
		return this.count == 0;
	}
}
