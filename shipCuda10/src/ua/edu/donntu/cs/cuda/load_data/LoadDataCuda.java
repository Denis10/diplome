/*Class to load data*/
package ua.edu.donntu.cs.cuda.load_data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.edu.donntu.cs.inout.LoadData;

/**
 * Этот класс загружает данные из файлов и сохраняет их в одномерные массивы.
 * Одномерные массивы используются для вычисления на GPU
 * 
 * @author Denis Vodolazskiy
 */
public class LoadDataCuda {
	private final int n = 3;
	private int countPoints = 0;
	private int countPolygons = 0;

	// ----------method for normal creation----------------------------
	/**
	 * Вычисление вектора нормали
	 * 
	 * @param b
	 *            координаты первой точки
	 * @param c
	 *            координаты второй точки
	 * @param d
	 *            координаты третей точки
	 * @return вектор нормали
	 */
	private int[] createNewNormal(int b[], int c[], int d[]) {
		int normal[] = new int[3];
		int x[] = new int[3];
		int y[] = new int[3];
		int z[] = new int[3];
		// for (int i=0;i<4;i++){
		x[0] = b[0];
		x[1] = c[0];
		x[2] = d[0];

		y[0] = b[1];
		y[1] = c[1];
		y[2] = d[1];

		z[0] = b[2];
		z[1] = c[2];
		z[2] = d[2];
		// }
		int j = 0;
		for (int i = 0; i < 3; i++) {
			if (i < 2) {
				j = i + 1;
				normal[0] += -(y[i] - y[j]) * (z[i] + z[j]);
				normal[1] += -(z[i] - z[j]) * (x[i] + x[j]);
				normal[2] += -(x[i] - x[j]) * (y[i] + y[j]);
			} else {
				j = 0;
				normal[0] += -(y[i] - y[j]) * (z[i] + z[j]);
				normal[1] += -(z[i] - z[j]) * (x[i] + x[j]);
				normal[2] += -(x[i] - x[j]) * (y[i] + y[j]);
			}
		}
		return normal;
	}

	// ----------------------------------Cuda-------------------------
	// ----------------------load points CUDA-------------------------
	/**
	 * Загружает координаты точек из файла
	 * 
	 * @return одномерный массив координат точек
	 */
	public int[] loadPoints() {
		ArrayList<Integer> p = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(new File(
					"data/inputfiles/points2.txt"));
			while (scanner.hasNext()) {
				p.add(scanner.nextInt());
				countPoints++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("som");
			Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		int[] points = new int[countPoints];
		int i = 0;
		while (i < countPoints) {
			points[i] = p.get(i);
			i++;
		}
		countPoints /= 3;
		return points;
	}

	// ----------------------load polygons Cuda-------------------------
	/**
	 * Загружает список точек для каждого треугольника
	 * 
	 * @return массив треугольников
	 */
	public int[] loadPolygons() {
		ArrayList<Integer> p = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(new File(
					"data/inputfiles/pointsAndPolygons2.txt"));
			while (scanner.hasNext()) {
				p.add(scanner.nextInt());
				countPolygons++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("som");
			Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		int[] polygons = new int[countPolygons];
		int i = 0;
		while (i < countPolygons) {
			polygons[i] = p.get(i);
			i++;
		}
		countPolygons /= 3;
		return polygons;
	}

	// -----------------------------------------------------------------
	/**
	 * Обрабатывает массивы точек и полигонов. Вызывается функция
	 * createNewNormal
	 * 
	 * @param points
	 *            массив координат точек
	 * @param polygons
	 *            массив точек для полигонов
	 * @return вектор нормали
	 */
	public int[] loadNormals(int points[], int polygons[]) {
		int[] normals = new int[countPolygons * n];
		int[] norm = new int[3];
		for (int i = 0; i < countPolygons; i++) {
			int[] x = { points[3 * polygons[3 * i]],
					points[3 * polygons[3 * i] + 1],
					points[3 * polygons[3 * i] + 2] };
			int[] y = { points[3 * polygons[3 * i + 1]],
					points[3 * polygons[3 * i + 1] + 1],
					points[3 * polygons[3 * i + 1] + 2] };
			int[] z = { points[3 * polygons[3 * i + 2]],
					points[3 * polygons[3 * i + 2] + 1],
					points[3 * polygons[3 * i + 2] + 2] };
			norm = createNewNormal(x, y, z);
			normals[3 * i] = norm[0];
			normals[3 * i + 1] = norm[1];
			normals[3 * i + 2] = norm[2];
		}
		// new WriteToFile(normals, "normal2.txt");
		return normals;
	}

	/**
	 * Взять количество точек
	 * 
	 * @return количество точек
	 */
	public int getCountPoints() {
		return countPoints;
	}

	/**
	 * Взять количество полигонов
	 * 
	 * @return количество полигонов
	 */
	public int getCountPolygons() {
		return countPolygons;
	}

}
