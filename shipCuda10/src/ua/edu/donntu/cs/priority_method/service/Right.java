package ua.edu.donntu.cs.priority_method.service;

/**
 * Правая ветка метода приоритетов
 * 
 * @author Denis Vodolazskiy
 * 
 */
public class Right {
	private int pow[];
	private int num[];

	public Right(int num[], int pow[]) {
		this.pow = pow;
		this.num = num;
	}

	public int getPowI(int i) {
		return pow[i];
	}

	public int getNumI(int i) {
		return num[i];
	}
}
