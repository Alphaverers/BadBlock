package net.minecraft.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class PanelCrashReport extends Panel {
	public PanelCrashReport(UnexpectedThrowable throwable) {
		this.setBackground(new Color(3028036));
		this.setLayout(new BorderLayout());
		StringWriter stringWriter2 = new StringWriter();
		throwable.exception.printStackTrace(new PrintWriter(stringWriter2));
		String string3 = stringWriter2.toString();
		String string4 = "";
		String string5 = "";

		try {
			string5 = string5 + "Generated " + (new SimpleDateFormat()).format(new Date()) + "\n";
			string5 = string5 + "\n";
			string5 = string5 + "Minecraft: Minecraft Alpha v1.1.0\n";
			string5 = string5 + "OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version") + "\n";
			string5 = string5 + "Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor") + "\n";
			string5 = string5 + "VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor") + "\n";
			string5 = string5 + "LWJGL: " + Sys.getVersion() + "\n";
			string4 = GL11.glGetString(GL11.GL_VENDOR);
			string5 = string5 + "OpenGL: " + GL11.glGetString(GL11.GL_RENDERER) + " version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR) + "\n";
		} catch (Throwable throwable8) {
			string5 = string5 + "[failed to get system properties (" + throwable8 + ")]\n";
		}

		string5 = string5 + "\n";
		string5 = string5 + string3;
		String string6 = "";
		string6 = string6 + "\n";
		string6 = string6 + "\n";
		if(string3.contains("Pixel format not accelerated")) {
			string6 = string6 + "      Bad video card drivers!      \n";
			string6 = string6 + "      -----------------------      \n";
			string6 = string6 + "\n";
			string6 = string6 + "Minecraft was unable to start because it failed to find an accelerated OpenGL mode.\n";
			string6 = string6 + "This can usually be fixed by updating the video card drivers.\n";
			if(string4.toLowerCase().contains("nvidia")) {
				string6 = string6 + "\n";
				string6 = string6 + "You might be able to find drivers for your video card here:\n";
				string6 = string6 + "  http://www.nvidia.com/\n";
			} else if(string4.toLowerCase().contains("ati")) {
				string6 = string6 + "\n";
				string6 = string6 + "You might be able to find drivers for your video card here:\n";
				string6 = string6 + "  http://www.amd.com/\n";
			}
		} else {
			string6 = string6 + "      Minecraft has crashed!      \n";
			string6 = string6 + "      ----------------------      \n";
			string6 = string6 + "\n";
			string6 = string6 + "Minecraft has stopped running because it encountered a problem.\n";
			string6 = string6 + "\n";
			string6 = string6 + "If you wish to report this, please copy this entire text and email it to support@mojang.com.\n";
			string6 = string6 + "Please include a description of what you did when the error occured.\n";
		}

		string6 = string6 + "\n";
		string6 = string6 + "\n";
		string6 = string6 + "\n";
		string6 = string6 + "--- BEGIN ERROR REPORT " + Integer.toHexString(string6.hashCode()) + " --------\n";
		string6 = string6 + string5;
		string6 = string6 + "--- END ERROR REPORT " + Integer.toHexString(string6.hashCode()) + " ----------\n";
		string6 = string6 + "\n";
		string6 = string6 + "\n";
		TextArea textArea7 = new TextArea(string6, 0, 0, 1);
		textArea7.setFont(new Font("Monospaced", 0, 12));
		this.add(new CanvasMojangLogo(), "North");
		this.add(new CanvasCrashReport(80), "East");
		this.add(new CanvasCrashReport(80), "West");
		this.add(new CanvasCrashReport(100), "South");
		this.add(textArea7, "Center");
	}
}
