package ua.edu.donntu.cs.cuda.compare_frame;

/**
 * Класс для вызова заставки при запуске построения графика сравнения значений времени выполнения на GPU и CPU  
 * 
 * @author Denis Vodolazskiy
 */

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import ua.edu.donntu.cs.cuda.exception.InitCuda;
import ua.edu.donntu.cs.cuda.kutt.full_engine_many.ParallelFullCalcCuda;
import ua.edu.donntu.cs.cuda.kutt.test_time.SerialSpeedCalc;
import ua.edu.donntu.cs.inout.LoadTimeValues;
import ua.edu.donntu.cs.inout.QuantityOfStringsInFile;
import ua.edu.donntu.cs.inout.WriteToFile;

public class CPUandGPUCompareFrame extends JFrame {
	/**
	 * Панель для размещения компонентов
	 */
	private PaintCompare panel;

	/**
	 * Конструктор, задаёт расположение окна и его свойства. Принимает данные,
	 * проверяет, являются ли данные новыми. Если да, то заново рассчитывает
	 * время выполнения и записывает результат в текстовый файл. Если нет, то
	 * читает данные из текстового файла. Затем строит график. Перед выполнением
	 * рассчётов проверяет наличие видеокарты
	 */
	public CPUandGPUCompareFrame() {
		super("Сравнение времени выполнения на CPU и GPU:");
		// System Look-And-Feel:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Exception handle
		}
		int nShips = 144;// any value, not big //20_000 for
		// ParallelSpeedCalcCuda, 3200 for
		// ParallelFullCalcCuda
		final int size = 8_000;// define size in cu-file.size=8000,
		float timesGPU[] = new float[nShips + 1];
		float timesCPU[] = new float[nShips + 1];
		final String nameGPU = "timesGPU.txt";
		final String nameCPU = "timesCPU.txt";

		int stringQuantatyGPU = 0;
		stringQuantatyGPU = new QuantityOfStringsInFile()
				.quantityOfStringsTime(nameGPU);
		int stringQuantatyCPU = 0;
		stringQuantatyCPU = new QuantityOfStringsInFile()
				.quantityOfStringsTime(nameCPU);

		if ((stringQuantatyGPU == nShips + 1)
				&& (stringQuantatyCPU == nShips + 1)) {
			timesGPU = new LoadTimeValues().loadTimes(nameGPU);
			timesCPU = new LoadTimeValues().loadTimes(nameCPU);
		} else {
			if (new InitCuda().isPresent()) {// check GPU
				for (int i = 1; i < nShips + 1; i++) {
					timesGPU[i] = new ParallelFullCalcCuda(i, size)
							.getEl_time();
					timesCPU[i] = new SerialSpeedCalc(i, size).getTimeout();
				}
				new WriteToFile(timesGPU, nameGPU);
				new WriteToFile(timesCPU, nameCPU);
			}
		}

		panel = new PaintCompare("ships", "time, ms", timesGPU, timesCPU, 1, 1);
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
