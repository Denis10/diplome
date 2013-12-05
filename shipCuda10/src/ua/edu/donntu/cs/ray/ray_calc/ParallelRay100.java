package ua.edu.donntu.cs.ray.ray_calc;

import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyHostToDevice;

import java.awt.Color;
import java.awt.Graphics2D;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.runtime.cudaStream_t;
import jcuda.utils.KernelLauncher;
import ua.edu.donntu.cs.cuda.service.CUDATiming;
import ua.edu.donntu.cs.ray.base_draw.BaseDrawRay;

/**
 * Этот класс выполняет рассчёты для метода трассировки лучей на GPU.
 * Делится на 100 потоков. Результат - медленне.
 * 
 * @author Denis Vodolazskiy
 */
public class ParallelRay100 extends BaseDrawRay {
	/**
	 * Время выполнения
	 */
	private float el_time;
	/**
	 * Массив цветов пикселов
	 */
	private int pixelColor[] = new int[N];
	/**
	 * Пустой конструктор
	 */
	private final int NumThreads = 100;// Nx должен бытькратен 100!

	/**
	 * Пустой конструктор
	 */
	public ParallelRay100() {

	}
	/**
	 * Конструктор для отрисовки объекта с помощью видеокарты
	 * 
	 * @param points
	 *            точки объекта
	 * @param polygons
	 *            полигоны объекта
	 * @param normals
	 *            нормали объекта
	 */
	public ParallelRay100(int[] points, int[] polygons, int[] normals) {
		obtainTime(points, polygons, normals);
	}
	/**
	 * Функция для отрисовки объекта с помощью видеокарты экрана
	 * 
	 * @param points
	 *            точки объекта
	 * @param polygons
	 *            полигоны объекта
	 * @param normals
	 *            нормали объекта
	 */
	public void obtainTime(int[] points, int[] polygons, int[] normals) {
		cudaStream_t stream = new cudaStream_t();
		CUDATiming ct = new CUDATiming(stream);
		ct.startTiming();

		// create device pointers and allocate memory on the device
		Pointer dev_pointsBaseCuda = new Pointer();
		cudaMalloc(dev_pointsBaseCuda, 3 * Q_POINTS * Sizeof.INT);
		Pointer dev_polygonsCuda = new Pointer();
		cudaMalloc(dev_polygonsCuda, 3 * Q_POLYGONS * Sizeof.INT);
		Pointer dev_normalsCuda = new Pointer();
		cudaMalloc(dev_normalsCuda, 3 * Q_POLYGONS * Sizeof.INT);

		Pointer dev_pixelColor = new Pointer();
		cudaMalloc(dev_pixelColor, N * Sizeof.INT);

		cudaMemcpy(dev_pointsBaseCuda, Pointer.to(points), 3 * Q_POINTS
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_polygonsCuda, Pointer.to(polygons), 3 * Q_POLYGONS
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_normalsCuda, Pointer.to(normals), 3 * Q_POLYGONS
				* Sizeof.INT, cudaMemcpyHostToDevice);

		// set cu-file
		KernelLauncher kernelLauncher = KernelLauncher.create(
				"data/cuSource/ParallelRay100.cu", "ParallelRay100", false);

		// parameters of CUDA-function
		int threadsPerBlock = 128;
		int blockPerGrid = (NumThreads + threadsPerBlock - 1) / threadsPerBlock;// Nx
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain visible polygons
		kernelLauncher.call(dev_pixelColor, dev_pointsBaseCuda,
				dev_polygonsCuda, dev_normalsCuda, N, Nx, Ny, a0, b0, d0, hx,
				hy, distanceX, distanceY, distanceZ, psi, teta, gamma,
				NumThreads);

		// copy data from device to host
		cudaMemcpy(Pointer.to(pixelColor), dev_pixelColor, N * Sizeof.INT,
				cudaMemcpyDeviceToHost);

		cudaFree(dev_pointsBaseCuda);
		cudaFree(dev_polygonsCuda);
		cudaFree(dev_normalsCuda);
		cudaFree(dev_pixelColor);
		//
		el_time = ct.stopTiming();
	}
	/**
	 * Функция для отрисовки пикселов рассчитанным цветом
	 * 
	 * @param g2
	 *            полотно
	 * @param points
	 *            точки объекта
	 * @param polygons
	 *            полигоны объекта
	 * @param normals
	 *            нормали объекта
	 */
	public void calc(Graphics2D g2, int[] points, int[] polygons, int[] normals) {
		obtainTime(points, polygons, normals);

		//
		for (int i = 0; i < N; i++) {
			if (pixelColor[i] == 0) {
				fillPixcel(g2, i / Nx, i % Nx, Color.BLACK);
			} else {
				fillPixcel(g2, i / Nx, i % Nx, Color.WHITE);
			}
		}
	}
	/**
	 * Функция, выполняющая закраску пиксела
	 * 
	 * @param g2
	 *            полотно
	 * @param x
	 *            x-координата пиксела
	 * @param y
	 *            y-координата пиксела
	 * @param c
	 *            цвет пиксела
	 * 
	 */
	protected void fillPixcel(Graphics2D g2, int x, int y, Color c) {
		g2.setPaint(c);
		g2.drawLine(x, y, x, y);
	}

	/**
	 * Передаёт время выполнения
	 */
	public float getEl_time() {
		return el_time;
	}
}