package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumOS;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.InputHandler;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MinecraftImpl;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldRenderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft implements Runnable {
	public PlayerController playerController;
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean appletMode = true;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public String loadMapUser = null;
	public int loadMapID = 0;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings options;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public File mcDataDir;
	public static long[] frameTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	long prevFrameTime = -1L;
	public boolean inGameHasFocus = false;
	private int mouseTicksRan = 0;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;

	public Minecraft(Component component, Canvas canvas, MinecraftApplet mcApplet, int width, int height, boolean fullscreen) {
		this.tempDisplayWidth = width;
		this.tempDisplayHeight = height;
		this.fullscreen = fullscreen;
		this.mcApplet = mcApplet;
		new ThreadSleepForever(this, "Timer hack thread");
		this.mcCanvas = canvas;
		this.displayWidth = width;
		this.displayHeight = height;
		this.fullscreen = fullscreen;
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable unexpectedThrowable1);

	public void setServer(String string1, int i2) {
		this.serverName = string1;
		this.serverPort = i2;
	}

	public void startGame() throws LWJGLException {
		if(this.mcCanvas != null) {
			Graphics graphics1 = this.mcCanvas.getGraphics();
			if(graphics1 != null) {
				graphics1.setColor(Color.BLACK);
				graphics1.fillRect(0, 0, this.displayWidth, this.displayHeight);
				graphics1.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if(this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("Minecraft Minecraft Alpha v1.1.0");

		try {
			Display.create();
		} catch (LWJGLException lWJGLException6) {
			lWJGLException6.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException5) {
			}

			Display.create();
		}

		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.mcDataDir = getMinecraftDir();
		this.options = new GameSettings(this, this.mcDataDir);
		this.renderEngine = new RenderEngine(this.options);
		this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
		this.loadScreen();
		Keyboard.create();
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.options);
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception exception3) {
		}

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution scaledResolution1 = new ScaledResolution(this.displayWidth, this.displayHeight);
		int i2 = scaledResolution1.getScaledWidth();
		int i3 = scaledResolution1.getScaledHeight();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)i2, (double)i3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		tessellator4.startDrawingQuads();
		tessellator4.setColorOpaque_I(0xFFFFFF);
		tessellator4.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator4.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator4.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator4.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator4.draw();
		short s5 = 256;
		short s6 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator4.setColorOpaque_I(0xFFFFFF);
		this.scaledTessellator((this.displayWidth / 2 - s5) / 2, (this.displayHeight / 2 - s6) / 2, 0, 0, s5, s6);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int i1, int i2, int i3, int i4, int i5, int i6) {
		float f7 = 0.00390625F;
		float f8 = 0.00390625F;
		Tessellator tessellator9 = Tessellator.instance;
		tessellator9.startDrawingQuads();
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.draw();
	}

	public static File getMinecraftDir() {
		if(minecraftDir == null) {
			minecraftDir = getAppDir("minecraft");
		}

		return minecraftDir;
	}

	public static File getAppDir(String string0) {
		String string1 = System.getProperty("user.home", ".");
		File file2;
		switch(Minecraft.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOS[getOs().ordinal()]) {
		case 1:
		case 2:
			file2 = new File(string1, '.' + string0 + '/');
			break;
		case 3:
			String string3 = System.getenv("APPDATA");
			if(string3 != null) {
				file2 = new File(string3, "." + string0 + '/');
			} else {
				file2 = new File(string1, '.' + string0 + '/');
			}
			break;
		case 4:
			file2 = new File(string1, "Library/Application Support/" + string0);
			break;
		default:
			file2 = new File(string1, string0 + '/');
		}

		if(!file2.exists() && !file2.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + file2);
		} else {
			return file2;
		}
	}

	private static EnumOS getOs() {
		String string0 = System.getProperty("os.name").toLowerCase();
		return string0.contains("win") ? EnumOS.windows : (string0.contains("mac") ? EnumOS.macos : (string0.contains("solaris") ? EnumOS.solaris : (string0.contains("sunos") ? EnumOS.solaris : (string0.contains("linux") ? EnumOS.linux : (string0.contains("unix") ? EnumOS.linux : EnumOS.unknown)))));
	}

	public void displayGuiScreen(GuiScreen guiScreen1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(guiScreen1 == null && this.theWorld == null) {
				guiScreen1 = new GuiMainMenu();
			} else if(guiScreen1 == null && this.thePlayer.health <= 0) {
				guiScreen1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)guiScreen1;
			if(guiScreen1 != null) {
				this.setIngameNotInFocus();
				ScaledResolution scaledResolution2 = new ScaledResolution(this.displayWidth, this.displayHeight);
				int i3 = scaledResolution2.getScaledWidth();
				int i4 = scaledResolution2.getScaledHeight();
				((GuiScreen)guiScreen1).setWorldAndResolution(this, i3, i4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}

		}
	}

	private void checkGLError(String string1) {
		int i2 = GL11.glGetError();
		if(i2 != 0) {
			String string3 = GLU.gluErrorString(i2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string1);
			System.out.println(i2 + ": " + string3);
			System.exit(0);
		}

	}

	public void shutdownMinecraftApplet() {
		if(this.mcApplet != null) {
			this.mcApplet.clearApplet();
		}

		try {
			if(this.downloadResourcesThread != null) {
				this.downloadResourcesThread.b();
			}
		} catch (Exception exception8) {
		}

		try {
			System.out.println("Stopping!");
			this.changeWorld1((World)null);

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Exception exception6) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception exception13) {
			exception13.printStackTrace();
			this.displayUnexpectedThrowable(new UnexpectedThrowable("Failed to start game", exception13));
			return;
		}

		try {
			long j1 = System.currentTimeMillis();
			int i3 = 0;

			while(this.running && (this.mcApplet == null || this.mcApplet.isActive())) {
				AxisAlignedBB.clearBoundingBoxPool();
				Vec3D.initialize();
				if(this.mcCanvas == null && Display.isCloseRequested()) {
					this.shutdown();
				}

				if(this.isGamePaused && this.theWorld != null) {
					float f4 = this.timer.renderPartialTicks;
					this.timer.updateTimer();
					this.timer.renderPartialTicks = f4;
				} else {
					this.timer.updateTimer();
				}

				for(int i17 = 0; i17 < this.timer.elapsedTicks; ++i17) {
					++this.ticksRan;

					try {
						this.runTick();
					} catch (MinecraftException minecraftException12) {
						this.theWorld = null;
						this.changeWorld1((World)null);
						this.displayGuiScreen(new GuiConflictWarning());
					}
				}

				this.checkGLError("Pre render");
				this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				if(this.theWorld != null) {
					while(this.theWorld.updatingLighting()) {
					}
				}

				if(!this.skipRenderWorld) {
					if(this.playerController != null) {
						this.playerController.setPartialTime(this.timer.renderPartialTicks);
					}

					this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
				}

				if(!Display.isActive()) {
					if(this.fullscreen) {
						this.toggleFullscreen();
					}

					Thread.sleep(10L);
				}

				if(InputHandler.IsKeyDown(64)) {
					this.displayDebugInfo();
				} else {
					this.prevFrameTime = System.nanoTime();
				}

				Thread.yield();
				Display.update();
				if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
					if(this.displayWidth <= 0) {
						this.displayWidth = 1;
					}

					if(this.displayHeight <= 0) {
						this.displayHeight = 1;
					}

					this.resize(this.displayWidth, this.displayHeight);
				}

				if(this.options.limitFramerate) {
					Thread.sleep(5L);
				}

				this.checkGLError("Post render");
				++i3;

				for(this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= j1 + 1000L; i3 = 0) {
					this.debug = i3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
					WorldRenderer.chunksUpdated = 0;
					j1 += 1000L;
				}
			}
		} catch (MinecraftError minecraftError14) {
		} catch (Throwable throwable15) {
			this.theWorld = null;
			throwable15.printStackTrace();
			this.displayUnexpectedThrowable(new UnexpectedThrowable("Unexpected error", throwable15));
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	private void displayDebugInfo() {
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = System.nanoTime();
		}

		long j1 = System.nanoTime();
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = j1 - this.prevFrameTime;
		this.prevFrameTime = j1;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator3 = Tessellator.instance;
		tessellator3.startDrawing(7);
		tessellator3.setColorOpaque_I(0x20200000);
		tessellator3.addVertex(0.0D, (double)(this.displayHeight - 100), 0.0D);
		tessellator3.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator3.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator3.addVertex((double)frameTimes.length, (double)(this.displayHeight - 100), 0.0D);
		tessellator3.draw();
		long j4 = 0L;

		int i6;
		for(i6 = 0; i6 < frameTimes.length; ++i6) {
			j4 += frameTimes[i6];
		}

		i6 = (int)(j4 / 200000L / (long)frameTimes.length);
		tessellator3.startDrawing(7);
		tessellator3.setColorOpaque_I(0x20400000);
		tessellator3.addVertex(0.0D, (double)(this.displayHeight - i6), 0.0D);
		tessellator3.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator3.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator3.addVertex((double)frameTimes.length, (double)(this.displayHeight - i6), 0.0D);
		tessellator3.draw();
		tessellator3.startDrawing(1);

		for(int i7 = 0; i7 < frameTimes.length; ++i7) {
			int i8 = (i7 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int i9 = i8 * i8 / 255;
			i9 = i9 * i9 / 255;
			int i10 = i9 * i9 / 255;
			i10 = i10 * i10 / 255;
			tessellator3.setColorOpaque_I(0xFF000000 + i10 + i9 * 256 + i8 * 65536);
			long j11 = frameTimes[i7] / 200000L;
			tessellator3.addVertex((double)((float)i7 + 0.5F), (double)((float)((long)this.displayHeight - j11) + 0.5F), 0.0D);
			tessellator3.addVertex((double)((float)i7 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
		}

		tessellator3.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void shutdown() {
		this.running = false;
	}

	public void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.mouseTicksRan = this.ticksRan + 10000;
			}
		}
	}

	public void setIngameNotInFocus() {
		if(this.inGameHasFocus) {
			if(this.thePlayer != null) {
				this.thePlayer.resetPlayerKeyState();
			}

			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(int button, boolean clicked) {
		if(!this.playerController.isInTestMode) {
			if(button != 0 || this.leftClickCounter <= 0) {
				if(clicked && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0 && button == 0) {
					int i3 = this.objectMouseOver.blockX;
					int i4 = this.objectMouseOver.blockY;
					int i5 = this.objectMouseOver.blockZ;
					this.playerController.sendBlockRemoving(i3, i4, i5, this.objectMouseOver.sideHit);
					this.effectRenderer.addBlockHitEffects(i3, i4, i5, this.objectMouseOver.sideHit);
				} else {
					this.playerController.resetBlockRemoving();
				}

			}
		}
	}

	private void clickMouse(int button) {
		if(button != 0 || this.leftClickCounter <= 0) {
			if(button == 0) {
				this.thePlayer.swingItem();
			}

			int i3;
			if(this.objectMouseOver == null) {
				if(button == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
					this.leftClickCounter = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == 1) {
				if(button == 0) {
					this.thePlayer.attackEntity(this.objectMouseOver.entityHit);
				}

				if(button == 1) {
					this.thePlayer.interactWithEntity(this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == 0) {
				int i2 = this.objectMouseOver.blockX;
				i3 = this.objectMouseOver.blockY;
				int i4 = this.objectMouseOver.blockZ;
				int i5 = this.objectMouseOver.sideHit;
				Block block6 = Block.blocksList[this.theWorld.getBlockId(i2, i3, i4)];
				if(button == 0) {
					this.theWorld.extinguishFire(i2, i3, i4, this.objectMouseOver.sideHit);
					if(block6 != Block.bedrock || this.thePlayer.unusedMiningCooldown >= 100) {
						this.playerController.clickBlock(i2, i3, i4, this.objectMouseOver.sideHit);
					}
				} else {
					ItemStack itemStack7 = this.thePlayer.inventory.getCurrentItem();
					int i8 = itemStack7 != null ? itemStack7.stackSize : 0;
					if(this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemStack7, i2, i3, i4, i5)) {
						this.thePlayer.swingItem();
					}

					if(itemStack7 == null) {
						return;
					}

					if(itemStack7.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(itemStack7.stackSize != i8) {
						this.entityRenderer.itemRenderer.resetEquippedProgress();
					}
				}
			}

			if(button == 1) {
				ItemStack itemStack9 = this.thePlayer.inventory.getCurrentItem();
				if(itemStack9 != null) {
					i3 = itemStack9.stackSize;
					ItemStack itemStack10 = itemStack9.useItemRightClick(this.theWorld, this.thePlayer);
					if(itemStack10 != itemStack9 || itemStack10 != null && itemStack10.stackSize != i3) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = itemStack10;
						this.entityRenderer.itemRenderer.resetEquippedProgress2();
						if(itemStack10.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
						}
					}
				}
			}

		}
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			System.out.println("Toggle fullscreen!");
			if(this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}

				Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
			}

			this.setIngameNotInFocus();
			Display.setFullscreen(this.fullscreen);
			Display.update();
			Thread.sleep(1000L);
			if(this.fullscreen) {
				this.setIngameFocus();
			}

			if(this.currentScreen != null) {
				this.setIngameNotInFocus();
				this.resize(this.displayWidth, this.displayHeight);
			}

			System.out.println("Size: " + this.displayWidth + ", " + this.displayHeight);
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}

	}

	private void resize(int width, int height) {
		if(width <= 0) {
			width = 1;
		}

		if(height <= 0) {
			height = 1;
		}

		this.displayWidth = width;
		this.displayHeight = height;
		if(this.currentScreen != null) {
			ScaledResolution scaledResolution3 = new ScaledResolution(width, height);
			int i4 = scaledResolution3.getScaledWidth();
			int i5 = scaledResolution3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, i4, i5);
		}

	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int i1 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(i1 == Block.grass.blockID) {
				i1 = Block.dirt.blockID;
			}

			if(i1 == Block.stairDouble.blockID) {
				i1 = Block.stairSingle.blockID;
			}

			if(i1 == Block.bedrock.blockID) {
				i1 = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(i1, this.playerController instanceof PlayerControllerCreative);
		}

	}

	public void runTick() {
		this.ingameGUI.updateTick();
		this.entityRenderer.getMouseOver(1.0F);
		if(this.thePlayer != null) {
			this.thePlayer.onPlayerUpdate();
		}

		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.onUpdate();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.mouseTicksRan = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label234:
			while(true) {
				while(true) {
					while(true) {
						long j1;
						do {
							if(!InputHandler.NextEvent()) {
								if(this.leftClickCounter > 0) {
									--this.leftClickCounter;
								}

								while(true) {
									while(true) {
										do {
											if(!InputHandler.NextKBEvent()) {
												if(this.currentScreen == null) {
													if(InputHandler.InputMouseAction(0) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(0);
														this.mouseTicksRan = this.ticksRan;
													}

													if(InputHandler.InputMouseAction(1) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(1);
														this.mouseTicksRan = this.ticksRan;
													}
												}

												this.sendClickBlockToController(0, this.currentScreen == null && InputHandler.InputMouseAction(0) && this.inGameHasFocus);
												break label234;
											}

											this.thePlayer.handleKeyPress(InputHandler.GetKBEventKey(), InputHandler.GetKBEventKeyState());
										} while(!InputHandler.GetKBEventKeyState());

										if(InputHandler.GetKBEventKey() == 87) {
											this.toggleFullscreen();
										} else {
											if(this.currentScreen != null) {
												this.currentScreen.handleKeyboardInput();
											} else {
												if(InputHandler.GetKBEventKey() == 1) {
													this.displayInGameMenu();
												}

												if(InputHandler.GetKBEventKey() == 31 && InputHandler.IsKeyDown(61)) {
													this.t();
												}

												if(InputHandler.GetKBEventKey() == 63) {
													this.options.thirdPersonView = !this.options.thirdPersonView;
												}

												if(InputHandler.GetKBEventKey() == this.options.keyBindInventory.keyCode) {
													this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory, this.thePlayer.inventory.craftingInventory));
												}

												if(InputHandler.GetKBEventKey() == this.options.keyBindDrop.keyCode) {
													this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
												}

												if(this.isMultiplayerWorld() && InputHandler.GetKBEventKey() == this.options.keyBindChat.keyCode) {
													this.displayGuiScreen(new GuiChat());
												}
											}

											for(int i4 = 0; i4 < 9; ++i4) {
												if(InputHandler.GetKBEventKey() == 2 + i4) {
													this.thePlayer.inventory.currentItem = i4;
												}
											}

											if(InputHandler.GetKBEventKey() == this.options.keyBindToggleFog.keyCode) {
												this.options.setOptionValue(4, !InputHandler.IsKeyDown(42) && !InputHandler.IsKeyDown(54) ? 1 : -1);
											}
										}
									}
								}
							}

							j1 = System.currentTimeMillis() - this.systemTime;
						} while(j1 > 200L);

						int i3 = InputHandler.GetDWheel();
						if(i3 != 0) {
							this.thePlayer.inventory.changeCurrentItem(i3);
						}

						if(this.currentScreen == null) {
							if(!this.inGameHasFocus && InputHandler.GetEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(InputHandler.GetEventButton() == 0 && InputHandler.GetEventButtonState()) {
									this.clickMouse(0);
									this.mouseTicksRan = this.ticksRan;
								}

								if(InputHandler.GetEventButton() == 1 && InputHandler.GetEventButtonState()) {
									this.clickMouse(1);
									this.mouseTicksRan = this.ticksRan;
								}

								if(InputHandler.GetEventButton() == 2 && InputHandler.GetEventButtonState()) {
									this.clickMiddleMouseButton();
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}
				}
			}
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.joinPlayerCounter;
				if(this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.options.difficulty;
			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused || this.isMultiplayerWorld()) {
				this.theWorld.tick();
			}

			if(!this.isGamePaused && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

		this.systemTime = System.currentTimeMillis();
	}

	private void t() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.options);
		this.downloadResourcesThread.closeMinecraft();
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void startWorld(String worldName) {
		this.changeWorld1((World)null);
		System.gc();
		World world2 = new World(new File(getMinecraftDir(), "saves"), worldName);
		if(world2.isNewWorld) {
			this.changeWorld(world2, "Generating level");
		} else {
			this.changeWorld(world2, "Loading level");
		}

	}

	public void changeWorld1(World world) {
		this.changeWorld(world, "");
	}

	public void changeWorld(World world, String title) {
		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.saveWorldIndirectly(this.loadingScreen);
		}

		this.theWorld = world;
		if(world != null) {
			this.playerController.onWorldChange(world);
			world.fontRenderer = this.fontRenderer;
			if(!this.isMultiplayerWorld()) {
				this.thePlayer = (EntityPlayerSP)world.createDebugPlayer(EntityPlayerSP.class);
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(world != null) {
					world.spawnEntityInWorld(this.thePlayer);
				}
			}

			if(!world.multiplayerWorld) {
				this.preloadWorld(title);
			}

			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(world);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(world);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(world);
			}

			this.playerController.onRespawn(this.thePlayer);
			world.spawnPlayerWithLoadedChunks(this.thePlayer);
			if(world.isNewWorld) {
				world.saveWorldIndirectly(this.loadingScreen);
			}
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void preloadWorld(String title) {
		this.loadingScreen.resetProgressAndMessage(title);
		this.loadingScreen.displayLoadingString("Building terrain");
		short s2 = 128;
		int i3 = 0;
		int i4 = s2 * 2 / 16 + 1;
		i4 *= i4;

		for(int i5 = -s2; i5 <= s2; i5 += 16) {
			int i6 = this.theWorld.spawnX;
			int i7 = this.theWorld.spawnZ;
			if(this.thePlayer != null) {
				i6 = (int)this.thePlayer.posX;
				i7 = (int)this.thePlayer.posZ;
			}

			for(int i8 = -s2; i8 <= s2; i8 += 16) {
				this.loadingScreen.setLoadingProgress(i3++ * 100 / i4);
				this.theWorld.getBlockId(i6 + i5, 64, i7 + i8);

				while(this.theWorld.updatingLighting()) {
				}
			}
		}

		this.loadingScreen.displayLoadingString("Simulating world for a bit");
		boolean z9 = true;
		this.theWorld.dropOldChunks();
	}

	public void installResource(String name, File file) {
		int i3 = name.indexOf("/");
		String string4 = name.substring(0, i3);
		name = name.substring(i3 + 1);
		if(string4.equalsIgnoreCase("sound")) {
			this.sndManager.addSound(name, file);
		} else if(string4.equalsIgnoreCase("newsound")) {
			this.sndManager.addSound(name, file);
		} else if(string4.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(name, file);
		} else if(string4.equalsIgnoreCase("music")) {
			this.sndManager.addMusic(name, file);
		} else if(string4.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(name, file);
		}

	}

	public OpenGlCapsChecker getOpenGlCapsChecker() {
		return this.glCapabilities;
	}

	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String getEntityDebug() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
	}

	public void respawn() {
		this.theWorld.setSpawnLocation();
		if(this.thePlayer != null) {
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);
		this.thePlayer.preparePlayerToSpawn();
		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
		this.playerController.onRespawn(this.thePlayer);
		this.preloadWorld("Respawning");
	}

	public static void startMainThread(String username, String sessionId) {
		startMainThread(username, sessionId, (String)null);
	}

	public static void startMainThread(String username, String sessionId, String server) {
		boolean z3 = false;
		Frame frame5 = new Frame("Minecraft");
		Canvas canvas6 = new Canvas();
		frame5.setLayout(new BorderLayout());
		frame5.add(canvas6, "Center");
		canvas6.setPreferredSize(new Dimension(854, 480));
		frame5.pack();
		frame5.setLocationRelativeTo((Component)null);
		MinecraftImpl minecraftImpl7 = new MinecraftImpl(frame5, canvas6, (MinecraftApplet)null, 854, 480, z3, frame5);
		Thread thread8 = new Thread(minecraftImpl7, "Minecraft main thread");
		thread8.setPriority(10);
		minecraftImpl7.appletMode = false;
		minecraftImpl7.minecraftUri = "www.minecraft.net";
		if(username != null && sessionId != null) {
			minecraftImpl7.session = new Session(username, sessionId);
		} else {
			minecraftImpl7.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");
		}

		if(server != null) {
			String[] string9 = server.split(":");
			minecraftImpl7.setServer(string9[0], Integer.parseInt(string9[1]));
		}

		frame5.setVisible(true);
		frame5.addWindowListener(new GameWindowListener(minecraftImpl7, thread8));
		thread8.start();
	}

	public static void main(String[] args) {
		String string1 = "Player" + System.currentTimeMillis() % 1000L;
		if(args.length > 0) {
			string1 = args[0];
		}

		String string2 = "-";
		if(args.length > 1) {
			string2 = args[1];
		}

		startMainThread(string1, string2);
	}

	static final class SyntheticClass_1 {
		public static final int[] $SwitchMap$net$minecraft$src$EnumOS = new int[EnumOS.values().length];

		static {
			try {
				$SwitchMap$net$minecraft$src$EnumOS[EnumOS.linux.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError4) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS[EnumOS.solaris.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS[EnumOS.windows.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS[EnumOS.macos.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

		}
	}
}
