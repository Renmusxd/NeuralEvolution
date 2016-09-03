package NeuralEvolution.BodyClasses;

import NeuralEvolution.GameClasses.World;
import NeuralEvolution.NeuronClasses.BactBrain;
import NeuralEvolution.NeuronClasses.BrainNode;
import NeuralEvolution.NeuronClasses.NeuralTree.NeuralTreeManager;
import NeuralEvolution.NeuronClasses.NeuralTree.NeuralTreeNode;
import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.SpecificGameClasses.Mutator;
import NeuralEvolution.UtilityClasses.Movement;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @author Sumner
 */
public class Bact {
    /*
     * TODO get sexual reproduction integrated, we'll start with asexual.
     */
    public static final String  ALPHABET            = "ABCD";
    // TODO make fix gene length, eat extra
    public static final char    TRAIT_PRIMER        = 'A';
    public static final byte    TRAIT_LENGTH        = 8; // [XX][XXXX] = [OP]-[VAL]
    public static final char    NEURON_PRIMER       = 'B';
    public static final byte    NEURON_LENGTH       = 8; // [XXXX][xxXX] = [ID]-[OP]
    public static final char    NPATH_PRIMER        = 'C';
    public static final byte    NPATH_LENGTH        = 8; // [XXXX][XXXX] = [ID]-[ID]
    public static final char    SPECIAL_PRIMER      = 'D';
    public final static int     DEFAULT_DNA_LENGTH  = 2000;
    
    public static int ID_GLOBAL = 1;
    private int id;
    
    private int parent;
    
    public final static int MAX_AGE = 10000;
    
    public final static int MAX_HUNGER = 2400; // Two minutes at 1hunger/tick
    public final static int MAX_HEALTH = 100;
    
    public final static int MAX_REWARD = 15;
    
    private World w;
    
    private Gene[] DNA;
    
    private BactBrain brain;
    //Traits
    private int mutationFrequency,
                speed,
                mateReward,
                eatGReward,
                eatMReward,
                sightVal;
    // Calculated values
    private double  mov_speed,
                    mutationRate;
    private int     metabolism,
                    mateRewardAmount,
                    eatGRewardAmount,
                    eatMRewardAmount,
                    sightRange,       // Square sight distance
                    red,
                    blue,
                    green;
        
    // Physical Variables
    private Movement pos;
    private int size = 10;
    private final int drawSize = size;
    private Color drawColor;
    
    private int hunger,
                health;
    
    private int age = 0;
    
    // Nodes to listen to.
    private BrainNode  leftWheel,
                        rightWheel,
                        mouthExtend,
                        eatG,
                        eatM,
                        replicate;
    // For sight and touch
    // 0,0 is in front and to the left of the bact
    // 1,0 is directly in front
    // 2,1 is in front and to the right
    //...
    private final BactInt[][] closests = new BactInt[3][3];
    private BrainNode[][][] sight; // [0->red 1->green 2->blue][x][y]
    private BrainNode[][]  touch;
    private BrainNode   hungerNode,
                        healthNode,
                        lastLWheel,
                        lastRWheel,
                        lastMouthX,
                        lastEatG,
                        lastEatM,
                        lastReplicate;
    
    private int lastThingSaid;
    private int agesaid;
    
    // Score Keeping
    private int meat_eaten;
    private int bacts_killed;
    private boolean[][] blockmap;
    private int blocks_explored;
    
    public Bact(World w, int x, int y, int theta, Gene[] g,int parent,int initialhunger){
        this(w, x,y,theta,g,parent);
        hunger = initialhunger;
    }
    public Bact(World w, int x, int y, int theta, int parent){
        this(w, x,y,theta,Bact.randomDNA(Bact.DEFAULT_DNA_LENGTH),parent);
    }
    public Bact(World w, int x, int y, int theta,Gene[] DNA, int parent){
        this.id = ++ID_GLOBAL;
        this.parent = parent;
        this.DNA = DNA;
        this.w = w;
        this.parseDNA(DNA);
        brain = new NeuralTreeManager(this);
        pos = new Movement(x,y,theta);
        
        hunger = MAX_HUNGER;
        health = MAX_HEALTH;
        
        // Now add nodes for motion and stuff
            leftWheel = new NeuralTreeNode("LW",true,false);
            rightWheel = new NeuralTreeNode("RW",true,false);
            mouthExtend = new NeuralTreeNode("M",true,false);
            eatG = new NeuralTreeNode("Eg",true,false);
            eatM = new NeuralTreeNode("Em",true,false);
            replicate = new NeuralTreeNode("R",true,false);

            brain.registerNode(leftWheel);
            brain.registerNode(rightWheel);
            brain.registerNode(mouthExtend);
            brain.registerNode(eatG);
            brain.registerNode(eatM);
            brain.registerNode(replicate);
        
        // Add input nodes
            // Always on
            NeuralTreeNode on = new NeuralTreeNode("On",1);
            NeuralTreeNode maxhung = new NeuralTreeNode("MHu",MAX_HUNGER);
            NeuralTreeNode maxhealth = new NeuralTreeNode("MHe",MAX_HEALTH);
            brain.registerInputNode(on);
            brain.registerInputNode(maxhung);
            brain.registerInputNode(maxhealth);
            // Sight
            sight = new NeuralTreeNode[3][3][3];
            char[] cs = "RGB".toCharArray();
            for (int c=0; c<3; c++){
                for (int i=0; i<3; i++){
                    for (int j=0; j<3; j++) {
                        NeuralTreeNode n = new NeuralTreeNode("I"+cs[c]+""+i+""+j,false);
                        sight[c][i][j] = n;
                        brain.registerInputNode(n);
                    }
                }
            }
            // Tactile
            touch = new NeuralTreeNode[3][3];
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++) {
                    NeuralTreeNode n = new NeuralTreeNode("T"+i+""+j,false);
                    touch[i][j] = n;
                    brain.registerInputNode(n);
                }
            }
            // Hunger and Health
            hungerNode = new NeuralTreeNode("Hunger",false,false);
            healthNode = new NeuralTreeNode("Health",false,false);
            brain.registerInputNode(hungerNode);
            brain.registerInputNode(healthNode);
            // Past output
            lastLWheel = new NeuralTreeNode("LastLW",false,false);
            lastRWheel = new NeuralTreeNode("LastRW",false,false);
            lastMouthX = new NeuralTreeNode("LastMX",false,false);
            lastEatG = new NeuralTreeNode("LastEatG",false,false);
            lastEatM = new NeuralTreeNode("LastEatM",false,false);
            lastReplicate = new NeuralTreeNode("LastRepl",false,false);
            brain.registerInputNode(lastLWheel);
            brain.registerInputNode(lastRWheel);
            brain.registerInputNode(lastMouthX);
            brain.registerInputNode(lastEatG);
            brain.registerInputNode(lastEatM);
            brain.registerInputNode(lastReplicate);
        // Now add stuff from DNA
        brain.parseDNA(DNA);
        
        // Scorekeeping
        blockmap = new boolean[w.getBlockWidth()][w.getBlockHeight()]; // All false
    }
    
    public class BactInt{
        public Bact bact; public int dist2;
        /**
         * Holds a Bact and its squared distance
         * @param b Bact
         * @param d distance squared from host
         */
        public BactInt(Bact b, int d){bact=b;dist2=d;}
    }
    /**
     * Used to make more efficient collision detection O(nlogn) instead of O(n^2)
     * Report bact within distance
     * @param b - bact within range
     */
    public void interact(Bact b){
        if (this.equals(b)) return;
        // TODO alertbacts of others with sight
        int x = pos.getX();
        int y = pos.getY();
        int bx = b.getMov().getX();
        int by = b.getMov().getY();
        
        double[] forwardVec = pos.getVec();
        double[] perpVec = pos.getPerpVec();
        
        double perp_dist =    (bx-x)*perpVec[0] + (by-y)*perpVec[1];
        double forw_dist = (bx-x)*forwardVec[0] + (by-y)*forwardVec[1];
        
        int perp_dir = (Math.abs(perp_dist)<=size/2)?
                0:(int)Math.signum(perp_dist);
        int for_dir = (Math.abs(forw_dist)<=size/2)?
                0:(int)Math.signum(forw_dist);
        
        int close_for_dir = (Math.abs(forw_dist)<=size/4)?
                0:(int)Math.signum(forw_dist);
        
        for (int i = 0; i<closests.length; i++){
            for (int j = 0; j<closests.length; j++) {
                if (i == 1+perp_dir && j==1-for_dir){
                    // Sight
                    int d2 = (int)(Math.pow(bx-x,2)+Math.pow(by-y,2));
                    boolean proceed = (closests[i][j]==null || closests[i][j].dist2>=d2);
                    if (d2 <= this.sightRange && proceed){
                        this.closests[i][j] = new BactInt(b,d2);
                        this.sight[0][i][j].setState(b.getRed());
                        this.sight[1][i][j].setState(b.getGreen());
                        this.sight[2][i][j].setState(b.getBlue());
                    }
                    // Touch and Damage
                    
                    if (d2<=Math.pow(this.size,2)){
                        touch[i][j].setState(1);
                        if (mouthExtend.getState()>0 &&
                                1+perp_dir==1 && 1-close_for_dir>=0){
                            System.out.println("Attacking!");
                            boolean killed = b.inflictDamage(100);
                            if (killed)
                                this.bacts_killed++;
                        }
                    }
                }
            }
        }
    }
    
    public void clearInteractions(){
        for (int i=0; i<sight.length; i++){
            Arrays.fill(closests[i],0,closests[i].length,null);
            for (int j=0; j<sight.length; j++){
                touch[i][j].setState(0);
                sight[0][i][j].setState(0);
                sight[1][i][j].setState(0);
                sight[2][i][j].setState(0);
            }
        }
    }
    
    public void seeFood(){
        // Sight of Grass, Bacts only see the green of grass from 0 to 255
        final int rotNum = (int)Math.round((pos.getTheta()+292.5)/45);
        final int[][] rot = {{0,0},{0,1},{0,2},{1,2},{2,2},{2,1},{2,0},{1,0}};
        // TODO fix
        this.sight[1][1][1].setState(
                255*w.getGrass(pos.getX(), pos.getY())/World.default_grass
            );
        if (w.getMeat(pos.getX(), pos.getY())>0)
            this.sight[0][1][1].setState(255);
        for (int i = 0; i<rot.length; i++){
            int ix = rot[i][0];
            int iy = rot[i][1];
            int jx = rot[(i+rotNum)%rot.length][0];
            int jy = rot[(i+rotNum)%rot.length][1];
            int lookx = pos.getX() + (jx-1)*World.food_size;
            int looky = pos.getY() + (jy-1)*World.food_size;
            // TODO fix issue where this overwrites other vision and vice versa
            // This is because this is updating for next time, and the inter bact
            // vision is done possibly before and possibly after this update cycle
            // Need to clear somehow
            this.sight[1][ix][iy].setState(
                    255*w.getGrass(lookx, looky)/World.default_grass
                );
            if (w.getMeat(lookx, looky)>0)
                this.sight[0][ix][iy].setState(255);
        }
    }
    
    public boolean inflictDamage(int amount){
      // TODO damage
        health -= amount;
        if (health<=0){
            health = 0;
            return true;
        }
        return false;
    }
    
    private boolean mapupdated = false;
    public void updateNeurons(){
        if (!mapupdated){
            brain.update();
            mapupdated = true;
        }
    }
    /**
     * Updates the Bact, updates the neural network if it hasn't been done since
     * the last update call.
     */
    public void update(){
        age++;
        // Check what the neural network is up to if it hasn't been done
        if (!mapupdated){
            brain.update();
        } else {
            mapupdated = false;
        }     
        // Update from bottom (CHEATING);
//        leftWheel.update(); rightWheel.update(); 
//        eatG.update(); eatM.update();
//        replicate.update(); mouthExtend.update();
        
        // Check outputs and update stuff
        int leftWheelVal = leftWheel.getState();
        int rightWheelVal = rightWheel.getState();
        int eatGState = eatG.getState();
        int eatMState = eatM.getState();
        // Update position
        if (leftWheelVal>0 && rightWheelVal>0){
            pos.forward(mov_speed);
        } else if (leftWheelVal<0 && rightWheelVal<0){
            pos.forward(-mov_speed);
        } else if (leftWheelVal>0 && rightWheelVal==0){
            pos.forward(mov_speed/2.0);
            pos.addTheta(2);
        } else if (leftWheelVal<0 && rightWheelVal==0){
            pos.forward(-mov_speed/2.0);
            pos.addTheta(-2);
        } else if (leftWheelVal==0 && rightWheelVal>0){
            pos.forward(mov_speed/2.0);
            pos.addTheta(-2);
        } else if (leftWheelVal==0 && rightWheelVal<0){
            pos.forward(-mov_speed/2.0);
            pos.addTheta(2);
        } else if (leftWheelVal>0 && rightWheelVal<0){
            pos.addTheta(4);
        } else if (rightWheelVal>0 && leftWheelVal<0){
            pos.addTheta(-4);
        }
        
        // Eat food
        if (eatGState>0){
            int toeat = Math.min(eatGState, MAX_HUNGER-hunger);
            int eaten = w.eatGrass(pos.getX(), pos.getY(), toeat);
            hunger += eaten;
            if (eaten!=0)
                brain.reward(eatGRewardAmount);
        }
        if (eatMState>0){
            int toeat = Math.min(eatGState, MAX_HUNGER-hunger);
            int eaten = w.eatMeat(pos.getX(), pos.getY(), toeat);
            hunger += eaten;
            if (eaten!=0)
                brain.reward(eatMRewardAmount);
            this.meat_eaten++;
        }
        if (hunger>MAX_HUNGER) hunger = MAX_HUNGER;
        // Hunger and Health
        this.hunger -= this.metabolism;
        if (health<MAX_HEALTH && health>0) this.health += 1;
        
        // Set node states for next time
        hungerNode.setState(this.hunger);
        healthNode.setState(this.health);
        
        lastLWheel.setState(leftWheel.getState());
        lastRWheel.setState(rightWheel.getState());
        lastMouthX.setState(mouthExtend.getState());
        lastEatG.setState(eatG.getState());
        lastEatM.setState(eatM.getState());
        lastReplicate.setState(replicate.getState());
        
        // Replicate
        if (replicate.getState()>0 && hunger>MAX_HEALTH+2*replicate.getState()){
            
            Gene[] newDNA = Mutator.swap(DNA, this.mutationRate);
            newDNA = Mutator.changeBase(newDNA, this.mutationRate);
            w.replBact(id,replicate.getState(),pos.getX(),pos.getY(),newDNA);
            hunger -= MAX_HEALTH+replicate.getState();
        }
        int[] blockpos = w.getBlockPos(pos.getX(), pos.getY());
        if (!blockmap[blockpos[0]][blockpos[1]]){
            blocks_explored++;
            blockmap[blockpos[0]][blockpos[1]] = true;
        }
        
    }
    
    public boolean isAlive(){
        return (health>0 && hunger>0) || age>=MAX_AGE;
    }
    
    /**
     * Parses a series of Genes and edits Bact.
     * @param DNA 
     */
    private void parseDNA(Gene[] DNA){
        int totalRed   = 0;
        int totalGreen = 0;
        int totalBlue  = 0;
        for (Gene gene : DNA){
            if (gene.getPrimer()==TRAIT_PRIMER){
                if (gene.getGene().length()==TRAIT_LENGTH){
                    // Remove header and first two characters
                    int delta = convertGeneticsToInt(gene.getGene().substring(2));
                    switch (gene.getGene().substring(0, 2)){
                        case "AA": this.mutationFrequency     += delta; break;
                        case "AB": totalRed                   += delta; break;
                        case "AC": totalGreen                 += delta; break;
                        case "AD": totalBlue                  += delta; break;
                        case "BA": this.speed                 += delta; break;
                        case "BB": this.mateReward            += delta; break;
                        case "BC": this.eatGReward            += delta; break;
                        case "BD": this.eatMReward            += delta; break;
                        case "CA": this.sightVal              += delta; break;
                    }
                }
            }
        }
        float totalColor = totalRed+totalGreen+totalBlue;
        sightRange = sightVal;
        if (totalColor!=0){
            red = (int)(255*totalRed/totalColor);
            green = (int)(255*totalGreen/totalColor);
            blue = (int)(255*totalBlue/totalColor);
            drawColor = new Color(red,blue,green);
        } else {
            red = green = blue = 0;
            drawColor = Color.BLACK;   
        }
        float totalReward = mateReward + eatGReward + eatMReward;
        if (totalReward==0){
            this.mateRewardAmount = 0;
            this.eatGRewardAmount = 0;
            this.eatMRewardAmount = 0;
        } else {
            
        }
        this.mov_speed = 1.0 + ((double)speed / (10.0*Bact.dnalength(DNA)));
        this.mutationRate = 0.0001 + (mutationFrequency)/(mutationFrequency+Bact.dnalength(DNA));
        this.metabolism = (int)Math.round( (DNA.length * 10)/(Bact.dnalength(DNA)) + this.mov_speed);
    }
    
    private static int convertGeneticsToInt(String gene){
        // Reads from left to right, left: ^0, etc...
        int n = ALPHABET.length();
        int total = 0;
        for (int i = 0; i<gene.length(); i++){
            int v = ALPHABET.indexOf(gene.charAt(i));
            total+= v*(Math.pow(n, i));
        }
        return total;
    }
    
    public static int dnalength(Gene[] a){
            int s = 0;
            for (Gene g : a)
                s+=g.getGene().length();
            return s;
    }
    
    public void draw(Graphics2D g, int xoffset, int yoffset){
        int x = pos.getX();
        int y = pos.getY();
        g.setColor(drawColor);
        g.fillOval(x+xoffset-drawSize/2, y+yoffset-drawSize/2, drawSize, drawSize);
        if (this.isAttacking())
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);
        int linex = (int)Math.round(pos.getX()+drawSize*pos.cos());
        int liney = (int)Math.round(pos.getY()+drawSize*pos.sin());
        g.drawLine(x, y, linex, liney);
        //g.drawString(""+id, x-drawSize/2, y-drawSize/2);
    }
    
    public static Gene[] makeGenesFromString(String DNA){
        int traits = 0;
        int neurons = 0;
        int paths = 0;
        int specials = 0;
        
        int botViewPos = 0;
        int stringLength = DNA.length();
        ArrayList<Gene> genes = new ArrayList<>();
        while (botViewPos<stringLength-1){
            char primerChar = DNA.charAt(botViewPos);
            // adds gene with primer plus either (rest of DNA) or (demanded amount of DNA)
            if (primerChar==TRAIT_PRIMER){
                traits++;
                genes.add(
                    new Gene(TRAIT_PRIMER,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+TRAIT_LENGTH>=stringLength)?
                                        DNA.length()-1:botViewPos+TRAIT_LENGTH+1)
                            )
                );
                botViewPos+=TRAIT_LENGTH;
            } else if (primerChar==NEURON_PRIMER){
                neurons++;
                genes.add(
                    new Gene(NEURON_PRIMER,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+NEURON_LENGTH>=stringLength)?
                                        DNA.length()-1:botViewPos+NEURON_LENGTH+1)
                            )
                );
                botViewPos+=NEURON_LENGTH;
            } else if (primerChar==NPATH_PRIMER || primerChar==SPECIAL_PRIMER){
                paths++;
                genes.add(
                    new Gene(NPATH_PRIMER,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+NPATH_LENGTH>=stringLength)?
                                        DNA.length()-1:botViewPos+NPATH_LENGTH+1)
                            )
                );
                botViewPos+=NPATH_LENGTH;
            } else if (primerChar==SPECIAL_PRIMER){
                // TODO special
            }
            botViewPos++;
        }
        return genes.toArray(new Gene[0]);
    }
    
    public static Gene[] randomDNA(int length){
        // Makes random string
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ ) 
            sb.append( ALPHABET.charAt( rnd.nextInt(ALPHABET.length())) );
        String DNA = sb.toString();
        return makeGenesFromString(DNA);
    }
    
    public Movement getMov(){return this.pos;}
    public Gene[] getDNA(){return DNA;}
    public int getAge(){return age;}
    @Override
    public String toString(){
        return "Bact: "+ brain.toString();
    }
    public int getHunger(){return hunger;}
    public int getHealth(){return health;}
    public int getMetabolism(){return metabolism;}
    public double getMovSpeed(){return mov_speed;}
    public int getID(){return id;}
    public int getParent(){return parent;}
    
    public BrainNode[] getOutputs(){
        return new BrainNode[]{leftWheel,rightWheel,mouthExtend,eatG,eatM,replicate};
    }
    public double getMutRate(){return this.mutationRate;}
    public boolean isAttacking(){return this.mouthExtend.getState()>0;}
    public int getSightRange(){return this.sightRange;}
    public int getRed(){return red;}
    public int getGreen(){return green;}
    public int getBlue(){return blue;}
    public BactInt[][] getClosests(){return closests.clone();}
    
    // Score Keeping
    public int getKills(){return this.bacts_killed;}
    public int getBlocksExplored(){return this.blocks_explored;}
    public int getMeatEaten(){return this.meat_eaten;}
}
