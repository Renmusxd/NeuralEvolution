package NeuralEvolution.UtilityClasses;

/**
 *
 * @author Sumner
 */
public class Movement {
    private double x,y,theta;
    public Movement(int x, int y, int theta){
        this.x = x;
        this.y = y;
        this.theta = theta;
    }
    /**
     * Moves val distance in direction of theta
     * @param val 
     */
    public void forward(double val){
        
    }
    public void add(Movement m){
        this.x+=m.getX();
        this.y+=m.getY();
        this.theta+=m.getTheta();
    }
    public int getX() {
        return (int)Math.round(x);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int)Math.round(y);
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
    public void addTheta(double val){
        this.theta += val;
    }
    
}
