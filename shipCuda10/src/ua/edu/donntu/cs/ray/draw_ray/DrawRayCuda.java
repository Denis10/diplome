/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.ray.draw_ray;

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
import ua.edu.donntu.cs.ray.ray_calc.ParallelRay;
import ua.edu.donntu.cs.ray.ray_key_manager.CraftFast;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Этот класс выполняет отрисовку объекта с помощью метода трассировки лучей на
 * GPU
 * 
 * @author Denis Vodolazskiy
 */
public class DrawRayCuda extends ParallelRay implements ActionListener {

	private static final long serialVersionUID = 1L;
	//
	private Timer timer;
	private CraftFast craft;

	/**
	 * Конструктор для отрисовки объекта с помощью метода трассировки лучей на
	 * GPU
	 * 
	 */
	public DrawRayCuda() {
		// decrease default distance
		final int distX = new MetMmTransform().meter2mm(-10);
		// System.out.print(distX);
		addKeyListener(new DrawRayCuda.TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		// init key_manager
		// psi=180;
		craft = new CraftFast(psi, teta, gamma, distX, distanceY, distanceZ);
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
		// calc(g2,points, polygons, normals);
		calc(g2, pointsBaseCuda, polygonsCuda, normalsCuda);
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
