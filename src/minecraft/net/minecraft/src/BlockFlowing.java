package net.minecraft.src;

import java.util.Random;

public class BlockFlowing extends BlockFluid {
	int numAdjacentSources = 0;
	boolean[] isOptimalFlowDirection = new boolean[4];
	int[] flowCost = new int[4];

	protected BlockFlowing(int i1, Material material2) {
		super(i1, material2);
	}

	private void updateFlow(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		worldObj.setBlockAndMetadata(x, y, z, this.blockID + 1, i5);
		worldObj.markBlocksDirty(x, y, z, x, y, z);
		worldObj.markBlockNeedsUpdate(x, y, z);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		int i6 = this.getFlowDecay(worldObj, x, y, z);
		boolean z7 = true;
		int i9;
		if(i6 > 0) {
			byte b8 = -100;
			this.numAdjacentSources = 0;
			int i11 = this.getSmallestFlowDecay(worldObj, x - 1, y, z, b8);
			i11 = this.getSmallestFlowDecay(worldObj, x + 1, y, z, i11);
			i11 = this.getSmallestFlowDecay(worldObj, x, y, z - 1, i11);
			i11 = this.getSmallestFlowDecay(worldObj, x, y, z + 1, i11);
			i9 = i11 + this.fluidType;
			if(i9 >= 8 || i11 < 0) {
				i9 = -1;
			}

			if(this.getFlowDecay(worldObj, x, y + 1, z) >= 0) {
				int i10 = this.getFlowDecay(worldObj, x, y + 1, z);
				if(i10 >= 8) {
					i9 = i10;
				} else {
					i9 = i10 + 8;
				}
			}

			if(this.numAdjacentSources >= 2 && this.material == Material.water) {
				if(worldObj.isBlockNormalCube(x, y - 1, z)) {
					i9 = 0;
				} else if(worldObj.getBlockMaterial(x, y - 1, z) == this.material && worldObj.getBlockMetadata(x, y, z) == 0) {
					i9 = 0;
				}
			}

			if(this.material == Material.lava && i6 < 8 && i9 < 8 && i9 > i6 && rand.nextInt(4) != 0) {
				i9 = i6;
				z7 = false;
			}

			if(i9 != i6) {
				i6 = i9;
				if(i9 < 0) {
					worldObj.setBlockWithNotify(x, y, z, 0);
				} else {
					worldObj.setBlockMetadataWithNotify(x, y, z, i9);
					worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
					worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				}
			} else if(z7) {
				this.updateFlow(worldObj, x, y, z);
			}
		} else {
			this.updateFlow(worldObj, x, y, z);
		}

		if(this.liquidCanDisplaceBlock(worldObj, x, y - 1, z)) {
			if(i6 >= 8) {
				worldObj.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i6);
			} else {
				worldObj.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i6 + 8);
			}
		} else if(i6 >= 0 && (i6 == 0 || this.blockBlocksFlow(worldObj, x, y - 1, z))) {
			boolean[] z12 = this.getOptimalFlowDirections(worldObj, x, y, z);
			i9 = i6 + this.fluidType;
			if(i6 >= 8) {
				i9 = 1;
			}

			if(i9 >= 8) {
				return;
			}

			if(z12[0]) {
				this.flowIntoBlock(worldObj, x - 1, y, z, i9);
			}

			if(z12[1]) {
				this.flowIntoBlock(worldObj, x + 1, y, z, i9);
			}

			if(z12[2]) {
				this.flowIntoBlock(worldObj, x, y, z - 1, i9);
			}

			if(z12[3]) {
				this.flowIntoBlock(worldObj, x, y, z + 1, i9);
			}
		}

	}

	private void flowIntoBlock(World worldObj, int x, int y, int z, int metadata) {
		if(this.liquidCanDisplaceBlock(worldObj, x, y, z)) {
			int i6 = worldObj.getBlockId(x, y, z);
			if(i6 > 0) {
				if(this.material == Material.lava) {
					this.triggerLavaMixEffects(worldObj, x, y, z);
				} else {
					Block.blocksList[i6].dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
				}
			}

			worldObj.setBlockAndMetadataWithNotify(x, y, z, this.blockID, metadata);
		}

	}

	private int calculateFlowCost(World worldObj, int x, int y, int z, int i5, int i6) {
		int i7 = 1000;

		for(int i8 = 0; i8 < 4; ++i8) {
			if((i8 != 0 || i6 != 1) && (i8 != 1 || i6 != 0) && (i8 != 2 || i6 != 3) && (i8 != 3 || i6 != 2)) {
				int i9 = x;
				int i11 = z;
				if(i8 == 0) {
					i9 = x - 1;
				}

				if(i8 == 1) {
					++i9;
				}

				if(i8 == 2) {
					i11 = z - 1;
				}

				if(i8 == 3) {
					++i11;
				}

				if(!this.blockBlocksFlow(worldObj, i9, y, i11) && (worldObj.getBlockMaterial(i9, y, i11) != this.material || worldObj.getBlockMetadata(i9, y, i11) != 0)) {
					if(!this.blockBlocksFlow(worldObj, i9, y - 1, i11)) {
						return i5;
					}

					if(i5 < 4) {
						int i12 = this.calculateFlowCost(worldObj, i9, y, i11, i5 + 1, i8);
						if(i12 < i7) {
							i7 = i12;
						}
					}
				}
			}
		}

		return i7;
	}

	private boolean[] getOptimalFlowDirections(World worldObj, int x, int y, int z) {
		int i5;
		int i6;
		for(i5 = 0; i5 < 4; ++i5) {
			this.flowCost[i5] = 1000;
			i6 = x;
			int i8 = z;
			if(i5 == 0) {
				i6 = x - 1;
			}

			if(i5 == 1) {
				++i6;
			}

			if(i5 == 2) {
				i8 = z - 1;
			}

			if(i5 == 3) {
				++i8;
			}

			if(!this.blockBlocksFlow(worldObj, i6, y, i8) && (worldObj.getBlockMaterial(i6, y, i8) != this.material || worldObj.getBlockMetadata(i6, y, i8) != 0)) {
				if(!this.blockBlocksFlow(worldObj, i6, y - 1, i8)) {
					this.flowCost[i5] = 0;
				} else {
					this.flowCost[i5] = this.calculateFlowCost(worldObj, i6, y, i8, 1, i5);
				}
			}
		}

		i5 = this.flowCost[0];

		for(i6 = 1; i6 < 4; ++i6) {
			if(this.flowCost[i6] < i5) {
				i5 = this.flowCost[i6];
			}
		}

		for(i6 = 0; i6 < 4; ++i6) {
			this.isOptimalFlowDirection[i6] = this.flowCost[i6] == i5;
		}

		return this.isOptimalFlowDirection;
	}

	private boolean blockBlocksFlow(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockId(x, y, z);
		if(i5 != Block.doorWood.blockID && i5 != Block.doorSteel.blockID && i5 != Block.signStanding.blockID && i5 != Block.ladder.blockID && i5 != Block.reed.blockID) {
			if(i5 == 0) {
				return false;
			} else {
				Material material6 = Block.blocksList[i5].material;
				return material6.isSolid();
			}
		} else {
			return true;
		}
	}

	protected int getSmallestFlowDecay(World worldObj, int x, int y, int z, int i5) {
		int i6 = this.getFlowDecay(worldObj, x, y, z);
		if(i6 < 0) {
			return i5;
		} else {
			if(i6 == 0) {
				++this.numAdjacentSources;
			}

			if(i6 >= 8) {
				i6 = 0;
			}

			return i5 >= 0 && i6 >= i5 ? i5 : i6;
		}
	}

	private boolean liquidCanDisplaceBlock(World worldObj, int x, int y, int z) {
		Material material5 = worldObj.getBlockMaterial(x, y, z);
		return material5 == this.material ? false : (material5 == Material.lava ? false : !this.blockBlocksFlow(worldObj, x, y, z));
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		super.onBlockAdded(world1, i2, i3, i4);
		if(world1.getBlockId(i2, i3, i4) == this.blockID) {
			world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
		}

	}
}
