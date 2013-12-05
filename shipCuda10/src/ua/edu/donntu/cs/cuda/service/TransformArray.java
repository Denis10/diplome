/*Transforms 2D array in 1D array*/
package ua.edu.donntu.cs.cuda.service;


/**
 * Класс для преобразования двумерного массива в одномерный. Функции JCUDA
 * работают только с одномерными массивами
 * 
 * @author Denis Vodolazskiy
 */
public class TransformArray {
	/**
	 * Преобразование двумерного массива в одномерный
	 * 
	 * @return одномерный массив
	 * @param b
	 *            двумерный массив
	 * @param N
	 *            размерность второго измерения
	 */
	public int[] in1D(int[][] b, int N) {
		int a[] = new int[3 * N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 3; j++) {
				a[3 * i + j] = b[i][j];
			}
		}
		return a;
	}
}
