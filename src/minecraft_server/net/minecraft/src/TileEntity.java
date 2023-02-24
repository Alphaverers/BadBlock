package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
	private static Map nameToClassMap = new HashMap();
	private static Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;

	private static void addMapping(Class clazz, String tileEntityName) {
		if(classToNameMap.containsKey(tileEntityName)) {
			throw new IllegalArgumentException("Duplicate id: " + tileEntityName);
		} else {
			nameToClassMap.put(tileEntityName, clazz);
			classToNameMap.put(clazz, tileEntityName);
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.xCoord = nbttagcompound.getInteger("x");
		this.yCoord = nbttagcompound.getInteger("y");
		this.zCoord = nbttagcompound.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		String string2 = (String)classToNameMap.get(this.getClass());
		if(string2 == null) {
			throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
		} else {
			nbttagcompound.setString("id", string2);
			nbttagcompound.setInteger("x", this.xCoord);
			nbttagcompound.setInteger("y", this.yCoord);
			nbttagcompound.setInteger("z", this.zCoord);
		}
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound nbttagcompound) {
		TileEntity tileEntity1 = null;

		try {
			Class class2 = (Class)nameToClassMap.get(nbttagcompound.getString("id"));
			if(class2 != null) {
				tileEntity1 = (TileEntity)class2.newInstance();
			}
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

		if(tileEntity1 != null) {
			tileEntity1.readFromNBT(nbttagcompound);
		} else {
			System.out.println("Skipping TileEntity with id " + nbttagcompound.getString("id"));
		}

		return tileEntity1;
	}

	public void onInventoryChanged() {
		this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
	}

	static {
		addMapping(TileEntityFurnace.class, "Furnace");
		addMapping(TileEntityChest.class, "Chest");
		addMapping(TileEntitySign.class, "Sign");
		addMapping(TileEntityMobSpawner.class, "MobSpawner");
	}
}
