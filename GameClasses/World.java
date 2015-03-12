package NeuralEvolution.GameClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.SpecificGameClasses.Map;
import NeuralEvolution.SpecificGameClasses.Mutator;
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
        
        private final int MIN_POP = 20;
        
        public static final int food_size = 10;
        private final int[][] grazingsquares;
        public static final int default_grass = 100;
        
        private final int[][] meatsquares;
    
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
            
            grazingsquares = new int[XBOUND/food_size][YBOUND/food_size];
            meatsquares = new int[XBOUND/food_size][YBOUND/food_size];
            for (int i = 0; i<grazingsquares.length; i++)
                for (int j = 0; j<grazingsquares.length; j++)
                    grazingsquares[i][j] = default_grass;
	}
        
        Incrementor leftText = new Incrementor(10,10);
        Incrementor rightText = new Incrementor(10,10);
	@Override
	public void draw(Graphics2D g) { // please keep in mind this can be multithreaded with the updates, disable if needed
            //myMap.draw(g);  // myMap draws based on myMap.setViewPosition(x,y) last entry, make sure to update
            // Draw selbact stuff first to avoid if statement inside loop
            double alpha = 0;
            for (int j = 0; j<grazingsquares[0].length; j++)
                for (int i = 0; i<grazingsquares.length; i++){
                    alpha = grazingsquares[i][j]/(double)default_grass;
                    g.setColor(new Color(
                            (int)(155 + alpha * (55-155)),
                            (int)(118 + alpha*(125-118)),
                            (int)(83 + alpha*(55-83)))
                    );
                    g.fillRect(i*food_size+view_Xoffset, j*food_size-view_Yoffset, food_size, food_size);
                    if (meatsquares[i][j]>0){
                        g.setColor(Color.PINK);
                        g.fillOval(i*food_size+view_Xoffset+food_size/4, j*food_size-view_Yoffset+food_size/4, food_size/2, food_size/2);
                    }
            }
            
            for (Bact b : longest){
                if (b!=null && b.isAlive()){
                    g.setColor(Color.YELLOW);
                    g.drawOval(b.getMov().getX()+view_Xoffset-10, b.getMov().getY()+view_Yoffset-10,20 ,20);
                    break;
                }
            }
            
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
                leftText.clear();
                rightText.clear();
                g.setColor(Color.black);
                g.drawString("update fps: "+Math.round(this.myWorldController.u_fps), 0, leftText.getInc());
                g.drawString("graphics fps: "+Math.round(this.myWorldController.g_fps), 0, leftText.getInc());
                if (this.paused){
                        g.setColor(Color.red);
                        g.drawString("PAUSED", WindowWidth/2, 10);
                }
                g.drawString("Pop:    "+bactArray.size(), 0, leftText.getInc());
                if (this.selBact!=null){
                    g.drawString("Id:     "+selBact.getID(),0,leftText.getInc());
                    g.drawString("Parent: "+selBact.getParent(),0,leftText.getInc());
                    g.drawString("Age:    "+selBact.getAge(), 0, leftText.getInc());
                    g.drawString("Hunger: "+selBact.getHunger(),0,leftText.getInc());
                    g.drawString("Health: "+selBact.getHealth(),0,leftText.getInc());
                    g.drawString("Metab:  "+selBact.getMetabolism(),0,leftText.getInc());
                    g.drawString("Speed:  "+(Math.floor((selBact.getMovSpeed()*10))/10.0),0,leftText.getInc());
                }
                for (Bact b : longest){
                    if (b!=null){
                        g.drawString("Id:  "+b.getID(), this.WindowWidth-70, rightText.getInc());
                        g.drawString("Age: "+b.getAge(),this.WindowWidth-70, rightText.getInc());
                    }
                }
            }
	}

        /* The four record holding longest living bacts */
        Bact[] longest = new Bact[3];
	@Override
	public void update() {
            if (!paused){
                for (int j = 0; j<grazingsquares[0].length; j++){
                    for (int i = 0; i<grazingsquares.length; i++){
                        this.grazingsquares[i][j] = (grazingsquares[i][j]>=default_grass)?
                                default_grass : grazingsquares[i][j]+1;
                        this.meatsquares[i][j] = (meatsquares[i][j]>0)?
                                meatsquares[i][j]-1 : 0;
                    }
                }
                NeuralThreader.updateNetworks(bactArray);
                
                for (int ib = 0; ib<bactArray.size(); ib++){
                    Bact b = bactArray.get(ib);
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
                        if (longest[i]==null || b.getAge()>=longest[i].getAge()){
                            // TODO fix
                            for (int j = longest.length-1; j>i; j--){
                                longest[j] = longest[j-1];
                            }
                            longest[i] = b;
                            break;
                        }
                    }
                    // Alert of others
                    for (int jb = ib+1; jb<bactArray.size(); jb++ ){
                        Bact nb = bactArray.get(jb);
                        nb.alertOfPresence(b);
                        b.alertOfPresence(nb);
                    }
                }
                for (Bact b : bactRemoveArray){
                    int x = b.getMov().getX() / food_size;
                    int y = b.getMov().getY() / food_size;
                    if (!(x<0 || x>meatsquares.length || y<0 || y>meatsquares.length))
                        this.meatsquares[x][y] = b.getHunger()+b.getHealth();
                }
                this.bactArray.removeAll(this.bactRemoveArray);
                this.bactRemoveArray.clear();
                this.bactArray.addAll(this.bactAddArray);
                this.bactAddArray.clear();
                // Repopulate
                if (bactArray.size()<MIN_POP){
                    int before = bactAddArray.size();
                    for (Bact b : longest){
                        if (b!=null) {
                            Gene[] newGenes = b.getDNA();
                            Gene[] mutnewGenes = Mutator.swap(newGenes, 2.0/newGenes.length);
                            mutnewGenes = Mutator.changeBase(mutnewGenes, 2.0/Bact.dnalength(newGenes));
                            int b1x = r.nextInt(XBOUND-40)+20;
                            int b1y = r.nextInt(YBOUND-40)+20;
                            int b2x = b1x + r.nextInt(40)-20;
                            int b2y = b1y + r.nextInt(40)-20;
                            int b3x = r.nextInt(XBOUND-40)+20;
                            int b3y = r.nextInt(YBOUND-40)+20;
                            int b4x = b3x + r.nextInt(40)-20;
                            int b4y = b3y + r.nextInt(40)-20;
                            Bact newb1 = new Bact(this,b1x,b1y,r.nextInt(360),newGenes,b.getID());
                            Bact newb2 = new Bact(this,b2x,b2y,r.nextInt(360),newGenes,b.getID());
                            Bact newb3 = new Bact(this,b3x,b3y,r.nextInt(360),mutnewGenes,b.getID());
                            Bact newb4 = new Bact(this,b4x,b4y,r.nextInt(360),mutnewGenes,b.getID());
                            selBact = newb4;
                            this.addBact(newb1);this.addBact(newb2);this.addBact(newb3);this.addBact(newb4);
                        }
                    }
                    int rb1x = r.nextInt(XBOUND-40)+20;
                    int rb1y = r.nextInt(YBOUND-40)+20;
                    int rb2x = rb1x + r.nextInt(40)-20;
                    int rb2y = rb1y + r.nextInt(40)-20;
                    int rb3x = r.nextInt(XBOUND-40)+20;
                    int rb3y = r.nextInt(YBOUND-40)+20;
                    int rb4x = rb3x + r.nextInt(40)-20;
                    int rb4y = rb3y + r.nextInt(40)-20;
                    Bact randB1 = new Bact(this,rb1x,rb1y,r.nextInt(360),-1);
                    Bact randB2 = new Bact(this,rb2x,rb2y,r.nextInt(360),randB1.getDNA(),-1);
                    Bact randB3 = new Bact(this,rb3x,rb3y,r.nextInt(360),-1);
                    Bact randB4 = new Bact(this,rb4x,rb4y,r.nextInt(360),randB3.getDNA(),-1);
                    this.addBact(randB1); this.addBact(randB2); this.addBact(randB3); this.addBact(randB4);
                    System.out.println("\tAdded "+(bactAddArray.size()-before)+" bacts");
                }
                if (selBact==null || !selBact.isAlive()){
                    selBact=null;
                    for (int i=0; i<longest.length; i++){
                        if (longest[i]!=null){
                            selBact=longest[i];
                            break;
                        }
                    }
                        
                }
            }
	}
        public Bact getSelBact(){
            return selBact;
        }
        
        public int getGrass(int x, int y){
            if (x/food_size >= grazingsquares.length || y/food_size >= grazingsquares.length)
                return 0;
            if (x/food_size < 0 || y/food_size < 0)
                return 0;
            return grazingsquares[x/food_size][y/food_size];
        }
        public int getMeat(int x, int y){
            if (x/food_size >= meatsquares.length || y/food_size >= meatsquares.length)
                return 0;
            if (x/food_size < 0 || y/food_size < 0)
                return 0;
            return meatsquares[x/food_size][y/food_size];
        }
        
        public int eatGrass(int x,int y, int maxamount){
            int newx = x/food_size;
            int newy = y/food_size;
            if (grazingsquares[newx][newy]>maxamount){
                grazingsquares[newx][newy] -= maxamount;
                return maxamount;
            } else {
                int t = grazingsquares[newx][newy];
                grazingsquares[newx][newy] = 0;
                return t;
            }
        }
        public int eatMeat(int x,int y, int maxamount){
            int newx = x/food_size;
            int newy = y/food_size;
            if (meatsquares[newx][newy]>maxamount){
                meatsquares[newx][newy] -= maxamount;
                return maxamount;
            } else {
                int t = meatsquares[newx][newy];
                meatsquares[newx][newy] = 0;
                return t;
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
        
        public void replBact(int id,int starthung, int x, int y, Gene[] dna){
            Bact b = new Bact(this,x+r.nextInt(40)-20,y+r.nextInt(40)-20,
                    r.nextInt(360),dna,id,starthung);
            this.addBact(b);
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
                else if (arg0.getKeyChar()=='r'){
                    this.bactDrawRemoveArray.addAll(this.bactArray);
                    this.bactRemoveArray.addAll(this.bactArray);
                    for (int i = 0; i<longest.length; i++)
                        longest[i] = null;
                }
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
        
        // -------------------------------------------------------------------- PRIVATE CLASSES -----------------------------------------------------------------------
        private class Incrementor{
            int base = 0;
            int val = 0;
            int i = 0;
            public Incrementor(int base,int amount){
                this.base = base;
                this.val = base;
                this.i = amount;
            }
            public int getInc(){
                int t = val;
                val += i;
                return t;
            }
            public void clear(){
                val = base;
            }
        }
}
