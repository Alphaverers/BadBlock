package net.minecraft.src;

public class MathHelper {
	private static float[] SIN_TABLE = new float[65536];

	public static final float sin(float value) {
		return SIN_TABLE[(int)(value * 10430.378F) & 65535];
	}

	public static final float cos(float value) {
		return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
	}

	public static final float sqrt_float(float value) {
		return (float)Math.sqrt((double)value);
	}

	public static final float sqrt_double(double value) {
		return (float)Math.sqrt(value);
	}

	public static int floor_float(float value) {
		int i1 = (int)value;
		return value < (float)i1 ? i1 - 1 : i1;
	}

	public static int floor_double(double value) {
		int i2 = (int)value;
		return value < (double)i2 ? i2 - 1 : i2;
	}

	public static float abs(float value) {
		return value >= 0.0F ? value : -value;
	}

	public static double abs_max(double value1, double value2) {
		if(value1 < 0.0D) {
			value1 = -value1;
		}

		if(value2 < 0.0D) {
			value2 = -value2;
		}

		return value1 > value2 ? value1 : value2;
	}

	public static int bucketInt(int value1, int value2) {
		return value1 < 0 ? -((-value1 - 1) / value2) - 1 : value1 / value2;
	}

	static {
		for(int i0 = 0; i0 < 65536; ++i0) {
			SIN_TABLE[i0] = (float)Math.sin((double)i0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}
