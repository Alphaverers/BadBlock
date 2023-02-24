package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase {
	public long longValue;

	public NBTTagLong() {
	}

	public NBTTagLong(long longValue) {
		this.longValue = longValue;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeLong(this.longValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.longValue = dataInput1.readLong();
	}

	public byte getType() {
		return (byte)4;
	}

	public String toString() {
		return "" + this.longValue;
	}
}
