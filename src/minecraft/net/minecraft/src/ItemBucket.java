package net.minecraft.src;

public class ItemBucket extends Item {
	private int isFull;

	public ItemBucket(int id, int liquid) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 64;
		this.isFull = liquid;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		float f4 = 1.0F;
		float f5 = entityPlayer3.prevRotationPitch + (entityPlayer3.rotationPitch - entityPlayer3.prevRotationPitch) * f4;
		float f6 = entityPlayer3.prevRotationYaw + (entityPlayer3.rotationYaw - entityPlayer3.prevRotationYaw) * f4;
		double d7 = entityPlayer3.prevPosX + (entityPlayer3.posX - entityPlayer3.prevPosX) * (double)f4;
		double d9 = entityPlayer3.prevPosY + (entityPlayer3.posY - entityPlayer3.prevPosY) * (double)f4;
		double d11 = entityPlayer3.prevPosZ + (entityPlayer3.posZ - entityPlayer3.prevPosZ) * (double)f4;
		Vec3D vec3D13 = Vec3D.createVector(d7, d9, d11);
		float f14 = MathHelper.cos(-f6 * 0.017453292F - (float)Math.PI);
		float f15 = MathHelper.sin(-f6 * 0.017453292F - (float)Math.PI);
		float f16 = -MathHelper.cos(-f5 * 0.017453292F);
		float f17 = MathHelper.sin(-f5 * 0.017453292F);
		float f18 = f15 * f16;
		float f20 = f14 * f16;
		double d21 = 5.0D;
		Vec3D vec3D23 = vec3D13.addVector((double)f18 * d21, (double)f17 * d21, (double)f20 * d21);
		MovingObjectPosition movingObjectPosition24 = world2.rayTraceBlocks_do(vec3D13, vec3D23, this.isFull == 0);
		if(movingObjectPosition24 == null) {
			return itemStack1;
		} else {
			if(movingObjectPosition24.typeOfHit == 0) {
				int i25 = movingObjectPosition24.blockX;
				int i26 = movingObjectPosition24.blockY;
				int i27 = movingObjectPosition24.blockZ;
				if(this.isFull == 0) {
					if(world2.getBlockMaterial(i25, i26, i27) == Material.water && world2.getBlockMetadata(i25, i26, i27) == 0) {
						world2.setBlockWithNotify(i25, i26, i27, 0);
						return new ItemStack(Item.bucketWater);
					}

					if(world2.getBlockMaterial(i25, i26, i27) == Material.lava && world2.getBlockMetadata(i25, i26, i27) == 0) {
						world2.setBlockWithNotify(i25, i26, i27, 0);
						return new ItemStack(Item.bucketLava);
					}
				} else {
					if(this.isFull < 0) {
						return new ItemStack(Item.bucketEmpty);
					}

					if(movingObjectPosition24.sideHit == 0) {
						--i26;
					}

					if(movingObjectPosition24.sideHit == 1) {
						++i26;
					}

					if(movingObjectPosition24.sideHit == 2) {
						--i27;
					}

					if(movingObjectPosition24.sideHit == 3) {
						++i27;
					}

					if(movingObjectPosition24.sideHit == 4) {
						--i25;
					}

					if(movingObjectPosition24.sideHit == 5) {
						++i25;
					}

					if(world2.getBlockId(i25, i26, i27) == 0 || !world2.getBlockMaterial(i25, i26, i27).isSolid()) {
						world2.setBlockAndMetadataWithNotify(i25, i26, i27, this.isFull, 0);
						return new ItemStack(Item.bucketEmpty);
					}
				}
			} else if(this.isFull == 0 && movingObjectPosition24.entityHit instanceof EntityCow) {
				return new ItemStack(Item.bucketMilk);
			}

			return itemStack1;
		}
	}
}
