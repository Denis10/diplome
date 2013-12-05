package ua.edu.donntu.cs.priority_method.service;

import java.awt.Color;

import ua.edu.donntu.cs.inout.IPrepareData;

/**
 * Корабль, его форма, положение, цвет
 * 
 * @author Denis Vodolazskiy
 */
public class Ship implements IPrepareData {
	private int pointsShip[][] = new int[Q_POINTS][3];
	private int polygonsShip[][] = new int[Q_POLYGONS][3];
	private int normalsShip[][] = new int[Q_POLYGONS][3];
	private Color colorShip;

	/**
	 * Конструктор создания объекта "Корабль"
	 * 
	 * @param points
	 *            точки
	 * @param polygons
	 *            треугольники
	 * @param normals
	 *            нормали к треугольникам
	 * @param color
	 *            цвет корабля
	 */
	public Ship(int[][] points, int[][] polygons, int[][] normals, Color color) {
		this.pointsShip = points;
		this.polygonsShip = polygons;
		this.normalsShip = normals;
		this.colorShip = color;
	}

	/**
	 * Взять точки
	 * 
	 * @return точки
	 */
	public int[][] getPoints() {
		return pointsShip;
	}

	/**
	 * Установить точки
	 * 
	 * @param points
	 *            точки
	 */

	public void setPoints(int[][] points) {
		this.pointsShip = points;
	}

	/**
	 * Взять треугольники
	 * 
	 * @return треугольники
	 */
	public int[][] getPolygons() {
		return polygonsShip;
	}

	/**
	 * Установить треугольники
	 * 
	 * @param polygons
	 *            треугольники
	 */
	public void setPolygons(int[][] polygons) {
		this.polygonsShip = polygons;
	}

	/**
	 * Взять нормали
	 * 
	 * @return нормали
	 */
	public int[][] getNormals() {
		return normalsShip;
	}

	/**
	 * Установить нормали
	 * 
	 * @param normals
	 *            нормали
	 */
	public void setNormals(int[][] normals) {
		this.normalsShip = normals;
	}

	/**
	 * Взять цвет корабля
	 * 
	 * @return цвет корабля
	 */
	public Color getColor() {
		return colorShip;
	}

	/**
	 * Установить цвет корабля
	 * 
	 * @param color
	 *            цвет корабля
	 */
	public void setColor(Color color) {
		this.colorShip = color;
	}

}
