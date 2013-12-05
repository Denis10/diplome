/*Class to write data*/
package ua.edu.donntu.cs.inout;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Этот класс записывает данные в файл
 * 
 * @author Denis Vodolazskiy
 */
public class WriteToFile {
	/**
	 * 
	 * @param a
	 *            массив входных данных
	 * @param name
	 *            имя файла
	 */
	public WriteToFile(int a[][], String name) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter("data/outfiles/" + name));
			for (int i = 0; i < a.length; i++) {
				out.write(i + "   ");
				for (int j = 0; j < a[i].length; j++) {
					out.write(a[i][j] + "   ");
				}
				out.write("\n");
			}
		} catch (IOException ex) {
			Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE,
					null, ex);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				System.out.println(ex);
			}

		}
	}

	// -----------------------------------------------------------------------------
	/**
	 * 
	 * @param a
	 *            массив входных данных
	 * @param name
	 *            имя файла
	 */
	public WriteToFile(double a[][], String name) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter("data/outfiles/" + name));
			for (int i = 0; i < a.length; i++) {
				out.write(i + "   ");
				for (int j = 0; j < a[i].length; j++) {
					out.write(a[i][j] + "   ");
				}
				out.write("\n");
			}
		} catch (IOException ex) {
			Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE,
					null, ex);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				System.out.println(ex);
			}

		}
	}

	// -------------------------------------------------------------------------------
	/**
	 * 
	 * @param a
	 *            входное число
	 * @param name
	 *            имя файла
	 */
	public WriteToFile(int a, String name) {// �������� �� ������ �����

		try {
			BufferedWriter out = null;
			out = new BufferedWriter(new FileWriter("data/outfiles/" + name,
					false));
			out.write(a + "   ");
			out.close();
		} catch (IOException ex) {
			Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE,
					null, ex);
		} finally {

		}
	}

	/**
	 * 
	 * @param a
	 *            массив входных данных
	 * @param name
	 *            имя файла
	 */
	public WriteToFile(float[] a, String name) {// �������� �� ������ �����

		try {
			BufferedWriter out = null;
			out = new BufferedWriter(new FileWriter("data/outfiles/" + name,
					false));
			for (int i = 0; i < a.length; i++) {
				out.write(a[i] + "\n");
			}
			out.close();
		} catch (IOException ex) {
			Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE,
					null, ex);
		} finally {

		}
	}
}
