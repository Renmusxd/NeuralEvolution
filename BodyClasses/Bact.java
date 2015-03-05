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
    public static final byte    TRAIT_LENGTH        = 9;
    public static final char    NEURON_PRIMER       = 'B';
    public static final byte    NEURON_LENGTH       = 7;
    public static final char    NPATH_PRIMER        = 'C';
    public static final byte    NPATH_LENGTH        = 7;
    public static final char    SPECIAL_PRIMER      = 'D';
    public final static int     DEFAULT_DNA_LENGTH  = 900;
    
    private NeuralNetworkManager nnm;
    //Traits
    private int meatEnzymeProduction, 
                vegEnzymeProduction, 
                mutationFrequency;
    // Physical Variables
    private Movement pos;
    private int drawSize = 5;
    
    private int hunger,
                health;
    
    // Nodes to listen too.
    private NeuralNode leftWheel;
    private NeuralNode rightWheel;
    private NeuralNode mouthExtend;
    
    public Bact(int x, int y, int theta){
        this(x,y,theta,Bact.randomDNA(Bact.DEFAULT_DNA_LENGTH));
    }
    public Bact(int x, int y, int theta,Gene[] DNA){
        this.parseDNA(DNA);
        nnm = new NeuralNetworkManager(this);
        pos = new Movement(x,y,theta);
        
        // Now add nodes for motion and stuff
        leftWheel = new NeuralNode();
        rightWheel = new NeuralNode();
        mouthExtend = new NeuralNode();
        nnm.registerNode(leftWheel);
        nnm.registerNode(rightWheel);
        nnm.registerNode(mouthExtend);
        
        // Now add stuff from DNA
        nnm.parseDNA(DNA);
    }
    
    private boolean mapupdated = false;
    public void updateNeurons(){
        if (!mapupdated){
            nnm.update();
            mapupdated = true;
        }
    }
    public void update(){
        // Check what the neural network is up to if it hasn't been done
        if (!mapupdated){
            nnm.update();
        } else {
            mapupdated = false;
        }
        // TODO check outputs and update stuff
        
    }
    
    public boolean isAlive(){
        // TODO bact death
        return true;
    }
    
    private void parseDNA(Gene[] DNA){
        for (Gene gene : DNA){
            if (gene.getPrimer()==TRAIT_PRIMER){
                if (gene.getGene().length()==(TRAIT_LENGTH-1)){
                    int delta = convertGeneticsToInt(gene.getGene().substring(1));
                    switch (gene.getGene().charAt(0)){
                        case 'A': this.meatEnzymeProduction+=delta; break;
                        case 'B': this.vegEnzymeProduction+=delta; break;
                        case 'C': this.mutationFrequency+=delta; break;
                        case 'D': break;
                    }
                }
            }
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
        g.setColor(Color.red);
        g.fillOval(pos.getX(), pos.getY(), drawSize, drawSize);
    }
    
    public static Gene[] makeGenesFromString(String DNA){
        int botViewPos = 0;
        int stringLength = DNA.length();
        ArrayList<Gene> genes = new ArrayList<>();
        while (botViewPos<stringLength){
            char primerChar = DNA.charAt(botViewPos);
            // adds gene with primer plus either (rest of DNA) or (demanded amount of DNA)
            if (primerChar==TRAIT_PRIMER){
                genes.add(new Gene(primerChar,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+TRAIT_LENGTH>=DNA.length())?DNA.length()-1:botViewPos+TRAIT_LENGTH)));
                botViewPos+=TRAIT_LENGTH;
            } else if (primerChar==NEURON_PRIMER){
                genes.add(new Gene(primerChar,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+NEURON_LENGTH>=DNA.length())?DNA.length()-1:botViewPos+NEURON_LENGTH)));
                botViewPos+=NEURON_LENGTH;
            } else if (primerChar==NPATH_PRIMER){
                genes.add(new Gene(primerChar,
                        DNA.substring(botViewPos+1, 
                                (botViewPos+NPATH_LENGTH>=DNA.length())?DNA.length()-1:botViewPos+NPATH_LENGTH)));
                botViewPos+=NPATH_LENGTH;
            } else if (primerChar==SPECIAL_PRIMER){
                botViewPos++;
                // TODO special
            }
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
}
