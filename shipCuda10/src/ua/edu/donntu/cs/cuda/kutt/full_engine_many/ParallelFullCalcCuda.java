package ua.edu.donntu.cs.cuda.kutt.full_engine_many;

import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.runtime.cudaStream_t;
import jcuda.utils.KernelLauncher;
import ua.edu.donntu.cs.cuda.service.CUDATiming;

/**
 * Этот класс вычисляет координаты и скорости для произвольного количества
 * кораблей с помощью CUDA. Для одного корабля выделяется память объёмом
 * 8*nShips*size*Sizeof.FLOAT. Замеряется время выполнения
 * 
 * @author Denis Vodolazskiy
 */
public class ParallelFullCalcCuda {
	// with k11 - 580.91; coef Vx, Vy
	/**
	 * Количество кораблей
	 */
	private int nShips;// = 20_000;// any value
	/**
	 * Количество шагов (секунд)
	 */
	private int size;// = 8_000;// define size in cu-file.size=8000,
						// otherwise - CUDA_ERROR_OUT_OF_MEMORY
	/**
	 * Время выполнения
	 */
	private float el_time;

	/**
	 * Конструктор, выполняющий вычисления координат и скоростей для
	 * произвольного количества кораблей
	 * 
	 * @param nShips
	 *            количество кораблей
	 * @param size
	 *            количество шагов вычислений (количество секунд)
	 * 
	 */
	public ParallelFullCalcCuda(int nShips, int size) {
		this.nShips = nShips;
		this.size = size;
		float VX[] = new float[nShips * size];
		float VY[] = new float[nShips * size];
		float ww[] = new float[nShips * size];
		// для второго интеграла
		float X[] = new float[nShips * size];
		float Y[] = new float[nShips * size];
		float W[] = new float[nShips * size];
		// неподвижная система координат
		float Xobs[] = new float[nShips * size];
		float Yobs[] = new float[nShips * size];

		cudaStream_t stream = new cudaStream_t();
		CUDATiming ct = new CUDATiming(stream);
		ct.startTiming();
		// create device pointers and allocate memory on the device
		Pointer dev_VX = new Pointer();
		cudaMalloc(dev_VX, nShips * size * Sizeof.FLOAT);
		Pointer dev_VY = new Pointer();
		cudaMalloc(dev_VY, nShips * size * Sizeof.FLOAT);
		Pointer dev_ww = new Pointer();
		cudaMalloc(dev_ww, nShips * size * Sizeof.FLOAT);
		Pointer dev_X = new Pointer();
		cudaMalloc(dev_X, nShips * size * Sizeof.FLOAT);
		Pointer dev_Y = new Pointer();
		cudaMalloc(dev_Y, nShips * size * Sizeof.FLOAT);
		Pointer dev_W = new Pointer();
		cudaMalloc(dev_W, nShips * size * Sizeof.FLOAT);
		Pointer dev_Xobs = new Pointer();
		cudaMalloc(dev_Xobs, nShips * size * Sizeof.FLOAT);
		Pointer dev_Yobs = new Pointer();
		cudaMalloc(dev_Yobs, nShips * size * Sizeof.FLOAT);

		// set cu-file
		KernelLauncher kernelLauncher = KernelLauncher.create(
				"data/cuSource/ParallelFullCalcCuda.cu",
				"ParallelFullCalcCuda", false);

		// parameters of CUDA-function
		int threadsPerBlock = 64;
		int blockPerGrid = (nShips + threadsPerBlock - 1) / threadsPerBlock;
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain visible polygons
		kernelLauncher.call(dev_VX, dev_VY, dev_ww, dev_X, dev_Y, dev_W,
				dev_Xobs, dev_Yobs, nShips);

		// copy data from device to host
		cudaMemcpy(Pointer.to(VX), dev_VX, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(VY), dev_VY, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(ww), dev_ww, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(X), dev_X, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Y), dev_Y, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(W), dev_W, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Xobs), dev_Xobs, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Yobs), dev_Yobs, nShips * size * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaFree(dev_VX);
		cudaFree(dev_VY);
		cudaFree(dev_ww);
		cudaFree(dev_X);
		cudaFree(dev_Y);
		cudaFree(dev_W);
		cudaFree(dev_Xobs);
		cudaFree(dev_Yobs);
		el_time = ct.stopTiming();
	}

	/**
	 * Передаёт время выполнения
	 */
	public float getEl_time() {
		return el_time;
	}

}