package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldServer extends World {
	public ChunkProviderServer chunkProviderServer;
	public boolean disableSpawnProtection = false;
	public boolean levelSaving;
	private boolean monsters;
	private SpawnerAnimals monsterSpawner = new SpawnerMonsters(this, 200, IMobs.class, new Class[]{EntityZombie.class, EntitySkeleton.class, EntityCreeper.class, EntitySpider.class, EntitySlime.class});
	private SpawnerAnimals animalSpawner = new SpawnerAnimals(15, EntityAnimal.class, new Class[]{EntitySheep.class, EntityPig.class, EntityCow.class, EntityChicken.class});

	public WorldServer(File file1, String string2, boolean z3) {
		super(file1, string2);
		this.monsters = z3;
	}

	public void tick() {
		super.tick();
		if(this.monsters) {
			this.monsterSpawner.onUpdate(this);
		}

		this.animalSpawner.onUpdate(this);
	}

	protected IChunkProvider getChunkProvider(File file1) {
		this.chunkProviderServer = new ChunkProviderServer(this, new ChunkLoader(file1, true), new ChunkProviderGenerate(this, this.randomSeed));
		return this.chunkProviderServer;
	}

	public List getTileEntityList(int i1, int i2, int i3, int i4, int i5, int i6) {
		ArrayList arrayList7 = new ArrayList();

		for(int i8 = 0; i8 < this.loadedTileEntityList.size(); ++i8) {
			TileEntity tileEntity9 = (TileEntity)this.loadedTileEntityList.get(i8);
			if(tileEntity9.xCoord >= i1 && tileEntity9.yCoord >= i2 && tileEntity9.zCoord >= i3 && tileEntity9.xCoord < i4 && tileEntity9.yCoord < i5 && tileEntity9.zCoord < i6) {
				arrayList7.add(tileEntity9);
			}
		}

		return arrayList7;
	}
}
