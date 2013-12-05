package ua.edu.donntu.cs.cuda.load_data;

import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyHostToDevice;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.utils.KernelLauncher;
import ua.edu.donntu.cs.inout.WriteToFile;

/**
 * Этот класс вычисляет векторы нормалей.Используется CUDA. Двумерные массивы
 * используются для вычисления на CPU
 * 
 * @author Denis Vodolazskiy
 */
public class LoadNormalsCuda2D {
	// the same method for 2D arrays
	/**
	 * Рассчитывает векторы нормалей на GPU
	 * 
	 * @param points
	 *            координаты точек
	 * @param polygons
	 *            точки полигонов
	 * @param countPoints
	 *            количество точек
	 * @param countPolygons
	 *            количество полигонов
	 * @return двумерный массив векторов нормалей
	 */
	public int[][] loadNormalsCuda(int points[][], int polygons[][],
			int countPoints, int countPolygons) {
		int normals[][] = new int[countPolygons][3];
		int sizePoly = 3 * countPolygons * Sizeof.INT;
		int sizePoint = 3 * countPoints * Sizeof.INT;
		int size = countPolygons * Sizeof.INT;
		int point[] = new int[sizePoint];
		int polygon[] = new int[sizePoly];
		int normalX[] = new int[countPolygons];
		int normalY[] = new int[countPolygons];
		int normalZ[] = new int[countPolygons];
		//
		for (int i = 0; i < countPoints; i++) {
			for (int j = 0; j < 3; j++) {
				point[3 * i + j] = points[i][j];
			}
		}
		for (int i = 0; i < countPolygons; i++) {
			for (int j = 0; j < 3; j++) {
				polygon[3 * i + j] = polygons[i][j];
			}
		}

		// create device pointers and allocate memory on the device
		Pointer poi = new Pointer();
		cudaMalloc(poi, sizePoint);
		Pointer pol = new Pointer();
		cudaMalloc(pol, sizePoly);
		Pointer nX = new Pointer();
		cudaMalloc(nX, size);
		Pointer nY = new Pointer();
		cudaMalloc(nY, size);
		Pointer nZ = new Pointer();
		cudaMalloc(nZ, size);

		// copy data from host to device. Pointer.to(points) - host pointer
		cudaMemcpy(poi, Pointer.to(point), sizePoint, cudaMemcpyHostToDevice);
		cudaMemcpy(pol, Pointer.to(polygon), sizePoly, cudaMemcpyHostToDevice);

		// set cu-file
		KernelLauncher kernelLauncher = KernelLauncher.create(
				"data/cuSource/TestNormalFull.cu", "TestNormalFull", false);

		// parameters of CUDA-function
		int threadsPerBlock = 64;
		int blockPerGrid = (countPolygons + threadsPerBlock - 1)
				/ threadsPerBlock;
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain normals
		kernelLauncher.call(poi, pol, nX, nY, nZ, countPolygons);

		// copy data from device to host
		cudaMemcpy(Pointer.to(normalX), nX, size, cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(normalY), nY, size, cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(normalZ), nZ, size, cudaMemcpyDeviceToHost);

		// free memory on the device
		cudaFree(poi);
		cudaFree(pol);
		cudaFree(nX);
		cudaFree(nY);
		cudaFree(nZ);

		for (int i = 0; i < countPolygons; i++) {
			normals[i][0] = normalX[i];
			normals[i][1] = normalY[i];
			normals[i][2] = normalZ[i];
		}
		new WriteToFile(normals, "normal2.txt");
		return normals;
	}
}
