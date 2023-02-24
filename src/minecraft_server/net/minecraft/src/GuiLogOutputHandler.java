package net.minecraft.src;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;

public class GuiLogOutputHandler extends Handler {
	private int[] allNums = new int[1024];
	private int currentNum = 0;
	Formatter formatter = new GuiLogFormatter(this);
	private JTextArea textArea;

	public GuiLogOutputHandler(JTextArea textArea) {
		this.setFormatter(this.formatter);
		this.textArea = textArea;
	}

	public void close() {
	}

	public void flush() {
	}

	public void publish(LogRecord logRecord) {
		int i2 = this.textArea.getDocument().getLength();
		this.textArea.append(this.formatter.format(logRecord));
		this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
		int i3 = this.textArea.getDocument().getLength() - i2;
		if(this.allNums[this.currentNum] != 0) {
			this.textArea.replaceRange("", 0, this.allNums[this.currentNum]);
		}

		this.allNums[this.currentNum] = i3;
		this.currentNum = (this.currentNum + 1) % 1024;
	}
}
