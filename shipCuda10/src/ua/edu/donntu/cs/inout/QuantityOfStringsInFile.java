/*Calc number of strings*/

package ua.edu.donntu.cs.inout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Этот класс определяет количество строк в файле
 * 
 * @author Denis Vodolazskiy
 */
public class QuantityOfStringsInFile {

	// for CompareFrame !!! outfiles
	/**
	 * Количество строк в файле времени выполнения
	 * 
	 * @param name
	 *            имя файла
	 * @return количество строк
	 */
	public int quantityOfStringsTime(String name) {
		int count = 0;
		FileReader fr;
		try {
			fr = new FileReader("data/outfiles/" + name);
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				count++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * Не используется
	 */
	public int quantityOfPoints() {
		int count = 0;
		FileReader fr;
		try {
			fr = new FileReader("data/inputfiles/points2.txt");
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				count++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * Не используется
	 */
	public int quantityOfPolygons() {
		int count = 0;
		FileReader fr;
		try {
			fr = new FileReader("data/inputfiles/pointsAndPolygons2.txt");
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				count++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
