package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer {
	private SignModel modelSign = new SignModel();

	public void renderTileEntitySignAt(TileEntitySign tileEntitySign1, double d2, double d4, double d6, float f8) {
		Block block9 = tileEntitySign1.getBlockType();
		GL11.glPushMatrix();
		float f10 = 0.6666667F;
		float f12;
		if(block9 == Block.signStanding) {
			GL11.glTranslatef((float)d2 + 0.5F, (float)d4 + 0.75F * f10, (float)d6 + 0.5F);
			float f11 = (float)(tileEntitySign1.getBlockMetadata() * 360) / 16.0F;
			GL11.glRotatef(-f11, 0.0F, 1.0F, 0.0F);
			this.modelSign.signStick.showModel = true;
		} else {
			int i16 = tileEntitySign1.getBlockMetadata();
			f12 = 0.0F;
			if(i16 == 2) {
				f12 = 180.0F;
			}

			if(i16 == 4) {
				f12 = 90.0F;
			}

			if(i16 == 5) {
				f12 = -90.0F;
			}

			GL11.glTranslatef((float)d2 + 0.5F, (float)d4 + 0.75F * f10, (float)d6 + 0.5F);
			GL11.glRotatef(-f12, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
			this.modelSign.signStick.showModel = false;
		}

		this.bindTextureByName("/item/sign.png");
		GL11.glPushMatrix();
		GL11.glScalef(f10, -f10, -f10);
		this.modelSign.renderSign();
		GL11.glPopMatrix();
		FontRenderer fontRenderer17 = this.getFontRenderer();
		f12 = 0.016666668F * f10;
		GL11.glTranslatef(0.0F, 0.5F * f10, 0.07F * f10);
		GL11.glScalef(f12, -f12, f12);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f12);
		GL11.glDepthMask(false);
		byte b13 = 0;

		for(int i14 = 0; i14 < tileEntitySign1.signText.length; ++i14) {
			String string15 = tileEntitySign1.signText[i14];
			if(i14 == tileEntitySign1.lineBeingEdited) {
				string15 = "> " + string15 + " <";
				fontRenderer17.drawString(string15, -fontRenderer17.getStringWidth(string15) / 2, i14 * 10 - tileEntitySign1.signText.length * 5, b13);
			} else {
				fontRenderer17.drawString(string15, -fontRenderer17.getStringWidth(string15) / 2, i14 * 10 - tileEntitySign1.signText.length * 5, b13);
			}
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
		this.renderTileEntitySignAt((TileEntitySign)tileEntity1, d2, d4, d6, f8);
	}
}
