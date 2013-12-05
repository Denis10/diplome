/*Этот класс переводит координаты в в метры. За основу взято отношение длины корабля в точках к длине в метрах*/
package ua.edu.donntu.cs.service.transform_pixels_meters;

import ua.edu.donntu.cs.path.calculations_kutt.IKuttCalc;

/**
 * Преобразоваяние метров в пикселы и наоборот. За основу взято отношение длины
 * корабля в точках к длине в метрах
 * 
 * @author Denis Vodolazskiy
 * 
 */
public class MetMmTransform implements IKuttCalc {
	private double meter;
	private double mm;

	/**
	 * Конструктор устанавоивает цену деления для пиксела и метра
	 */
	public MetMmTransform() {		
		meter = 1000;
		mm = 1.0 / meter;
	}

	/**
	 * Перевод метров в пикселы
	 * 
	 * @param Xm
	 *            количество метров
	 * @return количество пикселов
	 */
	public int meter2mm(double Xm) {
		if (Xm >= 0)
			return (int) (meter * Xm + 0.5);
		else
			return (int) (meter * Xm - 0.5);
	}

	/**
	 * Перевод пикселов в метры
	 * 
	 * @param Xp
	 *            количество пикселов
	 * @return количество метров
	 */
	public int mm2meter(double Xp) {
		if (Xp >= 0)
			return (int) (mm * Xp + 0.5);
		else
			return (int) (mm * Xp - 0.5);
	}
}