/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.NeuronClasses.NeuralNetworkManager;
import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.UtilityClasses.Movement;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author Sumner
 */
public class Bact {
    /*
     * TODO get sexual reproduction integrated, we'll start with asexual.
     */
    public static final String ALPHABET = "ABCD";
    public static final byte GENE_LENGTH = 9;
    
    // All use 1n+8n data
    public static final char TRAIT_PRIMER = 'A';
    public static final char BODY_PRIMER = 'B';
    public static final char NEURAL_PRIMER = 'C';
    
    private final static int DEFAULT_DNA_LENGTH = 900;
    
    private NeuralNetworkManager nnm;
    
    private BodyGraph body;
    //Traits
    private int meatEnzymeProduction, vegEnzymeProduction, mutationFrequency;
    
    private Movement pos;
    private int drawSize;
    
    private float maxBloodVolume; //calculated from sum of hearts and such
    private float totalBloodVolume;
    
    
    public Bact(int x, int y, int theta){
        this(x,y,theta,Bact.randomDNA(Bact.DEFAULT_DNA_LENGTH));
    }
    public Bact(int x, int y, int theta,Gene[] DNA){
        this.parseDNA(DNA);
        body = new BodyGraph(DNA);
        nnm = new NeuralNetworkManager(DNA);
        pos = new Movement(x,y,theta);
        
        // Getting appropriate values from body
        this.maxBloodVolume = body.getMaxBloodVolume();
        
    }
    
    public void update(){
        this.body.updateBodyParts();
        this.pos.add(this.body.bodyMotion());
        
    }
    
    public boolean isAlive(){
        // blood = 0 or brain damaged
        return true;
    }
    
    private void parseDNA(Gene[] DNA){
        for (Gene gene : DNA){
            if (gene.getPrimer()==TRAIT_PRIMER){
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
    
    private int convertGeneticsToInt(String gene){
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
        
    }
    
    public static Gene[] randomDNA(int length){
        // TODO Should be fixed later
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ ) 
            sb.append( ALPHABET.charAt( rnd.nextInt(ALPHABET.length()) ) );
        String DNA = sb.toString();
        String[] geneArray = DNA.split("(?<=\\G.{4})");
        Gene[] genes = new Gene[(length%GENE_LENGTH==0)?length/GENE_LENGTH:(length/GENE_LENGTH)+1];
        int i = 0;
        for (String gene:geneArray){
            genes[i] = new Gene(gene.charAt(0),gene.substring(1));
            i++;
        }
        return genes;
    }
}
