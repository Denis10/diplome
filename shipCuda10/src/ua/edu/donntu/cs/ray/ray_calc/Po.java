/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.edu.donntu.cs.ray.ray_calc;

/**
 * Служебный класс для создания точки наблюдателя в методе трассировки лучей.
 * 
 * @author Denis Vodolazskiy
 */
public class Po {
    private int x;
    private int y;
    private int z;
    private float psi;
    private float teta;
    private float gamma;

    public Po(int x, int y, int z, float psi, float teta, float gamma) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.psi = psi;
        this.teta = teta;
        this.gamma = gamma;
    }

    public float getPsi() {
        return psi;
    }

    public float getTeta() {
        return teta;
    }

    public float getGamma() {
        return gamma;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
}
