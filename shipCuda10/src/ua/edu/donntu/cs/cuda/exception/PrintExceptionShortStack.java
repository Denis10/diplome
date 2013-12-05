package ua.edu.donntu.cs.cuda.exception;

/**
 * Укороченный стек исключения
 * 
 * @author Denis Vodolazskiy
 */
public class PrintExceptionShortStack {
	Exception e;

	/**
	 * Конструктор вывода в консоль стека. Выводятся первые 6 элементов
	 * 
	 */
	public PrintExceptionShortStack(Exception e) {
		this.e = e;
		assert e != null;
		StackTraceElement ste[] = e.getStackTrace();
		System.err.println("Short stack messages: " + e.getLocalizedMessage());
		int n = 6;
		if (ste.length < 6) {
			n = ste.length;
		}
		for (int i = 0; i < n; i++) {
			System.err.println(ste[i]);
		}
	}
}
