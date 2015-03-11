package NeuralEvolution.BodyClasses;

import NeuralEvolution.GameClasses.World;
import NeuralEvolution.NeuronClasses.NeuralNetworkManager;
import NeuralEvolution.NeuronClasses.NeuralNode;
import NeuralEvolution.SpecificGameClasses.Gene;
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
    
    private World w;
    
    private Gene[] DNA;
    
    private NeuralNetworkManager nnm;
    //Traits
    private int meatEnzymeProduction, 
                vegEnzymeProduction, 
                mutationFrequency,
                speed;
    // Calculated values
    private double  mov_speed,
                    mutationRate;
    private int     metabolism;
        
    // Physical Variables
    private Movement pos;
    private final int drawSize = 10;
    private Color drawColor;
    
    private int hunger,
                health;
    
    private int age = 0;
    
    // Nodes to listen too.
    private NeuralNode  leftWheel,
                        rightWheel,
                        mouthExtend,
                        eatG,
                        eatM;
    private NeuralNode[][][] sight; // [0->red 1->green 2->blue][x][y]
    private NeuralNode[][] touch;
    private NeuralNode hungerNode;
    private NeuralNode healthNode;
    private NeuralNode lastLWheel;
    private NeuralNode lastRWheel;
    private NeuralNode lastMouthX;
    private NeuralNode lastEatG;
    private NeuralNode lastEatM;
    
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
            leftWheel = new NeuralNode("LWheel",true);
            rightWheel = new NeuralNode("RWheel",true);
            mouthExtend = new NeuralNode("Mouth",true);
            eatG = new NeuralNode("Eatg",true);
            eatM = new NeuralNode("Eatm",true);

            nnm.registerNode(leftWheel);
            nnm.registerNode(rightWheel);
            nnm.registerNode(mouthExtend);
            nnm.registerNode(eatG);
            nnm.registerNode(eatM);
        
        // Add input nodes
            // Always on
            NeuralNode on = new NeuralNode("On",false);
            on.setState(1);
            nnm.registerInputNode(on);
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
            hungerNode = new NeuralNode("Hunger",false);
            healthNode = new NeuralNode("Health",false);
            nnm.registerInputNode(hungerNode);
            nnm.registerInputNode(healthNode);
            // Past output
            lastLWheel = new NeuralNode("LastLW",false);
            lastRWheel = new NeuralNode("LastRW",false);
            lastMouthX = new NeuralNode("LastMX",false);
            lastEatG = new NeuralNode("LastEatG",false);
            lastEatM = new NeuralNode("LastEatM",false);
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
        // TODO
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
        if (leftWheelVal!=0 && rightWheelVal!=0){
            pos.forward(mov_speed);
        } else if (leftWheelVal!=0){
            pos.forward(mov_speed/2.0);
            pos.addTheta(-2);
        } else if (rightWheelVal!=0){
            pos.forward(mov_speed/2.0);
            pos.addTheta(2);
        }
        // TODO Calculate damage
        
        // Eat food
        if (eatGState!=0 || eatMState!=0)
            System.out.println("trying to eat");
        if (eatGState>0){
            int eaten = w.eatGrass(pos.getX(), pos.getY(), eatGState);
            hunger +=(int)( eaten * this.vegEnzymeProduction / (double)
                    (this.meatEnzymeProduction + this.vegEnzymeProduction));
        }
        if (eatMState>0){
            int eaten = w.eatMeat(pos.getX(), pos.getY(), eatGState);
            hunger +=(int)( eaten * this.meatEnzymeProduction / (double)
                    (this.meatEnzymeProduction + this.vegEnzymeProduction));
        }
        
        // Remove hunger based on speed and traits
        this.hunger -= this.metabolism;
        
        // Set node states for next time
        hungerNode.setState(this.hunger);
        healthNode.setState(this.health);
        
        lastLWheel.setState(leftWheel.getState());
        lastRWheel.setState(rightWheel.getState());
        lastMouthX.setState(mouthExtend.getState());
        lastEatG.setState(eatG.getState());
        lastEatM.setState(eatM.getState());
        
        // TODO sight and tactile
    }
    
    public boolean isAlive(){
        return (hunger>0 && health>0);
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
                        case "AA": this.meatEnzymeProduction  += delta; break;
                        case "AB": this.vegEnzymeProduction   += delta; break;
                        case "AC": this.mutationFrequency     += delta; break;
                        case "AD": totalRed                   += delta; break;
                        case "BA": totalGreen                 += delta; break;
                        case "BB": totalBlue                  += delta; break;
                        case "BC": this.speed                 += delta; break;
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
        this.mov_speed = 1.0 + ((double)speed / (10.0*Bact.dnalength(DNA)));
        this.mutationRate = (1.0 + mutationFrequency)/(Bact.dnalength(DNA));
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
                    new Gene(primerChar,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+TRAIT_LENGTH>=stringLength)?
                                        DNA.length()-1:botViewPos+TRAIT_LENGTH+1)
                            )
                );
                botViewPos+=TRAIT_LENGTH;
            } else if (primerChar==NEURON_PRIMER){
                neurons++;
                genes.add(
                    new Gene(primerChar,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+NEURON_LENGTH>=stringLength)?
                                        DNA.length()-1:botViewPos+NEURON_LENGTH+1)
                            )
                );
                botViewPos+=NEURON_LENGTH;
            } else if (primerChar==NPATH_PRIMER){
                paths++;
                genes.add(
                    new Gene(primerChar,
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
    public String toString(){return "Bact: "+nnm.toString();}
    public int getHunger(){return hunger;}
    public int getHealth(){return health;}
    public int getMetabolism(){return metabolism;}
    public double getMovSpeed(){return mov_speed;}
    public int getID(){return id;}
    public int getParent(){return parent;}
}
