package net.minecraft.src;

import java.awt.Component;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class MouseHelper {
	private Component windowComponent;
	private Cursor cursor;
	public int deltaX;
	public int deltaY;
	private int mouseInt = 10;
	private long lastUpdate = -1L;

	public MouseHelper(Component component) {
		this.windowComponent = component;
		IntBuffer intBuffer2 = GLAllocation.createDirectIntBuffer(1);
		intBuffer2.put(0);
		intBuffer2.flip();
		IntBuffer intBuffer3 = GLAllocation.createDirectIntBuffer(1024);

		try {
			this.cursor = new Cursor(32, 32, 16, 16, 1, intBuffer3, intBuffer2);
		} catch (LWJGLException lWJGLException5) {
			lWJGLException5.printStackTrace();
		}

	}

	public void grabMouseCursor() {
		Mouse.setGrabbed(true);
		this.deltaX = 0;
		this.deltaY = 0;
	}

	public void ungrabMouseCursor() {
		Mouse.setCursorPosition(this.windowComponent.getWidth() / 2, this.windowComponent.getHeight() / 2);
		Mouse.setGrabbed(false);
	}

	public void mouseXYChange() {
		if(this.lastUpdate == -1L) {
			this.lastUpdate = System.currentTimeMillis();
		}

		float f1 = (float)(System.currentTimeMillis() - this.lastUpdate) / 1000.0F;
		this.lastUpdate = System.currentTimeMillis();
		if(InputHandler.gamepads != null) {
			for(int i2 = 0; i2 != InputHandler.gamepads.length; ++i2) {
				if(InputHandler.gamepads[i2] != null && (InputHandler.gamepads[i2].getRXAxisValue() != 0.0F || InputHandler.gamepads[i2].getRYAxisValue() != 0.0F) && (InputHandler.gamepads[i2].getRXAxisValue() != -1.0F || InputHandler.gamepads[i2].getRYAxisValue() != -1.0F)) {
					this.deltaX = (int)(InputHandler.gamepads[i2].getRXAxisValue() * 500.0F * (float)InputHandler.lookSens * f1);
					if(this.deltaX > -1 && this.deltaX < 1) {
						this.deltaX = 0;
					}

					this.deltaY = (int)(-InputHandler.gamepads[i2].getRYAxisValue() * 250.0F * (float)InputHandler.lookSens * f1);
					if(this.deltaY > -1 && this.deltaY < 1) {
						this.deltaY = 0;
					}

					if(this.deltaX != 0 || this.deltaY != 0) {
						return;
					}
				}
			}
		}

		this.deltaX = Mouse.getDX();
		this.deltaY = Mouse.getDY();
	}
}
