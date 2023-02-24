package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CanvasIsomPreview extends Canvas implements KeyListener, MouseListener, MouseMotionListener, Runnable {
	private int currentRender = 0;
	private int zoomLevel = 2;
	private boolean displayHelpText = true;
	private World level;
	private File dataFolder = this.getWorkingDirectory();
	private boolean running = true;
	private List zonesToRender = Collections.synchronizedList(new LinkedList());
	private IsoImageBuffer[][] zoneMap = new IsoImageBuffer[64][64];
	private int translateX;
	private int translateY;
	private int xPosition;
	private int yPosition;

	public File getWorkingDirectory() {
		if(this.dataFolder == null) {
			this.dataFolder = this.getWorkingDirectory("minecraft");
		}

		return this.dataFolder;
	}

	public File getWorkingDirectory(String name) {
		String string2 = System.getProperty("user.home", ".");
		File file3;
		switch(CanvasIsomPreview.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOSIsom[getPlatform().ordinal()]) {
		case 1:
		case 2:
			file3 = new File(string2, '.' + name + '/');
			break;
		case 3:
			String string4 = System.getenv("APPDATA");
			if(string4 != null) {
				file3 = new File(string4, "." + name + '/');
			} else {
				file3 = new File(string2, '.' + name + '/');
			}
			break;
		case 4:
			file3 = new File(string2, "Library/Application Support/" + name);
			break;
		default:
			file3 = new File(string2, name + '/');
		}

		if(!file3.exists() && !file3.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + file3);
		} else {
			return file3;
		}
	}

	private static EnumOSIsom getPlatform() {
		String string0 = System.getProperty("os.name").toLowerCase();
		return string0.contains("win") ? EnumOSIsom.windows : (string0.contains("mac") ? EnumOSIsom.macos : (string0.contains("solaris") ? EnumOSIsom.solaris : (string0.contains("sunos") ? EnumOSIsom.solaris : (string0.contains("linux") ? EnumOSIsom.linux : (string0.contains("unix") ? EnumOSIsom.linux : EnumOSIsom.unknown)))));
	}

	public CanvasIsomPreview() {
		for(int i1 = 0; i1 < 64; ++i1) {
			for(int i2 = 0; i2 < 64; ++i2) {
				this.zoneMap[i1][i2] = new IsoImageBuffer((World)null, i1, i2);
			}
		}

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		this.setBackground(Color.red);
	}

	public void loadLevel(String levelName) {
		this.translateX = this.translateY = 0;
		this.level = new WorldIso(this, new File(this.dataFolder, "saves"), levelName);
		this.level.skylightSubtracted = 0;
		List list2 = this.zonesToRender;
		synchronized(this.zonesToRender) {
			this.zonesToRender.clear();

			for(int i3 = 0; i3 < 64; ++i3) {
				for(int i4 = 0; i4 < 64; ++i4) {
					this.zoneMap[i3][i4].setLevel(this.level, i3, i4);
				}
			}

		}
	}

	private void setBrightness(int brightness) {
		List list2 = this.zonesToRender;
		synchronized(this.zonesToRender) {
			this.level.skylightSubtracted = brightness;
			this.zonesToRender.clear();

			for(int i3 = 0; i3 < 64; ++i3) {
				for(int i4 = 0; i4 < 64; ++i4) {
					this.zoneMap[i3][i4].setLevel(this.level, i3, i4);
				}
			}

		}
	}

	public void start() {
		(new ThreadRunIsoClient(this)).start();

		for(int i1 = 0; i1 < 8; ++i1) {
			(new Thread(this)).start();
		}

	}

	public void stop() {
		this.running = false;
	}

	private IsoImageBuffer getZone(int x, int z) {
		int i3 = x & 63;
		int i4 = z & 63;
		IsoImageBuffer isoImageBuffer5 = this.zoneMap[i3][i4];
		if(isoImageBuffer5.x == x && isoImageBuffer5.y == z) {
			return isoImageBuffer5;
		} else {
			List list6 = this.zonesToRender;
			synchronized(this.zonesToRender) {
				this.zonesToRender.remove(isoImageBuffer5);
			}

			isoImageBuffer5.init(x, z);
			return isoImageBuffer5;
		}
	}

	public void run() {
		TerrainTextureManager terrainTextureManager1 = new TerrainTextureManager();

		while(this.running) {
			IsoImageBuffer isoImageBuffer2 = null;
			List list3 = this.zonesToRender;
			synchronized(this.zonesToRender) {
				if(this.zonesToRender.size() > 0) {
					isoImageBuffer2 = (IsoImageBuffer)this.zonesToRender.remove(0);
				}
			}

			if(isoImageBuffer2 != null) {
				if(this.currentRender - isoImageBuffer2.lastVisible < 2) {
					terrainTextureManager1.render(isoImageBuffer2);
					this.repaint();
				} else {
					isoImageBuffer2.addedToRenderQueue = false;
				}
			}

			try {
				Thread.sleep(2L);
			} catch (InterruptedException interruptedException5) {
				interruptedException5.printStackTrace();
			}
		}

	}

	public void update(Graphics graphics) {
	}

	public void paint(Graphics graphics) {
	}

	public void render() {
		BufferStrategy bufferStrategy1 = this.getBufferStrategy();
		if(bufferStrategy1 == null) {
			this.createBufferStrategy(2);
		} else {
			this.render((Graphics2D)bufferStrategy1.getDrawGraphics());
			bufferStrategy1.show();
		}
	}

	public void render(Graphics2D graphics2D) {
		++this.currentRender;
		AffineTransform affineTransform2 = graphics2D.getTransform();
		graphics2D.setClip(0, 0, this.getWidth(), this.getHeight());
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics2D.translate(this.getWidth() / 2, this.getHeight() / 2);
		graphics2D.scale((double)this.zoomLevel, (double)this.zoomLevel);
		graphics2D.translate(this.translateX, this.translateY);
		if(this.level != null) {
			graphics2D.translate(-(this.level.spawnX + this.level.spawnZ), -(-this.level.spawnX + this.level.spawnZ) + 64);
		}

		Rectangle rectangle3 = graphics2D.getClipBounds();
		graphics2D.setColor(new Color(-15724512));
		graphics2D.fillRect(rectangle3.x, rectangle3.y, rectangle3.width, rectangle3.height);
		byte b4 = 16;
		byte b5 = 3;
		int i6 = rectangle3.x / b4 / 2 - 2 - b5;
		int i7 = (rectangle3.x + rectangle3.width) / b4 / 2 + 1 + b5;
		int i8 = rectangle3.y / b4 - 1 - b5 * 2;
		int i9 = (rectangle3.y + rectangle3.height + 16 + 128) / b4 + 1 + b5 * 2;

		int i10;
		for(i10 = i8; i10 <= i9; ++i10) {
			for(int i11 = i6; i11 <= i7; ++i11) {
				int i12 = i11 - (i10 >> 1);
				int i13 = i11 + (i10 + 1 >> 1);
				IsoImageBuffer isoImageBuffer14 = this.getZone(i12, i13);
				isoImageBuffer14.lastVisible = this.currentRender;
				if(!isoImageBuffer14.rendered) {
					if(!isoImageBuffer14.addedToRenderQueue) {
						isoImageBuffer14.addedToRenderQueue = true;
						this.zonesToRender.add(isoImageBuffer14);
					}
				} else {
					isoImageBuffer14.addedToRenderQueue = false;
					if(!isoImageBuffer14.noContent) {
						int i15 = i11 * b4 * 2 + (i10 & 1) * b4;
						int i16 = i10 * b4 - 128 - 16;
						graphics2D.drawImage(isoImageBuffer14.image, i15, i16, (ImageObserver)null);
					}
				}
			}
		}

		if(this.displayHelpText) {
			graphics2D.setTransform(affineTransform2);
			i10 = this.getHeight() - 32 - 4;
			graphics2D.setColor(new Color(Integer.MIN_VALUE, true));
			graphics2D.fillRect(4, this.getHeight() - 32 - 4, this.getWidth() - 8, 32);
			graphics2D.setColor(Color.WHITE);
			String string17 = "F1 - F5: load levels   |   0-9: Set time of day   |   Space: return to spawn   |   Double click: zoom   |   Escape: hide this text";
			graphics2D.drawString(string17, this.getWidth() / 2 - graphics2D.getFontMetrics().stringWidth(string17) / 2, i10 + 20);
		}

		graphics2D.dispose();
	}

	public void mouseDragged(MouseEvent mouseEvent) {
		int i2 = mouseEvent.getX() / this.zoomLevel;
		int i3 = mouseEvent.getY() / this.zoomLevel;
		this.translateX += i2 - this.xPosition;
		this.translateY += i3 - this.yPosition;
		this.xPosition = i2;
		this.yPosition = i3;
		this.repaint();
	}

	public void mouseMoved(MouseEvent mouseEvent) {
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount() == 2) {
			this.zoomLevel = 3 - this.zoomLevel;
			this.repaint();
		}

	}

	public void mouseEntered(MouseEvent mouseEvent) {
	}

	public void mouseExited(MouseEvent mouseEvent) {
	}

	public void mousePressed(MouseEvent mouseEvent) {
		int i2 = mouseEvent.getX() / this.zoomLevel;
		int i3 = mouseEvent.getY() / this.zoomLevel;
		this.xPosition = i2;
		this.yPosition = i3;
	}

	public void mouseReleased(MouseEvent mouseEvent) {
	}

	public void keyPressed(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == 48) {
			this.setBrightness(11);
		}

		if(keyEvent.getKeyCode() == 49) {
			this.setBrightness(10);
		}

		if(keyEvent.getKeyCode() == 50) {
			this.setBrightness(9);
		}

		if(keyEvent.getKeyCode() == 51) {
			this.setBrightness(7);
		}

		if(keyEvent.getKeyCode() == 52) {
			this.setBrightness(6);
		}

		if(keyEvent.getKeyCode() == 53) {
			this.setBrightness(5);
		}

		if(keyEvent.getKeyCode() == 54) {
			this.setBrightness(3);
		}

		if(keyEvent.getKeyCode() == 55) {
			this.setBrightness(2);
		}

		if(keyEvent.getKeyCode() == 56) {
			this.setBrightness(1);
		}

		if(keyEvent.getKeyCode() == 57) {
			this.setBrightness(0);
		}

		if(keyEvent.getKeyCode() == 112) {
			this.loadLevel("World1");
		}

		if(keyEvent.getKeyCode() == 113) {
			this.loadLevel("World2");
		}

		if(keyEvent.getKeyCode() == 114) {
			this.loadLevel("World3");
		}

		if(keyEvent.getKeyCode() == 115) {
			this.loadLevel("World4");
		}

		if(keyEvent.getKeyCode() == 116) {
			this.loadLevel("World5");
		}

		if(keyEvent.getKeyCode() == 32) {
			this.translateX = this.translateY = 0;
		}

		if(keyEvent.getKeyCode() == 27) {
			this.displayHelpText = !this.displayHelpText;
		}

		this.repaint();
	}

	public void keyReleased(KeyEvent keyEvent) {
	}

	public void keyTyped(KeyEvent keyEvent) {
	}

	static boolean isRunning(CanvasIsomPreview canvasIsomPreview0) {
		return canvasIsomPreview0.running;
	}

	static final class SyntheticClass_1 {
		static final int[] $SwitchMap$net$minecraft$src$EnumOSIsom = new int[EnumOSIsom.values().length];

		static {
			try {
				$SwitchMap$net$minecraft$src$EnumOSIsom[EnumOSIsom.linux.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError4) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOSIsom[EnumOSIsom.solaris.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOSIsom[EnumOSIsom.windows.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOSIsom[EnumOSIsom.macos.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

		}
	}
}
