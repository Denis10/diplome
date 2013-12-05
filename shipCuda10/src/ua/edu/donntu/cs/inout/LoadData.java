/*Class to load data*/
package ua.edu.donntu.cs.inout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Этот класс загружает данные из файлов и сохраняет их в двумерные массивы.
 * 
 * @author Denis Vodolazskiy
 */
public class LoadData {
	private int countPoints = 0;
	private int countPolygons = 0;

	// ----------------------load points-------------------------
	/**
	 * Загружает координаты точек из файла
	 * 
	 * @return двумерный массив координат точек
	 */
	public int[][] loadPoints(String path) {
		ArrayList<Integer> p = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNext()) {
				p.add(scanner.nextInt());
				p.add(scanner.nextInt());
				p.add(scanner.nextInt());
				countPoints++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("som");
			Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		int points[][] = new int[countPoints][3];
		int i = 0;
		while (i < countPoints) {
			points[i][0] = p.get(3 * i);
			points[i][1] = p.get(3 * i + 1);
			points[i][2] = p.get(3 * i + 2);
			i++;
		}
		// System.out.println(countPoints);
		return points;
	}

	// ----------------------load polygons-------------------------
	/**
	 * Загружает список точек для каждого треугольника
	 * 
	 * @return массив треугольников
	 */
	public int[][] loadPolygons(String path) {
		ArrayList<Integer> p = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNext()) {
				p.add(scanner.nextInt());
				p.add(scanner.nextInt());
				p.add(scanner.nextInt());
				countPolygons++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("som");
			Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		int i = 0;
		int polygons[][] = new int[countPolygons][3];
		while (i < countPolygons) {
			polygons[i][0] = p.get(3 * i);
			polygons[i][1] = p.get(3 * i + 1);
			polygons[i][2] = p.get(3 * i + 2);
			i++;
		}
		// System.out.println(countPolygons);
		return polygons;
	}

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
	public int[][] loadNormals(int[][] points, int[][] polygons) {
		int normals[][] = new int[countPolygons][3];
		for (int i = 0; i < countPolygons; i++) {
			normals[i] = createNewNormal(points[polygons[i][0]],
					points[polygons[i][1]], points[polygons[i][2]]);
		}
		new WriteToFile(normals, "normal2.txt");
		return normals;
	}

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
