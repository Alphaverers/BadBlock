package net.minecraft.src;

public class ColorUtil {
	public static float[] BlendColor(float f0, float f1, float f2, float f3) {
		float f4 = 0.58431375F;
		float f5 = 0.0F;
		float f6 = 1.0F;
		float[] f7 = new float[]{f0 * f4 + (1.0F - f0) * f1, f0 * f5 + (1.0F - f0) * f2, f0 * f6 + (1.0F - f0) * f3};
		return f7;
	}

	public static float[] BlendColorA(float f0, float f1, float f2, float f3, float f4, float f5, float f6) {
		float[] f7 = new float[]{f0 * f4 + (1.0F - f0) * f1, f0 * f5 + (1.0F - f0) * f2, f0 * f6 + (1.0F - f0) * f3};
		return f7;
	}
}
