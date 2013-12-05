package ua.edu.donntu.cs.cuda.kutt.test_time;

public class PrepareThreadSpeedCalc {
	/**
	 * Количество кораблей
	 */
	private int nShips;
	/**
	 * Время выполнения
	 */
	private long timeout;
	/**
	 * Количество шагов (секунд)
	 */
	private int size;// = 8_000;

	/**
	 * Конструктор, передающий данные в класс-поток
	 * 
	 * @param nShips
	 *            количество кораблей (циклов)
	 * @param size
	 *            количество шагов вычислений (количество секунд)
	 * 
	 */
	public PrepareThreadSpeedCalc(int nShips, int size) {
		this.nShips = nShips;
		this.size = size;
		timeout = System.currentTimeMillis();
		callThreads();
		timeout = System.currentTimeMillis() - timeout;
	}

	/**
	 * Запуск потока
	 */
	private void callThreads() {
		for (int i = 0; i < nShips; i++) {
			Thread t = new Thread(new ThreadSpeedCalc(nShips, size));
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Передаёт время выполнения
	 */
	public long getTimeout() {
		return timeout;
	}
}
