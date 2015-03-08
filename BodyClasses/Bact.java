package NeuralEvolution.BodyClasses;

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
    
    public final static int MAX_HUNGER = 2400; // Two minutes at 1hunger/tick
    public final static int MAX_HEALTH = 100;
    
    public Gene[] DNA;
    
    private NeuralNetworkManager nnm;
    //Traits
    private int meatEnzymeProduction, 
                vegEnzymeProduction, 
                mutationFrequency,
                speed;
    // Physical Variables
    private Movement pos;
    private final int drawSize = 10;
    private Color drawColor;
    
    private int hunger,
                health;
    
    // Nodes to listen too.
    private NeuralNode  leftWheel,
                        rightWheel,
                        mouthExtend,
                        eat;
    private NeuralNode[][][] sight; // [0->red 1->green 2->blue][x][y]
    private NeuralNode[][] touch;
    private NeuralNode hungerNode;
    private NeuralNode healthNode;
    private NeuralNode lastLWheel;
    private NeuralNode lastRWheel;
    private NeuralNode lastMouthX;
    private NeuralNode lastEat;
    
    public Bact(int x, int y, int theta){
        this(x,y,theta,Bact.randomDNA(Bact.DEFAULT_DNA_LENGTH));
    }
    public Bact(int x, int y, int theta,Gene[] DNA){
        this.DNA = DNA;
        this.parseDNA(DNA);
        nnm = new NeuralNetworkManager(this);
        pos = new Movement(x,y,theta);
        
        hunger = MAX_HUNGER;
        health = MAX_HEALTH;
        
        // Now add nodes for motion and stuff
            leftWheel = new NeuralNode("LWheel",true);
            rightWheel = new NeuralNode("RWheel",true);
            mouthExtend = new NeuralNode("Mouth",true);
            eat = new NeuralNode("Eat",true);

            nnm.registerNode(leftWheel);
            nnm.registerNode(rightWheel);
            nnm.registerNode(mouthExtend);
            nnm.registerNode(eat);
        
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
            lastEat = new NeuralNode("LastEat",false);
            nnm.registerInputNode(lastLWheel);
            nnm.registerInputNode(lastRWheel);
            nnm.registerInputNode(lastMouthX);
            nnm.registerInputNode(lastEat);
        
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
        // Check what the neural network is up to if it hasn't been done
        if (!mapupdated){
            nnm.update();
        } else {
            mapupdated = false;
        }
        // TODO check outputs and update stuff
        int leftWheelVal = leftWheel.getState();
        int rightWheelVal = rightWheel.getState();
        int mouthExtendState = mouthExtend.getState();
        int eatState = eat.getState();
        // Update position
        double movSpeed = speed;
        if (leftWheelVal!=0 && rightWheelVal!=0){
            pos.forward(movSpeed);
            System.out.println("Moving");
        } else if (leftWheelVal!=0){
            pos.forward(movSpeed/2.0);
            pos.addTheta(-0.5);
            System.out.println("Moving");
        } else if (rightWheelVal!=0){
            pos.forward(movSpeed/2.0);
            pos.addTheta(0.5);
            System.out.println("Moving");
        }
        
        // TODO Calculate damage
        
        // TODO Eat food
        
        // TODO remove hunger based on speed and traits
        
        // TODO set node states for next time
        hungerNode.setState(this.hunger);
        healthNode.setState(this.health);
        
        lastLWheel.setState(leftWheel.getState());
        lastRWheel.setState(rightWheel.getState());
        lastMouthX.setState(mouthExtend.getState());
        lastEat.setState(eat.getState());
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
            System.out.println(drawColor.toString());
        } else {
            drawColor = Color.BLACK;   
        }
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
    
    public void draw(Graphics2D g, int xoffset, int yoffset){
        int x = pos.getX();
        int y = pos.getY();
        g.setColor(drawColor);
        g.fillOval(x+xoffset-drawSize/2, y+yoffset-drawSize/2, drawSize, drawSize);
        g.setColor(Color.BLACK);
        int linex = (int)Math.round(pos.getX()+drawSize*Math.cos(180*speed/Math.PI));
        int liney = (int)Math.round(pos.getY()+drawSize*Math.sin(180*speed/Math.PI));
        g.drawLine(x, y, linex, liney);
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
        System.out.println(traits+" "+neurons+" "+paths);
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
    
    public Movement getMov(){
        return this.pos;
    }
    
    @Override
    public String toString(){
        return "Bact: "+nnm.toString();
    }
}
