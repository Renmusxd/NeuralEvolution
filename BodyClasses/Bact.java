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

/**
 *
 * @author Sumner
 */
public class Bact {
    /*
     * TODO get sexual reproduction integrated, we'll start with asexual.
     */
    public static final String ALPHABET = "ABCD";
    public static final char TRAIT_PRIMER = 'A';
    public static final char BODY_PRIMER = 'B';
    public static final char NEURAL_PRIMER = 'C';
    
    private final static int DEFAULT_DNA_LENGTH = 900;
    
    private NeuralNetworkManager nnm;
    
    private BodyGraph body;
    //Traits
    private int meatEnzymeProduction, vegEnzymeProduction, mutationFrequency;
    
    private Movement pos;
    
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
        //Picks appropriate DNA segments and sets traits
    }
    
    public void draw(Graphics2D g, int xoffset, int yoffset){
        
    }
    
    public static Gene[] randomDNA(int length){
        return null;
    }
}
