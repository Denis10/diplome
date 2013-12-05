package ua.edu.donntu.cs.draw.move;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

import ua.edu.donntu.cs.draw.information.InfoSwimPrint;
import ua.edu.donntu.cs.draw.main.DrawShip;
import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.path.calculations_kutt.Coordinates;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Класс управляет движущимся кораблём
 * 
 * @author Denis Vodolazskiy
 */
public class SwimShip extends DrawShip implements ActionListener {

	private static final long serialVersionUID = 1L;
	private int i = 0;
	private int obsDistX = new MetMmTransform().meter2mm(1000);// здесь
																	// положительное
																	// число!!!
	private int shipDistX = 0;
	private int shipDistZ = 0;
	private double shipSpeedX = 0;
	private double shipSpeedZ = 0;
	//
	private Timer timer;
	private Craft craft;
	//
	private Coordinates coord = new Coordinates();

	/**
	 * Конструктор для управления движущимся кораблём
	 * 
	 */
	public SwimShip() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		// Color cFill = new Color(20, 80, 255);
		// setBackground(cFill);
		setDoubleBuffered(true);

		// init key_manager
		craft = new Craft(psi, teta, gamma, obsDistX, distanceY, distanceZ);
		// init delay between key events
		timer = new Timer(5, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//
		cycle(i);
		i++;
		craft.move();
		//
		repaint();
	}

	@Override
	/**
	 * Выполняет перерисовку экрана
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		// setAntialiasing(g2);
		// psi = craft.getPsi();
		// teta = craft.getTeta();
		// gamma = craft.getGamma();
		distanceX = -(shipDistX + craft.getDistX());
		distanceY = craft.getDistY();
		distanceZ = -(shipDistZ + craft.getDistZ());
		// make calculations
		calc(points, polygons, normals, pr);
		// draw polygons
		makeAllPolygons(g2);
		// draw information
		new InfoSwimPrint(g2, craft, distanceX, distanceZ, shipDistX,
				shipDistZ, shipSpeedX, shipSpeedZ, coord.getRc());// -
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	// key adapter
	/**
	 * Обрабатывает нажатие клавиш
	 * 
	 * @author Denis Vodolazskiy
	 * 
	 */
	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			craft.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			craft.keyPressed(e);
		}
	}

	/**
	 * Изменение координат и угла пси
	 * 
	 * @param i
	 *            шаг
	 */
	private void cycle(int i) {

		if (i < coord.byX().length) {// i< length of algorithm
			shipDistX = new MetMmTransform().meter2mm(coord.byX()[i]);
			shipDistZ = new MetMmTransform().meter2mm(coord.byZ()[i]);
			shipSpeedX = coord.byVx()[i];
			shipSpeedZ = coord.byVz()[i];
			// *3
			// psi =(float) (180*(new Coordinates().byW()[i])/(Math.PI));

		}
	}

}