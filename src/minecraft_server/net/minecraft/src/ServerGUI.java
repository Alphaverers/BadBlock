package net.minecraft.src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.minecraft.server.MinecraftServer;

public class ServerGUI extends JComponent implements ICommandListener {
	public static Logger logger = Logger.getLogger("Minecraft");
	private MinecraftServer mcServer;

	public static void initGui(MinecraftServer minecraftServer) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception3) {
		}

		ServerGUI serverGUI1 = new ServerGUI(minecraftServer);
		JFrame jFrame2 = new JFrame("Minecraft server");
		jFrame2.add(serverGUI1);
		jFrame2.pack();
		jFrame2.setLocationRelativeTo((Component)null);
		jFrame2.setVisible(true);
		jFrame2.addWindowListener(new ServerWindowAdapter(minecraftServer));
	}

	public ServerGUI(MinecraftServer minecraftServer) {
		this.mcServer = minecraftServer;
		this.setPreferredSize(new Dimension(854, 480));
		this.setLayout(new BorderLayout());

		try {
			this.add(this.getLogComponent(), "Center");
			this.add(this.getStatsComponent(), "West");
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

	}

	private JComponent getStatsComponent() {
		JPanel jPanel1 = new JPanel(new BorderLayout());
		jPanel1.add(new GuiStatsComponent(), "North");
		jPanel1.add(this.getPlayerListComponent(), "Center");
		jPanel1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
		return jPanel1;
	}

	private JComponent getPlayerListComponent() {
		PlayerListBox playerListBox1 = new PlayerListBox(this.mcServer);
		JScrollPane jScrollPane2 = new JScrollPane(playerListBox1, 22, 30);
		jScrollPane2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
		return jScrollPane2;
	}

	private JComponent getLogComponent() {
		JPanel jPanel1 = new JPanel(new BorderLayout());
		JTextArea jTextArea2 = new JTextArea();
		logger.addHandler(new GuiLogOutputHandler(jTextArea2));
		JScrollPane jScrollPane3 = new JScrollPane(jTextArea2, 22, 30);
		jTextArea2.setEditable(false);
		JTextField jTextField4 = new JTextField();
		jTextField4.addActionListener(new ServerGuiCommandListener(this, jTextField4));
		jTextArea2.addFocusListener(new ServerGuiFocusadapter(this));
		jPanel1.add(jScrollPane3, "Center");
		jPanel1.add(jTextField4, "South");
		jPanel1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
		return jPanel1;
	}

	public void addHelpCommandMessage(String string1) {
		logger.info(string1);
	}

	public String getUsername() {
		return "CONSOLE";
	}

	static MinecraftServer getMinecraftServer(ServerGUI gui) {
		return gui.mcServer;
	}
}
