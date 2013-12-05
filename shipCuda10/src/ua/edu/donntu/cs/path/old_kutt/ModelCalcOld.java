/*Not Used*/
package ua.edu.donntu.cs.path.old_kutt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Этот класс не используется
 * 
 * @author Denis Vodolazskiy
 */
public class ModelCalcOld {

	//
	private double m = 2_0_000_000_000.0f; // масса судна
	private double l_ = 40.0f; // расстояние от ДРК до ЦТ судна
	private double Vx = 0.0f, Vy = 0.0f;// скорости по осям
	private double v = 0.0f, w = 0.0f;// линейная и угловая скорости
	private int t;
	private double VX[] = new double[750];
	private double VY[] = new double[750];
	private double ww[] = new double[750];
	// для второго интеграла
	private double X[] = new double[750];
	private double Y[] = new double[750];
	private double W[] = new double[750];
	// неподвижная система координат
	private double Xmov[] = new double[750];
	private double Ymov[] = new double[750];
	private double Wmov[] = new double[750];

	public ModelCalcOld() {
		double k11, k22, k66, k26;
		double T = 4.0f, L = 80.0f, B = 15.0f;// осадка, длина, ширина c 323,
												// 321
		double delta = 0.7f;// коэффициент общей полноты
		double c1, c2, c3, m1, m2;// коэффициеты Р.Я.Першица
		double w_ = 0.0f;// угол дрейфа approx
		double betta_d = 0.0f;// угол дрейфа в центре тяжести (rad)

		double Cxr, Cyr, Cmr;// коэффициенты Cxr, боковой силы, момента
		/*
		 * ??
		 */// double L1=B,T1=L/2;//хорда, полудлина вертикального крыла
			// ????????????????????
		double p = 1000.0f;// плотность воды
		double Xr, Yr, Mr;// гидродинамические усилия
		double ZpTei;// полезная тяга винтов при равномерном прямолинейном
						// движении =R+Xa
		double Jz;// момент инерции массы судна относительно оси Gz
		double ZpYri;// боковая сила ДРК

		double a66 = 0.31f;// числовой коэффициент
		double Ramp = 0.0f;// для одиночных рулей
		double lyamdaR = 1.0f;// удлинение c 335
		double deltaR = 0.349f;// угол перекладки руля - 20%
		// double Va;//скорость натекания воды на руль
		double Yri, Ysi;// боковая и стабилизирубщая силы
		double Ar = 5.0f;// площадь перьев
		double D = 2.0f;// диаметр диска винта
		double Cta = 10.0f, Ctt = 30.0f;// c 334
		double Kvx, Kvy, Kw;// коэффиценты для производных
		double No = 3.0f;// обороты двигателя //Першин
		double Re = 5000000.0f;// Число Рейнольда >5E8
		double K_betta;

		double fit;
		double xk = 1.0f;
		double bettar = 0.9f;
		double fik = 0.95f;
		double ld_;// относительная длина насадки c228
		double betta_D;// коэффициент расширения
		double fiD;// c 338
		double CyD1;
		double CyD;
		double A0;// площадь диска винта
		double xD;// c 339
		double viv = 0.0f;// угол для неподвижной системы координат с 27 ch3_2
		double[][] Rmatr = new double[][] {
				{ Math.cos(viv), -Math.sin(viv), 0.0f },
				{ Math.sin(viv), Math.cos(viv), 0.0f }, { 0.0f, 0.0f, 1.0f } };

		// Math.cos(viv),-Math.sin(viv),0.0f,Math.sin(viv),Math.cos(viv),0.0f,0.0f,0.0f,1.0f};
		// ------------------------------------------------------------
		// c 330
		k11 = (5.91f * (double) Math.pow(B / L, 2.0f) + 7.76f * (B / L) - 0.259f)
				/ (48.4f - 6.89f * (B / T) + 1.47f
						* (double) Math.pow(B / T, 2.0f) - 0.0475f * (double) Math
						.pow(B / T, 3.0f));
		k22 = ((0.722f + 0.224f * delta) * (1.022f - (double) Math.pow(B / L,
				2.0f))) / (0.264f + 0.368f * (B / T));
		k66 = (2.0f * T / B) * (2.59f + 0.781f * delta)
				* (0.357f - 1.77f * (double) Math.pow(B / L, 2.0f));
		k26 = k22;
		// k26=0;
		System.out.printf("k11=%f\tk22=%f\tk66=%f\tk26=%f\n", k11, k22, k66,
				k26);
		// c 323
		c1 = 3.14f * (T / L)
				* (double) Math.pow((0.63f / delta), (5.0f / 2.0f))
				* (double) Math.pow(L / (6.0f * B), (1.0f / 3.0f)) - 0.032f;
		c2 = -2.0f * k11 * delta * (B / L);
		c3 = 1.35f * (double) Math.pow(T / B, (1.0f / 2.0f))
				* (double) Math.pow((0.63f / delta), (3.0f / 2.0f)) - 0.029f;
		m1 = 1.67f * (T / L) - 0.56f * delta + 0.43f;
		m2 = -0.44f * (T / L) - 0.0375f;
		System.out.printf("c1=%f\tc2=%f\tc3=%f\tm1=%f\tm2=%f\n", c1, c2, c3,
				m1, m2);
		Jz = (m * (double) Math.pow(L, 2.0f) / 12.4f)
				* (0.463f + 0.574f * (double) Math.pow(delta, a66) + (double) Math
						.pow(B / L, 2.0f));// c 330
		/*
		 * коэффиценты для производных: 1/m(1+k11) 1/m(1+k22) 1/Jz(1+k26)
		 */
		Kvx = 1 / (m * (1 + k11));
		Kvy = 1 / (m * (1 + k22));
		Kw = 1 / (Jz * (1 + k26));
		System.out.printf("Kvx=%g\tKvy=%g\tKw=%g\n", Kvx, Kvy, Kw);
		// ----------------------------------------------------------------

		double k1, k2, k3, k4;
		double q1, q2, q3, q4;
		double z1, z2, z3, z4;
		double j1, j2, j3, j4;
		// t = 0.0f; // шаг времени
		t = 0;
		System.out.printf("\tVx\t\tVy\t\tw\tt\n");
		Vx = 5.0f;
		Vy = 0.0f;
		w = 0.0f;
		// -------------------------------------
		try {
			BufferedWriter outfile = new BufferedWriter(new FileWriter(
					"data/outfiles/VxVy.txt"));
			BufferedWriter outfile2 = new BufferedWriter(new FileWriter(
					"data/outfiles/viv.txt"));
			BufferedWriter outfile3 = new BufferedWriter(new FileWriter(
					"data/outfiles/XY.txt"));
			BufferedWriter outfile4 = new BufferedWriter(new FileWriter(
					"data/outfiles/XYmov.txt"));
			//
			outfile.write("\t Vx\t\t\t\t Vy\t\t\t\t w\t\t\t t\n");
			//
			outfile3.write("X\t\t Y\t\t W" + "\n");
			//
			outfile4.write("Xmov\t\t Ymov\t\t Wmov" + "\n");
			// ------------------------
			for (int i = 0; i < 750; i++) { // 16550
				v = (double) Math.sqrt((double) Math.pow(Vx, 2.0f)
						+ (double) Math.pow(Vy, 2.0f));
				if (Vx != 0) {
					// c 353 ?????????????????????????
					w_ = w * L / v;// ??????????????????????
					betta_d = -(double) Math.atan(Vy / Vx);// c 350
				} else {
					w_ = 0;
					betta_d = 0;
				}
				Cxr = 0.01f * (1.0f + 170.0f * (T / L));// для плота c 119
				Cyr = c1 * betta_d + c2 * w_ + c3 * betta_d * Math.abs(betta_d);// c
																				// 323
				Cmr = m1 * betta_d + m2 * w_;

				Xr = Cxr * L * T * (double) Math.pow(v, 2.0f) * p / 2.0f;// c
																			// 320
				Yr = Cyr * L * T * (double) Math.pow(v, 2.0f) * p / 2.0f;
				Mr = Cmr * L * T * (double) Math.pow(v, 2.0f) * p / 2.0f;

				K_betta = 0.43f * (double) Math.pow(Ctt, -0.6f);
				fit = (double) Math.pow(1.0f + Ctt, 0.508f);
				Yri = 3.14f
						* (lyamdaR - K_betta * xk * (betta_d + l_ * w_))
						* p
						* Ar
						* (double) Math.pow(v * fik * fit, 2.0f)
						/ (1.0f + 2.2f / (double) Math
								.pow(lyamdaR, 2.0f / 3.0f));

				ld_ = 0.77f - 0.125f * (double) Math.sqrt(Ctt)
						/ (1.65f * (double) Math.sqrt(Ctt) - 1.0f);
				betta_D = 1.22f - 0.0563f * (double) Math.sqrt(Ctt)
						/ (1.65f * (double) Math.sqrt(Ctt) - 1.0f);
				fiD = 0.5f * ((double) Math.sqrt(1.0f + 2.0f * Ctt / betta_D) + 1.0f);

				CyD1 = 12.0f * ld_ / (1.0f + 1.56f * ld_);
				CyD = CyD1 + 2.0f * betta_D * (double) Math.pow(fiD, 2.0f);
				xD = xk
						* (CyD1 + 2.0f * betta_D * fiD)
						/ (CyD1 + 2.0f * betta_D * (double) Math.pow(fiD, 2.0f));
				A0 = 3.14f * (double) Math.pow(D, 2.0f) / 4.0f;
				Ysi = CyD * (xD - 0.02f * xk) * (betta_d + l_ * w_)
						* (p / 2.0f) * A0 * (double) Math.pow(v, 2.0f)
						* (double) Math.pow(fik, 2.0f);

				// ZpTei = Xr;//при прямолинейном движении Xr=R
				ZpTei = 9.740f * (double) Math.pow(No, 2.0f) - 2.23f * v; // Пелевин

				ZpYri = 2.0f * (Yri - Ysi);// 2 винта
				// printf("ZpTei=%f\tZpYri=%f\t\n",ZpTei,ZpYri);

				k1 = vxd(Vy, w, ZpTei, Xr, Kvx);
				q1 = vyd(Vx, w, ZpYri, Yr, Kvy);
				z1 = wd(Mr, ZpTei, Yr, Kw);

				k2 = vxd(Vy + (t * q1) / 2.0f, w + (t * z1) / 2.0f, ZpTei, Xr,
						Kvx);
				q2 = vyd(Vx + (t * k1) / 2.0f, w + (t * z1) / 2.0f, ZpYri, Yr,
						Kvy);
				z2 = wd(Mr, ZpYri, Yr, Kw);

				k3 = vxd(Vy + (t * q2) / 2.0f, w + (t * z2) / 2.0f, ZpTei, Xr,
						Kvx);
				q3 = vyd(Vx + (t * k2) / 2.0f, w + (t * z2) / 2.0f, ZpYri, Yr,
						Kvy);
				z3 = wd(Mr, ZpYri, Yr, Kw);

				k4 = vxd(Vy + (t * q3), w + (t * z3), ZpTei, Xr, Kvx);
				q4 = vyd(Vx + (t * k3), w + (t * z3), ZpYri, Yr, Kvy);
				z4 = wd(Mr, ZpYri, Yr, Kw);

				Vx = Vx + (t / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
				VX[t] = Vx;
				Vy = Vy + (t / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
				VY[t] = Vy;
				w = w + (t / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);
				ww[t] = w;

				// ---второй интеграл
				k1 = vxd(Vy, w, ZpTei, Xr, Kvx);
				q1 = vyd(Vx, w, ZpYri, Yr, Kvy);
				z1 = wd(Mr, ZpTei, Yr, Kw);

				k2 = vxd(Vy + (t * q1) / 2.0f, w + (t * z1) / 2.0f, ZpTei, Xr,
						Kvx);
				q2 = vyd(Vx + (t * k1) / 2.0f, w + (t * z1) / 2.0f, ZpYri, Yr,
						Kvy);
				z2 = wd(Mr, ZpYri, Yr, Kw);

				k3 = vxd(Vy + (t * q2) / 2.0f, w + (t * z2) / 2.0f, ZpTei, Xr,
						Kvx);
				q3 = vyd(Vx + (t * k2) / 2.0f, w + (t * z2) / 2.0f, ZpYri, Yr,
						Kvy);
				z3 = wd(Mr, ZpYri, Yr, Kw);

				k4 = vxd(Vy + (t * q3), w + (t * z3), ZpTei, Xr, Kvx);
				q4 = vyd(Vx + (t * k3), w + (t * z3), ZpYri, Yr, Kvy);
				z4 = wd(Mr, ZpYri, Yr, Kw);

				X[t] = Vx + (t / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
				Y[t] = Vy + (t / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
				W[t] = w + (t / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);
				outfile3.write(X[t] + "\t" + Y[t] + "\t" + W[t] + "\n");
				// угол для неподвижной системы координат
				j1 = t * wd(Mr, ZpTei, Yr, Kw);
				j2 = t * wd(Mr, ZpYri, Yr, Kw);
				j3 = t * wd(Mr, ZpYri, Yr, Kw);
				j4 = t * wd(Mr, ZpYri, Yr, Kw);
				viv = w + (1 / 6.0f) * (j1 + 2.0f * j2 + 2.0f * j3 + j4);
				outfile2.write("viv=" + viv + "\n");
				// System.out.println("viv="+viv);
				// -------
				Rmatr[0][0] = Math.cos(viv);
				Rmatr[0][1] = -Math.sin(viv);
				Rmatr[1][0] = Math.sin(viv);
				Rmatr[1][1] = Math.cos(viv);
				Xmov[t] = Rmatr[0][0] * X[t] + Rmatr[0][1] * Y[t];
				Ymov[t] = Rmatr[1][0] * X[t] + Rmatr[1][1] * Y[t];
				outfile4.write(Xmov[t] + "\t" + Ymov[t] + "\n");
				// ----------
				t += 1f;

				outfile.write(Vx + "\t\t" + Vy + "\t\t" + w + "\t\t" + t + "\n");
			}
			outfile.close();
			outfile2.close();
			outfile3.close();
			outfile4.close();
		} catch (IOException e) {
		}
		// --------------------------------------------------------------------
	}

	public double vxd(double Vy, double w, double ZpTei, double Xr, double Kvx) {
		// double Kvx=4.95715e-008f;
		// double m = 2_000_000_000_000.0f;
		return ((ZpTei + m * Vy * w - Xr) * Kvx);
	}

	public double vyd(double Vx, double w, double ZpYri, double Yr, double Kvy) {
		// double Kvy=2.3871e-008f;
		// double m = 2_000_000_000_000.0f;
		return ((Yr - ZpYri - m * Vx * w) * Kvy);
	}

	public double wd(double Mr, double ZpYri, double Yr, double Kw) {
		// double Kw=4.70718e-011f;
		// double lr = 40.0f;//расстояние от ДРК до ЦТ судна
		return ((Mr + l_ * ZpYri) * Kw);
	}

	public double[] getVX() {
		return VX;
	}

	public double[] getVY() {
		return VY;
	}

	public double[] getw() {
		return ww;
	}

	public double[] getX() {
		return X;
	}

	public double[] getY() {
		return Y;
	}

	public double[] getXmov() {
		return Xmov;
	}

	public double[] getYmov() {
		return Ymov;
	}
}