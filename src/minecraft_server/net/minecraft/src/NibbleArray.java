package net.minecraft.src;

public class NibbleArray {
	public final byte[] data;

	public NibbleArray(int data) {
		this.data = new byte[data >> 1];
	}

	public NibbleArray(byte[] data) {
		this.data = data;
	}

	public int get(int i1, int i2, int i3) {
		int i4 = i1 << 11 | i3 << 7 | i2;
		int i5 = i4 >> 1;
		int i6 = i4 & 1;
		return i6 == 0 ? this.data[i5] & 15 : this.data[i5] >> 4 & 15;
	}

	public void set(int i1, int i2, int i3, int i4) {
		int i5 = i1 << 11 | i3 << 7 | i2;
		int i6 = i5 >> 1;
		int i7 = i5 & 1;
		if(i7 == 0) {
			this.data[i6] = (byte)(this.data[i6] & 240 | i4 & 15);
		} else {
			this.data[i6] = (byte)(this.data[i6] & 15 | (i4 & 15) << 4);
		}

	}

	public boolean isValid() {
		return this.data != null;
	}
}
