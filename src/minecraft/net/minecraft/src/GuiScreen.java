package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;
	public static String currentID = "";
	private boolean hasInputHandler = false;

	private boolean InputHandlerLoaded() {
		try {
			Class.forName("InputHandler");
			return true;
		} catch (ClassNotFoundException classNotFoundException2) {
			return false;
		}
	}

	public GuiScreen() {
		this.hasInputHandler = this.InputHandlerLoaded();
	}

	public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
		for(int i4 = 0; i4 < this.controlList.size(); ++i4) {
			((GuiButton)this.controlList.get(i4)).drawButton(this.mc, mouseX, mouseY);
		}

	}

	protected void keyTyped(char character, int key) {
		if(key == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	public static String getClipboardString() {
		try {
			Transferable transferable0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);
			if(transferable0 != null && transferable0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String)transferable0.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (Exception exception1) {
		}

		return null;
	}

	protected void mouseClicked(int i1, int i2, int i3) {
		if(i3 == 0) {
			for(int i4 = 0; i4 < this.controlList.size(); ++i4) {
				GuiButton guiButton5 = (GuiButton)this.controlList.get(i4);
				if(guiButton5.mousePressed(i1, i2)) {
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(guiButton5);
				}
			}
		}

	}

	protected void mouseMovedOrUp(int i1, int i2, int i3) {
	}

	protected void actionPerformed(GuiButton button) {
	}

	public void setWorldAndResolution(Minecraft minecraft1, int i2, int i3) {
		this.mc = minecraft1;
		if(this.hasInputHandler) {
			InputHandler.mc = minecraft1;
		}

		this.fontRenderer = minecraft1.fontRenderer;
		this.width = i2;
		this.height = i3;
		this.initGui();
	}

	public void initGui() {
	}

	public void handleInput() {
		while(Mouse.next()) {
			this.handleMouseInput();
		}

		if(this.hasInputHandler) {
			while(InputHandler.NextKBEvent()) {
				this.handleKeyboardInput();
			}
		} else {
			while(Keyboard.next()) {
				this.handleKeyboardInput();
			}
		}

	}

	public void handleMouseInput() {
		if(Mouse.getEventButtonState()) {
			this.mouseClicked(Mouse.getEventX() * this.width / this.mc.displayWidth, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1, Mouse.getEventButton());
		} else {
			this.mouseMovedOrUp(Mouse.getEventX() * this.width / this.mc.displayWidth, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1, Mouse.getEventButton());
		}

	}

	public void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
			if(this.hasInputHandler && InputHandler.cheatsEnabled) {
				if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD1) {
					currentID = currentID + '1';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2) {
					currentID = currentID + '2';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD3) {
					currentID = currentID + '3';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) {
					currentID = currentID + '4';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5) {
					currentID = currentID + '5';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) {
					currentID = currentID + '6';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD7) {
					currentID = currentID + '7';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) {
					currentID = currentID + '8';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD9) {
					currentID = currentID + '9';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0) {
					currentID = currentID + '0';
					System.out.println("Current entered ID: " + currentID);
				} else if(Keyboard.getEventKey() == Keyboard.KEY_SUBTRACT) {
					currentID = "";
				} else if(Keyboard.getEventKey() == Keyboard.KEY_ADD) {
					try {
						if(Block.blocksList.length > Integer.parseInt(currentID) && Block.blocksList[Integer.parseInt(currentID)] != null) {
							this.mc.thePlayer.dropPlayerItemWithRandomChoice(new ItemStack(Block.blocksList[Integer.parseInt(currentID)], 64), true);
							System.out.println("Given block to the player");
						} else if(Item.itemsList[Integer.parseInt(currentID)] != null) {
							this.mc.thePlayer.dropPlayerItemWithRandomChoice(new ItemStack(Item.itemsList[Integer.parseInt(currentID)], 1), true);
							System.out.println("Given item to the player");
						} else {
							System.out.println("No block or item with ID " + currentID);
						}
					} catch (Exception exception2) {
						exception2.printStackTrace();
					}
				}
			}

			if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
				this.mc.toggleFullscreen();
				return;
			}

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

	public void onGuiClosed() {
	}

	public void drawDefaultBackground() {
		this.drawWorldBackground(0);
	}

	public void drawWorldBackground(int i1) {
		if(this.mc.theWorld != null) {
			this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		} else {
			this.drawBackground(i1);
		}

	}

	public void drawBackground(int i1) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator2 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator2.startDrawingQuads();
		tessellator2.setColorOpaque_I(4210752);
		tessellator2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / 32.0F + (float)i1));
		tessellator2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)i1));
		tessellator2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / 32.0F), (double)(0 + i1));
		tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + i1));
		tessellator2.draw();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void deleteWorld(boolean z1, int i2) {
	}

	static {
		currentID = "";
	}
}
