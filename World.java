package neuralevolution.NeuralEvolution;

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

public class World implements Updatable, Drawable, KeyListener, MouseListener, ComponentListener { // The main world for objects to roam free

	private WorldController myWorldController;
        
        private boolean paused;
        private boolean debug = true;
	private Map myMap;
        private ArrayList<Bact> bactArray;
        private ArrayList<Bact> bactRemoveArray;
        
	private int WindowWidth;
	private int WindowHeight;
        
        private int view_Xoffset;
        private int view_Yoffset;
    
	
        @SuppressWarnings("LeakingThisInConstructor")
	public World(WorldController wc) {
		myWorldController = wc;
		myWorldController.getGameWindow().addKeyListener(this);
		myWorldController.getGameWindow().addMouseListener(this);
		myWorldController.getGameWindow().getFrame().addComponentListener(this);
		WindowWidth = myWorldController.getGameWindow().WIDTH;
		WindowHeight = myWorldController.getGameWindow().HEIGHT;
                
                bactArray = new ArrayList<Bact>();
                bactRemoveArray = new ArrayList<Bact>();
	}

	@Override
	public void draw(Graphics2D g) { // please keep in mind this can be multithreaded with the updates, disable if needed
		if (this.debug){
                    g.setColor(Color.black);
                    g.drawString("update fps: "+Math.round(this.myWorldController.u_fps), 0, 10);
                    g.drawString("graphics fps: "+Math.round(this.myWorldController.g_fps), 0, 30);
                    if (this.myWorldController.paused){
                            g.setColor(Color.red);
                            g.drawString("PAUSED", WindowWidth - 50, 10);
                    }
                }
                myMap.draw(g);  // myMap draws based on myMap.setViewPosition(x,y) last entry, make sure to update
                for (Bact b : bactArray){
                    /**
                     * If bacteria is at all visible, draw it, else don't
                     * tell the bacteria where to draw with: b.draw(g,xoffset,yoffset);
                     */
                    if (true){
                        b.draw(g,this.view_Xoffset, this.view_Yoffset);
                    }
                }
	}

	@Override
	public void update() { // please keep in mind this can be multithreaded with the graphics, disable if needed
                for (Bact b : bactArray){
                    b.update();
                    if (!b.isAlive()){bactRemoveArray.add(b);}
                }
                bactArray.removeAll(this.bactRemoveArray);
                this.bactRemoveArray.clear();
	}
	
        public void addBact(Bact b){}
        public void removeBact(Bact b){}
        
	// -------------------------------------------------------------------- KEYBOARD, COMPONENT, AND MOUSE EVENTS -------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e) {}
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
