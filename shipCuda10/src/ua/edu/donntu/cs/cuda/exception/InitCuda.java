package ua.edu.donntu.cs.cuda.exception;

import static jcuda.driver.JCudaDriver.cuDeviceGetCount;
import static jcuda.driver.JCudaDriver.cuInit;

/**
 * инициализация GPU. При отсутствии видеокарты бросает исключение
 * 
 * @author Denis Vodolazskiy
 */
public class InitCuda {
	private boolean present = true;

	/**
	 * Конструктор инициализации GPU. При отсутствии видеокарты вызывает
	 * диалоговое окно
	 * 
	 */
	public InitCuda() {
		try {
			doInit();
		} catch (MyCudaException ce) {
			present = false;
			javax.swing.JOptionPane.showMessageDialog(
					(java.awt.Component) null, "Error: " + ce.getMessage(),
					"Error:", javax.swing.JOptionPane.DEFAULT_OPTION);
		}
	}

	/**
	 * Выполняет инициализацию GPU
	 * 
	 * @throws MyCudaException
	 *             при отсутствии видеокарты
	 */
	private static void doInit() throws MyCudaException {
		try {
			cuInit(0);
			// Obtain the number of devices
			int deviceCountArray[] = { 0 };
			cuDeviceGetCount(deviceCountArray);
			int deviceCount = deviceCountArray[0];
			if (deviceCount == 0) {
				throw new MyCudaException("CUDA-device not found");
			} else {
				// System.out.println("Found " + deviceCount + " devices");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			new PrintExceptionShortStack(e);
			throw e;
		}
	}

	/**
	 * Сигнализирует о статусе GPU
	 * 
	 * @return true or false
	 */
	public boolean isPresent() {
		return present;
	}
}
