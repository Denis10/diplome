/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.path.frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import ua.edu.donntu.cs.path.calculations_kutt.IKuttCalc;
import ua.edu.donntu.cs.path.paint.PaintGraph;
import ua.edu.donntu.cs.path.paint.PaintGraphCoordCircle;

/**
 * Класс для построения графиков движения (увеличенный график)
 * 
 * @author Denis Vodolazskiy
 */
public class BigGraphicUniFrame extends JFrame implements ActionListener,
		IKuttCalc {

	private JButton jbt1;
	private JLabel jl1;
	private JTextField text1 = new JTextField(1);
	private PaintGraph pgXYmov;

	/**
	 * Выполняет действия при нажатии кнопок
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jbt1)) {
			pgXYmov.setDivideForRealModelingTime(Integer.parseInt(text1
					.getText()));
			pgXYmov.repaint();// перерисовка графика функции
		}
	}

	/**
	 * Конструктор, задаёт расположение окна и его свойства
	 */
	public BigGraphicUniFrame(PaintGraph pgXYmov) {
		super("Построение графика функции");
		this.pgXYmov = pgXYmov;
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		JPanel panel = new JPanel();
		panel.setLayout(null);
		setSize(1000, 730); // задание размеров
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// ----------------------
		jbt1 = new JButton("Paint");
		jbt1.setSize(140, 25);
		jbt1.setLocation(10, 30);
		panel.add(jbt1);
		jbt1.addActionListener(this);
		//
		jl1 = new JLabel("divide time by INT:");
		jl1.setHorizontalAlignment(0);
		jl1.setSize(140, 25);
		jl1.setLocation(10, 60);
		panel.add(jl1);
		//
		text1.setSize(140, 25);
		text1.setLocation(10, 90);
		text1.setText("1");
		panel.add(text1);

		if (pgXYmov instanceof PaintGraphCoordCircle) {
			JLabel jl2 = new JLabel("Rc=" + MC.getRc());
			jl2.setSize(140, 25);
			jl2.setHorizontalAlignment(0);
			jl2.setLocation(10, 150);
			panel.add(jl2);
		}

		pgXYmov.setSize(620, 620); // задание размеров
		pgXYmov.setBackground(Color.lightGray);// цвет
		pgXYmov.setLocation(200, 20);
		panel.add(pgXYmov);
		// ----------
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}
}