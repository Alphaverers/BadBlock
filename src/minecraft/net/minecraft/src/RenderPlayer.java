package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold", "obsidian"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

	protected boolean setArmorModel(EntityPlayer entityPlayer1, int i2) {
		ItemStack itemStack3 = entityPlayer1.inventory.armorItemInSlot(3 - i2);
		if(itemStack3 != null) {
			Item item4 = itemStack3.getItem();
			if(item4 instanceof ItemArmor) {
				ItemArmor itemArmor5 = (ItemArmor)item4;
				this.loadTexture("/armor/" + armorFilenamePrefix[itemArmor5.renderIndex] + "_" + (i2 == 2 ? 2 : 1) + ".png");
				ModelBiped modelBiped6 = i2 == 2 ? this.modelArmor : this.modelArmorChestplate;
				modelBiped6.bipedHead.showModel = i2 == 0;
				modelBiped6.bipedHeadwear.showModel = i2 == 0;
				modelBiped6.bipedBody.showModel = i2 == 1 || i2 == 2;
				modelBiped6.bipedRightArm.showModel = i2 == 1;
				modelBiped6.bipedLeftArm.showModel = i2 == 1;
				modelBiped6.bipedRightLeg.showModel = i2 == 2 || i2 == 3;
				modelBiped6.bipedLeftLeg.showModel = i2 == 2 || i2 == 3;
				this.setRenderPassModel(modelBiped6);
				return true;
			}
		}

		return false;
	}

	public void renderPlayer(EntityPlayer entityPlayer1, double d2, double d4, double d6, float f8, float f9) {
		ItemStack itemStack10 = entityPlayer1.inventory.getCurrentItem();
		ModelBiped modelBiped11 = (ModelBiped)this.mainModel;
		modelBiped11.heldItemRight = itemStack10 != null;
		super.doRenderLiving(entityPlayer1, d2, d4 - (double)entityPlayer1.yOffset, d6, f8, f9);
		modelBiped11.heldItemRight = false;
		FontRenderer fontRenderer12 = this.getFontRendererFromRenderManager();
		float f13 = 1.6F;
		float f14 = 0.016666668F * f13;
		if(!entityPlayer1.username.startsWith("\u0107") && !entityPlayer1.username.startsWith("\u015b")) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d2 + 0.0F, (float)d4 + 2.3F, (float)d6);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			float f15 = entityPlayer1.getDistanceToEntity(this.renderManager.player);
			f14 = (float)((double)f14 * (Math.sqrt((double)f15) / 2.0D));
			GL11.glScalef(-f14, -f14, f14);
			String string16 = entityPlayer1.username;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator17 = Tessellator.instance;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator17.startDrawingQuads();
			int i18 = fontRenderer12.getStringWidth(string16) / 2;
			tessellator17.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator17.addVertex((double)(-i18 - 1), -1.0D, 0.0D);
			tessellator17.addVertex((double)(-i18 - 1), 8.0D, 0.0D);
			tessellator17.addVertex((double)(i18 + 1), 8.0D, 0.0D);
			tessellator17.addVertex((double)(i18 + 1), -1.0D, 0.0D);
			tessellator17.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			fontRenderer12.drawString(string16, -fontRenderer12.getStringWidth(string16) / 2, 0, 553648127);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			fontRenderer12.drawString(string16, -fontRenderer12.getStringWidth(string16) / 2, 0, -1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	protected void renderSpecials(EntityPlayer entityPlayer1, float f2) {
		ItemStack itemStack3 = entityPlayer1.inventory.getCurrentItem();
		if(itemStack3 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.renderWithRotation(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
			float f4;
			if(itemStack3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack3.itemID].getRenderType())) {
				f4 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f4 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
			} else if(Item.itemsList[itemStack3.itemID].isFull3D()) {
				f4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f4 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f4, f4, f4);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(itemStack3);
			GL11.glPopMatrix();
		}

	}

	protected void scalePlayer(EntityPlayer entityPlayer1, float f2) {
		float f3 = 0.9375F;
		GL11.glScalef(f3, f3, f3);
	}

	public void drawFirstPersonHand() {
		this.modelBipedMain.swingProgress = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		this.scalePlayer((EntityPlayer)entityLiving1, f2);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return this.setArmorModel((EntityPlayer)entityLiving1, i2);
	}

	protected void renderEquippedItems(EntityLiving entityLiving1, float f2) {
		this.renderSpecials((EntityPlayer)entityLiving1, f2);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderPlayer((EntityPlayer)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderPlayer((EntityPlayer)entity1, d2, d4, d6, f8, f9);
	}
}
