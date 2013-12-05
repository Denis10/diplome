package ua.edu.donntu.cs.service;

/**
 * Класс для вызова заставки при запуске 
 * 
 * @author Denis Vodolazskiy
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

public class Splash extends JWindow {
	Image img = Toolkit.getDefaultToolkit().getImage("data/images/splash.JPG");
	ImageIcon imgicon = new ImageIcon(img);

	/**
	 * Конструктор, вызывает заставку
	 */
	public Splash() {
		try {
			setSize(imgicon.getIconWidth(), imgicon.getIconHeight());
			setLocationRelativeTo(null);
			setVisible(true);
			Thread.sleep(2000);
			dispose();
			/*
			 * javax.swing.JOptionPane.showMessageDialog( (java.awt.Component)
			 * null, "Welcome", "Welcome Screen:",
			 * javax.swing.JOptionPane.DEFAULT_OPTION);
			 */
		} catch (Exception exception) {
			/*
			 * javax.swing.JOptionPane.showMessageDialog( (java.awt.Component)
			 * null, "Error" + exception.getMessage(), "Error:",
			 * javax.swing.JOptionPane.DEFAULT_OPTION);
			 */
		}
		SplashScreen screen = SplashScreen.getSplashScreen();
		new StartFrame();
	}

	/**
	 * Выполняет перерисовку окна
	 */
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}
}
