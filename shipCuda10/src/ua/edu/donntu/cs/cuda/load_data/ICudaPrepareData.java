/*Class with constant arrays (for CUDA)*/

/*Not used*/
package ua.edu.donntu.cs.cuda.load_data;

/**
 * Этот интерфейс создаёт константные массивы точек, полигонов и нормалей. Для
 * реализации на CUDA
 * 
 * @author Denis Vodolazskiy
 */
public interface ICudaPrepareData {
	public final LoadDataCuda dataCuda = new LoadDataCuda();
	public final int pointsBaseCuda[] = dataCuda.loadPoints();
	// public final int pointsBaseCuda[] = new TransformArray().in1D(points, N);
	public final int polygonsCuda[] = dataCuda.loadPolygons();
	public final int normalsCuda[] = dataCuda.loadNormals(pointsBaseCuda,
			polygonsCuda);
	// public final int normalsCuda[] =
	// dataCuda.loadNormalsCuda(pointsBaseCuda,polygonsCuda);
}
