package net.minecraft.src;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class RenderList {
	private int posX;
	private int posY;
	private int posZ;
	private float playerPosX;
	private float playerPosY;
	private float playerPosZ;
	private IntBuffer buffer = GLAllocation.createDirectIntBuffer(65536);
	private boolean render = false;
	private boolean isCached = false;

	public void setLocation(int i1, int i2, int i3, double d4, double d6, double d8) {
		this.render = true;
		this.buffer.clear();
		this.posX = i1;
		this.posY = i2;
		this.posZ = i3;
		this.playerPosX = (float)d4;
		this.playerPosY = (float)d6;
		this.playerPosZ = (float)d8;
	}

	public boolean isRenderedAt(int i1, int i2, int i3) {
		return !this.render ? false : i1 == this.posX && i2 == this.posY && i3 == this.posZ;
	}

	public void render(int i1) {
		this.buffer.put(i1);
		if(this.buffer.remaining() == 0) {
			this.render();
		}

	}

	public void render() {
		if(this.render) {
			if(!this.isCached) {
				this.buffer.flip();
				this.isCached = true;
			}

			if(this.buffer.remaining() > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)this.posX - this.playerPosX, (float)this.posY - this.playerPosY, (float)this.posZ - this.playerPosZ);
				GL11.glCallLists(this.buffer);
				GL11.glPopMatrix();
			}

		}
	}

	public void reset() {
		this.render = false;
		this.isCached = false;
	}
}
