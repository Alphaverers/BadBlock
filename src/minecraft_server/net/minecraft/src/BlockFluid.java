package net.minecraft.src;

import java.util.Random;

public abstract class BlockFluid extends Block {
	protected int fluidType = 1;

	protected BlockFluid(int i1, Material material2) {
		super(i1, (material2 == Material.lava ? 14 : 12) * 16 + 13, material2);
		float f3 = 0.0F;
		float f4 = 0.0F;
		if(material2 == Material.lava) {
			this.fluidType = 2;
		}

		this.setBlockBounds(0.0F + f4, 0.0F + f3, 0.0F + f4, 1.0F + f4, 1.0F + f3, 1.0F + f4);
		this.setTickOnLoad(true);
	}

	public static float getFluidHeightPercent(int i0) {
		if(i0 >= 8) {
			i0 = 0;
		}

		float f1 = (float)(i0 + 1) / 9.0F;
		return f1;
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 != 0 && i1 != 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
	}

	protected int getFlowDecay(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z) != this.material ? -1 : world.getBlockMetadata(x, y, z);
	}

	protected int getEffectiveFlowDecay(IBlockAccess blockAccess, int x, int y, int z) {
		if(blockAccess.getBlockMaterial(x, y, z) != this.material) {
			return -1;
		} else {
			int i5 = blockAccess.getBlockMetadata(x, y, z);
			if(i5 >= 8) {
				i5 = 0;
			}

			return i5;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canCollideCheck(int i1, boolean z2) {
		return z2 && i1 == 0;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		Material material6 = iBlockAccess1.getBlockMaterial(i2, i3, i4);
		return material6 == this.material ? false : (material6 == Material.ice ? false : (i5 == 1 ? true : super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5)));
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public int getRenderType() {
		return 4;
	}

	public int idDropped(int i1, Random random2) {
		return 0;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	private Vec3D getFlowVector(IBlockAccess blockAccess, int x, int y, int z) {
		Vec3D vec3D5 = Vec3D.createVector(0.0D, 0.0D, 0.0D);
		int i6 = this.getEffectiveFlowDecay(blockAccess, x, y, z);

		for(int i7 = 0; i7 < 4; ++i7) {
			int i8 = x;
			int i10 = z;
			if(i7 == 0) {
				i8 = x - 1;
			}

			if(i7 == 1) {
				i10 = z - 1;
			}

			if(i7 == 2) {
				++i8;
			}

			if(i7 == 3) {
				++i10;
			}

			int i11 = this.getEffectiveFlowDecay(blockAccess, i8, y, i10);
			int i12;
			if(i11 < 0) {
				if(!blockAccess.getBlockMaterial(i8, y, i10).getIsSolid()) {
					i11 = this.getEffectiveFlowDecay(blockAccess, i8, y - 1, i10);
					if(i11 >= 0) {
						i12 = i11 - (i6 - 8);
						vec3D5 = vec3D5.addVector((double)((i8 - x) * i12), (double)((y - y) * i12), (double)((i10 - z) * i12));
					}
				}
			} else if(i11 >= 0) {
				i12 = i11 - i6;
				vec3D5 = vec3D5.addVector((double)((i8 - x) * i12), (double)((y - y) * i12), (double)((i10 - z) * i12));
			}
		}

		if(blockAccess.getBlockMetadata(x, y, z) >= 8) {
			boolean z13 = false;
			if(z13 || this.shouldSideBeRendered(blockAccess, x, y, z - 1, 2)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x, y, z + 1, 3)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x - 1, y, z, 4)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x + 1, y, z, 5)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x, y + 1, z - 1, 2)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x, y + 1, z + 1, 3)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x - 1, y + 1, z, 4)) {
				z13 = true;
			}

			if(z13 || this.shouldSideBeRendered(blockAccess, x + 1, y + 1, z, 5)) {
				z13 = true;
			}

			if(z13) {
				vec3D5 = vec3D5.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}

		vec3D5 = vec3D5.normalize();
		return vec3D5;
	}

	public void velocityToAddToEntity(World world1, int i2, int i3, int i4, Entity entity5, Vec3D vec3D6) {
		Vec3D vec3D7 = this.getFlowVector(world1, i2, i3, i4);
		vec3D6.xCoord += vec3D7.xCoord;
		vec3D6.yCoord += vec3D7.yCoord;
		vec3D6.zCoord += vec3D7.zCoord;
	}

	public int tickRate() {
		return this.material == Material.water ? 5 : (this.material == Material.lava ? 30 : 0);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		this.checkForHarden(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.checkForHarden(world1, i2, i3, i4);
	}

	private void checkForHarden(World world, int x, int y, int z) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			if(this.material == Material.lava) {
				boolean z5 = false;
				if(z5 || world.getBlockMaterial(x, y, z - 1) == Material.water) {
					z5 = true;
				}

				if(z5 || world.getBlockMaterial(x, y, z + 1) == Material.water) {
					z5 = true;
				}

				if(z5 || world.getBlockMaterial(x - 1, y, z) == Material.water) {
					z5 = true;
				}

				if(z5 || world.getBlockMaterial(x + 1, y, z) == Material.water) {
					z5 = true;
				}

				if(z5 || world.getBlockMaterial(x, y + 1, z) == Material.water) {
					z5 = true;
				}

				if(z5) {
					int i6 = world.getBlockMetadata(x, y, z);
					if(i6 == 0) {
						world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
					} else if(i6 <= 4) {
						world.setBlockWithNotify(x, y, z, Block.cobblestone.blockID);
					}

					this.triggerLavaMixEffects(world, x, y, z);
				}
			}

		}
	}

	protected void triggerLavaMixEffects(World world, int x, int y, int z) {
		world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		for(int i5 = 0; i5 < 8; ++i5) {
			world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 1.2D, (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
		}

	}
}
