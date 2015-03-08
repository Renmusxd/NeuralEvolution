package NeuralEvolution.GameClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.SpecificGameClasses.Map;
import NeuralEvolution.SpecificGameClasses.NeuralThreader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public final class World implements Updatable, Drawable, KeyListener, MouseListener, ComponentListener { // The main world for objects to roam free

	private WorldController myWorldController;
        
        private boolean paused;
        private boolean debug = true;
	private Map myMap;
        private ArrayList<Bact> bactAddArray;
        private ArrayList<Bact> bactArray;
        private ArrayList<Bact> bactRemoveArray;
        private ArrayList<Bact> bactDrawAddArray;
        private ArrayList<Bact> bactDrawArray;
        private ArrayList<Bact> bactDrawRemoveArray;
	private int WindowWidth;
	private int WindowHeight;
        
        private int view_Xoffset;
        private int view_Yoffset;
        
        private Bact selBact;
    
	public World(WorldController wc) {
            myWorldController = wc;
            myWorldController.getGameWindow().addKeyListener(this);
            myWorldController.getGameWindow().addMouseListener(this);
            myWorldController.getGameWindow().getFrame().addComponentListener(this);
            WindowWidth = myWorldController.getGameWindow().WIDTH;
            WindowHeight = myWorldController.getGameWindow().HEIGHT;

            myMap = new Map(0,0);
            bactAddArray = new ArrayList<>();
            bactArray = new ArrayList<>();
            bactRemoveArray = new ArrayList<>();
            bactDrawAddArray = new ArrayList<>();
            bactDrawArray = new ArrayList<>();
            bactDrawRemoveArray = new ArrayList<>();

            Bact b = new Bact(100,100,0);
            this.addBact(b);
            b = new Bact(200,200,0);
            this.addBact(b);
            System.out.println(b.toString());
            
	}

	@Override
	public void draw(Graphics2D g) { // please keep in mind this can be multithreaded with the updates, disable if needed
            myMap.draw(g);  // myMap draws based on myMap.setViewPosition(x,y) last entry, make sure to update
            for (Bact b : bactDrawArray){
                /**
                 * If bacteria is at all visible, draw it, else don't
                 * tell the bacteria where to draw with: b.draw(g,xoffset,yoffset);
                 */
                if (true){
                    b.draw(g,this.view_Xoffset, this.view_Yoffset);
                    if (b.equals(selBact)){
                        g.drawOval(b.getMov().getX()+view_Xoffset-10, b.getMov().getY()+view_Yoffset-10,20 ,20);
                    }
                }
            }
            this.bactDrawArray.addAll(this.bactDrawAddArray);
            this.bactDrawAddArray.clear();
            this.bactDrawArray.removeAll(this.bactDrawRemoveArray);
            this.bactDrawRemoveArray.clear();
            if (this.debug){
                g.setColor(Color.black);
                g.drawString("update fps: "+Math.round(this.myWorldController.u_fps), 0, 10);
                g.drawString("graphics fps: "+Math.round(this.myWorldController.g_fps), 0, 30);
                if (this.paused){
                        g.setColor(Color.red);
                        g.drawString("PAUSED", WindowWidth - 50, 10);
                }
            }
	}

	@Override
	public void update() {
            NeuralThreader.updateNetworks(bactArray);
            for (Bact b : bactArray){
                b.update();
                if (!b.isAlive()){
                    bactRemoveArray.add(b);
                    bactDrawRemoveArray.add(b);
                }
            }
            // TODO add dead bact remains
            this.bactArray.addAll(this.bactAddArray);
            this.bactAddArray.clear();
            this.bactArray.removeAll(this.bactRemoveArray);
            this.bactRemoveArray.clear();
	}
	
        public void addBact(Bact b){
            this.bactAddArray.add(b);
            this.bactDrawAddArray.add(b);
        }
        public void removeBact(Bact b){
            this.bactRemoveArray.add(b);
            this.bactDrawRemoveArray.add(b);
        }
        
	// -------------------------------------------------------------------- KEYBOARD, COMPONENT, AND MOUSE EVENTS -------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            x = x - this.view_Xoffset;
            y = y - this.view_Yoffset;
            Bact[] bs = bactArray.toArray(new Bact[0]);
            int min_x = Math.abs(bs[0].getMov().getX()-x);
            int min_y = Math.abs(bs[0].getMov().getY()-y);
            for (Bact b : bs){
                if (Math.abs(b.getMov().getX()-x)<=min_x &&
                        Math.abs(b.getMov().getY()-y)<=min_y){
                    this.selBact = b;
                }
            }
        }
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void keyPressed(KeyEvent arg0) {
		//System.out.println(">"+arg0.getKeyChar()+"<");
		if (arg0.getKeyChar()==' '){this.paused = !this.paused;}
                // TODO Update map motion
                else if (arg0.getKeyChar()=='w'){}
                else if (arg0.getKeyChar()=='a'){}
                else if (arg0.getKeyChar()=='s'){}
                else if (arg0.getKeyChar()=='d'){}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void componentHidden(ComponentEvent arg0) {}
	@Override
	public void componentMoved(ComponentEvent arg0) {}
	@Override
	public void componentResized(ComponentEvent arg0) {
		Insets my_in = myWorldController.getGameWindow().getFrame().getInsets();
		this.WindowHeight = myWorldController.getGameWindow().getFrame().getHeight() - (my_in.top + my_in.bottom);
		this.WindowWidth = myWorldController.getGameWindow().getFrame().getWidth() - (my_in.left + my_in.right);
	}
	@Override
	public void componentShown(ComponentEvent arg0) {}
	
	// -------------------------------------------------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
	
	public int getWindowHeight(){return this.WindowHeight;}
	public int getWindowWidth(){return this.WindowWidth;}
}
