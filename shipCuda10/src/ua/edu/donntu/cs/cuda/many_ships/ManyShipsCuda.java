/*Class to move group of ships using CUDA*/
package ua.edu.donntu.cs.cuda.many_ships;

import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyHostToDevice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.utils.KernelLauncher;
import ua.edu.donntu.cs.draw.information.InfoPrint;
import ua.edu.donntu.cs.draw.matrix_transform.MatrixTransform;
import ua.edu.donntu.cs.inout.IPrepareData;
import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Панель для построения 9-ти объектов с помощью GPU
 * 
 * @author Denis Vodolazskiy
 */
public class ManyShipsCuda extends JPanel implements ActionListener,
		IPrepareData {
	private static final long serialVersionUID = 1L;
	//
	private Timer timer;
	private Craft craft;

	// start corners
	protected float psi = 0;// -30;
	protected float teta = 0;// 30;
	protected float gamma = 0;
	protected int pr = 1;

	// start distances
	protected int distanceX = new MetMmTransform().meter2mm(-500);// 60000;
	protected int distanceY = 0;
	protected int distanceZ = 0;
	//
	final int n = 3;
	final int nShips = 9;
	protected int shipLocationX[] = { 0, 0, 0, 150000, -150000, 150000, -150000,
			-150000, 150000, };
	protected int shipLocationZ[] = { 0, 150000, -150000, 0, 0, 150000, 150000,
			-150000, -150000 };
	//

	protected int[] polygonToFillX = new int[Q_POLYGONS * n * nShips];
	protected int[] polygonToFillY = new int[Q_POLYGONS * n * nShips];
	protected int[] S = new int[Q_POLYGONS * nShips];

	/**
	 * Конструктор, активирует слушателя и менеджера клавиатуры. Запускает
	 * таймер.
	 */
	public ManyShipsCuda() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		craft = new Craft(psi, teta, gamma, distanceX, distanceY, distanceZ);
		timer = new Timer(5, this);
		timer.start();
	}

	/**
	 * Функции для определения видимых полигонов с помощью GPU
	 */
	protected void calc() {
		// ------------------matrix A B C------------------------------------
		double matrixA[][] = new double[3][3];
		MatrixTransform mt = new MatrixTransform();
		matrixA = mt.matr(psi, teta, gamma, pr);
		double matrixB[][] = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		float[] matrixC = new float[12];
		float pcs[] = { 0, 0, 0 };// вектор сцены 0,0,0

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrixC[i * 4 + j] = (float) (matrixA[i][0] * matrixB[j][0]
						+ matrixA[i][1] * matrixB[j][1] + matrixA[i][2]
						* matrixB[j][2]);
			}
		}
		pcs[0] = pcs[0] - distanceX;
		pcs[1] = pcs[1] - distanceY;
		pcs[2] = pcs[2] - distanceZ;
		matrixC[3] = (float) (pcs[0] * matrixB[0][0] + pcs[1] * matrixB[1][0] + pcs[2]
				* matrixB[2][0]);// 1350//150
		matrixC[7] = (float) (pcs[0] * matrixB[0][1] + pcs[1] * matrixB[1][1] + pcs[2]
				* matrixB[2][1]);// -150
		matrixC[11] = (float) (pcs[0] * matrixB[0][2] + pcs[1] * matrixB[1][2] + pcs[2]
				* matrixB[2][2]);// -150

		// create device pointers and allocate memory on the device
		Pointer dev_points = new Pointer();
		cudaMalloc(dev_points, 3 * Q_POINTS * Sizeof.INT);
		Pointer dev_polygons = new Pointer();
		cudaMalloc(dev_polygons, 3 * Q_POLYGONS * Sizeof.INT);
		Pointer dev_normals = new Pointer();
		cudaMalloc(dev_normals, 3 * Q_POLYGONS * Sizeof.INT);
		Pointer dev_matrixC = new Pointer();
		cudaMalloc(dev_matrixC, 12 * Sizeof.FLOAT);
		Pointer dev_shipLocationX = new Pointer();
		cudaMalloc(dev_shipLocationX, nShips * Sizeof.INT);
		Pointer dev_shipLocationZ = new Pointer();
		cudaMalloc(dev_shipLocationZ, nShips * Sizeof.INT);
		Pointer dev_S = new Pointer();
		cudaMalloc(dev_S, Q_POLYGONS * nShips * Sizeof.INT);
		Pointer dev_polygonToFillX = new Pointer();
		cudaMalloc(dev_polygonToFillX, 3 * Q_POLYGONS * nShips * Sizeof.INT);
		Pointer dev_polygonToFillY = new Pointer();
		cudaMalloc(dev_polygonToFillY, 3 * Q_POLYGONS * nShips * Sizeof.INT);

		// copy data from host to device. Pointer.to(pointsBase) - host pointer
		cudaMemcpy(dev_points, Pointer.to(pointsBaseCuda), 3 * Q_POINTS
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_polygons, Pointer.to(polygonsCuda), 3 * Q_POLYGONS
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_normals, Pointer.to(normalsCuda), 3 * Q_POLYGONS
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_matrixC, Pointer.to(matrixC), 12 * Sizeof.FLOAT,
				cudaMemcpyHostToDevice);
		cudaMemcpy(dev_shipLocationX, Pointer.to(shipLocationX), nShips
				* Sizeof.INT, cudaMemcpyHostToDevice);
		cudaMemcpy(dev_shipLocationZ, Pointer.to(shipLocationZ), nShips
				* Sizeof.INT, cudaMemcpyHostToDevice);

		// set cu-file
		KernelLauncher kernelLauncher = KernelLauncher.create(
				"data/cuSource/obtainPolygonsSteps.cu", "obtainPolygonsSteps",
				false);

		// parameters of CUDA-function
		int threadsPerBlock = 16;
		int blockPerGrid = (nShips + threadsPerBlock - 1) / threadsPerBlock;
		kernelLauncher.setGridSize(blockPerGrid, 1);
		kernelLauncher.setBlockSize(threadsPerBlock, 1, 1);

		// launch kernel to obtain visible polygons
		kernelLauncher.call(dev_S, dev_polygonToFillX, dev_polygonToFillY,
				dev_shipLocationX, dev_shipLocationZ, dev_matrixC, dev_points,
				dev_polygons, dev_normals, nShips);

		// copy data from device to host
		cudaMemcpy(Pointer.to(S), dev_S, Q_POLYGONS * nShips * Sizeof.INT,
				cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(polygonToFillX), dev_polygonToFillX, 3
				* Q_POLYGONS * nShips * Sizeof.INT, cudaMemcpyDeviceToHost);
		cudaMemcpy(Pointer.to(polygonToFillY), dev_polygonToFillY, 3
				* Q_POLYGONS * nShips * Sizeof.INT, cudaMemcpyDeviceToHost);

		// free memory on the device
		cudaFree(dev_points);
		cudaFree(dev_polygons);
		cudaFree(dev_normals);
		cudaFree(dev_matrixC);
		cudaFree(dev_shipLocationX);
		cudaFree(dev_shipLocationZ);
		cudaFree(dev_S);
		cudaFree(dev_polygonToFillX);
		cudaFree(dev_polygonToFillY);
	}

	// Turn on antialiasing.
	/**
	 * Включение антиалиасинга
	 */
	protected void setAntialiasing(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	// draw one polygon in default color
	/**
	 * 
	 * @param g2
	 *            графический контент
	 * @param x
	 *            x-координаты точек полигона
	 * @param y
	 *            y-координаты точек полигона
	 * @param num
	 *            количество вершин в полигоне
	 */
	protected void makePolygon(Graphics2D g2, int x[], int y[], int num) {
		// Color cFigure = new Color(0, 255, 0);
		// Color cFill = new Color(255, 255, 255);
		Color cFigure = new Color(0, 255, 0);
		Color cFill = new Color(255, 255, 255);
		g2.setPaint(cFigure);
		g2.drawPolygon(x, y, num);
		g2.setPaint(cFill);
		g2.fillPolygon(x, y, num);
	}

	// draw polygons in default color
	/**
	 * 
	 * Выделяет три точки, разносит из координаты по массивам x и y
	 */
	protected void makeAllPolygons(Graphics2D g2) {
		for (int i = 0; i < Q_POLYGONS * nShips; i++) {
			if (S[i] == 1) {
				int[] x = new int[3];
				int[] y = new int[3];
				x[0] = polygonToFillX[3 * i];
				x[1] = polygonToFillX[3 * i + 1];
				x[2] = polygonToFillX[3 * i + 2];
				y[0] = polygonToFillY[3 * i];
				y[1] = polygonToFillY[3 * i + 1];
				y[2] = polygonToFillY[3 * i + 2];
				makePolygon(g2, x, y, 3);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		craft.move();
		repaint();
	}

	/**
	 * Выполняет перерисовку экрана
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		// setAntialiasing(g2);
		psi = craft.getPsi();
		teta = craft.getTeta();
		gamma = craft.getGamma();
		distanceX = craft.getDistX();
		distanceY = craft.getDistY();
		distanceZ = craft.getDistZ();
		calc();
		makeAllPolygons(g2);
		new InfoPrint(g2, craft);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	/**
	 * Обрабатывает нажатие клавиш
	 * 
	 * @author Denis Vodolazskiy
	 * 
	 */
	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			craft.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			craft.keyPressed(e);
		}
	}
}
