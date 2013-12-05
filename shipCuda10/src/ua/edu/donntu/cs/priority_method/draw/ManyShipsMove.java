/*This class allows to move group of ships*/
package ua.edu.donntu.cs.priority_method.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import ua.edu.donntu.cs.draw.information.InfoPrint;
import ua.edu.donntu.cs.draw.main.DrawShip;
import ua.edu.donntu.cs.draw.matrix_transform.MatrixTransform;
import ua.edu.donntu.cs.inout.IPrepareData;
import ua.edu.donntu.cs.key_manager.Craft;
import ua.edu.donntu.cs.priority_method.service.Left;
import ua.edu.donntu.cs.priority_method.service.Right;
import ua.edu.donntu.cs.priority_method.service.Ship;

/**
 * Панель для построения 9-ти объектов с помощью CPU. Используется метод
 * приоритетов
 *
 * @author Denis Vodolazskiy
 */
public class ManyShipsMove extends DrawShip implements IPrepareData,
        ActionListener {

    private static final long serialVersionUID = 1L;
    private ArrayList<Ship> ships;
    private ArrayList<Ship> sortedShips;
    private Timer timer;
    private Craft craft;

    /**
     * Конструктор, активирует слушателя и менеджера клавиатуры. Запускает
     * таймер.
     */
    public ManyShipsMove() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        // Color cFill = new Color(20, 80, 255);
        // setBackground(cFill);
        setDoubleBuffered(true);
        initShips();
        // sortShips();
        // init key_manager
        craft = new Craft(psi, teta, gamma, distanceX, distanceY, distanceZ);
        // init delay between key events
        timer = new Timer(5, this);
        timer.start();
    }

    // add displacement by axes X, Z
    /**
     * Задаёт смещение кораблей относительно центрального корабля
     *
     * @param a смещение по X
     * @param b смещение по Y
     * @return новый массив точек
     */
    protected int[][] initPoints(int a, int b) {
        int myPoints[][] = new int[Q_POINTS][3];
        for (int i = 0; i < Q_POINTS; i++) {
            myPoints[i][0] = points[i][0] + a;
            myPoints[i][1] = points[i][1];
            myPoints[i][2] = points[i][2] + b;
        }
        return myPoints;
    }

    // set position and color of every ship
    /**
     * Инициализация кораблей. Задаётся расположение и цвет
     */
    protected void initShips() {
        ships = new ArrayList<Ship>();

        ships.add(new Ship(initPoints(0, 0), polygons, normals, new Color(255,
                255, 255)));// initPoints(0,0)
        ships.add(new Ship(initPoints(0, 150000), polygons, normals, new Color(
                255, 255, 0)));
        ships.add(new Ship(initPoints(0, -150000), polygons, normals, new Color(
                255, 0, 255)));
        ships.add(new Ship(initPoints(150000, 0), polygons, normals, new Color(
                0, 255, 255)));
        ships.add(new Ship(initPoints(-150000, 0), polygons, normals, new Color(
                0, 0, 255)));
        ships.add(new Ship(initPoints(150000, 150000), polygons, normals,
                new Color(255, 0, 0)));
        ships.add(new Ship(initPoints(-150000, 150000), polygons, normals,
                new Color(255, 127, 127)));
        ships.add(new Ship(initPoints(-150000, -150000), polygons, normals,
                new Color(127, 255, 127)));
        ships.add(new Ship(initPoints(150000, -150000), polygons, normals,
                new Color(127, 127, 255)));
    }

    // sort ships according to tree
    /**
     * Сортировка кораблей в соответствии с методом приоритетов
     */
    protected void sortShips() {
        //
        double matrixA[][] = new double[3][3];
        MatrixTransform mt = new MatrixTransform();
        matrixA = mt.matr(psi, teta, gamma, 2);
        double matrixB[][] = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        // matrix D
        double matrixD[][] = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrixD[i][j] = matrixA[i][0] * matrixB[j][0] + matrixA[i][1]
                        * matrixB[j][1] + matrixA[i][2] * matrixB[j][2];
            }
        }
        //
        int node = 0;
        int pow = 3;
        int Vx, Vy, Vz;

        // lists of ships to draw (order)
        int list[][] = {{7, 4, 6, 2, 0, 1, 8, 3, 5},
            {6, 7, 4, 1, 2, 0, 5, 8, 3}, {6, 4, 7, 1, 0, 2, 5, 3, 8},
            {8, 7, 2, 3, 4, 0, 5, 6, 1}, {5, 1, 6, 8, 7, 2, 3, 4, 0},
            // { 5, 8, 3, 1, 2, 0, 6, 7, 4 },
            {5, 6, 1, 3, 4, 0, 8, 7, 2}, {8, 3, 5, 2, 0, 1, 7, 4, 6},
            {5, 8, 3, 1, 2, 0, 6, 7, 4}, {5, 3, 8, 1, 0, 2, 6, 4, 7}};
        Left left;
        Right right;

        // coordinates of control points
        int p[][] = {{0, 0, -10000}, {0, 0, 10000}, {10000, 0, 0},
            {-10000, 0, 0}};

        // normals for each point
        int N[][] = {{0, 0, -1}, {0, 0, 1}, {1, 0, 0}, {-1, 0, 0}};

        int observer[] = new int[3];// = { distanceX, distanceY, distanceZ };
        observer[0] = (int) (distanceX * matrixD[0][0] + distanceY
                * matrixD[0][1] + distanceZ * matrixD[0][2]);
        observer[1] = (int) (distanceX * matrixD[1][0] + distanceY
                * matrixD[1][1] + distanceZ * matrixD[1][2]);
        observer[2] = (int) (distanceX * matrixD[2][0] + distanceY
                * matrixD[2][1] + distanceZ * matrixD[2][2]);

        // number of nodes for each node
        int relationsOfNodes[] = {2, 1, 3, 1, 1, 0, 0, 0};

        // numbers and powers of left nodes
        int lNum[] = {1, 0, 3, 3, 6, 1, 7, 4};
        int lPow[] = {2, 0, 2, 0, 0, 0, 0, 0};
        left = new Left(lNum, lPow);

        // numbers and powers of right nodes
        int rNum[] = {2, 5, 4, 7, 6, 2, 8, 5};
        int rPow[] = {3, 1, 2, 1, 1, 0, 0, 0};
        right = new Right(rNum, rPow);
        // System.out.println("observer[0]= "+observer[0]);
        // System.out.println("observer[2]= "+observer[2]);

        // algorithm for searching list
        while (pow != 0) {
            Vx = p[relationsOfNodes[node]][0] - observer[0];
            Vy = p[relationsOfNodes[node]][1] - observer[1];
            Vz = p[relationsOfNodes[node]][2] - observer[2];
            int S = Vx * N[relationsOfNodes[node]][0] + Vy
                    * N[relationsOfNodes[node]][1] + Vz
                    * N[relationsOfNodes[node]][2];
            if (S < 0) {
                pow = left.getPowI(node);
                node = left.getNumI(node);
            } else {
                pow = right.getPowI(node);
                node = right.getNumI(node);
            }
            // System.out.println("node= "+node);
            // System.out.println("pow= "+pow);
        }
        int listShips[] = list[node];
        sortedShips = new ArrayList<Ship>();

        for (int i = 0; i < listShips.length; i++) {
            sortedShips.add(ships.get(listShips[i]));
            // System.out.println("listShips[i]= "+listShips[i]);
        }
    }

    /**
     * Выполняет перерисовку экрана. Корабли берутся последовательно из
     * отсортированного списка
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
        //
        // calc(points, polygons, normals);
        // makeAllPolygons(g2);
        sortShips();
        int pr = 1;
        for (int i = 0; i < sortedShips.size(); i++) {
            // for (int i = 0; i < 3; i++) {
            calc(sortedShips.get(i).getPoints(), sortedShips.get(i)
                    .getPolygons(), sortedShips.get(i).getNormals(), pr);
            makeAllPolygons(g2, sortedShips.get(i).getColor());
        }

        // draw information
        new InfoPrint(g2, craft);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        craft.move();
        repaint();

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
