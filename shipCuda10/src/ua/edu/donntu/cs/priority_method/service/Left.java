package ua.edu.donntu.cs.priority_method.service;

/**
 * Левая ветка метода приоритетов
 * 
 * @author Denis Vodolazskiy
 * 
 */
public class Left {
	private int pow[];
	private int num[];

	public Left(int num[], int pow[]) {
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
