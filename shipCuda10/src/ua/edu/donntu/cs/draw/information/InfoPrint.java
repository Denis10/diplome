/*Information about keys and current state of object*/
package ua.edu.donntu.cs.draw.information;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.ray.ray_key_manager.CraftFast;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Этот класс выводит на панель пояснения по упралению объектами
 * 
 * @author Denis Vodolazskiy
 */
public class InfoPrint {
	private Graphics2D g2;
	private Craft craft;

	/**
	 * Конструктор класса InfoPrint
	 * 
	 * @param g2
	 *            графический контент
	 * @param craft
	 *            менеджер клавиатуры
	 */
	public InfoPrint(Graphics2D g2, Craft craft) {
		this.g2 = g2;
		this.craft = craft;
		Font font = new Font("Serif", Font.PLAIN, 14);
		Color words = new Color(255, 0, 255);
		g2.setPaint(words);
		g2.setFont(font);
		g2.drawString("\u2190 psi--", 10, 20);
		g2.drawString("\u2192 psi++", 140, 20);
		g2.drawString("\u2191 gamma--", 10, 40);
		g2.drawString("\u2193 gamma++", 140, 40);
		g2.drawString("PageUP   teta--", 10, 60);
		g2.drawString("PageDown   teta++", 140, 60);
		g2.drawString("F12   distanceX--", 10, 80);
		g2.drawString("F11   distanceX++", 140, 80);
		g2.drawString("F8   distanceZ--", 10, 100);
		g2.drawString("F7   distanceZ++", 140, 100);
		g2.drawString("F6   distanceY--", 10, 120);
		g2.drawString("F5   distanceY++", 140, 120);
		g2.drawString(
				"distX = "
						+ new MetMmTransform().mm2meter(craft.getDistX())
						+ " m", 10, 140);
		g2.drawString(
				"distY = "
						+ new MetMmTransform().mm2meter(craft.getDistY())
						+ " m", 140, 140);
		g2.drawString(
				"distZ = "
						+ new MetMmTransform().mm2meter(craft.getDistZ())
						+ " m", 10, 160);
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
        /**
	 * Конструктор класса InfoPrint (CraftFast)
	 * 
	 * @param g2
	 *            графический контент
	 * @param craft
	 *            менеджер клавиатуры
	 */
	public InfoPrint(Graphics2D g2, CraftFast craft) {
		this.g2 = g2;		
		Font font = new Font("Serif", Font.PLAIN, 14);
		Color words = new Color(255, 0, 255);
		g2.setPaint(words);
		g2.setFont(font);
		g2.drawString("\u2190 psi--", 10, 20);
		g2.drawString("\u2192 psi++", 140, 20);
		g2.drawString("\u2191 gamma--", 10, 40);
		g2.drawString("\u2193 gamma++", 140, 40);
		g2.drawString("PageUP   teta--", 10, 60);
		g2.drawString("PageDown   teta++", 140, 60);
		g2.drawString("F12   distanceX--", 10, 80);
		g2.drawString("F11   distanceX++", 140, 80);
		g2.drawString("F8   distanceZ--", 10, 100);
		g2.drawString("F7   distanceZ++", 140, 100);
		g2.drawString("F6   distanceY--", 10, 120);
		g2.drawString("F5   distanceY++", 140, 120);
		g2.drawString(
				"distX = "
						+ new MetMmTransform().mm2meter(craft.getDistX())
						+ " m", 10, 140);
		g2.drawString(
				"distY = "
						+ new MetMmTransform().mm2meter(craft.getDistY())
						+ " m", 140, 140);
		g2.drawString(
				"distZ = "
						+ new MetMmTransform().mm2meter(craft.getDistZ())
						+ " m", 10, 160);
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
