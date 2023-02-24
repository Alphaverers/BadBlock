package net.minecraft.src;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.minecraft.client.Minecraft;

public class ScreenKeyInput extends GuiScreen {
	private GuiScreen a;
	private int h = 0;
	private String i = "";
	private Minecraft b;
	private String starStr = "";
	private String einval = "";
	public static String name = "";
	private int keyIndex;
	static String str = "/X*Y).)W/\'\'[YX-Y(\'\\,\'\'[W&.\\,/Z+.Z(+Z/+&\',+.\'-X+/X[\\Z[\\(/-.\\&\'X\\&-";
	String[] md5s = new String[]{"", "676655d0337e63a3e0abd30e683ce639", "f816352e775a85592718d5d016888a33", "63bde9809ecbf1e3f1d4613b3ae6a88a", "ab99d76f9a4c41a0f3591b122bbc985c", "21741b9e1076d281b58c881ef6315c86", "9892a43b1ceb0f2175663fef1120d824", "0b8ffcdae74fab65891d2d2ba7e14707", "4da0a7353e9ec58890d81f135d53af1c"};
	String[] sha256s = new String[]{"", "65d9ac073f4da8453b9ad134a137620a816579be10b5e561ab2d3d098d5d0f4e", "155469cf5bf319cf4c134b65c76259859e5b0b69a581fa5f89b29b90f9455d78", "6de43140b9a682893a37c1528e89301313865bccbd6b7f93a6fc533e71224050", "4807e2b120d5b060aeca3f1e29be86a6a0b12ba7f9ac0fa196f7e718b35046eb", "9dd68e36a9b1f6e3869681d6e8c1b82b55e9172c833216bed5ddca78b57ec63f", "61bc906a6896d1cbd8310b52419a2993c114eeafa8f4c5bbe8da61a5d18b73f0", "c74329a1eef811c3affedba7645099d10c0f6441b78f73a893ccb405abe319ab", "a87527c2d10fbbe74917474b60f2fe44619ef6c6d6260488ae44e6a5ddd27842"};

	public ScreenKeyInput(Minecraft minecraft1, int i2) {
		this.b = minecraft1;
		this.keyIndex = i2;
	}

	public static int playerIndex() {
		int i0 = 0;

		for(int i1 = ScreenInputPass.name.length(); i1-- > 0; i0 += ScreenInputPass.name.toCharArray()[i1] - 48) {
		}

		return i0 % 5;
	}

	public static String calcString(long j0, int i2) {
		long j3 = ~(~(~(~(~(~(~(~j0))))))) >> ~(~(~(~(~(~(~(~(64 - (str.toCharArray()[str.length() - 1] - "+".toCharArray()[0] + 6)))))))));
		char[] c5 = str.substring((int)j3 * 13, (int)j3 * 13 + 13).toCharArray();

		for(int i6 = 13; ~(~(~(~(i6--)))) > 0; c5[i6] = (char)((int)((long)c5[i6] - -(~(~j3) + (long)(~i2) + (long)str.toCharArray()[str.length() - 1] - (long)"+".toCharArray()[0])))) {
		}

		return (new String(c5)).substring(~(~str.toCharArray()[0]) ^ 47);
	}

	public static String byteArrayToHex(byte[] b0) {
		StringBuilder stringBuilder1 = new StringBuilder(b0.length * 2);
		byte[] b2 = b0;
		int i3 = b0.length;

		for(int i4 = 0; i4 < i3; ++i4) {
			byte b5 = b2[i4];
			stringBuilder1.append(String.format("%02x", new Object[]{b5}));
		}

		return stringBuilder1.toString();
	}

	private boolean KeyValid(String string1) {
		try {
			string1 = this.keyIndex + string1;
			MessageDigest messageDigest2 = MessageDigest.getInstance("SHA-256");
			MessageDigest messageDigest3 = MessageDigest.getInstance("MD5");
			byte[] b4 = messageDigest2.digest(string1.getBytes(StandardCharsets.UTF_8));
			byte[] b5 = messageDigest3.digest(string1.getBytes(StandardCharsets.UTF_8));
			return byteArrayToHex(b4).equals(this.sha256s[this.keyIndex]) && byteArrayToHex(b5).equals(this.md5s[this.keyIndex]);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException6) {
			System.out.println("Unknown error");
			return false;
		}
	}

	public void updateScreen() {
		++this.h;
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Input"));
		((GuiButton)this.controlList.get(0)).enabled = false;
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 0 && this.KeyValid(this.i)) {
				if(this.keyIndex == 8) {
					System.out.println(this.sha256s[playerIndex() * 4 - playerIndex() * 3 + playerIndex() * 0 - playerIndex() + 7]);
				} else {
					if(this.keyIndex == 3) {
						System.out.println(this.md5s[4]);
					}

					this.b.displayGuiScreen(new ScreenKeyInput(this.b, this.keyIndex + 1));
				}
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

		if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}".indexOf(c1) >= 0 && this.i.length() < 64) {
			this.i = this.i + c1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.i.length() > 0;
		super.keyTyped(c1, i2);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Input " + this.keyIndex, this.width / 2, this.height / 4 - 60 + 20, -1593835521);
		int i4 = this.width / 2 - 70;
		int i5 = this.height / 4 - 10 + 50 + 18;
		this.drawRect(i4 - 1, i5 - 1, i4 + 100 + 1, i5 + 20 + 1, -6250336);
		this.drawRect(i4, i5, i4 + 100, i5 + 20, 0xFF000000);
		this.drawString(this.fontRenderer, this.i + (this.h / 6 % 2 == 0 ? "|" : ""), i4 + 4, i5 + 6, 14737632);
		this.drawString(this.fontRenderer, this.einval, this.width / 2 - 120, this.height / 4 - 60 + 60 + 90, 16711680);
		super.drawScreen(i1, i2, f3);
	}
}
