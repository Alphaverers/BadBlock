package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float floatValue) {
		this.floatValue = floatValue;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeFloat(this.floatValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.floatValue = dataInput1.readFloat();
	}

	public byte getType() {
		return (byte)5;
	}

	public String toString() {
		return "" + this.floatValue;
	}
}
