/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.service;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ua.edu.donntu.cs.athor_doc.Athor;
import ua.edu.donntu.cs.athor_doc.Documents;
import ua.edu.donntu.cs.cuda.compare_frame.CPUandGPUCompareFrame;
import ua.edu.donntu.cs.cuda.exception.InitCuda;
import ua.edu.donntu.cs.cuda.many_ships.ManyShipsCuda;
import ua.edu.donntu.cs.cuda.properties.ShowDeviceProperties;
import ua.edu.donntu.cs.draw.animation.AnimateShip;
import ua.edu.donntu.cs.draw.animation.SwimShipThread;
import ua.edu.donntu.cs.draw.main.StartDrawClass;
import ua.edu.donntu.cs.draw.move.MoveShip;
import ua.edu.donntu.cs.draw.move.SwimShip;
import ua.edu.donntu.cs.path.calculations_kutt.IKuttCalc;
import ua.edu.donntu.cs.path.frames.BigGraphicUniFrame;
import ua.edu.donntu.cs.path.frames.ObjectCoordFrame;
import ua.edu.donntu.cs.path.frames.ObserverCoordFrame;
import ua.edu.donntu.cs.path.frames.VelocityFrame;
import ua.edu.donntu.cs.path.paint.PaintGraph;
import ua.edu.donntu.cs.path.paint.PaintGraphCoordCircle;
import ua.edu.donntu.cs.priority_method.draw.ManyShipsMove;
import ua.edu.donntu.cs.ray.compare_graphic.CPUandGPUCompareRayFrame;
import ua.edu.donntu.cs.ray.draw_ray.DrawRay;
import ua.edu.donntu.cs.ray.draw_ray.DrawRayCuda;

/**
 * Класс для построения начального окна.
 * 
 * @author Denis Vodolazskiy
 */
public class StartFrame extends JFrame implements ActionListener, IKuttCalc {

	private JButton jbt1, jbt2, jbt3, jexit, jbt5, jbt6, jbt7, jbt8, jbt9,
			jbt10, jbt11, jbt12, jbt13, jbt14, jbt15, jbt16, jbt17, jbt18;
	private PaintGraph pgXYmov;

	/**
	 * Выполняет действия при нажатии кнопок
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(jbt1)) {
			new VelocityFrame();
		} else if (e.getSource().equals(jbt2)) {
			new ObjectCoordFrame();
		} else if (e.getSource().equals(jbt3)) {
			new ObserverCoordFrame();
		} else if (e.getSource().equals(jexit)) {
			System.exit(0);
		} else if (e.getSource().equals(jbt5)) {
			pgXYmov = new PaintGraphCoordCircle("Xmov", "Ymov", MC.getXobs(),
					MC.getYobs(), 1, 1, 3500, 3500, 1);
			new BigGraphicUniFrame(pgXYmov);
		} else if (e.getSource().equals(jbt6)) {
			new StartDrawClass(new AnimateShip());
		} else if (e.getSource().equals(jbt7)) {
			new StartDrawClass(new MoveShip());
		} else if (e.getSource().equals(jbt8)) {
			new StartDrawClass(new ManyShipsMove());
		} else if (e.getSource().equals(jbt9)) {
			if (new InitCuda().isPresent())
				new StartDrawClass(new ManyShipsCuda());
		} else if (e.getSource().equals(jbt10)) {
			new StartDrawClass(new SwimShipThread());
		} else if (e.getSource().equals(jbt11)) {
			new StartDrawClass(new SwimShip());
		} else if (e.getSource().equals(jbt12)) {
			new CPUandGPUCompareFrame();
		} else if (e.getSource().equals(jbt13)) {
			if (new InitCuda().isPresent())
				new ShowDeviceProperties();
		} else if (e.getSource().equals(jbt14)) {
			new Athor();
		} else if (e.getSource().equals(jbt15)) {
			new Documents();
		} else if (e.getSource().equals(jbt16)) {
			new StartDrawClass(new DrawRay());
		} else if (e.getSource().equals(jbt17)) {
			new CPUandGPUCompareRayFrame();
		} else if (e.getSource().equals(jbt18)) {
			new StartDrawClass(new DrawRayCuda());
		}
	}

	/**
	 * Конструктор, задаёт расположение окна и его свойства
	 */
	public StartFrame() {
		super("Построить:");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(15, 1, 1, 1));
		setSize(280, 580); // задание размеров
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // задание параметров //
														// главного окна при
														// закрытии

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// int width = getSize().width;
		// int height = getSize().height;
		int x = dim.width - 280;// (dim.width - width) / 2;
		int y = 0;// (dim.height - height) / 2;
		setLocation(x, y);

		Image img = Toolkit.getDefaultToolkit().getImage(
				"data/images/splash.JPG");
		setIconImage(img);
		// setIconImage(getToolkit().getImage(getClass().getResource("/data/images/splash.JPG")));

		jbt1 = new JButton("VelocityFrame");
		jbt2 = new JButton("ObjectCoordFrame");
		jbt3 = new JButton("ObserverCoordFrame");
		jbt5 = new JButton("Path");
		jexit = new JButton("Exit");
		jbt6 = new JButton("AnimateShip");
		jbt7 = new JButton("MoveShip");
		jbt8 = new JButton("Метод приоритетов");
		jbt9 = new JButton("Метод приоритетов Cuda");
		jbt10 = new JButton("SwimShipThread");
		jbt11 = new JButton("SwimShip");
		jbt12 = new JButton("GPU vs CPU");
		jbt13 = new JButton("Свойства системы");
		jbt14 = new JButton("Об авторе и проекте");
		jbt15 = new JButton("Документация");
		jbt16 = new JButton("Ray CPU");
		jbt17 = new JButton("Ray GPU vs CPU");
		jbt18 = new JButton("Ray GPU");

		// -------------
		panel.add(jbt1);
		jbt2.setEnabled(false);
		// panel.add(jbt2);
		jbt3.setEnabled(false);
		// panel.add(jbt3);
		panel.add(jbt5);
		panel.add(jbt6);
		panel.add(jbt7);
		panel.add(jbt8);
		panel.add(jbt9);
		// panel.add(jbt10);
		jbt10.setEnabled(false);
		panel.add(jbt11);
		panel.add(jbt12);
		panel.add(jbt17);
		panel.add(jbt13);
		panel.add(jbt16);
		panel.add(jbt18);
		panel.add(jbt15);
		panel.add(jbt14);
		panel.add(jexit);
		// ----------
		jbt1.addActionListener(this);
		jbt2.addActionListener(this);
		jbt3.addActionListener(this);
		jexit.addActionListener(this);
		jbt5.addActionListener(this);
		jbt6.addActionListener(this);
		jbt7.addActionListener(this);
		jbt8.addActionListener(this);
		jbt9.addActionListener(this);
		jbt10.addActionListener(this);
		jbt11.addActionListener(this);
		jbt12.addActionListener(this);
		jbt13.addActionListener(this);
		jbt14.addActionListener(this);
		jbt15.addActionListener(this);
		jbt16.addActionListener(this);
		jbt17.addActionListener(this);
		jbt18.addActionListener(this);
		// ----------
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}
}
