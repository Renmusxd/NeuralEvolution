package NeuralEvolution.UtilityClasses;

/**
 *
 * @author Sumner
 */
public class Movement {
    private double x,y,theta;
    private boolean cosCalc = false;
    private boolean sinCalc = false;
    private double cosTheta, sinTheta;
    /**
     * Position and motion tracker
     * @param x
     * @param y
     * @param theta in degrees
     */
    public Movement(double x, double y, double theta){
        this.x = x;
        this.y = y;
        this.theta = theta;
        cosCalc = sinCalc = false;
    }
    public double[] getVec(){
        return new double[]{cos(),sin()};
    }
    public double[] getPerpVec(){
        return new double[]{-sin(),cos()};
    }
    /**
     * Moves val distance in direction of theta
     * @param val 
     */
    public void forward(double val){
        this.x = this.x + val*cos();
        this.y = this.y + val*sin();
    }
    public Movement makeAhead(double theta, double val){
        double nx = this.x + val*cos();
        double ny = this.y + val*sin();
        return new Movement(nx,ny,0);
    }
    public void add(Movement m){
        this.x+=m.getX();
        this.y+=m.getY();
        this.theta+=m.getTheta();
        cosCalc = sinCalc = false;
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
        cosCalc = sinCalc = false;
    }
    public void addTheta(double val){
        this.theta += val;
        if (theta<0) theta += 360;
        if (theta>=360) theta -= 360;
        cosCalc = sinCalc = false;
    }
    public double dist(Movement m){
        return dist(m.getX(),m.getY());
    }
    public double dist(double x, double y){
        return Math.sqrt(Math.pow(this.x - x,2) + Math.pow(this.y - y,2));
    }
    
    /**
     * Saves value after calculation to speed up subsequent calcs
     * @return Math.cos(Math.PI*this.theta/180);
     */
    public double cos(){
        if (!cosCalc){
            cosCalc = true;
            cosTheta = Math.cos(Math.PI*this.theta/180);
        }
        return cosTheta;
    }
    /**
     * Saves value after calculation to speed up subsequent calcs
     * @return Math.sin(Math.PI*this.theta/180);
     */
    public double sin(){
        if (!sinCalc){
            sinCalc = true;
            sinTheta = Math.sin(Math.PI*this.theta/180);
        }
        return sinTheta;
    }
}
