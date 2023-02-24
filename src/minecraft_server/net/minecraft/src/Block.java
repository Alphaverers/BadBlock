package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class Block {
	public static final StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
	public static final StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
	public static final StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
	public static final StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
	public static final StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
	public static final StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
	public static final StepSound soundGlassFootstepo = new StepSoundGlass("stone", 1.0F, 1.0F);
	public static final StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
	public static final StepSound soundSandFootstep = new StepSoundSand("sand", 1.0F, 1.0F);
	public static final Block[] blockList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	public static final boolean[] opaqueCubeLookup = new boolean[256];
	public static final boolean[] isBlockContainer = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	public static final boolean[] canBlockGrass = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final BlockGrass grass = (BlockGrass)(new BlockGrass(2)).setHardness(0.6F).setStepSound(soundGrassFootstep);
	public static final Block dirt = (new BlockDirt(3, 2)).setHardness(0.5F).setStepSound(soundGravelFootstep);
	public static final Block cobblestone = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block planks = (new Block(5, 4, Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep);
	public static final Block sapling = (new BlockSapling(6, 15)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final Block bedrock = (new Block(7, 17, Material.rock)).setHardness(-1.0F).setResistance(6000000.0F).setStepSound(soundStoneFootstep);
	public static final Block waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3);
	public static final Block waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3);
	public static final Block lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
	public static final Block lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
	public static final Block sand = (new BlockSand(12, 18)).setHardness(0.5F).setStepSound(soundSandFootstep);
	public static final Block gravel = (new BlockGravel(13, 19)).setHardness(0.6F).setStepSound(soundGravelFootstep);
	public static final Block oreGold = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block oreIron = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block oreCoal = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block wood = (new BlockLog(17)).setHardness(2.0F).setStepSound(soundWoodFootstep);
	public static final BlockLeaves leaves = (BlockLeaves)(new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1).setStepSound(soundGrassFootstep);
	public static final Block sponge = (new BlockSponge(19)).setHardness(0.6F).setStepSound(soundGrassFootstep);
	public static final Block glass = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstepo);
	public static final Block clothRed = null;
	public static final Block clothOrange = null;
	public static final Block clothYellow = null;
	public static final Block clothChartreuse = null;
	public static final Block clothGreen = null;
	public static final Block clothSpringGreen = null;
	public static final Block clothCyan = null;
	public static final Block clothCapri = null;
	public static final Block clothUltramarine = null;
	public static final Block clothViolet = null;
	public static final Block clothPurple = null;
	public static final Block clothMagenta = null;
	public static final Block clothRose = null;
	public static final Block clothDarkGray = null;
	public static final Block cloth = (new Block(35, 64, Material.cloth)).setHardness(0.8F).setStepSound(soundClothFootstep);
	public static final Block clothWhite = null;
	public static final BlockFlower plantYellow = (BlockFlower)(new BlockFlower(37, 13)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final BlockFlower plantRed = (BlockFlower)(new BlockFlower(38, 12)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final BlockFlower mushroomBrown = (BlockFlower)(new BlockMushroom(39, 29)).setHardness(0.0F).setStepSound(soundGrassFootstep).setLightValue(0.125F);
	public static final BlockFlower mushroomRed = (BlockFlower)(new BlockMushroom(40, 28)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final Block blockGold = (new BlockOreBlock(41, 39)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundMetalFootstep);
	public static final Block blockSteel = (new BlockOreBlock(42, 38)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep);
	public static final Block stairDouble = (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block stairSingle = (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block brick = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block tnt = (new BlockTNT(46, 8)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final Block bookshelf = (new BlockBookshelf(47, 35)).setHardness(1.5F).setStepSound(soundWoodFootstep);
	public static final Block cobblestoneMossy = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block obsidian = (new BlockObsidian(49, 37)).setHardness(10.0F).setResistance(2000.0F).setStepSound(soundStoneFootstep);
	public static final Block torch = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(0.9375F).setStepSound(soundWoodFootstep);
	public static final BlockFire fire = (BlockFire)((BlockFire)(new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep));
	public static final Block spawner = (new BlockMobSpawner(52, 65)).setHardness(5.0F).setStepSound(soundMetalFootstep);
	public static final Block stairCompactWood = new BlockStairs(53, planks);
	public static final Block crate = (new BlockChest(54)).setHardness(2.5F).setStepSound(soundWoodFootstep);
	public static final Block redstoneWire = (new BlockRedstoneWire(55, 84)).setHardness(0.0F).setStepSound(soundPowderFootstep);
	public static final Block oreDiamond = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block blockDiamond = (new BlockOreBlock(57, 40)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep);
	public static final Block workbench = (new BlockWorkbench(58)).setHardness(2.5F).setStepSound(soundWoodFootstep);
	public static final Block crops = (new BlockCrops(59, 88)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final Block tilledField = (new BlockFarmland(60)).setHardness(0.6F).setStepSound(soundGravelFootstep);
	public static final Block stoneOvenIdle = (new BlockFurnace(61, false)).setHardness(3.5F).setStepSound(soundStoneFootstep);
	public static final Block stoneOvenActive = (new BlockFurnace(62, true)).setHardness(3.5F).setStepSound(soundStoneFootstep).setLightValue(0.875F);
	public static final Block signStanding = (new BlockSign(63, TileEntitySign.class, true)).setHardness(1.0F).setStepSound(soundWoodFootstep);
	public static final Block doorWood = (new BlockDoor(64, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep);
	public static final Block ladder = (new BlockLadder(65, 83)).setHardness(0.4F).setStepSound(soundWoodFootstep);
	public static final Block minecartTrack = (new BlockMinecartTrack(66, 128)).setHardness(0.7F).setStepSound(soundMetalFootstep);
	public static final Block stairCompactStone = new BlockStairs(67, cobblestone);
	public static final Block signWall = (new BlockSign(68, TileEntitySign.class, false)).setHardness(1.0F).setStepSound(soundWoodFootstep);
	public static final Block lever = (new BlockLever(69, 96)).setHardness(0.5F).setStepSound(soundWoodFootstep);
	public static final Block pressurePlateStone = (new BlockPressurePlate(70, stone.blockIndexInTexture, EnumMobType.mobs)).setHardness(0.5F).setStepSound(soundStoneFootstep);
	public static final Block doorSteel = (new BlockDoor(71, Material.iron)).setHardness(5.0F).setStepSound(soundMetalFootstep);
	public static final Block pressurePlateWood = (new BlockPressurePlate(72, planks.blockIndexInTexture, EnumMobType.everything)).setHardness(0.5F).setStepSound(soundWoodFootstep);
	public static final Block oreRedstone = (new BlockRedstoneOre(73, 51, false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block oreRedstoneGlowing = (new BlockRedstoneOre(74, 51, true)).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep);
	public static final Block torchRedstoneIdle = (new BlockRedstoneTorch(75, 115, false)).setHardness(0.0F).setStepSound(soundWoodFootstep);
	public static final Block torchRedstoneActive = (new BlockRedstoneTorch(76, 99, true)).setHardness(0.0F).setLightValue(0.5F).setStepSound(soundWoodFootstep);
	public static final Block button = (new BlockButton(77, stone.blockIndexInTexture)).setHardness(0.5F).setStepSound(soundStoneFootstep);
	public static final Block snow = (new BlockSnow(78, 66)).setHardness(0.1F).setStepSound(soundClothFootstep);
	public static final Block ice = (new BlockIce(79, 67)).setHardness(0.5F).setLightOpacity(3).setStepSound(soundGlassFootstepo);
	public static final Block blockSnow = (new BlockSnowBlock(80, 66)).setHardness(0.2F).setStepSound(soundClothFootstep);
	public static final Block cactus = (new BlockCactus(81, 70)).setHardness(0.4F).setStepSound(soundClothFootstep);
	public static final Block blockClay = (new BlockClay(82, 72)).setHardness(0.6F).setStepSound(soundGravelFootstep);
	public static final Block reed = (new BlockReed(83, 73)).setHardness(0.0F).setStepSound(soundGrassFootstep);
	public static final Block jukebox = (new BlockJukeBox(84, 74)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep);
	public static final Block fence = (new BlockFence(85, 4)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep);
	public int blockIndexInTexture;
	public final int blockID;
	protected float hardness;
	protected float resistance;
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	public StepSound stepSound;
	public float blockParticleGravity;
	public final Material material;
	public float slipperiness;

	protected Block(int id, Material material) {
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		this.slipperiness = 0.6F;
		if(blockList[id] != null) {
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + blockList[id] + " when adding " + this);
		} else {
			this.material = material;
			blockList[id] = this;
			this.blockID = id;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			opaqueCubeLookup[id] = this.isOpaqueCube();
			lightOpacity[id] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[id] = this.getCanBlockGrass();
			isBlockContainer[id] = false;
		}
	}

	protected Block(int id, int blockIndex, Material material) {
		this(id, material);
		this.blockIndexInTexture = blockIndex;
	}

	protected Block setStepSound(StepSound stepSound) {
		this.stepSound = stepSound;
		return this;
	}

	protected Block setLightOpacity(int opacity) {
		lightOpacity[this.blockID] = opacity;
		return this;
	}

	protected Block setLightValue(float value) {
		lightValue[this.blockID] = (int)(15.0F * value);
		return this;
	}

	protected Block setResistance(float resistance) {
		this.resistance = resistance * 3.0F;
		return this;
	}

	private boolean getCanBlockGrass() {
		return false;
	}

	public int getRenderType() {
		return 0;
	}

	protected Block setHardness(float hardness) {
		this.hardness = hardness;
		if(this.resistance < hardness * 5.0F) {
			this.resistance = hardness * 5.0F;
		}

		return this;
	}

	protected void setTickOnLoad(boolean doesTickOnLoad) {
		tickOnLoad[this.blockID] = doesTickOnLoad;
	}

	public void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = (double)minX;
		this.minY = (double)minY;
		this.minZ = (double)minZ;
		this.maxX = (double)maxX;
		this.maxY = (double)maxY;
		this.maxZ = (double)maxZ;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return side == 0 && this.minY > 0.0D ? true : (side == 1 && this.maxY < 1.0D ? true : (side == 2 && this.minZ > 0.0D ? true : (side == 3 && this.maxZ < 1.0D ? true : (side == 4 && this.minX > 0.0D ? true : (side == 5 && this.maxX < 1.0D ? true : !blockAccess.isBlockNormalCube(x, y, z))))));
	}

	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture;
	}

	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList arrayList) {
		AxisAlignedBB axisAlignedBB7 = this.getCollisionBoundingBoxFromPool(world, x, y, z);
		if(axisAlignedBB7 != null && aabb.intersectsWith(axisAlignedBB7)) {
			arrayList.add(axisAlignedBB7);
		}

	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBoxFromPool((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public boolean canCollideCheck(int i1, boolean z2) {
		return this.isCollidable();
	}

	public boolean isCollidable() {
		return true;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int flag) {
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
	}

	public int tickRate() {
		return 10;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int count, Random random) {
		return this.blockID;
	}

	public float blockStrength(EntityPlayer entityPlayer) {
		return this.hardness < 0.0F ? 0.0F : (!entityPlayer.canHarvestBlock(this) ? 1.0F / this.hardness / 100.0F : entityPlayer.getCurrentPlayerStrVsBlock(this) / this.hardness / 30.0F);
	}

	public void dropBlockAsItem(World world, int i2, int i3, int i4, int i5) {
		this.dropBlockAsItemWithChance(world, i2, i3, i4, i5, 1.0F);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int i5, float f6) {
		if(!world.multiplayerWorld) {
			int i7 = this.quantityDropped(world.rand);

			for(int i8 = 0; i8 < i7; ++i8) {
				if(world.rand.nextFloat() <= f6) {
					int i9 = this.idDropped(i5, world.rand);
					if(i9 > 0) {
						float f10 = 0.7F;
						double d11 = (double)(world.rand.nextFloat() * f10) + (double)(1.0F - f10) * 0.5D;
						double d13 = (double)(world.rand.nextFloat() * f10) + (double)(1.0F - f10) * 0.5D;
						double d15 = (double)(world.rand.nextFloat() * f10) + (double)(1.0F - f10) * 0.5D;
						EntityItem entityItem17 = new EntityItem(world, (double)x + d11, (double)y + d13, (double)z + d15, new ItemStack(i9));
						entityItem17.delayBeforeCanPickup = 10;
						world.spawnEntityInWorld(entityItem17);
					}
				}
			}

		}
	}

	public float getExplosionResistance(Entity entity) {
		return this.resistance / 5.0F;
	}

	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		vector1 = vector1.addVector((double)(-x), (double)(-y), (double)(-z));
		vector2 = vector2.addVector((double)(-x), (double)(-y), (double)(-z));
		Vec3D vec3D7 = vector1.getIntermediateWithXValue(vector2, this.minX);
		Vec3D vec3D8 = vector1.getIntermediateWithXValue(vector2, this.maxX);
		Vec3D vec3D9 = vector1.getIntermediateWithYValue(vector2, this.minY);
		Vec3D vec3D10 = vector1.getIntermediateWithYValue(vector2, this.maxY);
		Vec3D vec3D11 = vector1.getIntermediateWithZValue(vector2, this.minZ);
		Vec3D vec3D12 = vector1.getIntermediateWithZValue(vector2, this.maxZ);
		if(!this.isVecInsideYZBounds(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInsideYZBounds(vec3D8)) {
			vec3D8 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D9)) {
			vec3D9 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D10)) {
			vec3D10 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D11)) {
			vec3D11 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D12)) {
			vec3D12 = null;
		}

		Vec3D vec3D13 = null;
		if(vec3D7 != null && (vec3D13 == null || vector1.distanceTo(vec3D7) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D7;
		}

		if(vec3D8 != null && (vec3D13 == null || vector1.distanceTo(vec3D8) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D8;
		}

		if(vec3D9 != null && (vec3D13 == null || vector1.distanceTo(vec3D9) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D9;
		}

		if(vec3D10 != null && (vec3D13 == null || vector1.distanceTo(vec3D10) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D10;
		}

		if(vec3D11 != null && (vec3D13 == null || vector1.distanceTo(vec3D11) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D11;
		}

		if(vec3D12 != null && (vec3D13 == null || vector1.distanceTo(vec3D12) < vector1.distanceTo(vec3D13))) {
			vec3D13 = vec3D12;
		}

		if(vec3D13 == null) {
			return null;
		} else {
			byte b14 = -1;
			if(vec3D13 == vec3D7) {
				b14 = 4;
			}

			if(vec3D13 == vec3D8) {
				b14 = 5;
			}

			if(vec3D13 == vec3D9) {
				b14 = 0;
			}

			if(vec3D13 == vec3D10) {
				b14 = 1;
			}

			if(vec3D13 == vec3D11) {
				b14 = 2;
			}

			if(vec3D13 == vec3D12) {
				b14 = 3;
			}

			return new MovingObjectPosition(x, y, z, b14, vec3D13.addVector((double)x, (double)y, (double)z));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D vector) {
		return vector == null ? false : vector.yCoord >= this.minY && vector.yCoord <= this.maxY && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D vector) {
		return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D vector) {
		return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.yCoord >= this.minY && vector.yCoord <= this.maxY;
	}

	public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int i5 = world.getBlockId(x, y, z);
		return i5 == 0 || blockList[i5].material.getIsLiquid();
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		return false;
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
	}

	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D vector) {
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		return false;
	}

	public boolean canProvidePower() {
		return false;
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return false;
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return true;
	}

	static {
		for(int i0 = 0; i0 < 256; ++i0) {
			if(blockList[i0] != null) {
				Item.itemsList[i0] = new ItemBlock(i0 - 256);
			}
		}

	}
}
