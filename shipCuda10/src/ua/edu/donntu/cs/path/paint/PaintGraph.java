/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.path.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Абстрактная панель для построения графиков
 * 
 * @author Denis Vodolazskiy
 */
public abstract class PaintGraph extends JPanel {

	protected int xMagnitude = 5_000;
	protected int yMagnitude = 20;
	protected float velX[];// velocity
	protected float velY[];// velocity
	protected String xAxisName = "";
	protected String yAxisName = "";
	protected int divideForRealModelingTime = 1;

	/**
	 * Пустой конструктор
	 */
	public PaintGraph() {
	}

	@Override
	/**
	 * Выполняет перерисовку панели
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		drawAxis(g2);
		BasicStroke pen1 = new BasicStroke(2);
		g2.setStroke(pen1);
		g2.setColor(Color.CYAN);
		drawLegend(g2);
	}

	/**
	 * Выполняет отрисовку осей
	 */
	protected abstract void drawAxis(Graphics2D g2);

	/**
	 * Выполняет отрисовку легенд
	 */
	protected abstract void drawLegend(Graphics g2);

	{
	}

	/**
	 * Сокращает количество шагов
	 * 
	 * @param divideForRealModelingTime
	 *            сократить во столько раз
	 */
	public void setDivideForRealModelingTime(int divideForRealModelingTime) {
		this.divideForRealModelingTime = divideForRealModelingTime;
	}
}
