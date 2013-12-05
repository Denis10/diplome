package ua.edu.donntu.cs.athor_doc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import ua.edu.donntu.cs.cuda.exception.PrintExceptionShortStack;
/**
 * Класс для отображения документации в браузере
 * 
 * @author Denis Vodolazskiy
 */
public class Documents {
	/**
	 * Конструктор, открывает файл "/doc/index.html" в браузере по умолчанию
	 */
	public Documents() {
		try {
			// Запускаем браузер по-умолчанию и открываем в нем нужную
			// страницу
			URL url;
			url = getClass().getResource("/doc/index.html");
			// System.out.println("url = "+url);
			java.awt.Desktop.getDesktop().browse(url.toURI());

		} catch (IOException ex) {
			new PrintExceptionShortStack(ex);
		} catch (URISyntaxException ex) {
			new PrintExceptionShortStack(ex);
		}
	}
}
