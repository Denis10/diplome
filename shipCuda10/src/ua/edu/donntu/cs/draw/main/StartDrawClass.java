/*Class to set window properties*/
package ua.edu.donntu.cs.draw.main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Окно для панели с отрисовкой кораблей
 * 
 * @author Denis Vodolazskiy
 */
public class StartDrawClass extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel jp;

	/**
	 * Конструктор, задаёт расположение окна и его свойства
	 */
	public StartDrawClass(JPanel jp) {
		this.jp = jp;
		add(jp);
		setTitle("Ship 9.1");
		setSize(new Dimension(700, 700));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
