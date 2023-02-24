package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort() {
	}

	public NBTTagShort(short value) {
		this.shortValue = value;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeShort(this.shortValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.shortValue = dataInput1.readShort();
	}

	public byte getType() {
		return (byte)2;
	}

	public String toString() {
		return "" + this.shortValue;
	}
}
