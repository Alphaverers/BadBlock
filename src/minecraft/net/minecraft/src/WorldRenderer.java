package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class WorldRenderer {
	public World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	public int posX;
	public int posY;
	public int posZ;
	public int sizeWidth;
	public int sizeHeight;
	public int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean[] skipRenderPass = new boolean[2];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public float rendererRadius;
	public boolean needsUpdate;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	private boolean isInitialized = false;
	public List tileEntityRenderers = new ArrayList();
	private List tileEntities;

	public WorldRenderer(World world1, List list2, int i3, int i4, int i5, int i6, int i7) {
		this.worldObj = world1;
		this.tileEntities = list2;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = i6;
		this.rendererRadius = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
		this.glRenderList = i7;
		this.posX = -999;
		this.setPosition(i3, i4, i5);
		this.needsUpdate = false;
	}

	public void setPosition(int i1, int i2, int i3) {
		if(i1 != this.posX || i2 != this.posY || i3 != this.posZ) {
			this.setDontDraw();
			this.posX = i1;
			this.posY = i2;
			this.posZ = i3;
			this.posXPlus = i1 + this.sizeWidth / 2;
			this.posYPlus = i2 + this.sizeHeight / 2;
			this.posZPlus = i3 + this.sizeDepth / 2;
			this.posXClip = i1 & 1023;
			this.posYClip = i2;
			this.posZClip = i3 & 1023;
			this.posXMinus = i1 - this.posXClip;
			this.posYMinus = i2 - this.posYClip;
			this.posZMinus = i3 - this.posZClip;
			float f4 = 2.0F;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)i1 - f4), (double)((float)i2 - f4), (double)((float)i3 - f4), (double)((float)(i1 + this.sizeWidth) + f4), (double)((float)(i2 + this.sizeHeight) + f4), (double)((float)(i3 + this.sizeDepth) + f4));
			GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
			RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - f4), (double)((float)this.posYClip - f4), (double)((float)this.posZClip - f4), (double)((float)(this.posXClip + this.sizeWidth) + f4), (double)((float)(this.posYClip + this.sizeHeight) + f4), (double)((float)(this.posZClip + this.sizeDepth) + f4)));
			GL11.glEndList();
			this.markDirty();
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
	}

	public void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

			for(int i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			Chunk.isLit = false;
			HashSet hashSet21 = new HashSet();
			hashSet21.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			byte b8 = 1;
			ChunkCache chunkCache9 = new ChunkCache(this.worldObj, i1 - b8, i2 - b8, i3 - b8, i4 + b8, i5 + b8, i6 + b8);
			RenderBlocks renderBlocks10 = new RenderBlocks(chunkCache9);

			for(int i11 = 0; i11 < 2; ++i11) {
				boolean z12 = false;
				boolean z13 = false;
				boolean z14 = false;

				for(int i15 = i2; i15 < i5; ++i15) {
					for(int i16 = i3; i16 < i6; ++i16) {
						for(int i17 = i1; i17 < i4; ++i17) {
							int i18 = chunkCache9.getBlockId(i17, i15, i16);
							if(i18 > 0) {
								if(!z14) {
									z14 = true;
									GL11.glNewList(this.glRenderList + i11, GL11.GL_COMPILE);
									GL11.glPushMatrix();
									this.setupGLTranslation();
									float f19 = 1.000001F;
									GL11.glTranslatef((float)(-this.sizeDepth) / 2.0F, (float)(-this.sizeHeight) / 2.0F, (float)(-this.sizeDepth) / 2.0F);
									GL11.glScalef(f19, f19, f19);
									GL11.glTranslatef((float)this.sizeDepth / 2.0F, (float)this.sizeHeight / 2.0F, (float)this.sizeDepth / 2.0F);
									tessellator.startDrawingQuads();
									tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
								}

								if(i11 == 0 && Block.isBlockContainer[i18]) {
									TileEntity tileEntity23 = chunkCache9.getBlockTileEntity(i17, i15, i16);
									if(TileEntityRenderer.instance.hasSpecialRenderer(tileEntity23)) {
										this.tileEntityRenderers.add(tileEntity23);
									}
								}

								Block block24 = Block.blocksList[i18];
								int i20 = block24.getRenderBlockPass();
								if(i20 != i11) {
									z12 = true;
								} else if(i20 == i11) {
									z13 |= renderBlocks10.renderBlockByRenderType(block24, i17, i15, i16);
								}
							}
						}
					}
				}

				if(z14) {
					tessellator.draw();
					GL11.glPopMatrix();
					GL11.glEndList();
					tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
				} else {
					z13 = false;
				}

				if(z13) {
					this.skipRenderPass[i11] = false;
				}

				if(!z12) {
					break;
				}
			}

			HashSet hashSet22 = new HashSet();
			hashSet22.addAll(this.tileEntityRenderers);
			hashSet22.removeAll(hashSet21);
			this.tileEntities.addAll(hashSet22);
			hashSet21.removeAll(this.tileEntityRenderers);
			this.tileEntities.removeAll(hashSet21);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
		}
	}

	public float distanceToEntitySquared(Entity entity) {
		float f2 = (float)(entity.posX - (double)this.posXPlus);
		float f3 = (float)(entity.posY - (double)this.posYPlus);
		float f4 = (float)(entity.posZ - (double)this.posZPlus);
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	public void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

		this.isInFrustum = false;
		this.isInitialized = false;
	}

	public void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int i1) {
		return !this.isInFrustum ? -1 : (!this.skipRenderPass[i1] ? this.glRenderList + i1 : -1);
	}

	public void updateInFrustum(ICamera iCamera1) {
		this.isInFrustum = iCamera1.isBoundingBoxInFrustum(this.rendererBoundingBox);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
		return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1];
	}

	public void markDirty() {
		this.needsUpdate = true;
	}
}
