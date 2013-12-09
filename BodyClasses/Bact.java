/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.NeuronClasses.NeuralNetworkManager;
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
    //public static final String DNA_INITIALIZAER = "AAAA";
    
    private final static int DEFAULT_DNA_LENGTH = 100;
    
    private NeuralNetworkManager nnm;
    
    private BodyGraph body;
    //Traits
    private int meatEnzymeProduction, vegEnzymeProduction, mutationFrequency, muscleDensity;
    
    private Movement pos;
    
    private float maxBloodVolume; //calculated from sum of hearts and such
    private float totalBloodVolume;
    
    
    public Bact(int x, int y, int theta){
        this(x,y,theta,Bact.randomDNA());
    }
    public Bact(int x, int y, int theta,String DNA){
        nnm = new NeuralNetworkManager();
        body = new BodyGraph(new Brain());
        //Parse DNA
        this.parseDNA(DNA);
        nnm.parseDNA(DNA);
        body.parseDNA(DNA);
        pos = new Movement(x,y,theta);
    }
    
    public void update(){
        // TODO
    }
    
    public boolean isAlive(){
        return false;
    }
    
    private void parseDNA(String DNA){
        //Picks appropriate DNA segments and sets traits
    }
    
    public void draw(Graphics2D g, int xoffset, int yoffset){
        
    }
    
    public static String randomDNA(){
        return null;
    }
}
