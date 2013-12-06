/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.NeuronClasses.NeuralNetworkManager;
import NeuralEvolution.NeuronClasses.NeuralNode;
import java.awt.Graphics2D;

/**
 *
 * @author Sumner
 */
public class Bact {
    
    private final static int DEFAULT_DNA_LENGTH = 100;
    
    private NeuralNetworkManager nnm;
    
    // Output nodes (feed into neurons)
    private NeuralNode hungerNode, sevInjuryNode, dangerNode, painFrontNode, painLeftNode, painBackNode, painRightNode, sightRedLeftNode,
                       sightGreenLeftNode, sightBlueLeftNode, sightRedMidNode, sightGreenMidNode, sightBlueMidNode, sightRedRightNode,
                       sightGreenRightNode, sightBlueRightNode, objectCloseNode;
    // input nodes (operate bact based on neurons)
    private NeuralNode moveForwardNode, turnLeftNode, turnRightNode, attackNode, spitNode, eatNode;
   
    //TODO get sexual reproduction integrated, we'll start with asexual.
    private BodyGraph body;
    //Traits
    private int meatEnzymeProduction, vegEnzymeProduction, mutationFrequency, muscleDensity;
    
    public Bact(){
        //Init with random DNA
    }
    public Bact(String DNA){
        nnm = new NeuralNetworkManager();
        body = new BodyGraph();
        //Parse DNA
        this.parseDNA(DNA);
        nnm.parseDNA(DNA);
        body.parseDNA(DNA);
    }
    
    public void update(){
        
    }
    
    public boolean isAlive(){
        return false;
    }
    
    public void parseDNA(String DNA){
        //Picks appropriate DNA segments and sets traits
    }
    
    public void draw(Graphics2D g, int xoffset, int yoffset){
        
    }
}
