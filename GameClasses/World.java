package NeuralEvolution.GameClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.SpecificGameClasses.Map;
import NeuralEvolution.SpecificGameClasses.NeuralThreader;
import NeuralEvolution.UtilityClasses.Movement;
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
import java.util.Random;

public final class World implements Updatable, Drawable, KeyListener, MouseListener, ComponentListener { // The main world for objects to roam free

        private final Random r = new Random();
        private int XBOUND = 500;
        private int YBOUND = 500;
        
        private final int WINDOW_X;
        private final int WINDOW_Y;
    
	private final WorldController myWorldController;
        
        private boolean paused;
        private final boolean debug = true;
	private final Map myMap;
        private final ArrayList<Bact> bactAddArray;
        private final ArrayList<Bact> bactArray;
        private final ArrayList<Bact> bactRemoveArray;
        private final ArrayList<Bact> bactDrawAddArray;
        private final ArrayList<Bact> bactDrawArray;
        private final ArrayList<Bact> bactDrawRemoveArray;
	private int WindowWidth;
	private int WindowHeight;
        
        private int view_Xoffset;
        private int view_Yoffset;
        
        private Bact selBact;
        
        private final int MIN_POP = 10;
        
        // TODO grazing squares
    
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

            WINDOW_X = wc.getGameWindow().WIDTH;
            WINDOW_Y = wc.getGameWindow().HEIGHT;
            XBOUND = WINDOW_X;
            YBOUND = WINDOW_Y;
	}

	@Override
	public void draw(Graphics2D g) { // please keep in mind this can be multithreaded with the updates, disable if needed
            myMap.draw(g);  // myMap draws based on myMap.setViewPosition(x,y) last entry, make sure to update
            
// Draw selbact stuff first to avoid if statement inside loop
            if (selBact!=null){
                g.setColor(Color.RED);
                g.drawOval(selBact.getMov().getX()+view_Xoffset-10, selBact.getMov().getY()+view_Yoffset-10,20 ,20);
                // Anything else        
            }
            for (Bact b : bactDrawArray){
                /**
                 * If bacteria is at all visible, draw it, else don't
                 * tell the bacteria where to draw with: b.draw(g,xoffset,yoffset);
                 */
                if (true){
                    b.draw(g,this.view_Xoffset, this.view_Yoffset);
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
                if (this.selBact!=null){
                    g.drawString("Id:     "+selBact.getID(),0,50);
                    g.drawString("Hunger: "+selBact.getHunger(),0,70);
                    g.drawString("Health: "+selBact.getHealth(),0,90);
                }
            }
	}

        /* The four record holding longest living bacts */
        Bact[] longest = new Bact[4];
	@Override
	public void update() {
            if (!paused){
                NeuralThreader.updateNetworks(bactArray);
                for (Bact b : bactArray){
                    b.update();
                    Movement m = b.getMov();
                    if (m.getX()>XBOUND)
                        m.setX(XBOUND);
                    else if (m.getX()<0)
                        m.setX(0);
                    if (m.getY()>YBOUND)
                        m.setY(YBOUND);
                    else if (m.getY()<0)
                        m.setY(0);
                    if (!b.isAlive()){
                        bactRemoveArray.add(b);
                        bactDrawRemoveArray.add(b);
                    }
                    // Get some of the longest for repopulation
                    for (int i = 0; i<longest.length; i++){
                        if (longest[i]==null || b.getAge()>longest[i].getAge()){
                            longest[i] = b;
                            break;
                        }
                    }
                }
                this.bactArray.removeAll(this.bactRemoveArray);
                this.bactRemoveArray.clear();
                // TODO add dead bact remains
                this.bactArray.addAll(this.bactAddArray);
                this.bactAddArray.clear();
                // Repopulate
                if (bactArray.size()<MIN_POP){
                    for (Bact b : longest){
                        if (b!=null) {
                            Gene[] newGenes = b.getDNA();
                            Bact newb = new Bact(r.nextInt(XBOUND),r.nextInt(YBOUND),r.nextInt(360),newGenes);
                            System.out.println(newb.getMov().getX()+":"+newb.getMov().getY());
                            this.addBact(newb);
                        }
                    }
                    this.addBact(new Bact(r.nextInt(XBOUND),r.nextInt(YBOUND),r.nextInt(360)));
                }
            }
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
            double min_dist = bs[0].getMov().dist(x, y);
            for (Bact b : bs){
                double d = b.getMov().dist(x, y);
                if (d<=min_dist){
                    this.selBact = b;
                    min_dist = d;
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
