package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class Vec3D {
	private static List vectorList = new ArrayList();
	private static int nextVector = 0;
	public double xCoord;
	public double yCoord;
	public double zCoord;

	public static Vec3D createVectorHelper(double x, double y, double z) {
		return new Vec3D(x, y, z);
	}

	public static void initialize() {
		nextVector = 0;
	}

	public static Vec3D createVector(double x, double y, double z) {
		if(nextVector >= vectorList.size()) {
			vectorList.add(createVectorHelper(0.0D, 0.0D, 0.0D));
		}

		return ((Vec3D)vectorList.get(nextVector++)).setComponents(x, y, z);
	}

	private Vec3D(double x, double y, double z) {
		if(x == -0.0D) {
			x = 0.0D;
		}

		if(y == -0.0D) {
			y = 0.0D;
		}

		if(z == -0.0D) {
			z = 0.0D;
		}

		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	private Vec3D setComponents(double x, double y, double z) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		return this;
	}

	public Vec3D subtract(Vec3D vector) {
		return createVector(vector.xCoord - this.xCoord, vector.yCoord - this.yCoord, vector.zCoord - this.zCoord);
	}

	public Vec3D normalize() {
		double d1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
		return d1 < 1.0E-4D ? createVector(0.0D, 0.0D, 0.0D) : createVector(this.xCoord / d1, this.yCoord / d1, this.zCoord / d1);
	}

	public Vec3D crossProduct(Vec3D vector) {
		return createVector(this.yCoord * vector.zCoord - this.zCoord * vector.yCoord, this.zCoord * vector.xCoord - this.xCoord * vector.zCoord, this.xCoord * vector.yCoord - this.yCoord * vector.xCoord);
	}

	public Vec3D addVector(double x, double y, double z) {
		return createVector(this.xCoord + x, this.yCoord + y, this.zCoord + z);
	}

	public double distanceTo(Vec3D vector) {
		double d2 = vector.xCoord - this.xCoord;
		double d4 = vector.yCoord - this.yCoord;
		double d6 = vector.zCoord - this.zCoord;
		return (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4 + d6 * d6);
	}

	public double squareDistanceTo(Vec3D vector) {
		double d2 = vector.xCoord - this.xCoord;
		double d4 = vector.yCoord - this.yCoord;
		double d6 = vector.zCoord - this.zCoord;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public double squareDistanceTo(double x, double y, double z) {
		double d7 = x - this.xCoord;
		double d9 = y - this.yCoord;
		double d11 = z - this.zCoord;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}

	public double lengthVector() {
		return (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
	}

	public Vec3D getIntermediateWithXValue(Vec3D vector, double xValue) {
		double d4 = vector.xCoord - this.xCoord;
		double d6 = vector.yCoord - this.yCoord;
		double d8 = vector.zCoord - this.zCoord;
		if(d4 * d4 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d10 = (xValue - this.xCoord) / d4;
			return d10 >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null;
		}
	}

	public Vec3D getIntermediateWithYValue(Vec3D vector, double yValue) {
		double d4 = vector.xCoord - this.xCoord;
		double d6 = vector.yCoord - this.yCoord;
		double d8 = vector.zCoord - this.zCoord;
		if(d6 * d6 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d10 = (yValue - this.yCoord) / d6;
			return d10 >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null;
		}
	}

	public Vec3D getIntermediateWithZValue(Vec3D vector, double zValue) {
		double d4 = vector.xCoord - this.xCoord;
		double d6 = vector.yCoord - this.yCoord;
		double d8 = vector.zCoord - this.zCoord;
		if(d8 * d8 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d10 = (zValue - this.zCoord) / d8;
			return d10 >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null;
		}
	}

	public String toString() {
		return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
	}

	public void rotateAroundX(float x) {
		float f2 = MathHelper.cos(x);
		float f3 = MathHelper.sin(x);
		double d4 = this.xCoord;
		double d6 = this.yCoord * (double)f2 + this.zCoord * (double)f3;
		double d8 = this.zCoord * (double)f2 - this.yCoord * (double)f3;
		this.xCoord = d4;
		this.yCoord = d6;
		this.zCoord = d8;
	}

	public void rotateAroundY(float y) {
		float f2 = MathHelper.cos(y);
		float f3 = MathHelper.sin(y);
		double d4 = this.xCoord * (double)f2 + this.zCoord * (double)f3;
		double d6 = this.yCoord;
		double d8 = this.zCoord * (double)f2 - this.xCoord * (double)f3;
		this.xCoord = d4;
		this.yCoord = d6;
		this.zCoord = d8;
	}
}
