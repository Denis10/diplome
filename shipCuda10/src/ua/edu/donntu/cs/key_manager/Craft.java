/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.key_manager;

/**
 * Менеджер клавиатуры. По нажатию клавиш меняются углы вращения корабля вокру осей,
 *  а также расстояния от объекта до наблюдателя по осям
 * 
 * @author Denis Vodolazskiy
 */
import java.awt.event.KeyEvent;

public class Craft {

	private float psi;// = 0;// -30;
	private float teta;// = 0;// 30;
	private float gamma;// = 0;
	private float dpsi = 0;
	private float dteta = 0;
	private float dgamma = 0;
	private int distX;// = 60000;
	private int distY;// = 0;
	private int distZ;// = 0;
	private int deltadistX = 0;
	private int deltadistY = 0;
	private int deltadistZ = 0;

	/**
	 * Конструктор менеджера клавиатуры
	 * 
	 * @param psi
	 *            угол пси (вокруг оси Y)
	 * @param teta
	 *            угол тэта (вокруг оси Z)
	 * @param gamma
	 *            угол гамма (вокруг оси X)
	 * @param distX
	 *            текущее расстояние по оси X
	 * @param distY
	 *            текущее расстояние по оси Y
	 * @param distZ
	 *            текущее расстояние по оси Z
	 */
	public Craft(float psi, float teta, float gamma, int distX, int distY,
			int distZ) {
		this.psi = psi;
		this.teta = teta;
		this.gamma = gamma;
		this.distX = distX;
		this.distY = distY;
		this.distZ = distZ;
	}

	/**
	 * Если произошло событие
	 */
	public void move() {
		psi += dpsi;
		gamma += dgamma;
		teta += dteta;
		distX += deltadistX;
		distY += deltadistY;
		distZ += deltadistZ;
	}

	/**
	 * Взять psi
	 * 
	 * @return угол пси (вокруг оси Y)
	 */
	public float getPsi() {
		return psi;
	}

	/**
	 * Взять teta
	 * 
	 * @return угол тэта (вокруг оси Z)
	 */
	public float getTeta() {
		return teta;
	}

	/**
	 * Взять gamma
	 * 
	 * @return угол гамма (вокруг оси X)
	 */
	public float getGamma() {
		return gamma;
	}

	/**
	 * Взять distX
	 * 
	 * @return текущее расстояние по оси X
	 */
	public int getDistX() {
		return distX;
	}

	/**
	 * Взять distY
	 * 
	 * @return текущее расстояние по оси Y
	 */
	public int getDistY() {
		return distY;
	}

	/**
	 * Взять distZ
	 * 
	 * @return текущее расстояние по оси Z
	 */
	public int getDistZ() {
		return distZ;
	}

	/**
	 * Клавиша нажата
	 * 
	 * @param e
	 *            событие
	 */
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			dpsi = -1;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dpsi = 1;
		}

		if (key == KeyEvent.VK_UP) {
			dgamma = -1;
		}

		if (key == KeyEvent.VK_DOWN) {
			dgamma = 1;
		}
		if (key == KeyEvent.VK_PAGE_UP) {
			dteta = -1;
		}

		if (key == KeyEvent.VK_PAGE_DOWN) {
			dteta = 1;
		}
		if (key == KeyEvent.VK_F11) {
			deltadistX = -100;
		}

		if (key == KeyEvent.VK_F12) {
			deltadistX = 100;
		}
		if (key == KeyEvent.VK_F5) {
			deltadistY = -100;
		}

		if (key == KeyEvent.VK_F6) {
			deltadistY = 100;
		}
		if (key == KeyEvent.VK_F7) {
			deltadistZ = -100;
		}

		if (key == KeyEvent.VK_F8) {
			deltadistZ = 100;
		}
	}

	/**
	 * Клавиша нажата
	 * 
	 * @param e
	 *            событие
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			dpsi = 0;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dpsi = 0;
		}

		if (key == KeyEvent.VK_UP) {
			dgamma = 0;
		}

		if (key == KeyEvent.VK_DOWN) {
			dgamma = 0;
		}
		if (key == KeyEvent.VK_PAGE_UP) {
			dteta = 0;
		}

		if (key == KeyEvent.VK_PAGE_DOWN) {
			dteta = 0;
		}
		if (key == KeyEvent.VK_F11) {
			deltadistX = 0;
		}

		if (key == KeyEvent.VK_F12) {
			deltadistX = 0;
		}
		if (key == KeyEvent.VK_F5) {
			deltadistY = 0;
		}

		if (key == KeyEvent.VK_F6) {
			deltadistY = 0;
		}
		if (key == KeyEvent.VK_F7) {
			deltadistZ = 0;
		}

		if (key == KeyEvent.VK_F8) {
			deltadistZ = 0;
		}
	}
}
