package NeuralEvolution.UtilityClasses;

/**
 *
 * @author Sumner
 */
public class Movement {
    private double x,y,theta;
    /**
     * Position and motion tracker
     * @param x
     * @param y
     * @param theta in degrees
     */
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
        this.x = this.x + val*Math.cos(Math.PI*this.theta/180);
        this.y = this.y + val*Math.sin(Math.PI*this.theta/180);
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
    public double dist(Movement m){
        return dist(m.getX(),m.getY());
    }
    public double dist(double x, double y){
        return Math.sqrt(Math.pow(this.x - x,2) + Math.pow(this.y - y,2));
    }
    
}
