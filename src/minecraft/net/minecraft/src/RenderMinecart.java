package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMinecart extends Render {
	protected ModelBase modelMinecart;

	public RenderMinecart() {
		this.shadowSize = 0.5F;
		this.modelMinecart = new ModelMinecart();
	}

	public void renderMinecart(EntityMinecart entityMinecart1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		double d10 = entityMinecart1.lastTickPosX + (entityMinecart1.posX - entityMinecart1.lastTickPosX) * (double)f9;
		double d12 = entityMinecart1.lastTickPosY + (entityMinecart1.posY - entityMinecart1.lastTickPosY) * (double)f9;
		double d14 = entityMinecart1.lastTickPosZ + (entityMinecart1.posZ - entityMinecart1.lastTickPosZ) * (double)f9;
		double d16 = (double)0.3F;
		Vec3D vec3D18 = entityMinecart1.getPos(d10, d12, d14);
		float f19 = entityMinecart1.prevRotationPitch + (entityMinecart1.rotationPitch - entityMinecart1.prevRotationPitch) * f9;
		if(vec3D18 != null) {
			Vec3D vec3D20 = entityMinecart1.getPosOffset(d10, d12, d14, d16);
			Vec3D vec3D21 = entityMinecart1.getPosOffset(d10, d12, d14, -d16);
			if(vec3D20 == null) {
				vec3D20 = vec3D18;
			}

			if(vec3D21 == null) {
				vec3D21 = vec3D18;
			}

			d2 += vec3D18.xCoord - d10;
			d4 += (vec3D20.yCoord + vec3D21.yCoord) / 2.0D - d12;
			d6 += vec3D18.zCoord - d14;
			Vec3D vec3D22 = vec3D21.addVector(-vec3D20.xCoord, -vec3D20.yCoord, -vec3D20.zCoord);
			if(vec3D22.lengthVector() != 0.0D) {
				vec3D22 = vec3D22.normalize();
				f8 = (float)(Math.atan2(vec3D22.zCoord, vec3D22.xCoord) * 180.0D / Math.PI);
				f19 = (float)(Math.atan(vec3D22.yCoord) * 73.0D);
			}
		}

		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glRotatef(180.0F - f8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f19, 0.0F, 0.0F, 1.0F);
		float f23 = (float)entityMinecart1.timeSinceHit - f9;
		float f24 = (float)entityMinecart1.damageTaken - f9;
		if(f24 < 0.0F) {
			f24 = 0.0F;
		}

		if(f23 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f23) * f23 * f24 / 10.0F * (float)entityMinecart1.forwardDirection, 1.0F, 0.0F, 0.0F);
		}

		if(entityMinecart1.minecartType != 0) {
			this.loadTexture("/terrain.png");
			float f25 = 0.75F;
			GL11.glScalef(f25, f25, f25);
			GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			if(entityMinecart1.minecartType == 1) {
				(new RenderBlocks()).renderBlockOnInventory(Block.chest);
			} else if(entityMinecart1.minecartType == 2) {
				(new RenderBlocks()).renderBlockOnInventory(Block.stoneOvenIdle);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
			GL11.glScalef(1.0F / f25, 1.0F / f25, 1.0F / f25);
		}

		this.loadTexture("/item/cart.png");
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderMinecart((EntityMinecart)entity1, d2, d4, d6, f8, f9);
	}
}
