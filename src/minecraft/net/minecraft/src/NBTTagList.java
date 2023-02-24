package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTBase {
	private List tagList = new ArrayList();
	private byte tagType;

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		if(this.tagList.size() > 0) {
			this.tagType = ((NBTBase)this.tagList.get(0)).getType();
		} else {
			this.tagType = 1;
		}

		dataOutput1.writeByte(this.tagType);
		dataOutput1.writeInt(this.tagList.size());

		for(int i2 = 0; i2 < this.tagList.size(); ++i2) {
			((NBTBase)this.tagList.get(i2)).writeTagContents(dataOutput1);
		}

	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.tagType = dataInput1.readByte();
		int i2 = dataInput1.readInt();
		this.tagList = new ArrayList();

		for(int i3 = 0; i3 < i2; ++i3) {
			NBTBase nBTBase4 = NBTBase.createTagOfType(this.tagType);
			nBTBase4.readTagContents(dataInput1);
			this.tagList.add(nBTBase4);
		}

	}

	public byte getType() {
		return (byte)9;
	}

	public String toString() {
		return "" + this.tagList.size() + " entries of type " + NBTBase.getTagName(this.tagType);
	}

	public void setTag(NBTBase tag) {
		this.tagType = tag.getType();
		this.tagList.add(tag);
	}

	public NBTBase tagAt(int index) {
		return (NBTBase)this.tagList.get(index);
	}

	public int tagCount() {
		return this.tagList.size();
	}
}
