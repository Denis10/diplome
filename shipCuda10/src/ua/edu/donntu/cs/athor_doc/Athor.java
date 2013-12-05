package ua.edu.donntu.cs.athor_doc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ua.edu.donntu.cs.service.CalcStrings;

/**
 * Класс для отображения информации об авторе и проекте
 * 
 * @author Denis Vodolazskiy
 */
public class Athor extends JFrame {
	/**
	 * Конструктор, создаёт окно, добавляет информацию и ссылку
	 */
	public Athor() {
		super("Об авторе:");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		JPanel panel = new JPanel();
		panel.setLayout(null);
		setSize(280, 200); // задание размеров
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // задание параметров //
														// главного окна при
														// закрытии
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getSize().width;
		int height = getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		setLocation(x, y);

		JLabel jl2 = new JLabel(
				"<html>ДонНТУ, кафедра компьютерной инженерии,"
						+ "	группа КСм-12</html>");
		Font font = new Font(Font.SANS_SERIF, 1, 13);
		jl2.setFont(font);
		jl2.setSize(250, 60);
		jl2.setLocation(30, 10);
		
		font = new Font(Font.MONOSPACED, 1, 16);			
		JLabel jl1 = new JLabel();
		jl1.setFont(font);
		makeLink(jl1, "Денис Водолазский",
				"http://masters.donntu.edu.ua/2013/fknt/vodolazskiy/");
		jl1.setSize(280, 30);
		jl1.setLocation(0, 70);
		jl1.setHorizontalAlignment(0);
		
		CalcStrings cs=new CalcStrings();
		JLabel jl3 = new JLabel(
				"<html>Количество файлов: "+cs.getFilesNumber()
						+ "<br>Количество строк кода: "+cs.getCount()+"</html>");
		font = new Font(Font.SANS_SERIF, 1, 13);
		jl3.setFont(font);
		jl3.setSize(250, 60);
		jl3.setLocation(30, 100);
		
		panel.add(jl2);
		panel.add(jl1);
		panel.add(jl3);
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}
/**
 * Создаёт ссылку в метке
 * @param jl метка
 * @param name текст ссылки
 * @param address ссылка
 */
	private void makeLink(JLabel jl, String name, final String address) {
		// Чтобы JLabel выглядел как полноценная гиперссылка
		jl.setText("<html><a href=\"#\">" + name + "</a></html>");
		// Чтобы JLabel выглядел как полноценная гиперссылка при наведении
		// курсора мыши
		jl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		// Переопределяем MouseListener
		jl.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					// Запускаем браузер по-умолчанию и открываем в нем нужную
					// страницу
					java.awt.Desktop.getDesktop().browse(new URI(address));
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

}
