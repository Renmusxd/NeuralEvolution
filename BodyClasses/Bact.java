package NeuralEvolution.BodyClasses;

import NeuralEvolution.GameClasses.World;
import NeuralEvolution.NeuronClasses.NeuralNetworkManager;
import NeuralEvolution.NeuronClasses.NeuralNode;
import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.SpecificGameClasses.Mutator;
import NeuralEvolution.UtilityClasses.Movement;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
    public static final char    TRAIT_PRIMER        = 'A';
    public static final byte    TRAIT_LENGTH        = 8; // [XX][XXXX] = [OP]-[VAL]
    public static final char    NEURON_PRIMER       = 'B';
    public static final byte    NEURON_LENGTH       = 6; // [XXXX][XX] = [ID]-[OP]
    public static final char    NPATH_PRIMER        = 'C';
    public static final byte    NPATH_LENGTH        = 8; // [XXXX][XXXX] = [ID]-[ID]
    public static final char    SPECIAL_PRIMER      = 'D';
    public final static int     DEFAULT_DNA_LENGTH  = 1000;
    
    public static int ID_GLOBAL = 1;
    private int id;
    
    private int parent;
    
    public final static int MAX_HUNGER = 2400; // Two minutes at 1hunger/tick
    public final static int MAX_HEALTH = 100;
    
    public final static int MAX_REWARD = 15;
    
    private World w;
    
    private Gene[] DNA;
    
    private NeuralNetworkManager nnm;
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
                    sightRange;
        
    // Physical Variables
    private Movement pos;
    private final int drawSize = 10;
    private Color drawColor;
    
    private int hunger,
                health;
    
    private int age = 0;
    
    // Nodes to listen to.
    private NeuralNode  leftWheel,
                        rightWheel,
                        mouthExtend,
                        eatG,
                        eatM,
                        replicate;
    private NeuralNode[][][] sight; // [0->red 1->green 2->blue][x][y]
    private NeuralNode[][] touch;
    private NeuralNode hungerNode;
    private NeuralNode healthNode;
    private NeuralNode lastLWheel;
    private NeuralNode lastRWheel;
    private NeuralNode lastMouthX;
    private NeuralNode lastEatG;
    private NeuralNode lastEatM;
    
    private int lastThingSaid;
    private int agesaid;
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
        nnm = new NeuralNetworkManager(this);
        pos = new Movement(x,y,theta);
        
        hunger = MAX_HUNGER;
        health = MAX_HEALTH;
        
        // Now add nodes for motion and stuff
            leftWheel = new NeuralNode("LWheel",true,false);
            rightWheel = new NeuralNode("RWheel",true,false);
            mouthExtend = new NeuralNode("Mouth",true,false);
            eatG = new NeuralNode("Eatg",true,false);
            eatM = new NeuralNode("Eatm",true,false);
            replicate = new NeuralNode("Repl",true,false);

            nnm.registerNode(leftWheel);
            nnm.registerNode(rightWheel);
            nnm.registerNode(mouthExtend);
            nnm.registerNode(eatG);
            nnm.registerNode(eatM);
            nnm.registerNode(replicate);
        
        // Add input nodes
            // Always on
            NeuralNode on = new NeuralNode("On",1);
            NeuralNode maxhung = new NeuralNode("MaxHunger",MAX_HUNGER);
            NeuralNode maxhealth = new NeuralNode("MaxHealth",MAX_HEALTH);
            nnm.registerInputNode(on);
            nnm.registerInputNode(maxhung);
            nnm.registerInputNode(maxhealth);
            // Sight
            sight = new NeuralNode[3][3][3];
            char[] cs = "RGB".toCharArray();
            for (int c=0; c<3; c++){
                for (int i=0; i<3; i++){
                    for (int j=0; j<3; j++) {
                        NeuralNode n = new NeuralNode("Eye "+cs[c]+""+i+""+j,false);
                        sight[c][i][j] = n;
                        nnm.registerInputNode(n);
                    }
                }
            }
            // Tactile
            touch = new NeuralNode[3][3];
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++) {
                    NeuralNode n = new NeuralNode("Tch "+i+""+j,false);
                    touch[i][j] = n;
                    nnm.registerInputNode(n);
                }
            }
            // Hunger and Health
            hungerNode = new NeuralNode("Hunger",false,false);
            healthNode = new NeuralNode("Health",false,false);
            nnm.registerInputNode(hungerNode);
            nnm.registerInputNode(healthNode);
            // Past output
            lastLWheel = new NeuralNode("LastLW",false,false);
            lastRWheel = new NeuralNode("LastRW",false,false);
            lastMouthX = new NeuralNode("LastMX",false,false);
            lastEatG = new NeuralNode("LastEatG",false,false);
            lastEatM = new NeuralNode("LastEatM",false,false);
            nnm.registerInputNode(lastLWheel);
            nnm.registerInputNode(lastRWheel);
            nnm.registerInputNode(lastMouthX);
            nnm.registerInputNode(lastEatG);
            nnm.registerInputNode(lastEatM);
        // Now add stuff from DNA
        nnm.parseDNA(DNA);
    }
    /**
     * Used to make more efficient collision detection O(nlogn) instead of O(n^2)
     * Report bact within distance
     * @param b - bact within range
     */
    public void alertOfPresence(Bact b){
        // TODO alertbacts of others with sight
        
        
        
        
    }
    
    private boolean mapupdated = false;
    public void updateNeurons(){
        if (!mapupdated){
            nnm.update();
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
            nnm.update();
        } else {
            mapupdated = false;
        }
        // Check outputs and update stuff
        int leftWheelVal = leftWheel.getState();
        int rightWheelVal = rightWheel.getState();
        int mouthExtendState = mouthExtend.getState();
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
        // TODO Calculate damage
        
        // Eat food
        if (eatGState>0){
            int toeat = Math.min(eatGState, MAX_HUNGER-hunger);
            int eaten = w.eatGrass(pos.getX(), pos.getY(), toeat);
            hunger += eaten;
            if (eaten!=0)
                nnm.reward(eatGRewardAmount);
        }
        if (eatMState>0){
            int toeat = Math.min(eatGState, MAX_HUNGER-hunger);
            int eaten = w.eatMeat(pos.getX(), pos.getY(), toeat);
            hunger += eaten;
            if (eaten!=0)
                nnm.reward(eatMRewardAmount);
        }
        if (hunger>MAX_HUNGER)
                hunger = MAX_HUNGER;
        // Hunger and Health
        if (hunger-metabolism>0){
            this.hunger -= this.metabolism;
            if (health<MAX_HEALTH)
                this.health += 1;
            if (hunger<0)
                hunger=0;
        } else {
            this.health -= this.metabolism - this.hunger;
            this.hunger = 0;
            if (health<0)
                health = 0;
        }
        
        // Set node states for next time
        hungerNode.setState(this.hunger);
        healthNode.setState(this.health);
        
        lastLWheel.setState(leftWheel.getState());
        lastRWheel.setState(rightWheel.getState());
        lastMouthX.setState(mouthExtend.getState());
        lastEatG.setState(eatG.getState());
        lastEatM.setState(eatM.getState());
        
        // Sight of Grass, Bacts only see the green of grass from 0 to 255
        final int rotNum = (int)Math.round((pos.getTheta()+22.5)/45);
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
            int looky = pos.getY() + (jx-1)*World.food_size;
            this.sight[1][ix][iy].setState(
                    255*w.getGrass(lookx, looky)/World.default_grass
                );
            if (w.getMeat(lookx, looky)>0)
                this.sight[0][ix][iy].setState(255);
        }
        // Replicate
        if (replicate.getState()>0){
            System.out.println(id+" trying to mate");
            System.out.println("hunger: "+hunger);
        }
        if (replicate.getState()>0 && hunger>2*replicate.getState()){
            
            Gene[] newDNA = Mutator.swap(DNA, this.mutationRate);
            System.out.println(DNA.length+":"+newDNA.length+":"+mutationRate);
            newDNA = Mutator.changeBase(newDNA, this.mutationRate);
            w.replBact(id,replicate.getState(),pos.getX(),pos.getY(),newDNA);
            hunger -= replicate.getState();
        }
    }
    
    public boolean isAlive(){
        return health>0;
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
                    }
                }
            }
        }
        float totalColor = totalRed+totalGreen+totalBlue;
        if (totalColor!=0){
            drawColor = new Color(totalRed/totalColor,totalGreen/totalColor,totalBlue/totalColor);
        } else {
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
        this.mutationRate = (1.0 + mutationFrequency)/(10*(mutationFrequency+Bact.dnalength(DNA)));
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
        g.setColor(Color.BLACK);
        int linex = (int)Math.round(pos.getX()+drawSize*Math.cos(Math.PI*pos.getTheta()/180.0));
        int liney = (int)Math.round(pos.getY()+drawSize*Math.sin(Math.PI*pos.getTheta()/180.0));
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
        return "Bact: "+nnm.toString();
    }
    public int getHunger(){return hunger;}
    public int getHealth(){return health;}
    public int getMetabolism(){return metabolism;}
    public double getMovSpeed(){return mov_speed;}
    public int getID(){return id;}
    public int getParent(){return parent;}
    
    public NeuralNode[] getOutputs(){
        return new NeuralNode[]{leftWheel,rightWheel,mouthExtend,eatG,eatM,replicate};
    }
}
