/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.path.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ua.edu.donntu.cs.path.calculations_kutt.IKuttCalc;
import ua.edu.donntu.cs.path.paint.PaintGraph;
import ua.edu.donntu.cs.path.paint.PaintGraphCoordCircle;
import ua.edu.donntu.cs.path.paint.PaintGraphLine;

/**
 * Класс для построения графиков движения (в системе мировой)
 * 
 * @author Denis Vodolazskiy
 */
public class ObjectCoordFrame extends JFrame implements ActionListener,
		IKuttCalc {

	private JButton jbt1, jbt2;
	private PaintGraph pgVx, pgVy, pgw, pgXY;// класс вывода графика функции

	/**
	 * Выполняет действия при нажатии кнопок
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jbt2)) {
			pgVx.repaint();// перерисовка графика функции
			pgVy.repaint();// перерисовка графика функции
			pgw.repaint();// перерисовка графика функции
			pgXY.repaint();// перерисовка графика функции
		}
		if (e.getSource().equals(jbt1)) {
		}
	}

	/**
	 * Конструктор, задаёт расположение окна и его свойства
	 */
	public ObjectCoordFrame() {
		super("Построение графика функции");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		JPanel panel = new JPanel();
		panel.setLayout(null);
		setSize(1000, 730); // задание размеров
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // задание параметров
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getSize().width;
		int height = getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		setLocation(x, y);
		// ----------------------
		jbt2 = new JButton("Paint");
		jbt1 = new JButton("Do nothing");
		// -------------
		jbt2.setSize(100, 25);
		jbt2.setLocation(10, 20);
		panel.add(jbt2);
		// --
		jbt1.setSize(100, 25);
		jbt1.setLocation(10, 60);
		panel.add(jbt1);
		// --
		jbt2.addActionListener(this);
		jbt1.addActionListener(this);

		// ModelCalc2 MC = new ModelCalc2();

		pgVx = new PaintGraphLine("t", "X", MC.getX());
		pgVx.setSize(800, 160); // задание размеров
		pgVx.setBackground(Color.lightGray);// цвет
		pgVx.setLocation(180, 20);
		panel.add(pgVx);
		// --second graph
		pgVy = new PaintGraphLine("t", "Y", MC.getY()); // инициализация класса
														// построения графика
														// функции
		pgVy.setSize(800, 160); // задание размеров
		pgVy.setBackground(Color.lightGray);// цвет
		pgVy.setLocation(180, 190);
		panel.add(pgVy);
		// -------
		pgw = new PaintGraphLine("t", "w observer", MC.getW(), 100); // инициализация
																		// класса
																		// построения
																		// графика
																		// функции
		pgw.setSize(800, 160); // задание размеров
		pgw.setBackground(Color.lightGray);// цвет
		pgw.setLocation(180, 360);
		panel.add(pgw);
		// ----------
		pgXY = new PaintGraphCoordCircle("X", "Y", MC.getX(), MC.getY(), 1, 1,
				5000, 2000, 1);
		pgXY.setSize(800, 160); // задание размеров
		pgXY.setBackground(Color.lightGray);// цвет
		pgXY.setLocation(180, 530);
		panel.add(pgXY);
		// ----------
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}
}
