package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte byteValue) {
		this.byteValue = byteValue;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeByte(this.byteValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.byteValue = dataInput1.readByte();
	}

	public byte getType() {
		return (byte)1;
	}

	public String toString() {
		return "" + this.byteValue;
	}
}
