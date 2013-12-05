package ua.edu.donntu.cs.cuda.exception;

/**
 * Собственное исключение для GPU
 * 
 * @author Denis Vodolazskiy
 */
public class MyCudaException extends Exception {
	public MyCudaException(String msg) {
		super(msg);
	}
}
