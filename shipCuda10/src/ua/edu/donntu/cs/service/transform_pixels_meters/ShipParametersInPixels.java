/*Max and min values of point coordinates. Uses axis X*/
package ua.edu.donntu.cs.service.transform_pixels_meters;

import ua.edu.donntu.cs.inout.IPrepareData;

/**
 * Этот класс определяет длину корабля в пикселах. Разница между самым большим и
 * самым малым значением по иксу является основой для перевода из метров в
 * пикселы и наоборот
 * 
 * @author Denis Vodolazskiy
 * 
 */
public class ShipParametersInPixels implements IPrepareData {
	private int minPixX = 0;// -4905;
	private int maxPixX = 0;// 4728;

	public ShipParametersInPixels() {
		for (int i = 0; i < Q_POINTS; i++) {
			if (points[i][0] < minPixX) {
				minPixX = points[i][0];
			}
			if (points[i][0] > maxPixX) {
				maxPixX = points[i][0];
			}
		}
	}

	public int getMinPixX() {
		return minPixX;
	}

	public int getMaxPixX() {
		return maxPixX;
	}
}