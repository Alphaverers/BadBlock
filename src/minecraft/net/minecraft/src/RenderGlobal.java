package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class RenderGlobal implements IWorldAccess {
	public List tileEntities = new ArrayList();
	private World theWorld;
	private RenderEngine renderEngine;
	private List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	private Minecraft mc;
	private RenderBlocks globalRenderBlocks;
	private IntBuffer glOcclusionQueryBase;
	private boolean occlusionEnabled = false;
	private int cloudTickCounter = 0;
	private int starGLCallList;
	private int glSkyList;
	private int glSkyList2;
	private int minBlockX;
	private int minBlockY;
	private int minBlockZ;
	private int maxBlockX;
	private int maxBlockY;
	private int maxBlockZ;
	private int renderDistance = -1;
	private int renderEntitiesStartupCounter = 2;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	int[] dummyBuf50k = new int[50000];
	IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private int renderersSkippingRenderPass;
	private List glRenderLists = new ArrayList();
	private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};
	int dummyRenderInt = 0;
	int unusedGLCallList = GLAllocation.generateDisplayLists(1);
	double prevSortX = -9999.0D;
	double prevSortY = -9999.0D;
	double prevSortZ = -9999.0D;
	public float damagePartialTime;
	int frustumCheckOffset = 0;

	public RenderGlobal(Minecraft minecraft1, RenderEngine renderEngine2) {
		this.mc = minecraft1;
		this.renderEngine = renderEngine2;
		this.glRenderListBase = GLAllocation.generateDisplayLists(786432);
		this.occlusionEnabled = minecraft1.getOpenGlCapsChecker().checkARBOcclusion();
		if(this.occlusionEnabled) {
			this.occlusionResult.clear();
			(this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(262144)).clear();
			this.glOcclusionQueryBase.position(0);
			this.glOcclusionQueryBase.limit(262144);
			ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
		}

		this.starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glNewList(this.glSkyList = this.starGLCallList + 1, GL11.GL_COMPILE);

		int i9;
		for(int i8 = -384; i8 <= 384; i8 += 64) {
			for(i9 = -384; i9 <= 384; i9 += 64) {
				tessellator4.startDrawingQuads();
				tessellator4.addVertex((double)(i8 + 0), 16.0D, (double)(i9 + 0));
				tessellator4.addVertex((double)(i8 + 64), 16.0D, (double)(i9 + 0));
				tessellator4.addVertex((double)(i8 + 64), 16.0D, (double)(i9 + 64));
				tessellator4.addVertex((double)(i8 + 0), 16.0D, (double)(i9 + 64));
				tessellator4.draw();
			}
		}

		GL11.glEndList();
		GL11.glNewList(this.glSkyList2 = this.starGLCallList + 2, GL11.GL_COMPILE);
		tessellator4.startDrawingQuads();

		for(i9 = -384; i9 <= 384; i9 += 64) {
			for(int i10 = -384; i10 <= 384; i10 += 64) {
				tessellator4.addVertex((double)(i9 + 64), -16.0D, (double)(i10 + 0));
				tessellator4.addVertex((double)(i9 + 0), -16.0D, (double)(i10 + 0));
				tessellator4.addVertex((double)(i9 + 0), -16.0D, (double)(i10 + 64));
				tessellator4.addVertex((double)(i9 + 64), -16.0D, (double)(i10 + 64));
			}
		}

		tessellator4.draw();
		GL11.glEndList();
	}

	private void renderStars() {
		Random random1 = new Random(10842L);
		Tessellator tessellator2 = Tessellator.instance;
		tessellator2.startDrawingQuads();

		for(int i3 = 0; i3 < 1500; ++i3) {
			double d4 = (double)(random1.nextFloat() * 2.0F - 1.0F);
			double d6 = (double)(random1.nextFloat() * 2.0F - 1.0F);
			double d8 = (double)(random1.nextFloat() * 2.0F - 1.0F);
			double d10 = (double)(0.25F + random1.nextFloat() * 0.25F);
			double d12 = d4 * d4 + d6 * d6 + d8 * d8;
			if(d12 < 1.0D && d12 > 0.01D) {
				double d14 = 1.0D / Math.sqrt(d12);
				double d16 = d4 * d14;
				double d18 = d6 * d14;
				double d20 = d8 * d14;
				double d22 = d16 * 100.0D;
				double d24 = d18 * 100.0D;
				double d26 = d20 * 100.0D;
				double d28 = Math.atan2(d16, d20);
				double d30 = Math.sin(d28);
				double d32 = Math.cos(d28);
				double d34 = Math.atan2(Math.sqrt(d16 * d16 + d20 * d20), d18);
				double d36 = Math.sin(d34);
				double d38 = Math.cos(d34);
				double d40 = random1.nextDouble() * Math.PI * 2.0D;
				double d42 = Math.sin(d40);
				double d44 = Math.cos(d40);

				for(int i46 = 0; i46 < 4; ++i46) {
					double d49 = (double)((i46 & 2) - 1) * d10;
					double d51 = (double)((i46 + 1 & 2) - 1) * d10;
					double d55 = d49 * d44 - d51 * d42;
					double d57 = d51 * d44 + d49 * d42;
					double d59 = d55 * d36 + 0.0D * d38;
					double d61 = 0.0D * d36 - d55 * d38;
					tessellator2.addVertex(d22 + (d61 * d30 - d57 * d32), d24 + d59, d26 + d57 * d30 + d61 * d32);
				}
			}
		}

		tessellator2.draw();
	}

	public void changeWorld(World world1) {
		if(this.theWorld != null) {
			this.theWorld.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0D;
		this.prevSortY = -9999.0D;
		this.prevSortZ = -9999.0D;
		RenderManager.instance.set(world1);
		this.theWorld = world1;
		this.globalRenderBlocks = new RenderBlocks(world1);
		if(world1 != null) {
			world1.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public void loadRenderers() {
		Block.leaves.setGraphicsLevel(this.mc.options.fancyGraphics);
		this.renderDistance = this.mc.options.renderDistance;
		int i1;
		if(this.worldRenderers != null) {
			for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
				this.worldRenderers[i1].stopRendering();
			}
		}

		i1 = 64 << 3 - this.renderDistance;
		if(i1 > 400) {
			i1 = 400;
		}

		this.renderChunksWide = i1 / 16 + 1;
		this.renderChunksTall = 8;
		this.renderChunksDeep = i1 / 16 + 1;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		int i2 = 0;
		int i3 = 0;
		this.minBlockX = 0;
		this.minBlockY = 0;
		this.minBlockZ = 0;
		this.maxBlockX = this.renderChunksWide;
		this.maxBlockY = this.renderChunksTall;
		this.maxBlockZ = this.renderChunksDeep;

		int i4;
		for(i4 = 0; i4 < this.worldRenderersToUpdate.size(); ++i4) {
			((WorldRenderer)this.worldRenderersToUpdate.get(i4)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		this.tileEntities.clear();

		for(i4 = 0; i4 < this.renderChunksWide; ++i4) {
			for(int i5 = 0; i5 < this.renderChunksTall; ++i5) {
				for(int i6 = 0; i6 < this.renderChunksDeep; ++i6) {
					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4] = new WorldRenderer(this.theWorld, this.tileEntities, i4 * 16, i5 * 16, i6 * 16, 16, this.glRenderListBase + i2);
					if(this.occlusionEnabled) {
						this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].glOcclusionQuery = this.glOcclusionQueryBase.get(i3);
					}

					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].isWaitingOnOcclusionQuery = false;
					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].isVisible = true;
					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].isInFrustum = true;
					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].chunkIndex = i3++;
					this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4].markDirty();
					this.sortedWorldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4] = this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4];
					this.worldRenderersToUpdate.add(this.worldRenderers[(i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4]);
					i2 += 3;
				}
			}
		}

		if(this.theWorld != null) {
			EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
			this.markRenderersForNewPosition(MathHelper.floor_double(entityPlayerSP7.posX), MathHelper.floor_double(entityPlayerSP7.posY), MathHelper.floor_double(entityPlayerSP7.posZ));
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayerSP7));
		}

		this.renderEntitiesStartupCounter = 2;
	}

	public void renderEntities(Vec3D vector, ICamera camera, float renderPartialTick) {
		if(this.renderEntitiesStartupCounter > 0) {
			--this.renderEntitiesStartupCounter;
		} else {
			TileEntityRenderer.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, renderPartialTick);
			RenderManager.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, this.mc.options, renderPartialTick);
			this.countEntitiesTotal = 0;
			this.countEntitiesRendered = 0;
			this.countEntitiesHidden = 0;
			EntityPlayerSP entityPlayerSP4 = this.mc.thePlayer;
			RenderManager.renderPosX = entityPlayerSP4.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * (double)renderPartialTick;
			RenderManager.renderPosY = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * (double)renderPartialTick;
			RenderManager.renderPosZ = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * (double)renderPartialTick;
			TileEntityRenderer.staticPlayerX = entityPlayerSP4.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * (double)renderPartialTick;
			TileEntityRenderer.staticPlayerY = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * (double)renderPartialTick;
			TileEntityRenderer.staticPlayerZ = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * (double)renderPartialTick;
			List list5 = this.theWorld.getLoadedEntityList();
			this.countEntitiesTotal = list5.size();

			int i6;
			for(i6 = 0; i6 < list5.size(); ++i6) {
				Entity entity7 = (Entity)list5.get(i6);
				if(entity7.isInRangeToRenderVec3D(vector) && camera.isBoundingBoxInFrustum(entity7.boundingBox) && (entity7 != this.mc.thePlayer || this.mc.options.thirdPersonView)) {
					++this.countEntitiesRendered;
					RenderManager.instance.renderEntity(entity7, renderPartialTick);
				}
			}

			for(i6 = 0; i6 < this.tileEntities.size(); ++i6) {
				TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(i6), renderPartialTick);
			}

		}
	}

	public String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
	}

	public String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	private void markRenderersForNewPosition(int i1, int i2, int i3) {
		i1 -= 8;
		i2 -= 8;
		i3 -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;
		int i4 = this.renderChunksWide * 16;
		int i5 = i4 / 2;

		for(int i6 = 0; i6 < this.renderChunksWide; ++i6) {
			int i7 = i6 * 16;
			int i8 = i7 + i5 - i1;
			if(i8 < 0) {
				i8 -= i4 - 1;
			}

			int i9 = i7 - i8 / i4 * i4;
			if(i9 < this.minBlockX) {
				this.minBlockX = i9;
			}

			if(i9 > this.maxBlockX) {
				this.maxBlockX = i9;
			}

			for(int i10 = 0; i10 < this.renderChunksDeep; ++i10) {
				int i11 = i10 * 16;
				int i12 = i11 + i5 - i3;
				if(i12 < 0) {
					i12 -= i4 - 1;
				}

				int i13 = i11 - i12 / i4 * i4;
				if(i13 < this.minBlockZ) {
					this.minBlockZ = i13;
				}

				if(i13 > this.maxBlockZ) {
					this.maxBlockZ = i13;
				}

				for(int i14 = 0; i14 < this.renderChunksTall; ++i14) {
					int i15 = i14 * 16;
					if(i15 < this.minBlockY) {
						this.minBlockY = i15;
					}

					if(i15 > this.maxBlockY) {
						this.maxBlockY = i15;
					}

					WorldRenderer worldRenderer16 = this.worldRenderers[(i10 * this.renderChunksTall + i14) * this.renderChunksWide + i6];
					boolean z17 = worldRenderer16.needsUpdate;
					worldRenderer16.setPosition(i9, i15, i13);
					if(!z17 && worldRenderer16.needsUpdate) {
						this.worldRenderersToUpdate.add(worldRenderer16);
					}
				}
			}
		}

	}

	public int sortAndRender(EntityPlayer entityPlayer1, int i2, double d3) {
		if(this.mc.options.renderDistance != this.renderDistance) {
			this.loadRenderers();
		}

		if(i2 == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
			this.renderersSkippingRenderPass = 0;
		}

		double d5 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * d3;
		double d7 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * d3;
		double d9 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * d3;
		double d11 = entityPlayer1.posX - this.prevSortX;
		double d13 = entityPlayer1.posY - this.prevSortY;
		double d15 = entityPlayer1.posZ - this.prevSortZ;
		if(d11 * d11 + d13 * d13 + d15 * d15 > 16.0D) {
			this.prevSortX = entityPlayer1.posX;
			this.prevSortY = entityPlayer1.posY;
			this.prevSortZ = entityPlayer1.posZ;
			this.markRenderersForNewPosition(MathHelper.floor_double(entityPlayer1.posX), MathHelper.floor_double(entityPlayer1.posY), MathHelper.floor_double(entityPlayer1.posZ));
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayer1));
		}

		int i18;
		if(this.occlusionEnabled && !this.mc.options.anaglyph && i2 == 0) {
			int i20 = 16;
			this.checkOcclusionQueryResult(0, i20);

			int i21;
			for(i21 = 0; i21 < i20; ++i21) {
				this.sortedWorldRenderers[i21].isVisible = true;
			}

			i18 = 0 + this.renderSortedRenderers(0, i20, i2, d3);

			do {
				i21 = i20;
				i20 *= 2;
				if(i20 > this.sortedWorldRenderers.length) {
					i20 = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.checkOcclusionQueryResult(i21, i20);
				GL11.glPushMatrix();
				float f22 = 0.0F;
				float f23 = 0.0F;
				float f24 = 0.0F;

				for(int i25 = i21; i25 < i20; ++i25) {
					if(this.sortedWorldRenderers[i25].skipAllRenderPasses()) {
						this.sortedWorldRenderers[i25].isInFrustum = false;
					} else {
						if(!this.sortedWorldRenderers[i25].isInFrustum) {
							this.sortedWorldRenderers[i25].isVisible = true;
						}

						if(this.sortedWorldRenderers[i25].isInFrustum && !this.sortedWorldRenderers[i25].isWaitingOnOcclusionQuery) {
							int i26 = (int)(1.0F + MathHelper.sqrt_float(this.sortedWorldRenderers[i25].distanceToEntitySquared(entityPlayer1)) / 128.0F);
							if(this.cloudTickCounter % i26 == i25 % i26) {
								WorldRenderer worldRenderer27 = this.sortedWorldRenderers[i25];
								float f28 = (float)((double)worldRenderer27.posXMinus - d5);
								float f29 = (float)((double)worldRenderer27.posYMinus - d7);
								float f30 = (float)((double)worldRenderer27.posZMinus - d9);
								float f31 = f28 - f22;
								float f32 = f29 - f23;
								float f33 = f30 - f24;
								if(f31 != 0.0F || f32 != 0.0F || f33 != 0.0F) {
									GL11.glTranslatef(f31, f32, f33);
									f22 += f31;
									f23 += f32;
									f24 += f33;
								}

								ARBOcclusionQuery.glBeginQueryARB(GL15.GL_SAMPLES_PASSED, this.sortedWorldRenderers[i25].glOcclusionQuery);
								this.sortedWorldRenderers[i25].callOcclusionQueryList();
								ARBOcclusionQuery.glEndQueryARB(GL15.GL_SAMPLES_PASSED);
								this.sortedWorldRenderers[i25].isWaitingOnOcclusionQuery = true;
							}
						}
					}
				}

				GL11.glPopMatrix();
				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
				i18 += this.renderSortedRenderers(i21, i20, i2, d3);
			} while(i20 < this.sortedWorldRenderers.length);
		} else {
			i18 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i2, d3);
		}

		return i18;
	}

	private void checkOcclusionQueryResult(int i1, int i2) {
		for(int i3 = i1; i3 < i2; ++i3) {
			if(this.sortedWorldRenderers[i3].isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i3].glOcclusionQuery, GL15.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
				if(this.occlusionResult.get(0) != 0) {
					this.sortedWorldRenderers[i3].isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i3].glOcclusionQuery, GL15.GL_QUERY_RESULT, this.occlusionResult);
					this.sortedWorldRenderers[i3].isVisible = this.occlusionResult.get(0) != 0;
				}
			}
		}

	}

	private int renderSortedRenderers(int i1, int i2, int i3, double d4) {
		this.glRenderLists.clear();
		int i6 = 0;

		for(int i7 = i1; i7 < i2; ++i7) {
			if(i3 == 0) {
				++this.renderersLoaded;
				if(this.sortedWorldRenderers[i7].skipRenderPass[i3]) {
					++this.renderersSkippingRenderPass;
				} else if(!this.sortedWorldRenderers[i7].isInFrustum) {
					++this.renderersBeingClipped;
				} else if(this.occlusionEnabled && !this.sortedWorldRenderers[i7].isVisible) {
					++this.renderersBeingOccluded;
				} else {
					++this.renderersBeingRendered;
				}
			}

			if(!this.sortedWorldRenderers[i7].skipRenderPass[i3] && this.sortedWorldRenderers[i7].isInFrustum && this.sortedWorldRenderers[i7].isVisible && this.sortedWorldRenderers[i7].getGLCallListForPass(i3) >= 0) {
				this.glRenderLists.add(this.sortedWorldRenderers[i7]);
				++i6;
			}
		}

		EntityPlayerSP entityPlayerSP19 = this.mc.thePlayer;
		double d8 = entityPlayerSP19.lastTickPosX + (entityPlayerSP19.posX - entityPlayerSP19.lastTickPosX) * d4;
		double d10 = entityPlayerSP19.lastTickPosY + (entityPlayerSP19.posY - entityPlayerSP19.lastTickPosY) * d4;
		double d12 = entityPlayerSP19.lastTickPosZ + (entityPlayerSP19.posZ - entityPlayerSP19.lastTickPosZ) * d4;
		int i14 = 0;

		int i15;
		for(i15 = 0; i15 < this.allRenderLists.length; ++i15) {
			this.allRenderLists[i15].reset();
		}

		for(i15 = 0; i15 < this.glRenderLists.size(); ++i15) {
			WorldRenderer worldRenderer16 = (WorldRenderer)this.glRenderLists.get(i15);
			int i17 = -1;

			for(int i18 = 0; i18 < i14; ++i18) {
				if(this.allRenderLists[i18].isRenderedAt(worldRenderer16.posXMinus, worldRenderer16.posYMinus, worldRenderer16.posZMinus)) {
					i17 = i18;
				}
			}

			if(i17 < 0) {
				i17 = i14++;
				this.allRenderLists[i17].setLocation(worldRenderer16.posXMinus, worldRenderer16.posYMinus, worldRenderer16.posZMinus, d8, d10, d12);
			}

			this.allRenderLists[i17].render(worldRenderer16.getGLCallListForPass(i3));
		}

		this.renderAllRenderLists(i3, d4);
		return i6;
	}

	public void renderAllRenderLists(int i1, double d2) {
		for(int i4 = 0; i4 < this.allRenderLists.length; ++i4) {
			this.allRenderLists[i4].render();
		}

	}

	public void updateClouds() {
		++this.cloudTickCounter;
	}

	public void renderSky(float renderPartialTick) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3D vec3D2 = this.theWorld.getSkyColor(renderPartialTick);
		float f3 = (float)vec3D2.xCoord;
		float f4 = (float)vec3D2.yCoord;
		float f5 = (float)vec3D2.zCoord;
		if(this.mc.options.anaglyph) {
			float f6 = (f3 * 30.0F + f4 * 59.0F + f5 * 11.0F) / 100.0F;
			float f7 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
			float f8 = (f3 * 30.0F + f5 * 70.0F) / 100.0F;
			f3 = f6;
			f4 = f7;
			f5 = f8;
		}

		GL11.glColor3f(f3, f4, f5);
		Tessellator tessellator13 = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glColor3f(f3, f4, f5);
		GL11.glCallList(this.glSkyList);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0.0F, 0.0F, 0.0F);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.theWorld.getCelestialAngle(renderPartialTick) * 360.0F, 1.0F, 0.0F, 0.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
		tessellator13.startDrawingQuads();
		tessellator13.addVertexWithUV(-30.0D, 100.0D, -30.0D, 0.0D, 0.0D);
		tessellator13.addVertexWithUV(30.0D, 100.0D, -30.0D, 1.0D, 0.0D);
		tessellator13.addVertexWithUV(30.0D, 100.0D, 30.0D, 1.0D, 1.0D);
		tessellator13.addVertexWithUV(-30.0D, 100.0D, 30.0D, 0.0D, 1.0D);
		tessellator13.draw();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
		tessellator13.startDrawingQuads();
		tessellator13.addVertexWithUV(-20.0D, -100.0D, 20.0D, 1.0D, 1.0D);
		tessellator13.addVertexWithUV(20.0D, -100.0D, 20.0D, 0.0D, 1.0D);
		tessellator13.addVertexWithUV(20.0D, -100.0D, -20.0D, 0.0D, 0.0D);
		tessellator13.addVertexWithUV(-20.0D, -100.0D, -20.0D, 1.0D, 0.0D);
		tessellator13.draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f12 = this.theWorld.getStarBrightness(renderPartialTick);
		if(f12 > 0.0F) {
			GL11.glColor4f(f12, f12, f12, f12);
			GL11.glCallList(this.starGLCallList);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glColor3f(f3 * 0.2F + 0.04F, f4 * 0.2F + 0.04F, f5 * 0.6F + 0.1F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glCallList(this.glSkyList2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);
	}

	public void renderClouds(float f1) {
		if(this.mc.options.fancyGraphics) {
			this.renderCloudsFancy(f1);
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
			float f2 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)f1);
			Tessellator tessellator5 = Tessellator.instance;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Vec3D vec3D6 = this.theWorld.getCloudColor(f1);
			float f7 = (float)vec3D6.xCoord;
			float f8 = (float)vec3D6.yCoord;
			float f9 = (float)vec3D6.zCoord;
			if(this.mc.options.anaglyph) {
				float f10 = (f7 * 30.0F + f8 * 59.0F + f9 * 11.0F) / 100.0F;
				float f11 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
				float f12 = (f7 * 30.0F + f9 * 70.0F) / 100.0F;
				f7 = f10;
				f8 = f11;
				f9 = f12;
			}

			double d26 = this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)f1 + (double)(((float)this.cloudTickCounter + f1) * 0.03F);
			double d13 = this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)f1;
			int i15 = MathHelper.floor_double(d26 / 2048.0D);
			int i16 = MathHelper.floor_double(d13 / 2048.0D);
			double d17 = d26 - (double)(i15 * 2048);
			double d19 = d13 - (double)(i16 * 2048);
			float f21 = 120.0F - f2 + 0.33F;
			float f22 = (float)(d17 * 4.8828125E-4D);
			float f23 = (float)(d19 * 4.8828125E-4D);
			tessellator5.startDrawingQuads();
			tessellator5.setColorRGBA_F(f7, f8, f9, 0.8F);

			for(int i24 = -256; i24 < 256; i24 += 32) {
				for(int i25 = -256; i25 < 256; i25 += 32) {
					tessellator5.addVertexWithUV((double)(i24 + 0), (double)f21, (double)(i25 + 32), (double)((float)(i24 + 0) * 4.8828125E-4F + f22), (double)((float)(i25 + 32) * 4.8828125E-4F + f23));
					tessellator5.addVertexWithUV((double)(i24 + 32), (double)f21, (double)(i25 + 32), (double)((float)(i24 + 32) * 4.8828125E-4F + f22), (double)((float)(i25 + 32) * 4.8828125E-4F + f23));
					tessellator5.addVertexWithUV((double)(i24 + 32), (double)f21, (double)(i25 + 0), (double)((float)(i24 + 32) * 4.8828125E-4F + f22), (double)((float)(i25 + 0) * 4.8828125E-4F + f23));
					tessellator5.addVertexWithUV((double)(i24 + 0), (double)f21, (double)(i25 + 0), (double)((float)(i24 + 0) * 4.8828125E-4F + f22), (double)((float)(i25 + 0) * 4.8828125E-4F + f23));
				}
			}

			tessellator5.draw();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	public void renderCloudsFancy(float f1) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f2 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)f1);
		Tessellator tessellator3 = Tessellator.instance;
		double d6 = (this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)f1 + (double)(((float)this.cloudTickCounter + f1) * 0.03F)) / 12.0D;
		double d8 = (this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)f1) / 12.0D + (double)0.33F;
		float f10 = 108.0F - f2 + 0.33F;
		int i11 = MathHelper.floor_double(d6 / 2048.0D);
		int i12 = MathHelper.floor_double(d8 / 2048.0D);
		double d13 = d6 - (double)(i11 * 2048);
		double d15 = d8 - (double)(i12 * 2048);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Vec3D vec3D17 = this.theWorld.getCloudColor(f1);
		float f18 = (float)vec3D17.xCoord;
		float f19 = (float)vec3D17.yCoord;
		float f20 = (float)vec3D17.zCoord;
		float f21;
		float f22;
		if(this.mc.options.anaglyph) {
			f21 = (f18 * 30.0F + f19 * 59.0F + f20 * 11.0F) / 100.0F;
			f22 = (f18 * 30.0F + f19 * 70.0F) / 100.0F;
			float f23 = (f18 * 30.0F + f20 * 70.0F) / 100.0F;
			f18 = f21;
			f19 = f22;
			f20 = f23;
		}

		f21 = (float)(d13 * 0.0D);
		f22 = (float)(d15 * 0.0D);
		float f24 = (float)MathHelper.floor_double(d13) * 0.00390625F;
		float f25 = (float)MathHelper.floor_double(d15) * 0.00390625F;
		float f26 = (float)(d13 - (double)MathHelper.floor_double(d13));
		float f27 = (float)(d15 - (double)MathHelper.floor_double(d15));
		GL11.glScalef(12.0F, 1.0F, 12.0F);

		for(int i31 = 0; i31 < 2; ++i31) {
			if(i31 == 0) {
				GL11.glColorMask(false, false, false, false);
			} else {
				GL11.glColorMask(true, true, true, true);
			}

			for(int i32 = -2; i32 <= 3; ++i32) {
				for(int i33 = -2; i33 <= 3; ++i33) {
					tessellator3.startDrawingQuads();
					float f34 = (float)(i32 * 8);
					float f35 = (float)(i33 * 8);
					float f36 = f34 - f26;
					float f37 = f35 - f27;
					if(f10 > -5.0F) {
						tessellator3.setColorRGBA_F(f18 * 0.7F, f19 * 0.7F, f20 * 0.7F, 0.8F);
						tessellator3.setNormal(0.0F, -1.0F, 0.0F);
						tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + 8.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 0.0F), (double)(f37 + 8.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 0.0F), (double)(f37 + 0.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + 0.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
					}

					if(f10 <= 5.0F) {
						tessellator3.setColorRGBA_F(f18, f19, f20, 0.8F);
						tessellator3.setNormal(0.0F, 1.0F, 0.0F);
						tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 4.0F - 9.765625E-4F), (double)(f37 + 8.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 4.0F - 9.765625E-4F), (double)(f37 + 8.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 4.0F - 9.765625E-4F), (double)(f37 + 0.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
						tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 4.0F - 9.765625E-4F), (double)(f37 + 0.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
					}

					tessellator3.setColorRGBA_F(f18 * 0.9F, f19 * 0.9F, f20 * 0.9F, 0.8F);
					int i38;
					if(i32 > -1) {
						tessellator3.setNormal(-1.0F, 0.0F, 0.0F);

						for(i38 = 0; i38 < 8; ++i38) {
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + 8.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 0.0F), (double)(f10 + 4.0F), (double)(f37 + 8.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 0.0F), (double)(f10 + 4.0F), (double)(f37 + 0.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + 0.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
						}
					}

					if(i32 <= 1) {
						tessellator3.setNormal(1.0F, 0.0F, 0.0F);

						for(i38 = 0; i38 < 8; ++i38) {
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 1.0F - 9.765625E-4F), (double)(f10 + 0.0F), (double)(f37 + 8.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 1.0F - 9.765625E-4F), (double)(f10 + 4.0F), (double)(f37 + 8.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 8.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 1.0F - 9.765625E-4F), (double)(f10 + 4.0F), (double)(f37 + 0.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + (float)i38 + 1.0F - 9.765625E-4F), (double)(f10 + 0.0F), (double)(f37 + 0.0F), (double)((f34 + (float)i38 + 0.5F) * 0.00390625F + f24), (double)((f35 + 0.0F) * 0.00390625F + f25));
						}
					}

					tessellator3.setColorRGBA_F(f18 * 0.8F, f19 * 0.8F, f20 * 0.8F, 0.8F);
					if(i33 > -1) {
						tessellator3.setNormal(0.0F, 0.0F, -1.0F);

						for(i38 = 0; i38 < 8; ++i38) {
							tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 4.0F), (double)(f37 + (float)i38 + 0.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 4.0F), (double)(f37 + (float)i38 + 0.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 0.0F), (double)(f37 + (float)i38 + 0.0F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + (float)i38 + 0.0F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
						}
					}

					if(i33 <= 1) {
						tessellator3.setNormal(0.0F, 0.0F, 1.0F);

						for(i38 = 0; i38 < 8; ++i38) {
							tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 4.0F), (double)(f37 + (float)i38 + 1.0F - 9.765625E-4F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 4.0F), (double)(f37 + (float)i38 + 1.0F - 9.765625E-4F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 8.0F), (double)(f10 + 0.0F), (double)(f37 + (float)i38 + 1.0F - 9.765625E-4F), (double)((f34 + 8.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
							tessellator3.addVertexWithUV((double)(f36 + 0.0F), (double)(f10 + 0.0F), (double)(f37 + (float)i38 + 1.0F - 9.765625E-4F), (double)((f34 + 0.0F) * 0.00390625F + f24), (double)((f35 + (float)i38 + 0.5F) * 0.00390625F + f25));
						}
					}

					tessellator3.draw();
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public boolean updateRenderers(EntityPlayer entityPlayer1, boolean z2) {
		try {
			Collections.sort(this.worldRenderersToUpdate, new RenderSorter(entityPlayer1));
		} catch (IllegalArgumentException illegalArgumentException7) {
			System.out.println("NOT THIS TIME TIMOTHY");
		}

		int i3 = this.worldRenderersToUpdate.size() - 1;
		int i4 = this.worldRenderersToUpdate.size();

		for(int i5 = 0; i5 < i4; ++i5) {
			WorldRenderer worldRenderer6 = (WorldRenderer)this.worldRenderersToUpdate.get(i3 - i5);
			if(!z2) {
				if(worldRenderer6.distanceToEntitySquared(entityPlayer1) > 1024.0F) {
					if(worldRenderer6.isInFrustum) {
						if(i5 >= 3) {
							return false;
						}
					} else if(i5 >= 1) {
						return false;
					}
				}
			} else if(!worldRenderer6.isInFrustum) {
				continue;
			}

			worldRenderer6.updateRenderer();
			this.worldRenderersToUpdate.remove(worldRenderer6);
			worldRenderer6.needsUpdate = false;
		}

		return this.worldRenderersToUpdate.size() == 0;
	}

	public void drawBlockBreaking(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, ItemStack itemStack4, float f5) {
		Tessellator tessellator6 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		if(i3 == 0) {
			if(this.damagePartialTime > 0.0F) {
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
				GL11.glPushMatrix();
				int i7 = this.theWorld.getBlockId(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
				Block block8 = i7 > 0 ? Block.blocksList[i7] : null;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(-3.0F, -3.0F);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				tessellator6.startDrawingQuads();
				tessellator6.setTranslationD(-(entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f5), -(entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f5), -(entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f5));
				tessellator6.disableColor();
				if(block8 == null) {
					block8 = Block.stone;
				}

				this.globalRenderBlocks.renderBlockUsingTexture(block8, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
				tessellator6.draw();
				tessellator6.setTranslationD(0.0D, 0.0D, 0.0D);
				GL11.glPolygonOffset(0.0F, 0.0F);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glDepthMask(true);
				GL11.glPopMatrix();
			}
		} else if(itemStack4 != null) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float f11 = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
			GL11.glColor4f(f11, f11, f11, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
			int i12 = movingObjectPosition2.blockX;
			int i9 = movingObjectPosition2.blockY;
			int i10 = movingObjectPosition2.blockZ;
			if(movingObjectPosition2.sideHit == 0) {
				--i9;
			}

			if(movingObjectPosition2.sideHit == 1) {
				++i9;
			}

			if(movingObjectPosition2.sideHit == 2) {
				--i10;
			}

			if(movingObjectPosition2.sideHit == 3) {
				++i10;
			}

			if(movingObjectPosition2.sideHit == 4) {
				--i12;
			}

			if(movingObjectPosition2.sideHit == 5) {
				++i12;
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void drawSelectionBox(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, ItemStack itemStack4, float f5) {
		if(i3 == 0 && movingObjectPosition2.typeOfHit == 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			int i7 = this.theWorld.getBlockId(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
			if(i7 > 0) {
				Block.blocksList[i7].setBlockBoundsBasedOnState(this.theWorld, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
				this.drawOutlinedBoundingBox(Block.blocksList[i7].getSelectedBoundingBoxFromPool(this.theWorld, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).getOffsetBoundingBox(-(entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f5), -(entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f5), -(entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f5)));
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}

	}

	private void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB1) {
		Tessellator tessellator2 = Tessellator.instance;
		tessellator2.startDrawing(3);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.minY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.minY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.minZ);
		tessellator2.draw();
		tessellator2.startDrawing(3);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.maxY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.maxY, axisAlignedBB1.minZ);
		tessellator2.draw();
		tessellator2.startDrawing(1);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.maxY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.minY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.minZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.minY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.maxZ);
		tessellator2.addVertex(axisAlignedBB1.minX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
		tessellator2.draw();
	}

	public void markBlocksForUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		int i7 = MathHelper.bucketInt(i1, 16);
		int i8 = MathHelper.bucketInt(i2, 16);
		int i9 = MathHelper.bucketInt(i3, 16);
		int i10 = MathHelper.bucketInt(i4, 16);
		int i11 = MathHelper.bucketInt(i5, 16);
		int i12 = MathHelper.bucketInt(i6, 16);

		for(int i13 = i7; i13 <= i10; ++i13) {
			int i14 = i13 % this.renderChunksWide;
			if(i14 < 0) {
				i14 += this.renderChunksWide;
			}

			for(int i15 = i8; i15 <= i11; ++i15) {
				int i16 = i15 % this.renderChunksTall;
				if(i16 < 0) {
					i16 += this.renderChunksTall;
				}

				for(int i17 = i9; i17 <= i12; ++i17) {
					int i18 = i17 % this.renderChunksDeep;
					if(i18 < 0) {
						i18 += this.renderChunksDeep;
					}

					WorldRenderer worldRenderer19 = this.worldRenderers[(i18 * this.renderChunksTall + i16) * this.renderChunksWide + i14];
					if(!worldRenderer19.needsUpdate) {
						this.worldRenderersToUpdate.add(worldRenderer19);
					}

					worldRenderer19.markDirty();
				}
			}
		}

	}

	public void markBlockAndNeighborsNeedsUpdate(int i1, int i2, int i3) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
	}

	public void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.markBlocksForUpdate(minX - 1, minY - 1, minZ - 1, maxX + 1, maxY + 1, maxZ + 1);
	}

	public void clipRenderersByFrustum(ICamera iCamera1, float f2) {
		for(int i3 = 0; i3 < this.worldRenderers.length; ++i3) {
			if(!this.worldRenderers[i3].skipAllRenderPasses() && (!this.worldRenderers[i3].isInFrustum || (i3 + this.frustumCheckOffset & 15) == 0)) {
				this.worldRenderers[i3].updateInFrustum(iCamera1);
			}
		}

		++this.frustumCheckOffset;
	}

	public void playRecord(String record, int x, int y, int z) {
		if(record != null) {
			this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + record);
		}

		this.mc.sndManager.playStreaming(record, (float)x, (float)y, (float)z, 1.0F, 1.0F);
	}

	public void playSound(String sound, double posX, double posY, double posZ, float volume, float pitch) {
		float f10 = 16.0F;
		if(volume > 1.0F) {
			f10 *= volume;
		}

		if(this.mc.thePlayer.getDistanceSq(posX, posY, posZ) < (double)(f10 * f10)) {
			this.mc.sndManager.playSound(sound, (float)posX, (float)posY, (float)posZ, volume, pitch);
		}

	}

	public void spawnParticle(String string1, double d2, double d4, double d6, double d8, double d10, double d12) {
		double d14 = this.mc.thePlayer.posX - d2;
		double d16 = this.mc.thePlayer.posY - d4;
		double d18 = this.mc.thePlayer.posZ - d6;
		if(d14 * d14 + d16 * d16 + d18 * d18 <= 256.0D) {
			if(string1 == "bubble") {
				this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.theWorld, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "smoke") {
				this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.theWorld, d2, d4, d6));
			} else if(string1 == "explode") {
				this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.theWorld, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "flame") {
				this.mc.effectRenderer.addEffect(new EntityFlameFX(this.theWorld, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "lava") {
				this.mc.effectRenderer.addEffect(new EntityLavaFX(this.theWorld, d2, d4, d6));
			} else if(string1 == "splash") {
				this.mc.effectRenderer.addEffect(new EntitySplashFX(this.theWorld, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "largesmoke") {
				this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.theWorld, d2, d4, d6, 2.5F));
			} else if(string1 == "reddust") {
				this.mc.effectRenderer.addEffect(new EntityReddustFX(this.theWorld, d2, d4, d6));
			} else if(string1 == "snowballpoof") {
				this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.theWorld, d2, d4, d6, Item.snowball));
			} else if(string1 == "slime") {
				this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.theWorld, d2, d4, d6, Item.slimeBall));
			}

		}
	}

	public void obtainEntitySkin(Entity entity) {
		if(entity.skinUrl != null) {
			this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
		}

	}

	public void releaseEntitySkin(Entity entity) {
		if(entity.skinUrl != null) {
			this.renderEngine.releaseImageData(entity.skinUrl);
		}

	}

	public void updateAllRenderers() {
		for(int i1 = 0; i1 < this.worldRenderers.length; ++i1) {
			if(this.worldRenderers[i1].isChunkLit) {
				if(!this.worldRenderers[i1].needsUpdate) {
					this.worldRenderersToUpdate.add(this.worldRenderers[i1]);
				}

				this.worldRenderers[i1].markDirty();
			}
		}

	}

	public void doNothingWithTileEntity(int x, int y, int z, TileEntity tileEntity) {
	}
}
