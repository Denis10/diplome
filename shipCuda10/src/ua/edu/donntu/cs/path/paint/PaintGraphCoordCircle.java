/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.path.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Панель для построения графиков (спираль координат)
 * 
 * @author Denis Vodolazskiy
 */
public class PaintGraphCoordCircle extends PaintGraph {

	private int divScaleByX = 1;
	private int multScaleByY = 1;

	/**
	 * Первый конструктор
	 * 
	 * @param xAxisName
	 *            название оси X
	 * @param yAxisName
	 *            название оси Y
	 * @param velX
	 *            значение по оси X
	 * @param velY
	 *            значение по оси Y
	 */
	public PaintGraphCoordCircle(String xAxisName, String yAxisName,
			float velX[], float velY[]) {
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.velX = velX;
		this.velY = velY;
	}

	/**
	 * Второй конструктор
	 * 
	 * @param xAxisName
	 *            название оси X
	 * @param yAxisName
	 *            название оси Y
	 * @param velX
	 *            значение по оси X
	 * @param velY
	 *            значение по оси Y
	 * @param divScaleByX
	 *            делитель по оси X
	 * @param multScaleByY
	 *            множитель по оси Y
	 */
	public PaintGraphCoordCircle(String xAxisName, String yAxisName,
			float velX[], float velY[], int divScaleByX, int multScaleByY) {

		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.velX = velX;
		this.velY = velY;
		this.divScaleByX = divScaleByX;
		this.multScaleByY = multScaleByY;
	}

	/**
	 * Третий конструктор
	 * 
	 * @param xAxisName
	 *            название оси X
	 * @param yAxisName
	 *            название оси Y
	 * @param velX
	 *            значение по оси X
	 * @param velY
	 *            значение по оси Y
	 * @param divScaleByX
	 *            делитель по оси X
	 * @param multScaleByY
	 *            множитель по оси Y
	 * @param xMagnitude
	 *            деления по оси X
	 * @param yMagnitude
	 *            деления по оси Y
	 * @param divideForRealModelingTime
	 *            сокращает количество шагов
	 */
	public PaintGraphCoordCircle(String xAxisName, String yAxisName,
			float velX[], float velY[], int divScaleByX, int multScaleByY,
			int xMagnitude, int yMagnitude, int divideForRealModelingTime) {
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.velX = velX;
		this.velY = velY;
		this.divScaleByX = divScaleByX;
		this.multScaleByY = multScaleByY;
		this.xMagnitude = xMagnitude;// X
		this.yMagnitude = yMagnitude;// Y
		this.divideForRealModelingTime = divideForRealModelingTime;
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
		g2.drawString(xAxisName, this.getWidth() - 20,
				this.getHeight() / 2 + 15);
		g2.drawString(yAxisName, this.getWidth() / 2 + 10, 10);
		if (divScaleByX != 1) {
			g2.drawString("/" + divScaleByX, this.getWidth() - 20,
					this.getHeight() / 2 + 35);
		}
		if (multScaleByY != 1) {
			g2.drawString("*" + multScaleByY, this.getWidth() / 2 + 10, 30);
		}
		//
		g2.setColor(Color.blue);
		g2.drawLine(0, this.getHeight() / 2, this.getWidth(),
				this.getHeight() / 2);
		g2.drawLine(this.getWidth() / 2, this.getHeight(), this.getWidth() / 2,
				0);
		float k = this.getWidth();
		k /= xMagnitude;
		for (int i = 0, x = this.getWidth() / 2; i < xMagnitude / 2 + 1; i++, x = this
				.getWidth() / 2 + (int) (i * k)) {
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
		for (int i = 0, x = this.getWidth() / 2; i < xMagnitude / 2; i++, x = this
				.getWidth() / 2 - (int) (i * k)) {
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
			if (i % 500 == 0 && i != 0) {// second 0
				g2.drawString("-" + i, x - 4, this.getHeight() / 2 + 23);
			}
		}
		//
		float m = this.getHeight();
		m /= yMagnitude;
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			if (i % 100 == 0) {
				g2.drawLine(this.getWidth() / 2 + 3, x + this.getHeight() / 2,
						this.getWidth() / 2 - 3, x + this.getHeight() / 2);
			}
			if (i % 1000 == 0) {
				g2.drawLine(this.getWidth() / 2 + 10, x + this.getHeight() / 2,
						this.getWidth() / 2 - 10, x + this.getHeight() / 2);
			} else if (i % 500 == 0) {
				g2.drawLine(this.getWidth() / 2 + 5, x + this.getHeight() / 2,
						this.getWidth() / 2 - 5, x + this.getHeight() / 2);
			}
			if (i % 500 == 0 && i != 0) {// second 0
				g2.drawString("-" + i, this.getWidth() / 2 + 27,
						x + this.getHeight() / 2);
			}
		}
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			if (i % 100 == 0) {
				g2.drawLine(this.getWidth() / 2 + 3, -x + this.getHeight() / 2,
						this.getWidth() / 2 - 3, -x + this.getHeight() / 2);
			}
			if (i % 1000 == 0) {
				g2.drawLine(this.getWidth() / 2 + 10,
						-x + this.getHeight() / 2, this.getWidth() / 2 - 10, -x
								+ this.getHeight() / 2);
			} else if (i % 500 == 0) {
				g2.drawLine(this.getWidth() / 2 + 5, -x + this.getHeight() / 2,
						this.getWidth() / 2 - 5, -x + this.getHeight() / 2);
			}
			if (i % 500 == 0) {
				g2.drawString("" + i, this.getWidth() / 2 + 27,
						-x + this.getHeight() / 2);
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
		k = (k / xMagnitude);// MODELING_TIME);
		float m = this.getHeight();
		m = (m / yMagnitude);
		assert (velY.length >= xMagnitude);

		float previousX = velX[0];
		float previousY = velY[0];
		float nextX = velX[1];
		float nextY = velY[1];
		for (int i = 1; i < velX.length / divideForRealModelingTime; i++) {
			nextX = previousX + velX[i];
			nextY = previousY + velY[i];
			g2.drawLine((int) (previousX * k) / divScaleByX + this.getWidth()
					/ 2, (int) (this.getHeight() / 2 - previousY * m
					* multScaleByY),
					(int) (nextX * k) / divScaleByX + this.getWidth() / 2,
					(int) (this.getHeight() / 2 - nextY * m * multScaleByY));
			previousX = nextX;
			previousY = nextY;
		}
	}
}
