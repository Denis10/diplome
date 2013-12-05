/*Class with constant arrays*/
package ua.edu.donntu.cs.inout;

import ua.edu.donntu.cs.cuda.service.TransformArray;

/**
 * Этот интерфейс создаёт константные массивы точек, полигонов и нормалей
 * 
 * @author Denis Vodolazskiy
 */
public interface IPrepareData {
	public final LoadData data = new LoadData();//для остальных классов путь задан в их коде
	public final int points[][] = data.loadPoints("data/inputfiles/points2.txt");
	public final int polygons[][] = data.loadPolygons("data/inputfiles/pointsAndPolygons2.txt");
	public final int normals[][] = data.loadNormals(points, polygons);

	// obtain normals using CUDA
	// public final LoadNormalsCuda2D lnc = new LoadNormalsCuda2D();
	// public final int normals[][] = new LoadNormalsCuda2D().loadNormalsCuda(
	// points, polygons, data.getCountPoints(), data.getCountPolygons());

	// set Q_POINTS and Q_POLYGONS
	public final int Q_POINTS = data.getCountPoints();
	public final int Q_POLYGONS = data.getCountPolygons();

	// Load Cuda data
	// public final LoadDataCuda dataCuda = new LoadDataCuda();
	// public final int pointsBaseCuda[] = dataCuda.loadPoints();
	public final int pointsBaseCuda[] = new TransformArray().in1D(points,
			Q_POINTS);
	// public final int polygonsCuda[] = dataCuda.loadPolygons();
	public final int polygonsCuda[] = new TransformArray().in1D(polygons,
			Q_POLYGONS);
	// public final int normalsCuda[] = dataCuda.loadNormals(pointsBaseCuda,
	// polygonsCuda);
	// public final int normalsCuda[] = new LoadNormalsCuda1D().loadNormalsCuda(
	// pointsBaseCuda, polygonsCuda, Q_POINTS, Q_POLYGONS);
	public final int normalsCuda[] = new TransformArray().in1D(normals,
			Q_POLYGONS);
}
