/*Class to know finals speeds of many ships (same ship). define size in cu-file. Copies only final speeds*/
package ua.edu.donntu.cs.cuda.kutt.test_time;

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
 * Этот класс вычисляет конечные скорости для произвольного количества кораблей
 * с помощью CUDA. Для одного корабля выделяется память объёмом
 * 8*nShips*Sizeof.FLOAT. Замеряется время выполнения
 * 
 * @author Denis Vodolazskiy
 */
public class ParallelSpeedCalcCuda {
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
	 * Конструктор, выполняющий вычисления конечных скоростей для произвольного
	 * количества кораблей
	 * 
	 * @param nShips
	 *            количество кораблей
	 * @param size
	 *            количество шагов вычислений (количество секунд)
	 * 
	 */
	public ParallelSpeedCalcCuda(int nShips, int size) {
		this.nShips = nShips;
		this.size = size;
		float VX[] = new float[nShips];
		float VY[] = new float[nShips];
		float ww[] = new float[nShips];
		// для второго интеграла
		float X[] = new float[nShips];
		float Y[] = new float[nShips];
		float W[] = new float[nShips];
		// неподвижная система координат
		float Xobs[] = new float[nShips];
		float Yobs[] = new float[nShips];

		cudaStream_t stream = new cudaStream_t();
		CUDATiming ct = new CUDATiming(stream);
		ct.startTiming();
		// create device pointers and allocate memory on the device
		Pointer dev_VX = new Pointer();
		cudaMalloc(dev_VX, nShips * Sizeof.FLOAT);
		Pointer dev_VY = new Pointer();
		cudaMalloc(dev_VY, nShips * Sizeof.FLOAT);
		Pointer dev_ww = new Pointer();
		cudaMalloc(dev_ww, nShips * Sizeof.FLOAT);
		Pointer dev_X = new Pointer();
		cudaMalloc(dev_X, nShips * Sizeof.FLOAT);
		Pointer dev_Y = new Pointer();
		cudaMalloc(dev_Y, nShips * Sizeof.FLOAT);
		Pointer dev_W = new Pointer();
		cudaMalloc(dev_W, nShips * Sizeof.FLOAT);
		Pointer dev_Xobs = new Pointer();
		cudaMalloc(dev_Xobs, nShips * Sizeof.FLOAT);
		Pointer dev_Yobs = new Pointer();
		cudaMalloc(dev_Yobs, nShips * Sizeof.FLOAT);

		// set cu-file
		KernelLauncher kernelLauncher = KernelLauncher.create(
				"data/cuSource/ParallelSpeedCalcCuda.cu",
				"ParallelSpeedCalcCuda", false);

		// parameters of CUDA-function
		int threadsPerBlock = 64;
		int blockPerGrid = (nShips + threadsPerBlock - 1) / threadsPerBlock;
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain visible polygons
		kernelLauncher.call(dev_VX, dev_VY, dev_ww, dev_X, dev_Y, dev_W,
				dev_Xobs, dev_Yobs, nShips);

		// copy data from device to host
		cudaMemcpy(Pointer.to(VX), dev_VX, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(VY), dev_VY, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(ww), dev_ww, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(X), dev_X, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Y), dev_Y, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(W), dev_W, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Xobs), dev_Xobs, nShips * Sizeof.FLOAT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(Yobs), dev_Yobs, nShips * Sizeof.FLOAT,
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
		// System.out.println("(Final velocities) elapsed time GPU=" + el_time +
		// " ms");
		// for (int i = 0; i < nShips; i++)
		// System.out.println(VX[i]);

	}

	/**
	 * Передаёт время выполнения
	 */
	public float getEl_time() {
		return el_time;
	}

}
