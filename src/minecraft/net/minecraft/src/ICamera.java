package net.minecraft.src;

public interface ICamera {
	boolean isBoundingBoxInFrustum(AxisAlignedBB axisAlignedBB1);

	void setPosition(double d1, double d3, double d5);
}
