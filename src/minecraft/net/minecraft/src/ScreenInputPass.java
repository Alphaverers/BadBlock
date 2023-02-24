package net.minecraft.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import net.minecraft.client.Minecraft;

public class ScreenInputPass extends GuiScreen {
	private GuiScreen a;
	private int h = 0;
	private String i = "";
	private Minecraft b;
	private String starStr = "";
	private String einval = "";
	public static String name = "";
	public static final int[] EndBytes = new int[]{39, 86, 26, 72, 13, 91, 23};

	public void updateScreen() {
		++this.h;
	}

	public void initGui() {
		this.controlList.clear();
		if(!GuiMainMenu.shw) {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Input"));
			this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96 + 36, "Clear"));
			((GuiButton)this.controlList.get(0)).enabled = false;
		} else {
			this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96 + 12, "Continue"));
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 1) {
				this.i = "";
			} else if(guiButton1.id == 0) {
				if(YesThisIsEasyToCircumvent_howeverPleaseDont(this.i)) {
					try {
						FileWriter fileWriter2 = new FileWriter(System.getProperty("user.dir") + "/v3_act");
						fileWriter2.write(this.i);
						fileWriter2.close();
					} catch (IOException iOException3) {
						System.out.println("Unable to save act...");
					}

					GuiMainMenu.shw = true;
					this.b.displayGuiScreen(new GuiMainMenu());
				} else {
					this.einval = "Invalid key";
				}
			} else if(guiButton1.id == 3) {
				this.b.displayGuiScreen(new GuiMainMenu());
			}

		}
	}

	protected void keyTyped(char c1, int i2) {
		if(c1 == 22) {
			String string3 = GuiScreen.getClipboardString();
			if(string3 == null) {
				string3 = "";
			}

			int i4 = 64 - this.i.length();
			if(i4 > string3.length()) {
				i4 = string3.length();
			}

			if(i4 > 0) {
				this.i = this.i + string3.substring(0, i4);
			}
		}

		if(c1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		if(i2 == 14 && this.i.length() > 0) {
			this.i = this.i.substring(0, this.i.length() - 1);
		}

		if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1???\u00ae\u00ac???\u00ab\u00bb".indexOf(c1) >= 0 && this.i.length() < 64) {
			this.i = this.i + c1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.i.length() > 0;
	}

	public void drawBackground(int i1) {
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		if(!GuiMainMenu.shw) {
			this.drawCenteredString(this.fontRenderer, "Welcome back to Lilypad", this.width / 2, this.height / 4 - 60 + 20, -1593835521);
			this.drawString(this.fontRenderer, "Remember to report bugs to the bug tracker", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
		}

		super.drawScreen(i1, i2, f3);
	}

	public static boolean IsByteNameEnd(int i0) {
		int[] i1 = EndBytes;
		int i2 = i1.length;

		for(int i3 = 0; i3 < i2; ++i3) {
			int i4 = i1[i3];
			if(i4 == i0) {
				return true;
			}
		}

		return false;
	}

	public static boolean YesThisIsEasyToCircumvent_howeverPleaseDont(String string0) {
		if(string0.length() != 43) {
			return false;
		} else if(string0.charAt(6) == 45 && string0.charAt(15) == 45 && string0.charAt(23) == 45 && string0.charAt(31) == 45 && string0.charAt(36) == 45) {
			String string1 = string0.replaceAll("-", "");
			int i2 = Integer.parseInt((new StringBuilder(string1.substring(0, 3))).reverse().toString());
			String string3 = string1.substring(3, 33);
			int i4 = Integer.parseInt(string1.substring(33, 36));
			int i5 = Integer.parseInt(string1.substring(36, 38));
			int i6 = i2 - i4;
			boolean z7 = false;
			String string8 = "";
			int i9 = 0;
			int i10 = 0;
			boolean z11 = false;
			boolean z12 = false;

			for(int i13 = 0; i13 != string3.length() / 2; ++i13) {
				i10 += string3.charAt(i13 * 2) - 48 + (string3.charAt(i13 * 2 + 1) - 48);
				if(!z7) {
					i9 += string3.charAt(i13 * 2) - 48 + (string3.charAt(i13 * 2 + 1) - 48);
				}

				int i14 = Integer.parseInt(string3.substring(i13 * 2, i13 * 2 + 2));
				if(!z7 && IsByteNameEnd(i14)) {
					z7 = true;
					z12 = true;
				} else if(!z7 && !IsByteNameEnd(i14)) {
					char c15 = (char)(i14 - 70 + 26 + 65);
					string8 = string8 + c15;
					if((c15 < 65 || c15 > 90) && (c15 < 48 || c15 > 57) && c15 != 95) {
						z11 = true;
					}
				}
			}

			i9 %= 100;
			name = string8;
			return i5 == i9 && i6 == i10 && !z11 && z12;
		} else {
			return false;
		}
	}
}
