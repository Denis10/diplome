/*Class to get number of points and polygons*/

/*Not used*/
package ua.edu.donntu.cs.inout;

/**
 * Этот класс устанавливает количество точек и полигонов. Не используется
 * 
 * @author Denis Vodolazskiy
 */
public interface IQuantityOfPointsAndPolygons {
	// public final int Q_POLYGONS = 96;
	// public final int Q_POINTS = 58;

	public final int Q_POINTS = new QuantityOfStringsInFile()
			.quantityOfPoints();
	public final int Q_POLYGONS = new QuantityOfStringsInFile()
			.quantityOfPolygons();

}
