package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AxisAlignedBB {
	private static List boundingBoxes = new ArrayList();
	private static int numBoundingBoxesInUse = 0;
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;

	public static AxisAlignedBB getBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static void clearBoundingBoxPool() {
		numBoundingBoxesInUse = 0;
	}

	public static AxisAlignedBB getBoundingBoxFromPool(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		if(numBoundingBoxesInUse >= boundingBoxes.size()) {
			boundingBoxes.add(getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));
		}

		return ((AxisAlignedBB)boundingBoxes.get(numBoundingBoxesInUse++)).setBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public AxisAlignedBB setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		return this;
	}

	public AxisAlignedBB addCoord(double x, double y, double z) {
		double d7 = this.minX;
		double d9 = this.minY;
		double d11 = this.minZ;
		double d13 = this.maxX;
		double d15 = this.maxY;
		double d17 = this.maxZ;
		if(x < 0.0D) {
			d7 += x;
		}

		if(x > 0.0D) {
			d13 += x;
		}

		if(y < 0.0D) {
			d9 += y;
		}

		if(y > 0.0D) {
			d15 += y;
		}

		if(z < 0.0D) {
			d11 += z;
		}

		if(z > 0.0D) {
			d17 += z;
		}

		return getBoundingBoxFromPool(d7, d9, d11, d13, d15, d17);
	}

	public AxisAlignedBB expand(double x, double y, double z) {
		double d7 = this.minX - x;
		double d9 = this.minY - y;
		double d11 = this.minZ - z;
		double d13 = this.maxX + x;
		double d15 = this.maxY + y;
		double d17 = this.maxZ + z;
		return getBoundingBoxFromPool(d7, d9, d11, d13, d15, d17);
	}

	public AxisAlignedBB getOffsetBoundingBox(double offsetX, double offsetY, double offsetZ) {
		return getBoundingBoxFromPool(this.minX + offsetX, this.minY + offsetY, this.minZ + offsetZ, this.maxX + offsetX, this.maxY + offsetY, this.maxZ + offsetZ);
	}

	public double calculateXOffset(AxisAlignedBB aabb, double offsetX) {
		if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
			if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
				double d4;
				if(offsetX > 0.0D && aabb.maxX <= this.minX) {
					d4 = this.minX - aabb.maxX;
					if(d4 < offsetX) {
						offsetX = d4;
					}
				}

				if(offsetX < 0.0D && aabb.minX >= this.maxX) {
					d4 = this.maxX - aabb.minX;
					if(d4 > offsetX) {
						offsetX = d4;
					}
				}

				return offsetX;
			} else {
				return offsetX;
			}
		} else {
			return offsetX;
		}
	}

	public double calculateYOffset(AxisAlignedBB aabb, double offsetY) {
		if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
			if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
				double d4;
				if(offsetY > 0.0D && aabb.maxY <= this.minY) {
					d4 = this.minY - aabb.maxY;
					if(d4 < offsetY) {
						offsetY = d4;
					}
				}

				if(offsetY < 0.0D && aabb.minY >= this.maxY) {
					d4 = this.maxY - aabb.minY;
					if(d4 > offsetY) {
						offsetY = d4;
					}
				}

				return offsetY;
			} else {
				return offsetY;
			}
		} else {
			return offsetY;
		}
	}

	public double calculateZOffset(AxisAlignedBB aabb, double offsetZ) {
		if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
			if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
				double d4;
				if(offsetZ > 0.0D && aabb.maxZ <= this.minZ) {
					d4 = this.minZ - aabb.maxZ;
					if(d4 < offsetZ) {
						offsetZ = d4;
					}
				}

				if(offsetZ < 0.0D && aabb.minZ >= this.maxZ) {
					d4 = this.maxZ - aabb.minZ;
					if(d4 > offsetZ) {
						offsetZ = d4;
					}
				}

				return offsetZ;
			} else {
				return offsetZ;
			}
		} else {
			return offsetZ;
		}
	}

	public boolean intersectsWith(AxisAlignedBB aabb) {
		return aabb.maxX > this.minX && aabb.minX < this.maxX ? (aabb.maxY > this.minY && aabb.minY < this.maxY ? aabb.maxZ > this.minZ && aabb.minZ < this.maxZ : false) : false;
	}

	public AxisAlignedBB offset(double offsetX, double offsetY, double offsetZ) {
		this.minX += offsetX;
		this.minY += offsetY;
		this.minZ += offsetZ;
		this.maxX += offsetX;
		this.maxY += offsetY;
		this.maxZ += offsetZ;
		return this;
	}

	public double getAverageEdgeLength() {
		double d1 = this.maxX - this.minX;
		double d3 = this.maxY - this.minY;
		double d5 = this.maxZ - this.minZ;
		return (d1 + d3 + d5) / 3.0D;
	}

	public AxisAlignedBB copy() {
		return getBoundingBoxFromPool(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public MovingObjectPosition calculateIntercept(Vec3D vector1, Vec3D vector2) {
		Vec3D vec3D3 = vector1.getIntermediateWithXValue(vector2, this.minX);
		Vec3D vec3D4 = vector1.getIntermediateWithXValue(vector2, this.maxX);
		Vec3D vec3D5 = vector1.getIntermediateWithYValue(vector2, this.minY);
		Vec3D vec3D6 = vector1.getIntermediateWithYValue(vector2, this.maxY);
		Vec3D vec3D7 = vector1.getIntermediateWithZValue(vector2, this.minZ);
		Vec3D vec3D8 = vector1.getIntermediateWithZValue(vector2, this.maxZ);
		if(!this.isVecInYZ(vec3D3)) {
			vec3D3 = null;
		}

		if(!this.isVecInYZ(vec3D4)) {
			vec3D4 = null;
		}

		if(!this.isVecInXZ(vec3D5)) {
			vec3D5 = null;
		}

		if(!this.isVecInXZ(vec3D6)) {
			vec3D6 = null;
		}

		if(!this.isVecInXY(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInXY(vec3D8)) {
			vec3D8 = null;
		}

		Vec3D vec3D9 = null;
		if(vec3D3 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D3) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D3;
		}

		if(vec3D4 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D4) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D4;
		}

		if(vec3D5 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D5) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D5;
		}

		if(vec3D6 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D6) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D6;
		}

		if(vec3D7 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D7) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D7;
		}

		if(vec3D8 != null && (vec3D9 == null || vector1.squareDistanceTo(vec3D8) < vector1.squareDistanceTo(vec3D9))) {
			vec3D9 = vec3D8;
		}

		if(vec3D9 == null) {
			return null;
		} else {
			byte b10 = -1;
			if(vec3D9 == vec3D3) {
				b10 = 4;
			}

			if(vec3D9 == vec3D4) {
				b10 = 5;
			}

			if(vec3D9 == vec3D5) {
				b10 = 0;
			}

			if(vec3D9 == vec3D6) {
				b10 = 1;
			}

			if(vec3D9 == vec3D7) {
				b10 = 2;
			}

			if(vec3D9 == vec3D8) {
				b10 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, b10, vec3D9);
		}
	}

	private boolean isVecInYZ(Vec3D vector) {
		return vector == null ? false : vector.yCoord >= this.minY && vector.yCoord <= this.maxY && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3D vector) {
		return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3D vector) {
		return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.yCoord >= this.minY && vector.yCoord <= this.maxY;
	}

	public void setBB(AxisAlignedBB aabb) {
		this.minX = aabb.minX;
		this.minY = aabb.minY;
		this.minZ = aabb.minZ;
		this.maxX = aabb.maxX;
		this.maxY = aabb.maxY;
		this.maxZ = aabb.maxZ;
	}
}
