package ua.edu.donntu.cs.ray.base_draw;

import ua.edu.donntu.cs.cuda.service.TransformArray;
import ua.edu.donntu.cs.inout.LoadData;

/**
 * Этот класс задаёт основные данные для метода трассировки лучей.
 * Точки и полигоны берутся из файлов для куба.
 * 
 * @author Denis Vodolazskiy
 */
public interface IRayFigure {
	
	public final LoadData data = new LoadData();// для остальных классов путь
	/**
	 * Точки куба
	 */										// задан в их коде
	public final int points[][] = data
			.loadPoints("data/inputfiles/pointsKub.txt");
			//.loadPoints("data/inputfiles/points2.txt");
	/**
	 * Полигоны куба
	 */	
	public final int polygons[][] = data
			.loadPolygons("data/inputfiles/pointsAndPolygonsKub.txt");
			//.loadPolygons("data/inputfiles/pointsAndPolygons2.txt");
	/**
	 * Нормали куба
	 */	
	public final int normals[][] = data.loadNormals(points, polygons);

	// set Q_POINTS and Q_POLYGONS
	/**
	 * Количество точек
	 */	
	public final int Q_POINTS = data.getCountPoints();
	/**
	 * Количество полигонов
	 */	
	public final int Q_POLYGONS = data.getCountPolygons();

	/**
	 * Точки куба (CUDA)
	 */	
	public final int pointsBaseCuda[] = new TransformArray().in1D(points,
			Q_POINTS);
	/**
	 * Полигоны куба (CUDA)
	 */	
	public final int polygonsCuda[] = new TransformArray().in1D(polygons,
			Q_POLYGONS);
	/**
	 * Нормали куба (CUDA)
	 */	
	public final int normalsCuda[] = new TransformArray().in1D(normals,
			Q_POLYGONS);
}
