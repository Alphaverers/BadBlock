package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NBTTagCompound extends NBTBase {
	private Map tagMap = new HashMap();

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		Iterator iterator2 = this.tagMap.values().iterator();

		while(iterator2.hasNext()) {
			NBTBase nBTBase3 = (NBTBase)iterator2.next();
			NBTBase.writeNamedTag(nBTBase3, dataOutput1);
		}

		dataOutput1.writeByte(0);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.tagMap.clear();

		NBTBase nBTBase2;
		while((nBTBase2 = NBTBase.readNamedTag(dataInput1)).getType() != 0) {
			this.tagMap.put(nBTBase2.getKey(), nBTBase2);
		}

	}

	public byte getType() {
		return (byte)10;
	}

	public void setTag(String name, NBTBase tag) {
		this.tagMap.put(name, tag.setKey(name));
	}

	public void setByte(String name, byte value) {
		this.tagMap.put(name, (new NBTTagByte(value)).setKey(name));
	}

	public void setShort(String name, short value) {
		this.tagMap.put(name, (new NBTTagShort(value)).setKey(name));
	}

	public void setInteger(String name, int value) {
		this.tagMap.put(name, (new NBTTagInt(value)).setKey(name));
	}

	public void setLong(String name, long value) {
		this.tagMap.put(name, (new NBTTagLong(value)).setKey(name));
	}

	public void setFloat(String name, float value) {
		this.tagMap.put(name, (new NBTTagFloat(value)).setKey(name));
	}

	public void setDouble(String name, double value) {
		this.tagMap.put(name, (new NBTTagDouble(value)).setKey(name));
	}

	public void setString(String name, String value) {
		this.tagMap.put(name, (new NBTTagString(value)).setKey(name));
	}

	public void setByteArray(String name, byte[] value) {
		this.tagMap.put(name, (new NBTTagByteArray(value)).setKey(name));
	}

	public void setCompoundTag(String name, NBTTagCompound compoundTag) {
		this.tagMap.put(name, compoundTag.setKey(name));
	}

	public void setBoolean(String name, boolean value) {
		this.setByte(name, (byte)(value ? 1 : 0));
	}

	public boolean hasKey(String name) {
		return this.tagMap.containsKey(name);
	}

	public byte getByte(String name) {
		return !this.tagMap.containsKey(name) ? 0 : ((NBTTagByte)this.tagMap.get(name)).byteValue;
	}

	public short getShort(String name) {
		return !this.tagMap.containsKey(name) ? 0 : ((NBTTagShort)this.tagMap.get(name)).shortValue;
	}

	public int getInteger(String name) {
		return !this.tagMap.containsKey(name) ? 0 : ((NBTTagInt)this.tagMap.get(name)).intValue;
	}

	public long getLong(String name) {
		return !this.tagMap.containsKey(name) ? 0L : ((NBTTagLong)this.tagMap.get(name)).longValue;
	}

	public float getFloat(String name) {
		return !this.tagMap.containsKey(name) ? 0.0F : ((NBTTagFloat)this.tagMap.get(name)).floatValue;
	}

	public double getDouble(String name) {
		return !this.tagMap.containsKey(name) ? 0.0D : ((NBTTagDouble)this.tagMap.get(name)).doubleValue;
	}

	public String getString(String name) {
		return !this.tagMap.containsKey(name) ? "" : ((NBTTagString)this.tagMap.get(name)).stringValue;
	}

	public byte[] getByteArray(String name) {
		return !this.tagMap.containsKey(name) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(name)).byteArray;
	}

	public NBTTagCompound getCompoundTag(String name) {
		return !this.tagMap.containsKey(name) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(name);
	}

	public NBTTagList getTagList(String name) {
		return !this.tagMap.containsKey(name) ? new NBTTagList() : (NBTTagList)this.tagMap.get(name);
	}

	public boolean getBoolean(String name) {
		return this.getByte(name) != 0;
	}

	public String toString() {
		return "" + this.tagMap.size() + " entries";
	}
}
