/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.UtilityClasses.Movement;

/**
 *
 * @author Sumner
 */
class BodyGraph {
    /*
     * Holds body parts in appropriate order
     * manages attacks and venoms
     * checks for abilities (walking) and stats/states
     * 
     * Automatically starts with {brain} BodyPart at top of graph, everything descends from that
     * Automatically puts {skin} BodyPart at end of all branches of graph
     * 
     * Make sure there are no loops
     */
    private BodyPartNode startingNode;
    
    public BodyGraph(String DNA){
        this.parseDNA(DNA);
    }
    
    
    private void parseDNA(String DNA) {
        //TODO
    }
    
    public void updateBodyParts(){
        
    }
    
    public float getBloodVolume(){
        return 0;
    }
    
    public Movement bodyMotion(){
        // Based on current system behavior, calculate movement change
        return null;
    }
    
}
