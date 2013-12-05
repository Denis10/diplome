package ua.edu.donntu.cs.inout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Этот класс загружает из файла данные о времени выполнения на GPU и CPU для
 * различного количества кораблей.
 * 
 * @author Denis Vodolazskiy
 */
public class LoadTimeValues {

	// for CompareFrame !!! outfiles
	/**
	 * Загрузка данных о времени выполнения
	 * 
	 * @param name
	 *            имя файла
	 * @return массив времени выполнения
	 */
	public float[] loadTimes(String name) {
		int countPoints = 0;
		ArrayList<Float> p = new ArrayList<Float>();
		FileReader fr;
		try {
			fr = new FileReader("data/outfiles/" + name);
			BufferedReader br = new BufferedReader(fr);
			String S = br.readLine();
			while (S != null) {
				countPoints++;
				p.add(Float.parseFloat(S));
				S = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException ex) {
			System.out.println("som");
			Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float[] points = new float[countPoints];
		int i = 0;
		while (i < countPoints) {
			points[i] = p.get(i);
			i++;
		}
		return points;
	}
}
