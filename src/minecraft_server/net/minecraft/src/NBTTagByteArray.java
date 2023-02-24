package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeInt(this.byteArray.length);
		dataOutput1.write(this.byteArray);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		int i2 = dataInput1.readInt();
		this.byteArray = new byte[i2];
		dataInput1.readFully(this.byteArray);
	}

	public byte getType() {
		return (byte)7;
	}

	public String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}
