package ua.edu.donntu.cs.cuda.kutt.test_time;

import ua.edu.donntu.cs.cuda.kutt.full_engine_many.ParallelFullCalcCuda;

/**
 * Этот класс запускает различные реализации вычисления координат кораблей и
 * замеряет время выполнения. Данные выводятся в консоль
 * 
 * @author Denis Vodolazskiy
 */
public class TestElapsedTime {
	private final int nShips = 20;// any value, not big //20_000 for
									// ParallelSpeedCalcCuda, 3200 for
									// ParallelFullCalcCuda
	private final int size = 8_000;// define size in cu-file.size=8000,

	// otherwise (bigger) - CUDA_ERROR_OUT_OF_MEMORY
	/**
	 * Конструктор выводит данные в консоль
	 */
	public TestElapsedTime() {
		System.out.println("(Final velocities) elapsed time GPU="
				+ new ParallelSpeedCalcCuda(nShips, size).getEl_time() + " ms");
		System.out.println("(full data) elapsed time GPU="
				+ new ParallelFullCalcCuda(nShips, size).getEl_time() + " ms");
		System.out.println("(serial execution) elapsed time CPU="
				+ new SerialSpeedCalc(nShips, size).getTimeout() + " ms");
		System.out
				.println("(thread execution) elapsed time thread CPU="
						+ new PrepareThreadSpeedCalc(nShips, size).getTimeout()
						+ " ms");
	}
}
