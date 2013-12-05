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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ua.edu.donntu.cs.path.calculations_kutt.IKuttCalc;
import ua.edu.donntu.cs.path.paint.PaintGraph;
import ua.edu.donntu.cs.path.paint.PaintGraphCoordCircle;
import ua.edu.donntu.cs.path.paint.PaintGraphLine;

/**
 * Класс для построения графиков движения (в системе наблюдателя)
 * 
 * @author Denis Vodolazskiy
 */
public class ObserverCoordFrame extends JFrame implements ActionListener,
		MouseListener, IKuttCalc {

	private JButton jbt1, jbt2;
	// private JTextField text1 = new JTextField(1);//, text2, text3, text4,
	// text5, text6, text7, text8;
	private PaintGraph pgVx, pgVy, pgw, pgXY, pgXYmov;// класс вывода графика
														// функции
	MouseListener me;

	@Override
	/**
	 * Выполняет действия при нажатии кнопок
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jbt2)) {
			pgVx.repaint();// перерисовка графика функции
			pgVy.repaint();// перерисовка графика функции
			pgw.repaint();// перерисовка графика функции
			pgXY.repaint();// перерисовка графика функции
		}
		if (e.getSource().equals(jbt1)) {
			// ModelCalc2 MC = new ModelCalc2();
			pgXYmov = new PaintGraphCoordCircle("Xmov", "Ymov", MC.getXobs(),
					MC.getYobs(), 1, 1, 6000, 6000, 1);
			new BigGraphicUniFrame(pgXYmov);
		}

	}

	/**
	 * Конструктор, задаёт расположение окна и его свойства
	 */
	public ObserverCoordFrame() {
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
		jbt1 = new JButton("Path");
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

		pgVx = new PaintGraphLine("t", "Xobs", MC.getXobs());
		pgVx.setSize(800, 160); // задание размеров
		pgVx.setBackground(Color.lightGray);// цвет
		pgVx.setLocation(180, 20);
		panel.add(pgVx);
		// --second graph
		pgVy = new PaintGraphLine("t", "Yobs", MC.getYobs()); // инициализация
																// класса
																// построения
																// графика
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
		pgXY = new PaintGraphCoordCircle("Xobs", "Yobs", MC.getXobs(),
				MC.getYobs(), 1, 1, 10000, 6000, 1);
		pgXY.setSize(800, 160); // задание размеров
		pgXY.setBackground(Color.lightGray);// цвет
		pgXY.setLocation(180, 530);
		panel.add(pgXY);
		// ----------
		pgXY.addMouseListener(this);
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		if (me.getSource().equals(pgXY)) {
			pgXYmov = new PaintGraphCoordCircle("Xmov", "Ymov", MC.getXobs(),
					MC.getYobs(), 1, 1, 6000, 6000, 1);
			new BigGraphicUniFrame(pgXYmov);
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}
}
