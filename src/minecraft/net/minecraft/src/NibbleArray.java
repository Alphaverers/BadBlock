package net.minecraft.src;

public class NibbleArray {
	public final byte[] data;

	public NibbleArray(int size) {
		this.data = new byte[size >> 1];
	}

	public NibbleArray(byte[] data) {
		this.data = data;
	}

	public int get(int x, int y, int z) {
		int i4 = x << 11 | z << 7 | y;
		int i5 = i4 >> 1;
		int i6 = i4 & 1;
		return i6 == 0 ? this.data[i5] & 15 : this.data[i5] >> 4 & 15;
	}

	public void set(int x, int y, int z, int value) {
		int i5 = x << 11 | z << 7 | y;
		int i6 = i5 >> 1;
		int i7 = i5 & 1;
		if(i7 == 0) {
			this.data[i6] = (byte)(this.data[i6] & 240 | value & 15);
		} else {
			this.data[i6] = (byte)(this.data[i6] & 15 | (value & 15) << 4);
		}

	}

	public boolean isValid() {
		return this.data != null;
	}
}
