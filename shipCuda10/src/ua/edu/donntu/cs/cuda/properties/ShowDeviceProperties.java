package ua.edu.donntu.cs.cuda.properties;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Окно свойств системы
 * 
 * @author Denis Vodolazskiy
 */
public class ShowDeviceProperties extends JFrame {
	/**
	 * Панель для размещения компонентов
	 */
	private JPanel panel = new JPanel();

	/**
	 * Конструктор, задаёт расположение окна и его свойства. Выводит свойства в
	 * текстовую область
	 */
	public ShowDeviceProperties() {
		super("Свойства системы:");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		panel.setLayout(null);
		setSize(600, 650); // задание размеров
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // задание параметров
															// // главного окна
															// при закрытии

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getSize().width;
		int height = getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		setLocation(x, y);
		//
		TextArea ta = new TextArea();
		ta.setSize(width, height);
		Font font = new Font(Font.MONOSPACED, 1, 13);
		ta.setFont(font);
		//
		String compProperties = "";
		java.util.Properties properties = System.getProperties();
		compProperties += String.format("   %-20s %s", "User name: ",
				properties.getProperty("user.name") + "\n");
		compProperties += String.format("   %-20s %s", "OS: ",
				properties.getProperty("os.name") + "\n");
		compProperties += String.format("   %-20s %s", "OS version: ",
				properties.getProperty("os.version") + "\n");
		compProperties += String.format("   %-20s %s", "OS architecture: ",
				properties.getProperty("os.arch") + "\n");
		compProperties += String.format("   %-20s %s", "Java version: ",
				properties.getProperty("java.version") + "\n");
		compProperties += "\n";
		//

		ta.setText(compProperties + new JCudaDeviceQuery().getCudaProperties());
		ta.setCaretPosition(0);
		ta.setEditable(false);
		panel.add(ta);
		//
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}
}
