package ua.edu.donntu.cs.ray.ray_calc_time;

import ua.edu.donntu.cs.ray.base_draw.BaseDrawRay;
import ua.edu.donntu.cs.ray.ray_calc.ParallelRay;

/**
 * Этот класс замеряет времявыполнения для метода трассировки лучей на CPU и
 * GPU. Ресультат выводит в консоль. Не используется в основной программе.
 * 
 * @author Denis Vodolazskiy
 */
public class RayTimeCalc extends BaseDrawRay {
	private long CPUtime;
	private float GPUtime;

	public RayTimeCalc() {
		CPUtime = new SerialRayTime(points, polygons, normals, Nx, Ny)
				.getTimeout();
		GPUtime = new ParallelRay(pointsBaseCuda, polygonsCuda, normalsCuda)
				.getEl_time();

		System.out.println("SerialRayTime elapsed on CPU=" + CPUtime + " ms");

		System.out.println("ParallelRayTime elapsed on GPU=" + GPUtime + " ms");

		System.out.println("CPU/GPU=" + CPUtime / GPUtime + " times");

	}

}
