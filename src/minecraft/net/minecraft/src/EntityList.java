package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class EntityList {
	private static Map stringToClassMapping = new HashMap();
	private static Map classToStringMapping = new HashMap();
	private static Map IDtoClassMapping = new HashMap();
	private static Map classToIDMapping = new HashMap();

	private static void addMapping(Class entityClass, String entityName, int entityID) {
		stringToClassMapping.put(entityName, entityClass);
		classToStringMapping.put(entityClass, entityName);
		IDtoClassMapping.put(entityID, entityClass);
		classToIDMapping.put(entityClass, entityID);
	}

	public static Entity createEntityByName(String entityName, World worldObj) {
		Entity entity2 = null;

		try {
			Class class3 = (Class)stringToClassMapping.get(entityName);
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldObj});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		return entity2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound nbtCompound, World worldObj) {
		Entity entity2 = null;

		try {
			Class class3 = (Class)stringToClassMapping.get(nbtCompound.getString("id"));
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldObj});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		if(entity2 != null) {
			entity2.readFromNBT(nbtCompound);
		} else {
			System.out.println("Skipping Entity with id " + nbtCompound.getString("id"));
		}

		return entity2;
	}

	public static Entity createEntityByID(int entityID, World worldObj) {
		Entity entity2 = null;

		try {
			Class class3 = (Class)IDtoClassMapping.get(entityID);
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldObj});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		if(entity2 == null) {
			System.out.println("Skipping Entity with id " + entityID);
		}

		return entity2;
	}

	public static int getEntityID(Entity entity) {
		return ((Integer)classToIDMapping.get(entity.getClass())).intValue();
	}

	public static String getEntityString(Entity entity) {
		return (String)classToStringMapping.get(entity.getClass());
	}

	static {
		addMapping(EntityArrow.class, "Arrow", 10);
		addMapping(EntitySnowball.class, "Snowball", 11);
		addMapping(EntityItem.class, "Item", 1);
		addMapping(EntityPainting.class, "Painting", 9);
		addMapping(EntityLiving.class, "Mob", 48);
		addMapping(EntityMob.class, "Monster", 49);
		addMapping(EntityCreeper.class, "Creeper", 50);
		addMapping(EntitySkeleton.class, "Skeleton", 51);
		addMapping(EntitySpider.class, "Spider", 52);
		addMapping(EntityGiantZombie.class, "Giant", 53);
		addMapping(EntityZombie.class, "Zombie", 54);
		addMapping(EntitySlime.class, "Slime", 55);
		addMapping(EntityPig.class, "Pig", 90);
		addMapping(EntitySheep.class, "Sheep", 91);
		addMapping(EntityCow.class, "Cow", 91);
		addMapping(EntityChicken.class, "Chicken", 91);
		addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, "FallingSand", 21);
		addMapping(EntityMinecart.class, "Minecart", 40);
		addMapping(EntityBoat.class, "Boat", 41);
	}
}
