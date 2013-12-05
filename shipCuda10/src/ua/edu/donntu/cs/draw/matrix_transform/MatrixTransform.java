/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.draw.matrix_transform;

/**
 * Класс для преобразования локальной и глобальной матрицы
 * 
 * @author Denis Vodolazskiy
 */
public class MatrixTransform {

	// ---------------Transformation
	// Matrix-----------------------------------------
	/**
	 * Функция для преобразования данных локальной и глобальной матриц
	 * 
	 * @param psi
	 *            угол пси (вокруг оси Y)
	 * @param teta
	 *            угол тэта (вокруг оси Z)
	 * @param gamma
	 *            угол гамма (вокруг оси X)
	 * @param pr
	 *            приоритет (1 или 2)
	 * @return преобразованная матрица
	 */
	public double[][] matr(double psi, double teta, double gamma, int pr) {
		double m[][] = new double[3][3]; // опис
		{
			m[0][0] = m[1][1] = m[2][2] = 1;
			m[1][0] = m[0][1] = m[1][2] = 0;
			m[2][0] = m[2][1] = m[0][2] = 0;
		}
		// double psi = 0, gamma = 0, teta = 0;// – кути системи
		// координат, град;
		// double pr = 1;// - тип матриці
		// pr=1 з локальної до глобальної (типу A)
		// pr=2 з глобальної до локальної (типу В)
		double psir, ter, gammar;
		double psi_s, psi_c;
		double te_s, te_c;
		double gamma_s, gamma_c;
		double psigamma_ss, psigamma_sc, psigamma_cs, psigamma_cc;
		// градуси до радіан 0,017454=3.14/180
		psir = psi * 0.017454;
		ter = teta * 0.017454;
		gammar = gamma * 0.017454;
		// sin and cos
		psi_c = Math.cos(psir);
		psi_s = Math.sin(psir);
		te_c = Math.cos(ter);
		te_s = Math.sin(ter);
		gamma_c = Math.cos(gammar);
		gamma_s = Math.sin(gammar);
		// часткові додатки, які
		// використовуються двічі
		psigamma_ss = psi_s * gamma_s;
		psigamma_cc = psi_c * gamma_c;
		psigamma_sc = psi_s * gamma_c;
		psigamma_cs = psi_c * gamma_s;
		if (pr == 1) {
			m[0][0] = psi_c * te_c;
			m[0][1] = psigamma_ss - psigamma_cc * te_s;
			m[0][2] = psigamma_cs * te_s + psigamma_sc;
			m[1][0] = te_s;
			m[1][1] = te_c * gamma_c;
			m[1][2] = -te_c * gamma_s;
			m[2][0] = -psi_s * te_c;
			m[2][1] = te_s * psigamma_sc + psigamma_cs;
			m[2][2] = psigamma_cc - te_s * psigamma_ss;
		}
		// трансформована матриця
		if (pr == 2) {
			m[0][0] = te_c * psi_c;
			m[0][1] = te_s;
			m[0][2] = -te_c * psi_s;
			m[1][0] = psigamma_ss - psigamma_cc * te_s;
			m[1][1] = gamma_c * te_c;
			m[1][2] = psigamma_sc * te_s + psigamma_cs;
			m[2][0] = psigamma_cs * te_s + psigamma_sc;
			m[2][1] = -gamma_s * te_c;
			m[2][2] = psigamma_cc - psigamma_ss * te_s;
		}
		return m;
	}

}
