package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTBase {
	public double doubleValue;

	public NBTTagDouble() {
	}

	public NBTTagDouble(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeDouble(this.doubleValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.doubleValue = dataInput1.readDouble();
	}

	public byte getType() {
		return (byte)6;
	}

	public String toString() {
		return "" + this.doubleValue;
	}
}
