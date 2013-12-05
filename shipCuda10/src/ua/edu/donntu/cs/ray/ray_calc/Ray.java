package ua.edu.donntu.cs.ray.ray_calc;

import static ua.edu.donntu.cs.inout.IPrepareData.Q_POLYGONS;

import java.awt.Color;
import java.awt.Graphics2D;

import ua.edu.donntu.cs.draw.matrix_transform.MatrixTransform;
import ua.edu.donntu.cs.ray.base_draw.BaseDrawRay;

/**
 * Этот класс выполняет рассчёты для метода трассировки лучей на CPU.
 * 
 * @author Denis Vodolazskiy
 */
public class Ray extends BaseDrawRay {

	private float t;
	/**
	 * Цвета граней куба
	 */
	Color c[] = new Color[] { new Color(255, 255, 0), new Color(255, 0, 0),
			new Color(255, 0, 255), new Color(0, 255, 0),
			new Color(0, 255, 255), new Color(0, 0, 255),
			new Color(0, 127, 255), new Color(0, 127, 0),
			new Color(0, 127, 127), new Color(127, 0, 127),
			new Color(127, 0, 0), new Color(127, 127, 0) };
	private static final long serialVersionUID = 1L;

	public Ray() {

	}

	/**
	 * Функция, выполняющая рассчёт видимых пикселов
	 * 
	 * @param g2
	 *            полотно
	 * @param points
	 *            точки объекта
	 * @param polygons
	 *            полигоны объекта
	 * @param normals
	 *            нормали объекта
	 * 
	 */
	public void calc(Graphics2D g2, int points[][], int polygons[][],
			int normals[][]) {
		// 5. Calculate matrix E(local –global) to make rotation of coordinate
		// system
		// float E[][] = new MatrixTransform().matr(psi, teta, gamma, 1);
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
				// for (int i = Nx / 2; i < Nx / 2 + 1; i++) {
				// for (int j = Ny / 2; j < Ny / 2+2 ; j++) {
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
				/*
				 * System.out.println("Observer:" + X[i * Nx + j] + " " + Y[i *
				 * Nx + j] + " " + Z[i * Nx + j]);
				 * System.out.println("Global system:" + Xv[i * Nx + j] + " " +
				 * Yv[i * Nx + j] + " " + Zv[i * Nx + j]);
				 */
				// 10. Convert Ray’s parameters to global system:
				Vx = Xv + distanceX;
				Vy = Yv + distanceY;
				Vz = Zv + distanceZ;
				// System.out.println("Global vector:" + Vx[i * Nx + j] + " "
				// + Vy[i * Nx + j] + " " + Vz[i * Nx + j]);
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

				for (int io = 0; io < 1; io++) {
					// 16. Location of iob - object loading:-
					Po po = new Po(0, 0, 0, psi, teta, gamma);
					// Po po = new Po(0, 0, 0, 0, 0, 0);
					// 17. Calculation of matrix D (global to local) to make
					// rotation of coordinate system – transformed matrix.
					double D[][] = new MatrixTransform().matr(psi, teta, gamma,
							2);
					/*
					 * for (int k=0;k<3;k++){ System.out.println(); for (int
					 * mn=0;mn<3;mn++){ System.out.print(E[k][mn]+" "); } }
					 */
					// 18. Convert Ray parameters to object iob system - ????
					// float Po[] = {0, 0, 0};

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
						// System.out.println("normal:" + normals[iob][0] +
						// " "+normals[iob][1]+" "+normals[iob][2]);
						// System.out.println("vector:" + Vxo[io] +
						// " "+Vyo[io]+" "+Vzo[io]);
						div = normals[iob][0] * Vxo[io] + normals[iob][1]
								* Vyo[io] + normals[iob][2] * Vzo[io];
						d = normals[iob][0] * points[polygons[iob][0]][0]
								+ normals[iob][1] * points[polygons[iob][0]][1]
								+ normals[iob][2] * points[polygons[iob][0]][2];
						dev = normals[iob][0] * Xvo[io] + normals[iob][1]
								* Yvo[io] + normals[iob][2] * Zvo[io] + d;
						// System.out.println("iob:" + iob +
						// " div="+div+" dev="+dev);
						if (div != 0) {
							t = -dev / div;
							// p(i+"   "+j+"   "+t+"   "+iob);
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
							// p("i="+i+"   j="+j+"  t="+t+"   tin="+tin+"   tout="+tout+"   iob="+iob);
						} else {
							tin = -1_000_000;
							tout = 0;
						}
						// System.out.println("t="
						// +t+" tin="+tin+" tout="+tout);
					}// end of for iob< Q_POLYGONS

					//
					if (tin >= tout) {// ???????????
						cross = true;
					} else {
						cross = false;
					}
					t_ = tin;

					// 20
					// System.out.println("gin="+gin+" cross="+cross);
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
					// System.out.println("Pixcel="+(i * Nx + j)+" hit="+hit);
					// 13
				if (hit == 0) {
					// 14
					Color cFill = new Color(0, 0, 0);

					fillPixcel(g2, i, j, cFill);
				} else {// 15
					// Color cFill = new Color(255, 255, 255);
					Color cFill = c[gin];
					fillPixcel(g2, i, j, cFill);
				}
			}// Ny
		}// Nx
	}

	/**
	 * Функция, выполняющая закраску пиксела
	 * 
	 * @param g2
	 *            полотно
	 * @param x
	 *            x-координата пиксела
	 * @param y
	 *            y-координата пиксела
	 * 
	 */
	protected void fillPixcel(Graphics2D g2, int x, int y) {
		Color cFill = new Color(255, 255, 255);
		g2.setPaint(cFill);
		g2.drawLine(x, y, x, y);
	}

	/**
	 * Функция, выполняющая закраску пиксела
	 * 
	 * @param g2
	 *            полотно
	 * @param x
	 *            x-координата пиксела
	 * @param y
	 *            y-координата пиксела
	 * @param c
	 *            цвет пиксела
	 * 
	 */
	protected void fillPixcel(Graphics2D g2, int x, int y, Color c) {
		g2.setPaint(c);
		g2.drawLine(x, y, x, y);
	}
}
