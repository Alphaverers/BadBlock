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

	public void setTag(String key, NBTBase nbtbase) {
		this.tagMap.put(key, nbtbase.setKey(key));
	}

	public void setByte(String key, byte byteValue) {
		this.tagMap.put(key, (new NBTTagByte(byteValue)).setKey(key));
	}

	public void setShort(String key, short shortValue) {
		this.tagMap.put(key, (new NBTTagShort(shortValue)).setKey(key));
	}

	public void setInteger(String key, int integerValue) {
		this.tagMap.put(key, (new NBTTagInt(integerValue)).setKey(key));
	}

	public void setLong(String key, long longValue) {
		this.tagMap.put(key, (new NBTTagLong(longValue)).setKey(key));
	}

	public void setFloat(String key, float floatValue) {
		this.tagMap.put(key, (new NBTTagFloat(floatValue)).setKey(key));
	}

	public void setDouble(String key, double doubleValue) {
		this.tagMap.put(key, (new NBTTagDouble(doubleValue)).setKey(key));
	}

	public void setString(String key, String stringValue) {
		this.tagMap.put(key, (new NBTTagString(stringValue)).setKey(key));
	}

	public void setByteArray(String key, byte[] byteArray) {
		this.tagMap.put(key, (new NBTTagByteArray(byteArray)).setKey(key));
	}

	public void setCompoundTag(String key, NBTTagCompound nbttagcompound) {
		this.tagMap.put(key, nbttagcompound.setKey(key));
	}

	public void setBoolean(String key, boolean booleanValue) {
		this.setByte(key, (byte)(booleanValue ? 1 : 0));
	}

	public boolean hasKey(String key) {
		return this.tagMap.containsKey(key);
	}

	public byte getByte(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagByte)this.tagMap.get(key)).byteValue;
	}

	public short getShort(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagShort)this.tagMap.get(key)).shortValue;
	}

	public int getInteger(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagInt)this.tagMap.get(key)).intValue;
	}

	public long getLong(String key) {
		return !this.tagMap.containsKey(key) ? 0L : ((NBTTagLong)this.tagMap.get(key)).longValue;
	}

	public float getFloat(String key) {
		return !this.tagMap.containsKey(key) ? 0.0F : ((NBTTagFloat)this.tagMap.get(key)).floatValue;
	}

	public double getDouble(String key) {
		return !this.tagMap.containsKey(key) ? 0.0D : ((NBTTagDouble)this.tagMap.get(key)).doubleValue;
	}

	public String getString(String key) {
		return !this.tagMap.containsKey(key) ? "" : ((NBTTagString)this.tagMap.get(key)).stringValue;
	}

	public byte[] getByteArray(String key) {
		return !this.tagMap.containsKey(key) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).byteArray;
	}

	public NBTTagCompound getCompoundTag(String key) {
		return !this.tagMap.containsKey(key) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
	}

	public NBTTagList getTagList(String key) {
		return !this.tagMap.containsKey(key) ? new NBTTagList() : (NBTTagList)this.tagMap.get(key);
	}

	public boolean getBoolean(String key) {
		return this.getByte(key) != 0;
	}

	public String toString() {
		return "" + this.tagMap.size() + " entries";
	}
}
