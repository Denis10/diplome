package ua.edu.donntu.cs.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс выполняет подсчёт папок, файлов и строк кода во всех файлах. Тут
 * же можно скопировать все классы в один файл с помощью copyInOne
 * (раскомментировать одну строчку). Файл allCode.txt создаётся в корне проекта.
 * 
 * @author Denis Vodolazskiy
 */
public class CalcStrings {
	/**
	 * Количество папок
	 */
	private int directoriesNumber;
	/**
	 * Количество файлов
	 */
	private int filesNumber;
	/**
	 * Количество строк
	 */
	private int count = 0;
	/**
	 * Перезапись файла со всем кодом
	 */
	private boolean rewrite = true;

	/**
	 * Конструктор. Берёт файлы из .java из папки "src" и файлы ".cu" из папки
	 * "data/cuSource"
	 */

	public CalcStrings() {
		File f = new File("src");
		//
		ArrayList<File> res = new ArrayList<File>();
		search(f, res);
		f = new File("data/cuSource");
		search(f, res, ".cu");
	}

	/**
	 * Считает количество строк в файле
	 * 
	 * @param name
	 *            путь к файлу
	 */
	private void quantityOfStrings(String name) {

		FileReader fr;
		try {
			fr = new FileReader(name);
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				count++;
			}
			br.close();
			//copyInOne(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Копирует все файлы в один
	 * 
	 * @param name
	 *            путь к файлу
	 */
	private void copyInOne(String name) {
		FileReader fr = null;
		BufferedReader br = null;
		BufferedWriter writer = null;
		try {
			if (rewrite) {
				writer = new BufferedWriter(new FileWriter("allCode.txt", false));
				rewrite = false;
			}
			writer = new BufferedWriter(new FileWriter("allCode.txt", true));
			fr = new FileReader(name);
			br = new BufferedReader(fr);
			String s;
			writer.write(name);
			writer.write("\n");
			writer.write("\n");
			while ((s = br.readLine()) != null) {
				writer.write(s);
				writer.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fr.close();
				br.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Ишет все файлы в папке и подпапках
	 * 
	 * @param topDirectory
	 *            путь к файлу
	 * @param res
	 *            список файлов
	 */
	private void search(File topDirectory, List<File> res) {
		// получаем список всех объектов в текущей директории
		File[] list = topDirectory.listFiles();
		// просматриваем все объекты по-очереди
		for (int i = 0; i < list.length; i++) {
			// если это директория (папка)...
			if (list[i].isDirectory()) {
				directoriesNumber++;
				res.add(list[i]);
				// выполняем поиск во вложенных директориях
				search(list[i], res);
			}
			// если это файл
			else {
				if (list[i].isFile()) {
					// ...добавляем текущий объект в список результатов,
					// и обновляем значения счетчиков
					filesNumber++;
					res.add(list[i]);
					quantityOfStrings(list[i].getAbsolutePath());
				}
			}
		}
	}

	/**
	 * Ишет все файлы в папке и подпапках (.cu)
	 * 
	 * @param topDirectory
	 *            путь к файлу
	 * @param res
	 *            список файлов
	 * @param ext
	 *            расширение файла
	 */
	private void search(File topDirectory, List<File> res, String ext) {
		// получаем список всех объектов в текущей директории
		File[] list = topDirectory.listFiles();
		// просматриваем все объекты по-очереди
		for (int i = 0; i < list.length; i++) {
			// если это директория (папка)...
			if (list[i].isDirectory()) {
				directoriesNumber++;
				res.add(list[i]);
				// выполняем поиск во вложенных директориях
				search(list[i], res);
			}
			// если это файл
			else {
				if (list[i].isFile()) {
					// ...добавляем текущий объект в список результатов,
					// и обновляем значения счетчиков
					if (list[i].getName().endsWith(ext)) {
						filesNumber++;
						res.add(list[i]);
						quantityOfStrings(list[i].getAbsolutePath());
					}
				}
			}
		}
	}

	/**
	 * Возвращает количество папок
	 * 
	 * @return количество папок
	 */
	public int getDirectoriesNumber() {
		return directoriesNumber;
	}

	/**
	 * Возвращает количество файлов
	 * 
	 * @return количество файлов
	 */
	public int getFilesNumber() {
		return filesNumber;
	}

	/**
	 * Возвращает количество строк
	 * 
	 * @return количество строк
	 */
	public int getCount() {
		return count;
	}
}
