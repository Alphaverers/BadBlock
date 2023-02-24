package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	private static final String[] DIFFICULTY_LEVELS = new String[]{"Peaceful", "Easy", "Normal", "Hard", "Frail"};
	public boolean a = true;
	public boolean b = true;
	public boolean invertMouse = false;
	public boolean d = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public boolean fancyGraphics = true;
	public KeyBinding keyBindForward = new KeyBinding("Forward", Keyboard.KEY_W);
	public KeyBinding keyBindLeft = new KeyBinding("Left", Keyboard.KEY_A);
	public KeyBinding keyBindBack = new KeyBinding("Back", Keyboard.KEY_S);
	public KeyBinding keyBindRight = new KeyBinding("Right", Keyboard.KEY_D);
	public KeyBinding keyBindJump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	public KeyBinding keyBindInventory = new KeyBinding("Inventory", Keyboard.KEY_I);
	public KeyBinding keyBindDrop = new KeyBinding("Drop", Keyboard.KEY_Q);
	public KeyBinding keyBindChat = new KeyBinding("Chat", Keyboard.KEY_T);
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", Keyboard.KEY_F);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog};
	protected Minecraft mc;
	private File optionsFile;
	public int numberOfOptions = 10;
	public int difficulty = 2;
	public boolean thirdPersonView = false;

	public GameSettings(Minecraft minecraft, File file) {
		this.mc = minecraft;
		this.optionsFile = new File(file, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
	}

	public String getKeyBindingDescription(int i1) {
		return this.keyBindings[i1].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[i1].keyCode);
	}

	public void setKeyBinding(int i1, int i2) {
		this.keyBindings[i1].keyCode = i2;
		this.saveOptions();
	}

	public void setOptionValue(int i1, int i2) {
		if(i1 == 0) {
			this.a = !this.a;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(i1 == 1) {
			this.b = !this.b;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(i1 == 2) {
			this.invertMouse = !this.invertMouse;
		}

		if(i1 == 3) {
			this.d = !this.d;
		}

		if(i1 == 4) {
			this.renderDistance = this.renderDistance + i2 & 3;
		}

		if(i1 == 5) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(i1 == 6) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(i1 == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		if(i1 == 8) {
			this.difficulty = (this.difficulty + i2) % 5;
		}

		if(i1 == 9) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public String getOptionDisplayString(int i1) {
		return i1 == 0 ? "Music: " + (this.a ? "ON" : "OFF") : (i1 == 1 ? "Sound: " + (this.b ? "ON" : "OFF") : (i1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (i1 == 3 ? "Show FPS: " + (this.d ? "ON" : "OFF") : (i1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (i1 == 5 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (i1 == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (i1 == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (i1 == 8 ? "Difficulty: " + DIFFICULTY_LEVELS[this.difficulty] : (i1 == 9 ? "Graphics: " + (this.fancyGraphics ? "FANCY" : "FAST") : "")))))))));
	}

	public void loadOptions() {
		try {
			if(this.optionsFile.exists()) {
				BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));

				while(true) {
					String string2;
					do {
						if((string2 = bufferedReader1.readLine()) == null) {
							bufferedReader1.close();
							return;
						}
					} while(string2.startsWith("#"));

					String[] string3 = string2.split(":");
					if(string3[0].equals("music")) {
						this.a = string3[1].equals("true");
					}

					if(string3[0].equals("sound")) {
						this.b = string3[1].equals("true");
					}

					if(string3[0].equals("invertYMouse")) {
						this.invertMouse = string3[1].equals("true");
					}

					if(string3[0].equals("showFrameRate")) {
						this.d = string3[1].equals("true");
					}

					if(string3[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("bobView")) {
						this.viewBobbing = string3[1].equals("true");
					}

					if(string3[0].equals("anaglyph3d")) {
						this.anaglyph = string3[1].equals("true");
					}

					if(string3[0].equals("limitFramerate")) {
						this.limitFramerate = string3[1].equals("true");
					}

					if(string3[0].equals("a1605ext_difficulty")) {
						this.difficulty = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("a1605ext_gamepadSens")) {
						InputHandler.lookSens = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("fancyGraphics")) {
						this.fancyGraphics = string3[1].equals("true");
					}

					for(int i4 = 0; i4 < this.keyBindings.length; ++i4) {
						if(string3[0].equals("key_" + this.keyBindings[i4].keyDescription)) {
							this.keyBindings[i4].keyCode = Integer.parseInt(string3[1]);
						}
					}
				}
			}
		} catch (Exception exception5) {
			System.out.println("Failed to load options");
			exception5.printStackTrace();
		}
	}

	public void saveOptions() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.optionsFile));
			printWriter1.println("music:" + this.a);
			printWriter1.println("sound:" + this.b);
			printWriter1.println("invertYMouse:" + this.invertMouse);
			printWriter1.println("showFrameRate:" + this.d);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("difficulty:" + Math.min(this.difficulty, 3));
			printWriter1.println("bobView:" + this.viewBobbing);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("limitFramerate:" + this.limitFramerate);
			printWriter1.println("fancyGraphics:" + this.fancyGraphics);
			printWriter1.println("# Settings below are specific to 1605 extension.");
			printWriter1.println("a1605ext_difficulty:" + this.difficulty);
			printWriter1.println("a1605ext_gamepadSens:" + InputHandler.lookSens);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}
}
