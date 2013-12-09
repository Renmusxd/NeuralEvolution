/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.UtilityClasses;

/**
 *
 * @author Sumner
 */
public class Movement {
    private int x,y,theta;
    public Movement(int x, int y, int theta){
        this.x = x;
        this.y = y;
        this.theta = theta;
    }
    public void add(Movement m){
        this.x+=m.getX();
        this.y+=m.getY();
        this.theta+=m.getTheta();
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }
    
}
