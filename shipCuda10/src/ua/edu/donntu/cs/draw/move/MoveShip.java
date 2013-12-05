/*This class allows to move 1 ship*/
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

import ua.edu.donntu.cs.draw.information.InfoPrint;
import ua.edu.donntu.cs.draw.main.DrawShip;
import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Класс управляет неподвижным кораблём
 * 
 * @author Denis Vodolazskiy
 */
public class MoveShip extends DrawShip implements ActionListener {

	private static final long serialVersionUID = 1L;
	//
	private Timer timer;
	private Craft craft;

	/**
	 * Конструктор для управления неподвижным кораблём
	 * 
	 */
	public MoveShip() {
		// decrease default distance
		final int distX = new MetMmTransform().meter2mm(-150);//-165
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		// Color cFill = new Color(20, 80, 255);
		// setBackground(cFill);
		setDoubleBuffered(true);
		// init key_manager
		craft = new Craft(psi, teta, gamma, distX, distanceY, distanceZ);
		// init delay between key events
		timer = new Timer(5, this);
		timer.start();
	}

        @Override
	public void actionPerformed(ActionEvent e) {
		craft.move();
		repaint();
	}

	/**
	 * Выполняет перерисовку экрана
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		// setAntialiasing(g2);
		psi = craft.getPsi();
		teta = craft.getTeta();
		gamma = craft.getGamma();
		distanceX = craft.getDistX();
		distanceY = craft.getDistY();
		distanceZ = craft.getDistZ();
		// make calculations
		calc(points, polygons, normals, pr);
		// draw polygons
		makeAllPolygons(g2);
		// draw information
		new InfoPrint(g2, craft);
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

                @Override
		public void keyReleased(KeyEvent e) {
			craft.keyReleased(e);
		}

                @Override
		public void keyPressed(KeyEvent e) {
			craft.keyPressed(e);
		}
	}
}
