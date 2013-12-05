/*This class contains base instruments to calculate and draw*/
package ua.edu.donntu.cs.draw.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import ua.edu.donntu.cs.draw.matrix_transform.MatrixTransform;
import ua.edu.donntu.cs.inout.IPrepareData;
import ua.edu.donntu.cs.service.transform_pixels_meters.MetMmTransform;

/**
 * Базовая пнель для отрисовки кораблей. Суперкласс
 *
 * @author Denis Vodolazskiy
 */
public class DrawShip extends JPanel implements IPrepareData {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // start corners
    protected float psi = 0;// -30;
    protected float teta = 0;// 30;
    protected float gamma = 0;
    protected int pr = 1;
    protected boolean Vis[] = new boolean[Q_POLYGONS];
    protected int polygonToFillX[][] = new int[Q_POLYGONS][3];
    protected int polygonToFillY[][] = new int[Q_POLYGONS][3];
    // start distances
    protected int distanceX = new MetMmTransform().meter2mm(-500);// 60000;
    protected int distanceY = 0;
    protected int distanceZ = 0;

    /**
     * Конструктор суперкласса отрисовки кораблей
     */
    public DrawShip() {
    }

    /**
     * Сокращённый println
     *
     * @param string строка для вывода
     */
    protected void p(Object string) {
        System.out.println(string);
    }

    // obtain visible polygons
    /**
     * Функции для определения видимых полигонов
     */
    protected void calc(int points[][], int polygons[][], int normals[][],
            int pr) {
        // if (distanceX>new MetPixTransform().meter2pixel(-1)){
        // if (distanceX>points[25][0]||distanceX>points[15][0]){
        // for (int i = 0; i < Q_POLYGONS; i++) {
        // Vis[i] = false;
        // }
        // }else{
        // matrix A B C
        double matrixA[][] = new double[3][3];
        MatrixTransform mt = new MatrixTransform();
        matrixA = mt.matr(psi, teta, gamma, 1);
        double matrixB[][] = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double matrixC[][] = new double[3][4];
        double pcs[] = {0, 0, 0};// вектор сцены 0,0,0

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrixC[i][j] = matrixA[i][0] * matrixB[j][0] + matrixA[i][1]
                        * matrixB[j][1] + matrixA[i][2] * matrixB[j][2];
            }
        }
        pcs[0] = pcs[0] - distanceX;
        pcs[1] = pcs[1] - distanceY;
        pcs[2] = pcs[2] - distanceZ;
        matrixC[0][3] = pcs[0] * matrixB[0][0] + pcs[1] * matrixB[1][0]
                + pcs[2] * matrixB[2][0];// 1350//150
        matrixC[1][3] = pcs[0] * matrixB[0][1] + pcs[1] * matrixB[1][1]
                + pcs[2] * matrixB[2][1];// -150
        matrixC[2][3] = pcs[0] * matrixB[0][2] + pcs[1] * matrixB[1][2]
                + pcs[2] * matrixB[2][2];// -150

        // observer coordinates
        double observerCoord[][] = new double[Q_POINTS][3];
        for (int i = 0; i < Q_POINTS; i++) {
            observerCoord[i][0] = points[i][0] * matrixC[0][0] + points[i][1]
                    * matrixC[0][1] + points[i][2] * matrixC[0][2]
                    + matrixC[0][3];
            observerCoord[i][1] = points[i][0] * matrixC[1][0] + points[i][1]
                    * matrixC[1][1] + points[i][2] * matrixC[1][2]
                    + matrixC[1][3];
            observerCoord[i][2] = points[i][0] * matrixC[2][0] + points[i][1]
                    * matrixC[2][1] + points[i][2] * matrixC[2][2]
                    + matrixC[2][3];
        }

        // 2d
        double to2d[][] = new double[Q_POINTS][2];
        for (int i = 0; i < Q_POINTS; i++) {
            to2d[i][0] = 0.6 * observerCoord[i][2] / observerCoord[i][0];//d0=0.6
            to2d[i][1] = 0.6 * observerCoord[i][1] / observerCoord[i][0];
        }
        // to2dmm
        double to2dmm[][] = new double[Q_POINTS][2];
        for (int i = 0; i < Q_POINTS; i++) {
            to2dmm[i][0] = 160 * to2d[i][0];//Xe=160mm, a0=1m; S=Xe/a0=160
            to2dmm[i][1] = 160 * to2d[i][1];//Ye=160mm, b0=1m; S=Ye/b0=160
        }
        // spX, spY
        int spX[] = new int[Q_POINTS];
        int spY[] = new int[Q_POINTS];
        for (int i = 0; i < Q_POINTS; i++) {
            spX[i] = (int) Math.round(4.35 * to2dmm[i][0]);//700/160=4.35
            spY[i] = (int) Math.round(4.35 * to2dmm[i][1]);//700/160=4.35
        }

        // xp,yp
        int xp[] = new int[Q_POINTS];
        int yp[] = new int[Q_POINTS];
        for (int i = 0; i < Q_POINTS; i++) {
            xp[i] = spX[i] + 350;
            yp[i] = -spY[i] + 350;
        }

        // Nvec obs
        double NormalObs[][] = new double[Q_POLYGONS][3];
        for (int i = 0; i < Q_POLYGONS; i++) {
            NormalObs[i][0] = normals[i][0] * matrixC[0][0] + normals[i][1]
                    * matrixC[0][1] + normals[i][2] * matrixC[0][2];
            NormalObs[i][1] = normals[i][0] * matrixC[1][0] + normals[i][1]
                    * matrixC[1][1] + normals[i][2] * matrixC[1][2];
            NormalObs[i][2] = normals[i][0] * matrixC[2][0] + normals[i][1]
                    * matrixC[2][1] + normals[i][2] * matrixC[2][2];
        }

        // vector V
        double V[][] = new double[Q_POINTS][3];
        V = observerCoord;

        // S
        double S[] = new double[Q_POLYGONS];
        for (int i = 0; i < Q_POLYGONS; i++) {
            S[i] = NormalObs[i][0] * V[polygons[i][0]][0] + NormalObs[i][1]
                    * V[polygons[i][0]][1] + NormalObs[i][2]
                    * V[polygons[i][0]][2];
        }

        // visibility
        for (int i = 0; i < Q_POLYGONS; i++) {
            if (S[i] < 0) {
                Vis[i] = true;
            } else {
                Vis[i] = false;
            }
        }

        // points to draw
        for (int i = 0; i < Q_POLYGONS; i++) {
            polygonToFillX[i][0] = xp[polygons[i][0]];
            polygonToFillX[i][1] = xp[polygons[i][1]];
            polygonToFillX[i][2] = xp[polygons[i][2]];

            polygonToFillY[i][0] = yp[polygons[i][0]];
            polygonToFillY[i][1] = yp[polygons[i][1]];
            polygonToFillY[i][2] = yp[polygons[i][2]];

        }
        // }
    }

    // Turn on antialiasing
    /**
     * Включение антиалиасинга
     */
    protected void setAntialiasing(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    // draw polygons in default color
    /**
     *
     * Выделяет три точки, разносит из координаты по массивам polygonToFillX и
     * polygonToFillY
     */
    protected void makeAllPolygons(Graphics2D g2) {
        for (int i = 0; i < Q_POLYGONS; i++) {
            if (Vis[i]) {
                makePolygon(g2, polygonToFillX[i], polygonToFillY[i], 3);
            }
        }
    }

    // draw polygons in set color
    /**
     *
     * Выделяет три точки, разносит из координаты по массивам polygonToFillX и
     * polygonToFillY, передаёт цвет
     */
    protected void makeAllPolygons(Graphics2D g2, Color col) {
        for (int i = 0; i < Q_POLYGONS; i++) {
            if (Vis[i]) {
                makePolygon(g2, polygonToFillX[i], polygonToFillY[i], 3, col);
            }
        }
    }

    // draw one polygon in default color
    /**
     * draw one polygon in default color
     * @param g2 графический контент
     * @param x x-координаты точек полигона
     * @param y y-координаты точек полигона
     * @param num количество вершин в полигоне
     */
    protected void makePolygon(Graphics2D g2, int x[], int y[], int num) {
        Color cFigure = new Color(0, 255, 0);
        Color cFill = new Color(255, 255, 255);
        g2.setPaint(cFigure);
        g2.drawPolygon(x, y, num);
        g2.setPaint(cFill);
        g2.fillPolygon(x, y, num);
    }

    // draw one polygon in set color
    /**
     * draw one polygon in set color
     * @param g2 графический контент
     * @param x x-координаты точек полигона
     * @param y y-координаты точек полигона
     * @param num количество вершин в полигоне
     * @param col цвет полигонов
     */
    protected void makePolygon(Graphics2D g2, int x[], int y[], int num,
            Color col) {
        g2.setPaint(col);
        g2.fillPolygon(x, y, num);
    }
}
