package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class EffectRenderer {
	protected World worldObj;
	private List[] fxLayers = new List[4];
	private RenderEngine renderEngine;
	private Random rand = new Random();

	public EffectRenderer(World world, RenderEngine renderEngine) {
		if(world != null) {
			this.worldObj = world;
		}

		this.renderEngine = renderEngine;

		for(int i3 = 0; i3 < 4; ++i3) {
			this.fxLayers[i3] = new ArrayList();
		}

	}

	public void addEffect(EntityFX entityFX) {
		int i2 = entityFX.getFXLayer();
		this.fxLayers[i2].add(entityFX);
	}

	public void updateEffects() {
		for(int i1 = 0; i1 < 4; ++i1) {
			for(int i2 = 0; i2 < this.fxLayers[i1].size(); ++i2) {
				EntityFX entityFX3 = (EntityFX)this.fxLayers[i1].get(i2);
				entityFX3.onUpdate();
				if(entityFX3.isDead) {
					this.fxLayers[i1].remove(i2--);
				}
			}
		}

	}

	public void renderParticles(Entity viewerEntity, float renderPartialTick) {
		float f3 = MathHelper.cos(viewerEntity.rotationYaw * (float)Math.PI / 180.0F);
		float f4 = MathHelper.sin(viewerEntity.rotationYaw * (float)Math.PI / 180.0F);
		float f5 = -f4 * MathHelper.sin(viewerEntity.rotationPitch * (float)Math.PI / 180.0F);
		float f6 = f3 * MathHelper.sin(viewerEntity.rotationPitch * (float)Math.PI / 180.0F);
		float f7 = MathHelper.cos(viewerEntity.rotationPitch * (float)Math.PI / 180.0F);
		EntityFX.interpPosX = viewerEntity.lastTickPosX + (viewerEntity.posX - viewerEntity.lastTickPosX) * (double)renderPartialTick;
		EntityFX.interpPosY = viewerEntity.lastTickPosY + (viewerEntity.posY - viewerEntity.lastTickPosY) * (double)renderPartialTick;
		EntityFX.interpPosZ = viewerEntity.lastTickPosZ + (viewerEntity.posZ - viewerEntity.lastTickPosZ) * (double)renderPartialTick;

		for(int i8 = 0; i8 < 3; ++i8) {
			if(this.fxLayers[i8].size() != 0) {
				int i9 = 0;
				if(i8 == 0) {
					i9 = this.renderEngine.getTexture("/particles.png");
				}

				if(i8 == 1) {
					i9 = this.renderEngine.getTexture("/terrain.png");
				}

				if(i8 == 2) {
					i9 = this.renderEngine.getTexture("/gui/items.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i9);
				Tessellator tessellator10 = Tessellator.instance;
				tessellator10.startDrawingQuads();

				for(int i11 = 0; i11 < this.fxLayers[i8].size(); ++i11) {
					EntityFX entityFX12 = (EntityFX)this.fxLayers[i8].get(i11);
					entityFX12.renderParticle(tessellator10, renderPartialTick, f3, f7, f4, f5, f6);
				}

				tessellator10.draw();
			}
		}

	}

	public void renderLitParticles(Entity entity, float renderPartialTick) {
		byte b3 = 3;
		if(this.fxLayers[b3].size() != 0) {
			Tessellator tessellator4 = Tessellator.instance;

			for(int i5 = 0; i5 < this.fxLayers[b3].size(); ++i5) {
				EntityFX entityFX6 = (EntityFX)this.fxLayers[b3].get(i5);
				entityFX6.renderParticle(tessellator4, renderPartialTick, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public void clearEffects(World worldObj) {
		this.worldObj = worldObj;

		for(int i2 = 0; i2 < 4; ++i2) {
			this.fxLayers[i2].clear();
		}

	}

	public void addBlockDestroyEffects(int x, int y, int z) {
		int i4 = this.worldObj.getBlockId(x, y, z);
		if(i4 != 0) {
			Block block5 = Block.blocksList[i4];
			byte b6 = 4;

			for(int i7 = 0; i7 < b6; ++i7) {
				for(int i8 = 0; i8 < b6; ++i8) {
					for(int i9 = 0; i9 < b6; ++i9) {
						double d10 = (double)x + ((double)i7 + 0.5D) / (double)b6;
						double d12 = (double)y + ((double)i8 + 0.5D) / (double)b6;
						double d14 = (double)z + ((double)i9 + 0.5D) / (double)b6;
						this.addEffect(new EntityDiggingFX(this.worldObj, d10, d12, d14, d10 - (double)x - 0.5D, d12 - (double)y - 0.5D, d14 - (double)z - 0.5D, block5));
					}
				}
			}

		}
	}

	public void addBlockHitEffects(int x, int y, int z, int face) {
		int i5 = this.worldObj.getBlockId(x, y, z);
		if(i5 != 0) {
			Block block6 = Block.blocksList[i5];
			float f7 = 0.1F;
			double d8 = (double)x + this.rand.nextDouble() * (block6.maxX - block6.minX - (double)(f7 * 2.0F)) + (double)f7 + block6.minX;
			double d10 = (double)y + this.rand.nextDouble() * (block6.maxY - block6.minY - (double)(f7 * 2.0F)) + (double)f7 + block6.minY;
			double d12 = (double)z + this.rand.nextDouble() * (block6.maxZ - block6.minZ - (double)(f7 * 2.0F)) + (double)f7 + block6.minZ;
			if(face == 0) {
				d10 = (double)y + block6.minY - (double)f7;
			}

			if(face == 1) {
				d10 = (double)y + block6.maxY + (double)f7;
			}

			if(face == 2) {
				d12 = (double)z + block6.minZ - (double)f7;
			}

			if(face == 3) {
				d12 = (double)z + block6.maxZ + (double)f7;
			}

			if(face == 4) {
				d8 = (double)x + block6.minX - (double)f7;
			}

			if(face == 5) {
				d8 = (double)x + block6.maxX + (double)f7;
			}

			this.addEffect((new EntityDiggingFX(this.worldObj, d8, d10, d12, 0.0D, 0.0D, 0.0D, block6)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
