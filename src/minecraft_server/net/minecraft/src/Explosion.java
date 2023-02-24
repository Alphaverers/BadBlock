package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Explosion {
	public void doExplosion(World world, Entity entity, double x, double y, double z, float power) {
		world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet hashSet10 = new HashSet();
		float f11 = power;
		byte b12 = 16;

		int i13;
		int i14;
		int i15;
		double d25;
		double d27;
		double d29;
		for(i13 = 0; i13 < b12; ++i13) {
			for(i14 = 0; i14 < b12; ++i14) {
				for(i15 = 0; i15 < b12; ++i15) {
					if(i13 == 0 || i13 == b12 - 1 || i14 == 0 || i14 == b12 - 1 || i15 == 0 || i15 == b12 - 1) {
						double d16 = (double)((float)i13 / ((float)b12 - 1.0F) * 2.0F - 1.0F);
						double d18 = (double)((float)i14 / ((float)b12 - 1.0F) * 2.0F - 1.0F);
						double d20 = (double)((float)i15 / ((float)b12 - 1.0F) * 2.0F - 1.0F);
						double d22 = Math.sqrt(d16 * d16 + d18 * d18 + d20 * d20);
						d16 /= d22;
						d18 /= d22;
						d20 /= d22;
						float f24 = power * (0.7F + world.rand.nextFloat() * 0.6F);
						d25 = x;
						d27 = y;
						d29 = z;

						for(float f31 = 0.3F; f24 > 0.0F; f24 -= f31 * 0.75F) {
							int i32 = MathHelper.floor_double(d25);
							int i33 = MathHelper.floor_double(d27);
							int i34 = MathHelper.floor_double(d29);
							int i35 = world.getBlockId(i32, i33, i34);
							if(i35 > 0) {
								f24 -= (Block.blockList[i35].getExplosionResistance(entity) + 0.3F) * f31;
							}

							if(f24 > 0.0F) {
								hashSet10.add(new ChunkPosition(i32, i33, i34));
							}

							d25 += d16 * (double)f31;
							d27 += d18 * (double)f31;
							d29 += d20 * (double)f31;
						}
					}
				}
			}
		}

		power *= 2.0F;
		i13 = MathHelper.floor_double(x - (double)power - 1.0D);
		i14 = MathHelper.floor_double(x + (double)power + 1.0D);
		i15 = MathHelper.floor_double(y - (double)power - 1.0D);
		int i45 = MathHelper.floor_double(y + (double)power + 1.0D);
		int i17 = MathHelper.floor_double(z - (double)power - 1.0D);
		int i46 = MathHelper.floor_double(z + (double)power + 1.0D);
		List list19 = world.getEntitiesWithinAABBExcludingEntity(entity, AxisAlignedBB.getBoundingBoxFromPool((double)i13, (double)i15, (double)i17, (double)i14, (double)i45, (double)i46));
		Vec3D vec3D47 = Vec3D.createVector(x, y, z);

		double d55;
		double d56;
		double d57;
		for(int i21 = 0; i21 < list19.size(); ++i21) {
			Entity entity49 = (Entity)list19.get(i21);
			double d23 = entity49.getDistance(x, y, z) / (double)power;
			if(d23 <= 1.0D) {
				d25 = entity49.posX - x;
				d27 = entity49.posY - y;
				d29 = entity49.posZ - z;
				d55 = (double)MathHelper.sqrt_double(d25 * d25 + d27 * d27 + d29 * d29);
				d25 /= d55;
				d27 /= d55;
				d29 /= d55;
				d56 = (double)world.getBlockDensity(vec3D47, entity49.boundingBox);
				d57 = (1.0D - d23) * d56;
				entity49.attackEntityFrom(entity, (int)((d57 * d57 + d57) / 2.0D * 8.0D * (double)power + 1.0D));
				entity49.motionX += d25 * d57;
				entity49.motionY += d27 * d57;
				entity49.motionZ += d29 * d57;
			}
		}

		power = f11;
		ArrayList arrayList48 = new ArrayList();
		arrayList48.addAll(hashSet10);

		for(int i50 = arrayList48.size() - 1; i50 >= 0; --i50) {
			ChunkPosition chunkPosition51 = (ChunkPosition)arrayList48.get(i50);
			int i52 = chunkPosition51.x;
			int i53 = chunkPosition51.y;
			int i26 = chunkPosition51.z;
			int i54 = world.getBlockId(i52, i53, i26);

			for(int i28 = 0; i28 < 1; ++i28) {
				d29 = (double)((float)i52 + world.rand.nextFloat());
				d55 = (double)((float)i53 + world.rand.nextFloat());
				d56 = (double)((float)i26 + world.rand.nextFloat());
				d57 = d29 - x;
				double d37 = d55 - y;
				double d39 = d56 - z;
				double d41 = (double)MathHelper.sqrt_double(d57 * d57 + d37 * d37 + d39 * d39);
				d57 /= d41;
				d37 /= d41;
				d39 /= d41;
				double d43 = 0.5D / (d41 / (double)power + 0.1D);
				d43 *= (double)(world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
				d57 *= d43;
				d37 *= d43;
				d39 *= d43;
				world.spawnParticle("explode", (d29 + x * 1.0D) / 2.0D, (d55 + y * 1.0D) / 2.0D, (d56 + z * 1.0D) / 2.0D, d57, d37, d39);
				world.spawnParticle("smoke", d29, d55, d56, d57, d37, d39);
			}

			if(i54 > 0) {
				Block.blockList[i54].dropBlockAsItemWithChance(world, i52, i53, i26, world.getBlockMetadata(i52, i53, i26), 0.3F);
				world.setBlockWithNotify(i52, i53, i26, 0);
				Block.blockList[i54].onBlockDestroyedByExplosion(world, i52, i53, i26);
			}
		}

	}
}
