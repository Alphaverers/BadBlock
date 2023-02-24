package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLiving extends Render {
	protected ModelBase mainModel;
	protected ModelBase renderPassModel;

	public RenderLiving(ModelBase modelBase1, float f2) {
		this.mainModel = modelBase1;
		this.shadowSize = f2;
	}

	public void setRenderPassModel(ModelBase modelBase1) {
		this.renderPassModel = modelBase1;
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		this.mainModel.swingProgress = this.renderSwingProgress(entityLiving1, f9);
		this.mainModel.isRiding = entityLiving1.ridingEntity != null;
		if(this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		try {
			float f10 = entityLiving1.prevRenderYawOffset + (entityLiving1.renderYawOffset - entityLiving1.prevRenderYawOffset) * f9;
			float f11 = entityLiving1.prevRotationYaw + (entityLiving1.rotationYaw - entityLiving1.prevRotationYaw) * f9;
			float f12 = entityLiving1.prevRotationPitch + (entityLiving1.rotationPitch - entityLiving1.prevRotationPitch) * f9;
			GL11.glTranslatef((float)d2, (float)d4, (float)d6);
			float f13 = this.handleRotationFloat(entityLiving1, f9);
			GL11.glRotatef(180.0F - f10, 0.0F, 1.0F, 0.0F);
			float f14;
			if(entityLiving1.deathTime > 0) {
				f14 = ((float)entityLiving1.deathTime + f9 - 1.0F) / 20.0F * 1.6F;
				f14 = MathHelper.sqrt_float(f14);
				if(f14 > 1.0F) {
					f14 = 1.0F;
				}

				GL11.glRotatef(f14 * this.getDeathMaxRotation(entityLiving1), 0.0F, 0.0F, 1.0F);
			}

			f14 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entityLiving1, f9);
			GL11.glTranslatef(0.0F, -24.0F * f14 - 0.0078125F, 0.0F);
			float f15 = entityLiving1.prevLimbYaw + (entityLiving1.limbYaw - entityLiving1.prevLimbYaw) * f9;
			float f16 = entityLiving1.limbSwing - entityLiving1.limbYaw * (1.0F - f9);
			if(f15 > 1.0F) {
				f15 = 1.0F;
			}

			this.loadDownloadableImageTexture(entityLiving1.skinUrl, entityLiving1.getTexture());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.render(f16, f15, f13, f11 - f10, f12, f14);

			for(int i17 = 0; i17 < 4; ++i17) {
				if(this.shouldRenderPass(entityLiving1, i17)) {
					this.renderPassModel.render(f16, f15, f13, f11 - f10, f12, f14);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			this.renderEquippedItems(entityLiving1, f9);
			float f25 = entityLiving1.getBrightness(f9);
			int i18 = this.getColorMultiplier(entityLiving1, f25, f9);
			if((i18 >> 24 & 255) > 0 || entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
					GL11.glColor4f(f25, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(f16, f15, f13, f11 - f10, f12, f14);

					for(int i19 = 0; i19 < 4; ++i19) {
						if(this.shouldRenderPass(entityLiving1, i19)) {
							GL11.glColor4f(f25, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(f16, f15, f13, f11 - f10, f12, f14);
						}
					}
				}

				if((i18 >> 24 & 255) > 0) {
					float f26 = (float)(i18 >> 16 & 255) / 255.0F;
					float f20 = (float)(i18 >> 8 & 255) / 255.0F;
					float f21 = (float)(i18 & 255) / 255.0F;
					float f22 = (float)(i18 >> 24 & 255) / 255.0F;
					GL11.glColor4f(f26, f20, f21, f22);
					this.mainModel.render(f16, f15, f13, f11 - f10, f12, f14);

					for(int i23 = 0; i23 < 4; ++i23) {
						if(this.shouldRenderPass(entityLiving1, i23)) {
							GL11.glColor4f(f26, f20, f21, f22);
							this.renderPassModel.render(f16, f15, f13, f11 - f10, f12, f14);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception exception24) {
			exception24.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected float renderSwingProgress(EntityLiving entityLiving1, float f2) {
		return entityLiving1.getSwingProgress(f2);
	}

	protected float handleRotationFloat(EntityLiving entityLiving1, float f2) {
		return (float)entityLiving1.ticksExisted + f2;
	}

	protected void renderEquippedItems(EntityLiving entityLiving1, float f2) {
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving entityLiving1) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving entityLiving1, float f2, float f3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.doRenderLiving((EntityLiving)entity1, d2, d4, d6, f8, f9);
	}
}
