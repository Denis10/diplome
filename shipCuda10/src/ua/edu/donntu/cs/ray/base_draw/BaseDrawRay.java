package ua.edu.donntu.cs.ray.base_draw;

import javax.swing.JPanel;

import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;
/**
 * Этот класс задаёт основные данные для метода трассировки лучей.
 * Задаются параметры окна
 * 
 * @author Denis Vodolazskiy
 */
public class BaseDrawRay extends JPanel implements IRayFigure {
	protected float psi = 0;// -30;
	protected float teta = 0;// 30;
	protected float gamma = 0;
	protected int pr = 1;
	protected int distanceX = new MetMmTransform().meter2mm(-10);// meters
	protected int distanceY = 0;
	protected int distanceZ = 0;
	// 2 Initializing the following values:- a0,b0,d0,Nx,Ny,hx,hy
	/**
	 * Количество пикселов по Х
	 */			
	protected final int Nx = 700;// pixcels in window (X)
	/**
	 * Количество пикселов по Y
	 */	
	protected final int Ny = 700;// pixcels in window (Y)
	/**
	 * Размер окна по Х
	 */	
	protected final float a0 = 1;// размер окна по икс
	/**
	 * Размер окна по Y
	 */	
	protected final float b0 = 1;// размер окна по игрик
	/**
	 * Расстояние до окна
	 */	
	protected final float d0 = 0.6f;// расстояние до окна
	/**
	 * Размер пиксела по Х
	 */	
	protected final float hx = a0 / Nx;
	/**
	 * Размер пиксела по Y
	 */
	protected final float hy = b0 / Ny;
	// Initialization of arrays
	/**
	 * Общее количество пикселов
	 */
	protected final int N = Nx * Ny;
}
