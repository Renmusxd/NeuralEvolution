/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.NeuronClasses;

import NeuralEvolution.SpecificGameClasses.Gene;
import java.util.HashMap;

/**
 *
 * @author Sumner
 */
public class NeuralNetworkManager {
 
    
    private HashMap<String,NeuralNode> nodeIDMap;
    
    public NeuralNetworkManager(Gene[] DNA){
        this.parseDNA(DNA);
        nodeIDMap = new HashMap<String,NeuralNode>();
    }
    
    public void addInputNode(){
        
    }
    
    public String encodeNetwork(){
        return null;
    }

    private void parseDNA(Gene[] DNA){
        
    }
}
