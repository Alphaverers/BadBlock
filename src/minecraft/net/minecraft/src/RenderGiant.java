package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderGiant extends RenderLiving {
	private float f;

	public RenderGiant(ModelBase modelBase1, float f2, float f3) {
		super(modelBase1, f2 * f3);
		this.f = f3;
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		GL11.glScalef(this.f, this.f, this.f);
	}
}
