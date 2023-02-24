package net.minecraft.src;

public class Frustum implements ICamera {
	private ClippingHelper clippingHelper = ClippingHelperImplementation.getInstance();
	private double xPosition;
	private double yPosition;
	private double zPosition;

	public void setPosition(double d1, double d3, double d5) {
		this.xPosition = d1;
		this.yPosition = d3;
		this.zPosition = d5;
	}

	public boolean isBoxInFrustum(double d1, double d3, double d5, double d7, double d9, double d11) {
		return this.clippingHelper.isBoxInFrustum(d1 - this.xPosition, d3 - this.yPosition, d5 - this.zPosition, d7 - this.xPosition, d9 - this.yPosition, d11 - this.zPosition);
	}

	public boolean isBoundingBoxInFrustum(AxisAlignedBB axisAlignedBB1) {
		return this.isBoxInFrustum(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.minZ, axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
	}
}
