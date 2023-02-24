package net.minecraft.src;

public class ClippingHelper {
	public float[][] frustum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];

	public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		for(int i13 = 0; i13 < 6; ++i13) {
			if((double)this.frustum[i13][0] * minX + (double)this.frustum[i13][1] * minY + (double)this.frustum[i13][2] * minZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * maxX + (double)this.frustum[i13][1] * minY + (double)this.frustum[i13][2] * minZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * minX + (double)this.frustum[i13][1] * maxY + (double)this.frustum[i13][2] * minZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * maxX + (double)this.frustum[i13][1] * maxY + (double)this.frustum[i13][2] * minZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * minX + (double)this.frustum[i13][1] * minY + (double)this.frustum[i13][2] * maxZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * maxX + (double)this.frustum[i13][1] * minY + (double)this.frustum[i13][2] * maxZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * minX + (double)this.frustum[i13][1] * maxY + (double)this.frustum[i13][2] * maxZ + (double)this.frustum[i13][3] <= 0.0D && (double)this.frustum[i13][0] * maxX + (double)this.frustum[i13][1] * maxY + (double)this.frustum[i13][2] * maxZ + (double)this.frustum[i13][3] <= 0.0D) {
				return false;
			}
		}

		return true;
	}
}
