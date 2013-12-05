/*Class to view time of CUDA-execution*/
package ua.edu.donntu.cs.cuda.service;

import jcuda.CudaException;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaEvent_t;
import jcuda.runtime.cudaStream_t;

/**
 * Class, to provide CUDA-timing methods.<br>
 * Altough it is possible to reuse an object of this class it has not been
 * tested extensively and it is advised to instantiate a new object for every
 * timing.
 * 
 * @author Manuel Schaeidt
 * @version $Date$ - $Rev$
 */
public final class CUDATiming {

	/**
	 * Start-Event
	 */
	private cudaEvent_t start;

	/**
	 * Stop-Event
	 */
	private cudaEvent_t stop;

	/**
	 * Stream-Object
	 */
	private cudaStream_t stream;

	/**
	 * Standard-constructor, activates exceptions
	 */
	public CUDATiming() {
		this(new cudaStream_t());
	}

	/**
	 * Full-constructor, activates exceptions
	 * 
	 * @param stream
	 *            Stream-Object for this timing
	 */
	public CUDATiming(cudaStream_t stream) {
		this.start = new cudaEvent_t();
		this.stop = new cudaEvent_t();
		this.stream = stream;

		// Enable exceptions
		JCuda.setExceptionsEnabled(true);
		JCudaDriver.setExceptionsEnabled(true);
	}

	/**
	 * Starts a CUDA-Timing
	 * 
	 * @throws CudaException
	 *             If an error occured
	 */
	public void startTiming() throws CudaException {

		// Create events
		JCuda.cudaEventCreate(this.start);
		JCuda.cudaEventCreate(this.stop);

		// Start recording
		JCuda.cudaEventRecord(this.start, this.stream);
	}

	/**
	 * Stops a CUDA-Timing
	 * 
	 * @return Duration in ms rounded to 0.5 micro-seconds
	 * @throws CudaException
	 *             If an error occured
	 */
	public float stopTiming() throws CudaException {
		float[] elapsedTime = new float[1];

		// Stop recording
		JCuda.cudaEventRecord(this.stop, this.stream);
		JCuda.cudaEventSynchronize(this.stop);

		// Calculate duration
		JCuda.cudaEventElapsedTime(elapsedTime, this.start, this.stop);

		// Free memory
		JCuda.cudaEventDestroy(this.start);
		JCuda.cudaEventDestroy(this.stop);

		return elapsedTime[0];
	}
}