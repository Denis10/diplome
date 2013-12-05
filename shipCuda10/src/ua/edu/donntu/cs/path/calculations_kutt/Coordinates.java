/*gets coordinates and transform it to lehgth*/
package ua.edu.donntu.cs.path.calculations_kutt;

/**
 * Этот класс переводит значения координат в относительные. Например, вместо
 * скоростей V[0]=5,V[1]=4,V[2]=3 будет V[0]=5,V[1]=V[0]+4,V[2]=V[1]+3, то есть
 * 5, 9, 12. Скорости не переводятся
 * 
 * @author Denis Vodolazskiy
 */
public class Coordinates implements IKuttCalc {
	/**
	 * 
	 * @return координата по оси X в системе наблюдателя
	 */
	public float[] byX() {
		float[] x = new float[MC.getXobs().length];
		x[0] = MC.getXobs()[0];
		for (int i = 1; i < x.length; i++) {
			x[i] = x[i - 1] + MC.getXobs()[i];
		}
		return x;
	}

	/**
	 * 
	 * @return скорость по оси X в системе наблюдателя
	 */
	public float[] byVx() {
		return MC.getVX();
	}

	// Zpaint=Ymath
	/**
	 * 
	 * @return координата по оси Y в системе наблюдателя
	 */
	public float[] byZ() {
		float[] y = new float[MC.getYobs().length];
		y[0] = MC.getYobs()[0];
		for (int i = 1; i < y.length; i++) {
			y[i] = y[i - 1] + MC.getYobs()[i];
		}
		return y;
	}

	/**
	 * 
	 * @return скорость по оси Z в системе наблюдателя
	 */
	public float[] byVz() {
		return MC.getVY();
	}

	/**
	 * 
	 * @return угловая координата в системе наблюдателя
	 */
	public float[] byW() {
		float[] w = new float[MC.getW().length];
		w[0] = MC.getW()[0];
		for (int i = 1; i < w.length; i++) {
			w[i] = w[i - 1] + MC.getW()[i];
		}
		return w;
	}

	/**
	 * 
	 * @return угловая скорость в системе наблюдателя
	 */
	public float[] byW2() {
		return MC.getW();
	}

	/**
	 * 
	 * @return установившийся радиус циркуляции
	 */
	public float getRc() {
		return MC.getRc();
	}
}
