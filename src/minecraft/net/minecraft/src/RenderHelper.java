package net.minecraft.src;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

public class RenderHelper {
	private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

	public static void disableStandardItemLighting() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
	}

	public static void enableStandardItemLighting() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		float f0 = 0.4F;
		float f1 = 0.6F;
		float f2 = 0.0F;
		Vec3D vec3D3 = Vec3D.createVector((double)0.2F, 1.0D, -0.699999988079071D).normalize();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(vec3D3.xCoord, vec3D3.yCoord, vec3D3.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(f2, f2, f2, 1.0F));
		vec3D3 = Vec3D.createVector(-0.20000000298023224D, 1.0D, (double)0.7F).normalize();
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(vec3D3.xCoord, vec3D3.yCoord, vec3D3.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(f2, f2, f2, 1.0F));
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(f0, f0, f0, 1.0F));
	}

	private static FloatBuffer setColorBuffer(double d0, double d2, double d4, double d6) {
		return setColorBuffer((float)d0, (float)d2, (float)d4, (float)d6);
	}

	private static FloatBuffer setColorBuffer(float f0, float f1, float f2, float f3) {
		colorBuffer.clear();
		colorBuffer.put(f0).put(f1).put(f2).put(f3);
		colorBuffer.flip();
		return colorBuffer;
	}
}
