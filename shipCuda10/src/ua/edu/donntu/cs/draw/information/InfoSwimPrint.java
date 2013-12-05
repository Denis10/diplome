package ua.edu.donntu.cs.draw.information;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Этот класс выводит на панель пояснения по упралению объектами и текущее
 * состояние объектов
 * 
 * @author Denis Vodolazskiy
 */
public class InfoSwimPrint {
	private Graphics2D g2;
	private Craft craft;

	/**
	 * Конструктор
	 * 
	 * @param g2
	 *            графический контент
	 * @param craft
	 *            менеджер клавиатуры
	 * @param xObs
	 *            x-координата в системе наблюдателя
	 * @param zObs
	 *            z-координата в системе наблюдателя
	 * @param xFixed
	 *            x-координата в мировой системе
	 * @param zFixed
	 *            z-координата в мировой системе
	 * @param VxShip
	 *            скорость корабля по оси X
	 * @param VzShip
	 *            скорость корабля по оси Z
	 * @param Rcirculation
	 *            радиус циркуляции
	 */
	public InfoSwimPrint(Graphics2D g2, Craft craft, int xObs, int zObs,
			int xFixed, int zFixed, double VxShip, double VzShip,
			double Rcirculation) {
		this.g2 = g2;
		this.craft = craft;

		// for double
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(',');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00",
				decimalFormatSymbols);

		Font font = new Font("Serif", Font.PLAIN, 14);
		Color words = new Color(255, 0, 255);
		g2.setPaint(words);
		g2.setFont(font);
		g2.drawString("xObs = " + new MetMmTransform().mm2meter(-xObs)// -
				+ " m", 10, 20);
		g2.drawString("zObs = " + new MetMmTransform().mm2meter(-zObs)// -
				+ " m", 140, 20);
		g2.drawString("xFixed = " + new MetMmTransform().mm2meter(xFixed)
				+ " m", 270, 20);
		g2.drawString("zFixed = " + new MetMmTransform().mm2meter(zFixed)
				+ " m", 410, 20);
		g2.drawString(
				"dObsX = "
						+ new MetMmTransform().mm2meter(-craft.getDistX())// -
						+ " m", 10, 40);
		g2.drawString(
				"dObsY = "
						+ new MetMmTransform().mm2meter(craft.getDistY())
						+ " m", 140, 40);
		g2.drawString(
				"dObsZ = "
						+ new MetMmTransform().mm2meter(-craft.getDistZ())// -
						+ " m", 10, 60);
		g2.drawString("Rcirc = " + decimalFormat.format(Rcirculation) + " m",
				140, 60);
		g2.drawString("VxShip = " + decimalFormat.format(VxShip) + " m/s", 270,
				40);
		g2.drawString("VzShip = " + decimalFormat.format(VzShip) + " m/s", 410,
				40);
		g2.drawString("F12  distX++", 10, 80);
		g2.drawString("F11  distX--", 140, 80);
		g2.drawString("F8    distZ++", 10, 100);
		g2.drawString("F7    distZ--", 140, 100);
		g2.drawString("F6    distY++", 10, 120);
		g2.drawString("F5    distY--", 140, 120);
		// Axes
		g2.drawLine(600, 10, 600, 60);
		g2.drawLine(595, 20, 600, 10);
		g2.drawLine(605, 20, 600, 10);
		g2.drawString("X", 615, 20);
		g2.drawLine(600, 60, 650, 60);
		g2.drawLine(640, 55, 650, 60);
		g2.drawLine(640, 65, 650, 60);
		g2.drawString("Z", 660, 70);

	}
}
