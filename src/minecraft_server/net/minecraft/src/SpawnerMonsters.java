package net.minecraft.src;

class SpawnerMonsters extends SpawnerAnimals {
	final WorldServer worldServer;

	SpawnerMonsters(WorldServer worldServer1, int i2, Class class3, Class[] class4) {
		super(i2, class3, class4);
		this.worldServer = worldServer1;
	}

	protected ChunkPosition getRandomSpawningPointInChunk(World world1, int i2, int i3) {
		int i4 = i2 + world1.rand.nextInt(16);
		int i5 = world1.rand.nextInt(world1.rand.nextInt(120) + 8);
		int i6 = i3 + world1.rand.nextInt(16);
		return new ChunkPosition(i4, i5, i6);
	}
}
