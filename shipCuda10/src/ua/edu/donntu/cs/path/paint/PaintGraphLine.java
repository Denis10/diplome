/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.path.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Панель для построения графиков (линия)
 * 
 * @author Denis Vodolazskiy
 */
public class PaintGraphLine extends PaintGraph {

	private int multScaleByY = 1;

	/**
	 * Первый конструктор
	 * 
	 * @param xAxisName
	 *            название оси X
	 * @param yAxisName
	 *            название оси Y
	 * @param velY
	 *            значение по оси Y
	 */
	public PaintGraphLine(String xAxisName, String yAxisName, float velY[]) {
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.velY = velY;
	}

	/**
	 * Второй конструктор
	 * 
	 * @param xAxisName
	 *            название оси X
	 * @param yAxisName
	 *            название оси Y
	 * @param velY
	 *            значение по оси Y
	 * @param multScaleByY
	 *            множитель по оси Y
	 */
	public PaintGraphLine(String xAxisName, String yAxisName, float velY[],
			int multScaleByY) {
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.velY = velY;
		this.multScaleByY = multScaleByY;
	}

	@Override
	/**
	 * Выполняет отрисовку осей
	 */
	protected void drawAxis(Graphics2D g2) {
		Color c = new Color(187, 187, 187);
		g2.setColor(c);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		//
		g2.setColor(Color.black);
		g2.drawString(xAxisName, this.getWidth() - 10,
				this.getHeight() / 2 + 15);
		g2.drawString(yAxisName, 10, 10);
		if (multScaleByY != 1) {
			g2.drawString("* " + multScaleByY, 10, 25);
		}
		//
		g2.setColor(Color.blue);
		g2.drawLine(0, this.getHeight() / 2, this.getWidth(),
				this.getHeight() / 2);
		g2.drawLine(0, this.getHeight(), 0, 0);
		float k = this.getWidth();
		k /= xMagnitude;
		for (int i = 0, x = 0; i < xMagnitude + 1; i++, x = (int) (i * k)) {
			if (i % 100 == 0) {
				g2.drawLine(x, this.getHeight() / 2 + 3, x,
						this.getHeight() / 2 - 3);
			}
			if (i % 1000 == 0) {
				g2.drawLine(x, this.getHeight() / 2 + 10, x,
						this.getHeight() / 2 - 10);
			} else if (i % 500 == 0) {
				g2.drawLine(x, this.getHeight() / 2 + 5, x,
						this.getHeight() / 2 - 5);
			}
			if (i % 500 == 0) {
				g2.drawString("" + i, x - 4, this.getHeight() / 2 + 23);
			}
		}
		//
		float m = this.getHeight();
		m /= yMagnitude;
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			g2.drawLine(3, x + this.getHeight() / 2, -3, x + this.getHeight()
					/ 2);
			if (i % 10 == 0) {
				g2.drawLine(10, x + this.getHeight() / 2, -10,
						x + this.getHeight() / 2);
			} else if (i % 5 == 0) {
				g2.drawLine(5, x + this.getHeight() / 2, -5,
						x + this.getHeight() / 2);
			}
			if (i % 5 == 0 && i != 0) {// second 0
				g2.drawString("-" + i, 27, x + this.getHeight() / 2);
			}
		}
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			g2.drawLine(3, -x + this.getHeight() / 2, -3, -x + this.getHeight()
					/ 2);
			if (i % 10 == 0) {
				g2.drawLine(10, -x + this.getHeight() / 2, -10,
						-x + this.getHeight() / 2);
			} else if (i % 5 == 0) {
				g2.drawLine(+5, -x + this.getHeight() / 2, -5,
						-x + this.getHeight() / 2);
			}
			if (i % 5 == 0) {
				g2.drawString("" + i, 27, -x + this.getHeight() / 2);
			}
		}
	}

	@Override
	/**
	 * Выполняет отрисовку легенд
	 */
	protected void drawLegend(Graphics g2) {
		// g2.setColor(Color.CYAN);
		float k = this.getWidth();
		k = (k / xMagnitude);
		float m = this.getHeight();
		m = (m / yMagnitude);
		//
		assert (velY.length >= xMagnitude);

		for (int i = 1; i < xMagnitude; i++) {
			g2.drawLine((int) (k * (i - 1)),
					(int) (this.getHeight() / 2 - velY[i - 1] * m
							* multScaleByY), (int) (k * i),
					(int) (this.getHeight() / 2 - velY[i] * m * multScaleByY));
		}
	}
}
