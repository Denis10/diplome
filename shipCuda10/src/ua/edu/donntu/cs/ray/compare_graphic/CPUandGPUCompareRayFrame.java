package ua.edu.donntu.cs.ray.compare_graphic;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import ua.edu.donntu.cs.cuda.exception.InitCuda;
import ua.edu.donntu.cs.inout.LoadTimeValues;
import ua.edu.donntu.cs.inout.QuantityOfStringsInFile;
import ua.edu.donntu.cs.inout.WriteToFile;
import ua.edu.donntu.cs.ray.base_draw.IRayFigure;
import ua.edu.donntu.cs.ray.ray_calc.ParallelRay;
import ua.edu.donntu.cs.ray.ray_calc_time.SerialRayTime;

/**
 * Этот класс создаёт окно для графиков зависимости времени выполнения от
 * количества пикселов в окне. Метод трассировки лучей
 * 
 * @author Denis Vodolazskiy
 */
public class CPUandGPUCompareRayFrame extends JFrame implements IRayFigure {
	/**
	 * Панель для размещения компонентов
	 */
	private PaintCompareRay panel;

	/**
	 * Конструктор, задаёт расположение окна и его свойства. Принимает данные,
	 * проверяет, являются ли данные новыми. Если да, то заново рассчитывает
	 * время выполнения и записывает результат в текстовый файл. Если нет, то
	 * читает данные из текстового файла. Затем строит график. Перед выполнением
	 * рассчётов проверяет наличие видеокарты
	 */
	public CPUandGPUCompareRayFrame() {
		super("Метод трассировки лучей на CPU и GPU:");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		int Nx = 700;
		int increment = 1;

		float timesGPU[] = new float[Nx + 1];
		float timesCPU[] = new float[Nx + 1];
		final String nameGPU = "timesRayGPU.txt";
		final String nameCPU = "timesRayCPU.txt";

		int stringQuantatyGPU = 0;
		stringQuantatyGPU = new QuantityOfStringsInFile()
				.quantityOfStringsTime(nameGPU);
		int stringQuantatyCPU = 0;
		stringQuantatyCPU = new QuantityOfStringsInFile()
				.quantityOfStringsTime(nameCPU);

		if ((stringQuantatyGPU == Nx + 1) && (stringQuantatyCPU == Nx + 1)) {
			timesGPU = new LoadTimeValues().loadTimes(nameGPU);
			timesCPU = new LoadTimeValues().loadTimes(nameCPU);
		} else {
			if (new InitCuda().isPresent()) {// check GPU
				for (int i = 1; i < Nx + 1; i += increment) {
					timesGPU[i] = new ParallelRay(pointsBaseCuda, polygonsCuda,
							normalsCuda, i, i).getEl_time();
					timesCPU[i] = new SerialRayTime(points, polygons, normals,
							i, i).getTimeout();
				}
				new WriteToFile(timesGPU, nameGPU);
				new WriteToFile(timesCPU, nameCPU);
			}
		}

		panel = new PaintCompareRay("pixels", "time, ms", timesGPU, timesCPU,
				1, 1, increment);
		panel.setLayout(null);
		setSize(700, 500); // задание размеров
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // задание параметров
															// // главного окна
															// при закрытии

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getSize().width;
		int height = getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		setLocation(x, y);

		//
		setResizable(false);
		setVisible(true);
		setContentPane(panel);
	}

}
