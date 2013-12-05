package ua.edu.donntu.cs.draw.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import ua.edu.donntu.cs.draw.main.DrawShip;
import ua.edu.donntu.cs.path.calculations_kutt.Coordinates;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Панель для анимации (плывёт) одного объекта
 * 
 * @author Denis Vodolazskiy
 */
public class SwimShipThread extends DrawShip implements Runnable {

	private static final long serialVersionUID = 1L;
	//
	private Thread animator;
	private final int DELAY = 5;
	private int i = 0;
	private int distX = new MetMmTransform().meter2mm(1000);// +

	/**
	 * Конструктор
	 */
	public SwimShipThread() {
		// decrease default distance
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
	 * Изменение координат и угла пси
	 * 
	 * @param i
	 *            шаг
	 */
	public void cycle(int i) {
		// psi++;
		if (i < new Coordinates().byX().length) {
			distanceX = -(new MetMmTransform().meter2mm(new Coordinates()
					.byX()[i]) + distX);
			distanceZ = -(new MetMmTransform().meter2mm(new Coordinates()
					.byZ()[i]));
			// *3
			psi = (float) (180 * (new Coordinates().byW()[i]) / (Math.PI * 3));
			// System.out.println(new Coordinates().byX()[i]);
			// System.out.println(new Coordinates().byZ()[i]);
			// System.out.println(new Coordinates().byW()[i]);
		}
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

			cycle(i);
			i++;
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
