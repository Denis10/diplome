package ua.edu.donntu.cs.cuda.kutt.full_engine_one;

import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.utils.KernelLauncher;

/**
 * Этот класс вычисляет координаты и скорости для одного корабля с помощью CUDA.
 * Количество шагов - 40_000. Выделяется память объёмом
 * 8*nShips*size*Sizeof.FLOAT. Основной класс для вычисления траектории движения
 * (CUDA)
 * 
 * @author Denis Vodolazskiy
 */
public class ModelCalcFullEngineCudaOne {

	// with k11 - 580.91; coef Vx, Vy
	/**
	 * Количество кораблей
	 */
	final private int nShips = 1;// only 1 !!!
	final private float T = 4.0f, L = 80.0f, B = 15.0f;// осадка, длина, ширина
														// c 323, 321
	final private float m = 2_0_000_000_000.0f; // масса судна
	final private float lr = 40.0f; // расстояние от ДРК до ЦТ судна
	private float Vx = 5.0f, Vy = 0.0f;// скорости по осям
	private float v = 0.0f, w = 0.0f;// линейная и угловая скорости
	// private float Rc;//радиус циркуляции
	/**
	 * Количество шагов
	 */
	final private int size = 40_000;
	private float VX[] = new float[size];
	private float VY[] = new float[size];
	private float ww[] = new float[size];
	// для второго интеграла
	private float X[] = new float[size];
	private float Y[] = new float[size];
	private float W[] = new float[size];
	// неподвижная система координат
	private float Xobs[] = new float[size];
	private float Yobs[] = new float[size];

	// private float Wmov[] = new float[size];
	/**
	 * Конструктор, выполняющий вычисления координат и скоростей для одного
	 * корабля
	 */
	public ModelCalcFullEngineCudaOne() {

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
				"data/cuSource/modelCalc.cu", "modelCalc", false);

		// parameters of CUDA-function
		int threadsPerBlock = 16;
		int blockPerGrid = (nShips + threadsPerBlock - 1) / threadsPerBlock;
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain visible polygons
		kernelLauncher.call(dev_VX, dev_VY, dev_ww, dev_X, dev_Y, dev_W,
				dev_Xobs, dev_Yobs, T, L, B, m, lr, Vx, Vy, v, w, size, nShips);

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
		printToFile();
	}

	/**
	 * Запись данных в файл
	 */
	private void printToFile() {
		try {
			BufferedWriter outfile = new BufferedWriter(new FileWriter(
					"data/outfiles/VxVy.txt"));
			BufferedWriter outfile2 = new BufferedWriter(new FileWriter(
					"data/outfiles/viv.txt"));
			BufferedWriter outfile3 = new BufferedWriter(new FileWriter(
					"data/outfiles/XY.txt"));
			BufferedWriter outfile4 = new BufferedWriter(new FileWriter(
					"data/outfiles/XYmov.txt"));
			//
			outfile.write("\t Vx\t\t\t\t Vy\t\t\t\t w\t\t\t t\n");
			//
			outfile3.write("X\t\t Y\t\t W" + "\n");
			//
			outfile4.write("Xmov\t\t Ymov\t\t Wmov" + "\n");

			for (int t = 0; t < size; t++) {
				outfile3.write(X[t] + "\t" + Y[t] + "\t" + W[t] + "\n");
				outfile2.write("viv=" + W[t] + "\n");
				outfile4.write(Xobs[t] + "\t" + Yobs[t] + "\n");
				outfile.write(Vx + "\t\t" + Vy + "\t\t" + w + "\t\t" + t + "\n");
			}
			outfile.close();
			outfile2.close();
			outfile3.close();
			outfile4.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Взять VX
	 * 
	 * @return скорость по оси X в системе объекта
	 */
	public float[] getVX() {
		return VX;
	}

	/**
	 * Взять VY
	 * 
	 * @return скорость по оси Y в системе объекта
	 */
	public float[] getVY() {
		return VY;
	}

	/**
	 * Взять ww
	 * 
	 * @return угловая скорость в системе объекта
	 */
	public float[] getw() {
		return ww;
	}

	/**
	 * Взять X
	 * 
	 * @return скорость по оси X в мировой системе
	 */
	public float[] getX() {
		return X;
	}

	/**
	 * Взять Y
	 * 
	 * @return скорость по оси Y в мировой системе
	 */
	public float[] getY() {
		return Y;
	}

	/**
	 * Взять W
	 * 
	 * @return угловая скорость в мировой системе
	 */
	public float[] getW() {
		return W;
	}

	/**
	 * Взять Xobs
	 * 
	 * @return скорость по оси X в системе наблюдателя
	 */
	public float[] getXobs() {
		return Xobs;
	}

	/**
	 * Взять Yobs
	 * 
	 * @return скорость по оси Y в системе наблюдателя
	 */
	public float[] getYobs() {
		return Yobs;
	}

	/**
	 * Взять Rc
	 * 
	 * @return установившийся радиус циркуляции
	 */
	public float getRc() {
		// return Rc = v / w;
		return ((float) Math.sqrt((float) Math.pow(VX[size - 1], 2.0f)
				+ (float) Math.pow(VY[size - 1], 2.0f)))
				/ ww[size - 1];
	}

	/**
	 * Взять T
	 * 
	 * @return осадка корабля
	 */
	public float getT() {
		return T;
	}

	/**
	 * Взять L
	 * 
	 * @return длина корабля
	 */
	public float getL() {
		return L;
	}

	/**
	 * Взять B
	 * 
	 * @return ширина корабля
	 */
	public float getB() {
		return B;
	}
}