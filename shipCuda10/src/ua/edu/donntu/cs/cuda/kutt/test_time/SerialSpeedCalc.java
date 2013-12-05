package ua.edu.donntu.cs.cuda.kutt.test_time;

/**
 * Этот класс вычисляет конечные скорости для произвольного количества кораблей
 * последовательно. Для одного корабля выделяется память объёмом
 * 8*nShips*Sizeof.FLOAT. Замеряется время выполнения
 * 
 * @author Denis Vodolazskiy
 */
public class SerialSpeedCalc {
	/**
	 * Количество кораблей
	 */
	private int nShips;
	/**
	 * Время выполнения
	 */
	private long timeout;
	// with k11 - 580.91; coef Vx, Vy
	final private float T = 4.0f, L = 80.0f, B = 15.0f;// осадка, длина, ширина
														// c 323, 321
	final private float m = 2_0_000_000_000.0f; // масса судна
	final private float lr = 40.0f; // расстояние от ДРК до ЦТ судна
	private float Vx = 0.0f, Vy = 0.0f;// скорости по осям
	private float v = 0.0f, w = 0.0f;// линейная и угловая скорости
	private float Rc;// радиус циркуляции
	/**
	 * Количество шагов (секунд)
	 */
	private int size;// = 8_000;

	/**
	 * Конструктор, выполняющий вычисления конечных скоростей для произвольного
	 * количества кораблей. Вычисления выполняются в цикле
	 * 
	 * @param nShips
	 *            количество кораблей (циклов)
	 * @param size
	 *            количество шагов вычислений (количество секунд)
	 * 
	 */
	public SerialSpeedCalc(int nShips, int size) {
		this.nShips = nShips;
		this.size = size;

		// start of timer
		timeout = System.currentTimeMillis();

		float VX[] = new float[size];
		float VY[] = new float[size];
		float ww[] = new float[size];
		// для второго интеграла
		float X[] = new float[size];
		float Y[] = new float[size];
		float W[] = new float[size];
		// неподвижная система координат
		float Xobs[] = new float[size];
		float Yobs[] = new float[size];

		for (int j = 0; j < nShips; j++) {
			float k11, k22, k66, k26;

			final float delta = 0.7f;// коэффициент общей полноты
			float c1, c2, c3, m1, m2;// коэффициеты Р.Я.Першица
			float w_ = 0.0f;// угол дрейфа approx
			float betta_d = 0.0f;// угол дрейфа в центре тяжести (rad)
			float Cxr, Cyr, Cmr;// коэффициенты Cxr, боковой силы, момента
			// float L1=B,T1=L/2;//хорда, полудлина вертикального крыла
			// ????????????????????
			final float p = 1000.0f;// плотность воды
			float Xr, Yr, Mr;// гидродинамические усилия
			float ZpTei = 0;// полезная тяга винтов при равномерном
							// прямолинейном движении =R+Xa
			float Jz;// момент инерции массы судна относительно оси Gz
			float ZpYri;// боковая сила ДРК
			final float a66 = 0.31f;// числовой коэффициент
			float Ramp = 0.0f;// для одиночных рулей
			float lyamdaR = 1.4f;// удлинение c 335 //0.5--1.4 //1

			float deltaR = 0.349f;// угол перекладки руля - 20% //0.349f

			// float Va;//скорость натекания воды на руль
			float Yri, Ysi;// боковая и стабилизирубщая силы
			final float Ar = 6.0f;// площадь перьев//5 можно увеличить
			final float D = 1.5f;// диаметр диска винта//2 можно уменьшить
			final float Cta = 10.0f, Ctt = 50.0f;// c 334 //Cta<=20, 0.5--30,
													// Ctt<=20, 1--50 UP!!!
			// Ctt =30
			float Kvx, Kvy, Kw;// коэффиценты для производных
			final float No = 3.0f;// обороты двигателя //Першин//3 оборота
			final float Re = 5000000.0f;// Число Рейнольда >5E8
			float K_betta;
			float fit;
			final float xk = 1.0f;
			final float bettar = 0.9f;
			final float fik = 0.95f;
			float ld_;// относительная длина насадки c228
			float betta_D;// коэффициент расширения
			float fiD;// c 338
			float CyD1;
			float CyD;
			float A0;// площадь диска винта
			float xD;// c 339
			float viv = 0.0f;// угол для неподвижной системы координат с 27
								// ch3_2
			float[][] Rmatr = new float[][] {
					{ (float) Math.cos(viv), -(float) Math.sin(viv), 0.0f },
					{ (float) Math.sin(viv), (float) Math.cos(viv), 0.0f },
					{ 0.0f, 0.0f, 1.0f } };
			// Math.cos(viv),-Math.sin(viv),0.0f,Math.sin(viv),Math.cos(viv),0.0f,0.0f,0.0f,1.0f};
			// ------------------------------------------------------------
			// c 330 5.91-в книге, 580.91 - выравнять Х
			k11 = (580.91f * (float) Math.pow(B / L, 2.0f) + 7.76f * (B / L) - 0.259f)
					/ (48.4f - 6.89f * (B / T) + 1.47f
							* (float) Math.pow(B / T, 2.0f) - 0.0475f * (float) Math
							.pow(B / T, 3.0f));

			k22 = ((0.722f + 0.224f * delta) * (1.022f - (float) Math.pow(
					B / L, 2.0f))) / (0.264f + 0.368f * (B / T));
			k66 = (2.0f * T / B) * (2.59f + 0.781f * delta)
					* (0.357f - 1.77f * (float) Math.pow(B / L, 2.0f));
			k26 = k22;
			// k26=0;
			// System.out.printf("k11=%f\tk22=%f\tk66=%f\tk26=%f\n", k11, k22,
			// k66, k26);
			// c 323
			c1 = 3.14f * (T / L)
					* (float) Math.pow((0.63f / delta), (5.0f / 2.0f))
					* (float) Math.pow(L / (6.0f * B), (1.0f / 3.0f)) - 0.032f;
			c2 = -2.0f * k11 * delta * (B / L);
			c3 = 1.35f * (float) Math.pow(T / B, (1.0f / 2.0f))
					* (float) Math.pow((0.63f / delta), (3.0f / 2.0f)) - 0.029f;
			m1 = 1.67f * (T / L) - 0.56f * delta + 0.43f;
			m2 = -0.44f * (T / L) - 0.0375f;
			// System.out.printf("c1=%f\tc2=%f\tc3=%f\tm1=%f\tm2=%f\n", c1, c2,
			// c3, m1, m2);
			Jz = (m * (float) Math.pow(L, 2.0f) / 12.4f)
					* (0.463f + 0.574f * (float) Math.pow(delta, a66) + (float) Math
							.pow(B / L, 2.0f));// c 330
			/*
			 * коэффиценты для производных: 1/m(1+k11) 1/m(1+k22) 1/Jz(1+k26)
			 */
			Kvx = 1 / (m * (1 + k11));
			Kvy = 1 / (m * (1 + k22));
			Kw = 1 / (Jz * (1 + k66));// ????? m
			// System.out.printf("Kvx=%g\tKvy=%g\tKw=%g\n", Kvx, Kvy, Kw);
			// ----------------------------------------------------------------

			float k1, k2, k3, k4;
			float q1, q2, q3, q4;
			float z1, z2, z3, z4;
			float j1, j2, j3, j4;
			// t = 0.0f; // шаг времени
			// int t = 0;
			float h = 1f;
			// System.out.printf("\tVx\t\tVy\t\tw\tt\n");
			Vx = 5.0f;
			Vy = 0.0f;
			w = 0.0f;
			// -------------------------------------
			// ------------------------
			for (int t = 0; t < size; t++) { // 16550
				v = (float) Math.sqrt((float) Math.pow(Vx, 2.0f)
						+ (float) Math.pow(Vy, 2.0f));
				// assert(Vx==0);
				if (Vx != 0) {
					// c 353 ?????????????????????????
					w_ = w * L / v;// ??????????????????????
					betta_d = -(float) Math.atan(Vy / Vx);// c 350
				} else {
					w_ = w * L / v;
					// betta_d = 0;
					betta_d = -(float) Math.atan(Vy / Vx);// c 350
				}
				// System.out.println(w_);
				Cxr = 0.01f * (1.0f + 170.0f * (T / L));// для плота c 119
				Cyr = c1 * betta_d + c2 * w_ + c3 * betta_d * Math.abs(betta_d);// c
																				// 323
				Cmr = m1 * betta_d + m2 * w_;

				Xr = Cxr * L * T * (float) Math.pow(v, 2.0f) * p / 2.0f;// c 320
				Yr = Cyr * L * T * (float) Math.pow(v, 2.0f) * p / 2.0f;
				Mr = Cmr * L * T * (float) Math.pow(v, 2.0f) * p / 2.0f;

				K_betta = 0.43f * (float) Math.pow(Ctt, -0.6f);
				fit = (float) Math.pow(1.0f + Ctt, 0.508f);

				// IMOPRTANT!!! deltaR
				Yri = 3.14f
						* (deltaR - K_betta * xk * (betta_d + lr * w_))
						* p
						* Ar
						* (float) Math.pow(v * fik * fit, 2.0f)
						/ (1.0f + 2.2f / (float) Math.pow(lyamdaR, 2.0f / 3.0f));

				ld_ = 0.77f - 0.125f * (float) Math.sqrt(Ctt)
						/ (1.65f * (float) Math.sqrt(Ctt) - 1.0f);
				betta_D = 1.22f - 0.0563f * (float) Math.sqrt(Ctt)
						/ (1.65f * (float) Math.sqrt(Ctt) - 1.0f);
				fiD = 0.5f * ((float) Math.sqrt(1.0f + 2.0f * Ctt / betta_D) + 1.0f);

				CyD1 = 12.0f * ld_ / (1.0f + 1.56f * ld_);
				CyD = CyD1 + 2.0f * betta_D * (float) Math.pow(fiD, 2.0f);
				xD = xk * (CyD1 + 2.0f * betta_D * fiD)
						/ (CyD1 + 2.0f * betta_D * (float) Math.pow(fiD, 2.0f));
				A0 = 3.14f * (float) Math.pow(D, 2.0f) / 4.0f;
				Ysi = CyD * (xD - 0.02f * xk) * (betta_d + lr * w_)
						* (p / 2.0f) * A0 * (float) Math.pow(v, 2.0f)
						* (float) Math.pow(fik, 2.0f);

				// ZpTei = Xr;//при прямолинейном движении Xr=R
				ZpTei = 1000_000f * (9.740f * (float) Math.pow(No, 2.0f) - 2.23f * v); // Пелевин//9.740f

				ZpYri = 2.0f * (Yri - Ysi);// 2 винта
				// printf("ZpTei=%f\tZpYri=%f\t\n",ZpTei,ZpYri);

				k1 = h * vxd(Vy, w, ZpTei, Xr, Kvx);
				q1 = h * vyd(Vx, w, ZpYri, Yr, Kvy);
				z1 = h * wd(Mr, ZpTei, Yr, Kw);

				k2 = h * vxd(Vy + q1 / 2.0f, w + z1 / 2.0f, ZpTei, Xr, Kvx);
				q2 = h * vyd(Vx + k1 / 2.0f, w + z1 / 2.0f, ZpYri, Yr, Kvy);
				z2 = h * wd(Mr, ZpYri, Yr, Kw);

				k3 = h * vxd(Vy + q2 / 2.0f, w + z2 / 2.0f, ZpTei, Xr, Kvx);
				q3 = h * vyd(Vx + k2 / 2.0f, w + z2 / 2.0f, ZpYri, Yr, Kvy);
				z3 = h * wd(Mr, ZpYri, Yr, Kw);

				k4 = h * vxd(Vy + q3, w + z3, ZpTei, Xr, Kvx);
				q4 = h * vyd(Vx + k3, w + z3, ZpYri, Yr, Kvy);
				z4 = h * wd(Mr, ZpYri, Yr, Kw);

				Vx = Vx + (1.0f / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
				// VX[t] = Vx / 1.24;
				VX[t] = Vx;
				Vy = Vy + (1.0f / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
				VY[t] = Vy;
				w = w + (1.0f / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);
				ww[t] = w;

				// ---второй интеграл-----------------------------------------
				k1 = h * vxd(Vy, w, ZpTei, Xr, Kvx);
				q1 = h * vyd(Vx, w, ZpYri, Yr, Kvy);
				z1 = h * wd(Mr, ZpTei, Yr, Kw);

				k2 = h * vxd(Vy + q1 / 2.0f, w + z1 / 2.0f, ZpTei, Xr, Kvx);
				q2 = h * vyd(Vx + k1 / 2.0f, w + z1 / 2.0f, ZpYri, Yr, Kvy);
				z2 = h * wd(Mr, ZpYri, Yr, Kw);

				k3 = h * vxd(Vy + q2 / 2.0f, w + z2 / 2.0f, ZpTei, Xr, Kvx);
				q3 = h * vyd(Vx + k2 / 2.0f, w + z2 / 2.0f, ZpYri, Yr, Kvy);
				z3 = h * wd(Mr, ZpYri, Yr, Kw);

				k4 = h * vxd(Vy + q3, w + z3, ZpTei, Xr, Kvx);
				q4 = h * vyd(Vx + k3, w + z3, ZpYri, Yr, Kvy);
				z4 = h * wd(Mr, ZpYri, Yr, Kw);

				X[t] = Vx + (1.0f / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
				Y[t] = Vy + (1.0f / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
				W[t] = w + (1.0f / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);

				viv = W[t];

				// -------
				Rmatr[0][0] = (float) Math.cos(viv);
				Rmatr[0][1] = -(float) Math.sin(viv);
				Rmatr[1][0] = (float) Math.sin(viv);
				Rmatr[1][1] = (float) Math.cos(viv);
				Xobs[t] = Rmatr[0][0] * X[t] + Rmatr[0][1] * Y[t];
				Yobs[t] = Rmatr[1][0] * X[t] + Rmatr[1][1] * Y[t];

				// ----------
				// t += 1f;

			}
		}

		// End of timer. Calc elapsed time.
		timeout = System.currentTimeMillis() - timeout;
		// System.out.println("(serial execution) elapsed time CPU="+timeout +
		// " ms");
	}

	/**
	 * Прирашение скорости по оси X
	 * 
	 * @param Vy
	 *            скорость по оси Y
	 * @param w
	 *            угловая скорость
	 * @param ZpTei
	 *            тяга в продольном направлениии
	 * @param Xr
	 *            гидродинамические усилия в продольном направлении
	 * @param Kvx
	 *            коэффициент по X
	 * @return приращение скорости
	 */
	private float vxd(float Vy, float w, float ZpTei, float Xr, float Kvx) {
		if (Vy > 0) {
			return ((m * Vy * w - Xr + ZpTei) * Kvx);
		} else {
			return ((m * Vy * w * 1.09f - Xr + ZpTei) * Kvx);// Vx*1.061
																// ;N0=3;k11=580.91f
																// Ubuntu
			// return ((m * Vy * w - Xr + ZpTei) * Kvx);
		}// *1.1045; k11=5.91f windows ; 1.09; k11=580.91f
	}

	/**
	 * Прирашение скорости по оси Y
	 * 
	 * @param Vx
	 *            скорость по оси x
	 * @param w
	 *            угловая скорость
	 * @param ZpYri
	 *            тяга в поперечном направлениии
	 * @param Yr
	 *            гидродинамические усилия в поперечном направлении
	 * @param Kvy
	 *            коэффициент по Y
	 * @return приращение скорости
	 */
	private float vyd(float Vx, float w, float ZpYri, float Yr, float Kvy) {
		if (Vx > 0) {
			return ((Yr - ZpYri - m * Vx * w * 1.0165f) * Kvy);// Vx*1.0179
			// return ((Yr - ZpYri - m * Vx * w) * Kvy);
		} else {// *1.0165; k11=5.91f windows
			return ((Yr - ZpYri - m * Vx * w) * Kvy);
		}
	}

	/**
	 * Прирашение угловой скорости
	 * 
	 * @param Mr
	 *            момент гидродинамических усилий
	 * @param ZpYri
	 *            тяга в поперечном направлениии
	 * @param Yr
	 *            гидродинамические усилия в поперечном направлении
	 * @param Kw
	 *            коэффициент для угловой скорости
	 * @return приращение угловой скорости
	 */
	private float wd(float Mr, float ZpYri, float Yr, float Kw) {
		return ((Mr + lr * ZpYri) * Kw);
	}

	/**
	 * Передаёт время выполнения
	 */
	public long getTimeout() {
		return timeout;
	}
}
