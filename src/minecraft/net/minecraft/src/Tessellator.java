package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL15;

public class Tessellator {
	private static boolean convertQuadsToTriangles = true;
	private static boolean tryVBO = false;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private int[] rawBuffer;
	private int vertexCount = 0;
	private double textureU;
	private double textureV;
	private int color;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private boolean hasNormals = false;
	private int rawBufferIndex = 0;
	private int addedVertices = 0;
	private boolean isColorDisabled = false;
	private int drawMode;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private int normal;
	public static final Tessellator instance = new Tessellator(2097152);
	private boolean isDrawing = false;
	private boolean useVBO = false;
	private IntBuffer vertexBuffers;
	private int vboIndex = 0;
	private int vboCount = 10;
	private int bufferSize;

	private Tessellator(int i1) {
		this.bufferSize = i1;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(i1 * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.rawBuffer = new int[i1];
		this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
		if(this.useVBO) {
			this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

	}

	public void draw() {
		if(!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.isDrawing = false;
			if(this.vertexCount > 0) {
				this.intBuffer.clear();
				this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
				this.byteBuffer.position(0);
				this.byteBuffer.limit(this.rawBufferIndex * 4);
				if(this.useVBO) {
					this.vboIndex = (this.vboIndex + 1) % this.vboCount;
					ARBVertexBufferObject.glBindBufferARB(GL15.GL_ARRAY_BUFFER, this.vertexBuffers.get(this.vboIndex));
					ARBVertexBufferObject.glBufferDataARB(GL15.GL_ARRAY_BUFFER, this.byteBuffer, GL15.GL_STREAM_DRAW);
				}

				if(this.hasTexture) {
					if(this.useVBO) {
						GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
					} else {
						this.floatBuffer.position(3);
						GL11.glTexCoordPointer(2, 32, this.floatBuffer);
					}

					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if(this.hasColor) {
					if(this.useVBO) {
						GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
					} else {
						this.byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				}

				if(this.hasNormals) {
					if(this.useVBO) {
						GL11.glNormalPointer(GL11.GL_BYTE, 32, 24L);
					} else {
						this.byteBuffer.position(24);
						GL11.glNormalPointer(32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				}

				if(this.useVBO) {
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
				} else {
					this.floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, this.floatBuffer);
				}

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				if(this.drawMode == 7 && convertQuadsToTriangles) {
					GL11.glDrawArrays(GL11.GL_TRIANGLES, GL11.GL_POINTS, this.vertexCount);
				} else {
					GL11.glDrawArrays(this.drawMode, GL11.GL_POINTS, this.vertexCount);
				}

				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				if(this.hasTexture) {
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if(this.hasColor) {
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				}

				if(this.hasNormals) {
					GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
				}
			}

			this.reset();
		}
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.rawBufferIndex = 0;
		this.addedVertices = 0;
	}

	public void startDrawingQuads() {
		this.startDrawing(7);
	}

	public void startDrawing(int i1) {
		if(this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = i1;
			this.hasNormals = false;
			this.hasColor = false;
			this.hasTexture = false;
			this.isColorDisabled = false;
		}
	}

	public void setTextureUV(double d1, double d3) {
		this.hasTexture = true;
		this.textureU = d1;
		this.textureV = d3;
	}

	public void setColorOpaque_F(float f1, float f2, float f3) {
		this.setColorOpaque((int)(f1 * 255.0F), (int)(f2 * 255.0F), (int)(f3 * 255.0F));
	}

	public void setColorRGBA_F(float f1, float f2, float f3, float f4) {
		this.setColorRGBA((int)(f1 * 255.0F), (int)(f2 * 255.0F), (int)(f3 * 255.0F), (int)(f4 * 255.0F));
	}

	public void setColorOpaque(int i1, int i2, int i3) {
		this.setColorRGBA(i1, i2, i3, 255);
	}

	public void setColorRGBA(int i1, int i2, int i3, int i4) {
		if(!this.isColorDisabled) {
			if(i1 > 255) {
				i1 = 255;
			}

			if(i2 > 255) {
				i2 = 255;
			}

			if(i3 > 255) {
				i3 = 255;
			}

			if(i4 > 255) {
				i4 = 255;
			}

			if(i1 < 0) {
				i1 = 0;
			}

			if(i2 < 0) {
				i2 = 0;
			}

			if(i3 < 0) {
				i3 = 0;
			}

			if(i4 < 0) {
				i4 = 0;
			}

			this.hasColor = true;
			this.color = i4 << 24 | i3 << 16 | i2 << 8 | i1;
		}
	}

	public void addVertexWithUV(double d1, double d3, double d5, double d7, double d9) {
		this.setTextureUV(d7, d9);
		this.addVertex(d1, d3, d5);
	}

	public void addVertex(double d1, double d3, double d5) {
		++this.addedVertices;
		if(this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
			for(int i7 = 0; i7 < 2; ++i7) {
				int i8 = 8 * (3 - i7);
				if(this.hasTexture) {
					this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - i8 + 3];
					this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - i8 + 4];
				}

				if(this.hasColor) {
					this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - i8 + 5];
				}

				this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - i8 + 0];
				this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - i8 + 1];
				this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - i8 + 2];
				++this.vertexCount;
				this.rawBufferIndex += 8;
			}
		}

		if(this.hasTexture) {
			this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
			this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
		}

		if(this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		if(this.hasNormals) {
			this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
		}

		this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(d1 + this.xOffset));
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(d3 + this.yOffset));
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(d5 + this.zOffset));
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}

	}

	public void setColorOpaque_I(int i1) {
		int i2 = i1 >> 16 & 255;
		int i3 = i1 >> 8 & 255;
		int i4 = i1 & 255;
		this.setColorOpaque(i2, i3, i4);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float f1, float f2, float f3) {
		if(!this.isDrawing) {
			System.out.println("But..");
		}

		this.hasNormals = true;
		byte b4 = (byte)((int)(f1 * 128.0F));
		byte b5 = (byte)((int)(f2 * 127.0F));
		byte b6 = (byte)((int)(f3 * 127.0F));
		this.normal = b4 | b5 << 8 | b6 << 16;
	}

	public void setTranslationD(double d1, double d3, double d5) {
		this.xOffset = d1;
		this.yOffset = d3;
		this.zOffset = d5;
	}

	public void setTranslationF(float f1, float f2, float f3) {
		this.xOffset += (double)f1;
		this.yOffset += (double)f2;
		this.zOffset += (double)f3;
	}
}
