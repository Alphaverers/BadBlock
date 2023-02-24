package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Explosion {
	public void doExplosion(World world1, Entity entity2, double d3, double d5, double d7, float f9) {
		world1.playSoundEffect(d3, d5, d7, "random.explode", 4.0F, (1.0F + (world1.rand.nextFloat() - world1.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet hashSet10 = new HashSet();
		float f11 = f9;
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
						float f24 = f9 * (0.7F + world1.rand.nextFloat() * 0.6F);
						d25 = d3;
						d27 = d5;
						d29 = d7;

						for(float f31 = 0.3F; f24 > 0.0F; f24 -= f31 * 0.75F) {
							int i32 = MathHelper.floor_double(d25);
							int i33 = MathHelper.floor_double(d27);
							int i34 = MathHelper.floor_double(d29);
							int i35 = world1.getBlockId(i32, i33, i34);
							if(i35 > 0) {
								f24 -= (Block.blocksList[i35].getExplosionResistance(entity2) + 0.3F) * f31;
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

		f9 *= 2.0F;
		i13 = MathHelper.floor_double(d3 - (double)f9 - 1.0D);
		i14 = MathHelper.floor_double(d3 + (double)f9 + 1.0D);
		i15 = MathHelper.floor_double(d5 - (double)f9 - 1.0D);
		int i45 = MathHelper.floor_double(d5 + (double)f9 + 1.0D);
		int i17 = MathHelper.floor_double(d7 - (double)f9 - 1.0D);
		int i46 = MathHelper.floor_double(d7 + (double)f9 + 1.0D);
		List list19 = world1.getEntitiesWithinAABBExcludingEntity(entity2, AxisAlignedBB.getBoundingBoxFromPool((double)i13, (double)i15, (double)i17, (double)i14, (double)i45, (double)i46));
		Vec3D vec3D47 = Vec3D.createVector(d3, d5, d7);

		double d55;
		double d56;
		double d57;
		for(int i21 = 0; i21 < list19.size(); ++i21) {
			Entity entity49 = (Entity)list19.get(i21);
			double d23 = entity49.getDistance(d3, d5, d7) / (double)f9;
			if(d23 <= 1.0D) {
				d25 = entity49.posX - d3;
				d27 = entity49.posY - d5;
				d29 = entity49.posZ - d7;
				d55 = (double)MathHelper.sqrt_double(d25 * d25 + d27 * d27 + d29 * d29);
				d25 /= d55;
				d27 /= d55;
				d29 /= d55;
				d56 = (double)world1.getBlockDensity(vec3D47, entity49.boundingBox);
				d57 = (1.0D - d23) * d56;
				entity49.attackEntityFrom(entity2, (int)((d57 * d57 + d57) / 2.0D * 8.0D * (double)f9 + 1.0D));
				entity49.motionX += d25 * d57;
				entity49.motionY += d27 * d57;
				entity49.motionZ += d29 * d57;
			}
		}

		f9 = f11;
		ArrayList arrayList48 = new ArrayList();
		arrayList48.addAll(hashSet10);

		for(int i50 = arrayList48.size() - 1; i50 >= 0; --i50) {
			ChunkPosition chunkPosition51 = (ChunkPosition)arrayList48.get(i50);
			int i52 = chunkPosition51.x;
			int i53 = chunkPosition51.y;
			int i26 = chunkPosition51.z;
			int i54 = world1.getBlockId(i52, i53, i26);

			for(int i28 = 0; i28 < 1; ++i28) {
				d29 = (double)((float)i52 + world1.rand.nextFloat());
				d55 = (double)((float)i53 + world1.rand.nextFloat());
				d56 = (double)((float)i26 + world1.rand.nextFloat());
				d57 = d29 - d3;
				double d37 = d55 - d5;
				double d39 = d56 - d7;
				double d41 = (double)MathHelper.sqrt_double(d57 * d57 + d37 * d37 + d39 * d39);
				d57 /= d41;
				d37 /= d41;
				d39 /= d41;
				double d43 = 0.5D / (d41 / (double)f9 + 0.1D);
				d43 *= (double)(world1.rand.nextFloat() * world1.rand.nextFloat() + 0.3F);
				d57 *= d43;
				d37 *= d43;
				d39 *= d43;
				world1.spawnParticle("explode", (d29 + d3 * 1.0D) / 2.0D, (d55 + d5 * 1.0D) / 2.0D, (d56 + d7 * 1.0D) / 2.0D, d57, d37, d39);
				world1.spawnParticle("smoke", d29, d55, d56, d57, d37, d39);
			}

			if(i54 > 0) {
				Block.blocksList[i54].dropBlockAsItemWithChance(world1, i52, i53, i26, world1.getBlockMetadata(i52, i53, i26), 0.3F);
				world1.setBlockWithNotify(i52, i53, i26, 0);
				Block.blocksList[i54].onBlockDestroyedByExplosion(world1, i52, i53, i26);
			}
		}

	}
}
