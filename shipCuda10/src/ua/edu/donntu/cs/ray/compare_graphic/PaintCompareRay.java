package ua.edu.donntu.cs.ray.compare_graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Этот класс выполнет построение графиков зависимости времени выполнения от
 * количества пикселов в окне. Метод трассировки лучей
 * 
 * @author Denis Vodolazskiy
 */
public class PaintCompareRay extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int divScaleByX = 1;
	private int divScaleByY = 1;
	private int xMagnitude = 1;
	private int yMagnitude = 1000;// 200000
	// private float valX[];
	private float valY[];
	private float valY2[];
	private String xAxisName = "";
	private String yAxisName = "";
	// private int divideForRealModelingTime = 1;
	private Color c1 = new Color(0).CYAN;
	private Color c2 = new Color(0).RED;
	private int increment = 1;

	/**
	 * Конструктор, принимает параметры
	 * 
	 * @param xAxisName
	 *            подпись по оси X
	 * @param yAxisName
	 *            подпись по оси Y
	 * @param valY
	 *            массив значений первой легенды (GPU)
	 * @param valY2
	 *            массив значений первой легенды (CPU)
	 * @param divScaleByX
	 *            делитель значений по оси X
	 * @param divScaleByY
	 *            делитель значений по оси Y
	 * @param increment
	 *            шаг по Y
	 */
	public PaintCompareRay(String xAxisName, String yAxisName, float valY[],
			float valY2[], int divScaleByX, int divScaleByY, int increment) {
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.valY = valY;
		this.valY2 = valY2;
		this.divScaleByX = divScaleByX;
		this.divScaleByY = divScaleByY;
		xMagnitude = valY.length;
		this.increment = increment;
	}

	/**
	 * Выполняет перерисовку окна
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		drawAxis(g2);
		BasicStroke pen1 = new BasicStroke(2);
		g2.setStroke(pen1);
		// g2.setColor(Color.CYAN);
		drawLegend(g2);
	}

	/**
	 * Выполняет отрисовку осей
	 */
	private void drawAxis(Graphics2D g2) {
		Color c = new Color(187, 187, 187);
		g2.setColor(c);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		//
		g2.setColor(c1);
		g2.drawString("GPU", this.getWidth() - 80, this.getHeight() / 2 + 60);
		g2.setColor(c2);
		g2.drawString("CPU", this.getWidth() - 40, this.getHeight() / 2 + 60);
		//
		g2.setColor(Color.black);
		g2.drawString(xAxisName, this.getWidth() - 40,
				this.getHeight() / 2 + 40);
		g2.drawString(yAxisName, 60, 10);
		if (divScaleByY != 1) {
			g2.drawString("* " + divScaleByY, 60, 25);
		}
		if (divScaleByX != 1) {
			g2.drawString("/" + divScaleByX, this.getWidth() - 20,
					this.getHeight() / 2 - 35);
		}
		//
		g2.setColor(Color.blue);
		g2.drawLine(0, this.getHeight() / 2, this.getWidth(),
				this.getHeight() / 2);
		g2.drawLine(0, this.getHeight(), 0, 0);
		float k = this.getWidth();
		k /= xMagnitude;
		for (int i = 0, x = 0; i < xMagnitude + 1; i++, x = (int) (i * k)) {
			if (i % 10 == 0) {// 1
				g2.drawLine(x, this.getHeight() / 2 + 3, x,
						this.getHeight() / 2 - 3);
			}
			if (i % 100 == 0) {// 10
				g2.drawLine(x, this.getHeight() / 2 + 10, x,
						this.getHeight() / 2 - 10);
			} else if (i % 50 == 0) {
				g2.drawLine(x, this.getHeight() / 2 + 5, x,
						this.getHeight() / 2 - 5);
			}
			if (i % 50 == 0) {// 5
				g2.drawString("" + i, x - 4, this.getHeight() / 2 + 23);
			}
		}
		//
		float m = this.getHeight();
		m /= yMagnitude;
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			// g2.drawLine(3, x + this.getHeight() / 2, - 3, x +
			// this.getHeight() / 2);
			if (i % 100 == 0) {
				g2.drawLine(10, x + this.getHeight() / 2, -10,
						x + this.getHeight() / 2);
			} else if (i % 20 == 0) {
				g2.drawLine(5, x + this.getHeight() / 2, -5,
						x + this.getHeight() / 2);
			}
			if (i % 100 == 0 && i != 0) {// second 0
				g2.drawString("-" + i, 27, x + this.getHeight() / 2);
			}
		}
		for (int i = 0, x = 0; i < yMagnitude / 2 + 1; i++, x = (int) (i * m)) {
			// g2.drawLine(3, -x + this.getHeight() / 2, - 3, -x +
			// this.getHeight() / 2);
			if (i % 100 == 0) {// 5000
				g2.drawLine(10, -x + this.getHeight() / 2, -10,
						-x + this.getHeight() / 2);
			} else if (i % 20 == 0) {// 1000
				g2.drawLine(+5, -x + this.getHeight() / 2, -5,
						-x + this.getHeight() / 2);
			}
			if (i % 100 == 0) {// 50000
				g2.drawString("" + i, 27, -x + this.getHeight() / 2);
			}
		}
	}

	/**
	 * Выполняет отрисовку легенд
	 */
	private void drawLegend(Graphics g2) {

		float k = this.getWidth();
		k = (k / xMagnitude);
		float displ = 0;
		float m = this.getHeight();
		m = (m / yMagnitude);
		//
		// assert (valY.length >= xMagnitude);

		for (int i = 1 + increment; i < xMagnitude; i += increment) {
			g2.setColor(c1);
			g2.drawLine((int) (displ + k * (i - increment) / divScaleByX),
					(int) (this.getHeight() / 2 - valY[i - increment] * m
							/ divScaleByY),
					(int) (displ + k * i / divScaleByX),
					(int) (this.getHeight() / 2 - valY[i] * m / divScaleByY));
			g2.setColor(c2);
			g2.drawLine((int) (displ + k * (i - increment) / divScaleByX),
					(int) (this.getHeight() / 2 - valY2[i - increment] * m
							/ divScaleByY),
					(int) (displ + k * i / divScaleByX),
					(int) (this.getHeight() / 2 - valY2[i] * m / divScaleByY));
		}
	}
}
