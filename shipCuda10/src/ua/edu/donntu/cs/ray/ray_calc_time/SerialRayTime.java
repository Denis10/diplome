package ua.edu.donntu.cs.ray.ray_calc_time;

import java.awt.Color;

import ua.edu.donntu.cs.draw.matrix_transform.MatrixTransform;
import ua.edu.donntu.cs.ray.base_draw.BaseDrawRay;
import ua.edu.donntu.cs.ray.ray_calc.Po;

/**
 * Этот класс выполняет рассчёты для метода трассировки лучей на CPU. Необходим для
 * замера времени выполнения.
 * 
 * @author Denis Vodolazskiy
 */
public class SerialRayTime extends BaseDrawRay {
	/**
	 * Время выполнения
	 */
	private long timeout;
	private float t;

	/**
	 * Конструктор, выполняющий вычисления для метода трассировки лучей. 
	 * 
	 * @param points
	 *            точки объекта
	 * @param polygons
	 *            полигоны объекта
	 * @param normals
	 *            нормали объекта
	 * @param Nx
	 *            пикселы по X
	 * @param Ny
	 *            пикселы по Y
	 * 
	 */
	public SerialRayTime(int points[][], int polygons[][], int normals[][],
			int Nx, int Ny) {
		timeout = System.currentTimeMillis();
		//
		// 5. Calculate matrix E(local –global) to make rotation of coordinate
		// system
		double E[][] = new MatrixTransform().matr(0, 0, 0, 1);

		// Initialization of arrays
		float X;// расстояние до пиксела X
		float Y;// расстояние до пиксела Y
		float Z;// расстояние до пиксела Z
		float Xv;
		float Yv;
		float Zv;
		float Vx;
		float Vy;
		float Vz;

		// p(Q_POLYGONS);
		for (int i = 0; i < Nx; i++) {
			for (int j = 0; j < Ny; j++) {
				// 8. Element (i,j) of the window has 3D coordinate in observer
				// system (center - in right eye):
				X = d0;
				Y = b0 / 2.0f - j * hy;
				Z = -a0 / 2.0f + i * hx;
				// 9. Convert Ray’s parameters to global system:
				Xv = X * (float) E[0][0] + Y * (float) E[0][1] + Z
						* (float) E[0][2] - distanceX;
				Yv = X * (float) E[1][0] + Y * (float) E[1][1] + Z
						* (float) E[1][2] - distanceY;
				Zv = X * (float) E[2][0] + Y * (float) E[2][1] + Z
						* (float) E[2][2] - distanceZ;
				// 10. Convert Ray’s parameters to global system:
				Vx = Xv + distanceX;
				Vy = Yv + distanceY;
				Vz = Zv + distanceZ;

				// 11. Initializing:
				int hit = 0;
				float tmin = 1_000_000;// init min distance
				float tin = 1_000_000;// -Fmax
				float tout = -1_000_000;// Fmax
				float t_ = -1;// t*
				int gin = -1;
				int gmin;// index of polyfon
				// 12. Loop for objects analyses.
				// init arrays
				float Xvo[] = new float[Q_POLYGONS];
				float Yvo[] = new float[Q_POLYGONS];
				float Zvo[] = new float[Q_POLYGONS];
				float Vxo[] = new float[Q_POLYGONS];
				float Vyo[] = new float[Q_POLYGONS];
				float Vzo[] = new float[Q_POLYGONS];
				float NormalObs[][] = new float[Q_POLYGONS][3];

				for (int io = 0; io < 1; io++) {
					// 16. Location of iob - object loading:-
					Po po = new Po(0, 0, 0, psi, teta, gamma);
					// 17. Calculation of matrix D (global to local) to make
					// rotation of coordinate system – transformed matrix.
					double D[][] = new MatrixTransform().matr(psi, teta, gamma,
							2);

					// 18. Convert Ray parameters to object iob system - ????

					Xvo[io] = Xv * (float) D[0][0] + Yv * (float) D[0][1] + Zv
							* (float) D[0][2] + po.getX();
					Yvo[io] = Xv * (float) D[1][0] + Yv * (float) D[1][1] + Zv
							* (float) D[1][2] + po.getY();
					Zvo[io] = Xv * (float) D[2][0] + Yv * (float) D[2][1] + Zv
							* (float) D[2][2] + po.getZ();

					Vxo[io] = Vx * (float) D[0][0] + Vy * (float) D[0][1] + Vz
							* (float) D[0][2];
					Vyo[io] = Vx * (float) D[1][0] + Vy * (float) D[1][1] + Vz
							* (float) D[1][2];
					Vzo[io] = Vx * (float) D[2][0] + Vy * (float) D[2][1] + Vz
							* (float) D[2][2];

					// 19. Intersection stage – defined below.
					boolean cross = false;

					float div;
					float d;
					float dev;

					for (int iob = 0; iob < Q_POLYGONS; iob++) {
						div = normals[iob][0] * Vxo[io] + normals[iob][1]
								* Vyo[io] + normals[iob][2] * Vzo[io];
						d = normals[iob][0] * points[polygons[iob][0]][0]
								+ normals[iob][1] * points[polygons[iob][0]][1]
								+ normals[iob][2] * points[polygons[iob][0]][2];
						dev = normals[iob][0] * Xvo[io] + normals[iob][1]
								* Yvo[io] + normals[iob][2] * Zvo[io] + d;

						if (div != 0) {
							t = -dev / div;
							if (div < 0) {
								if (t < tin) {
									tin = t;
									gin = iob;
								}
							} else {
								if (t > tout) {
									tout = t;
								}
							}
						} else {
							tin = -1_000_000;
							tout = 0;
						}
					}// end of for iob< Q_POLYGONS

					//
					if (tin >= tout) {//
						cross = true;
					} else {
						cross = false;
					}
					t_ = tin;

					// 20
					if (cross) {
						hit++;// 23
						if (hit == 1) {// 21
							tmin = t_;
							gmin = gin;
						} else {
							if (t_ < tmin) {// 22 //<
								tmin = t_;
								gmin = gin;
								// continue;
							}
						}
					}
				}// end of for io<K objects
					// 13

				if (hit == 0) {
					// 14
					Color cFill = new Color(0, 0, 0);
					// fillPixcel(g2, i, j, cFill);
				} else {// 15
					Color cFill = new Color(255, 255, 255);
					// Color cFill = c[gin];
					// fillPixcel(g2, i, j, cFill);
				}
			}// Ny
		}// Nx

		// End of timer. Calc elapsed time.
		timeout = System.currentTimeMillis() - timeout;
	}

	/**
	 * Передаёт время выполнения
	 */
	public long getTimeout() {
		return timeout;
	}
}
