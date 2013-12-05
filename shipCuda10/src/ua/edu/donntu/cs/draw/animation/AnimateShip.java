/*Class to animate 1 ship*/
package ua.edu.donntu.cs.draw.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import ua.edu.donntu.cs.draw.main.DrawShip;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Панель для анимации (вращения) одного объекта
 * 
 * @author Denis Vodolazskiy
 */
public class AnimateShip extends DrawShip implements Runnable {

	private static final long serialVersionUID = 1L;
	//
	private Thread animator;
	private final int DELAY = 20;

	/**
	 * Конструктор
	 */
	public AnimateShip() {
		// decrease default distance
		distanceX = new MetMmTransform().meter2mm(-205);//-80
		Color cBack = new Color(0, 0, 0);
		setBackground(cBack);
		setDoubleBuffered(true);
	}

	/**
	 * Создание потока
	 */
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	/**
	 * инкремент угла пси
	 */
	private void cycle() {
		psi++;
	}

	/**
	 * Перерисовка экрана
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		// setAntialiasing(g2);

		calc(points, polygons, normals, pr);

		makeAllPolygons(g2);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	/**
	 * Запуск потока
	 */
	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {

			cycle();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {// delay (sleep) must be positive
				sleep = 2;
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}

			beforeTime = System.currentTimeMillis();
		}
	}
}
