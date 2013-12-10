/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.UtilityClasses.Movement;
import java.util.HashMap;
import java.util.Map.Entry;

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
    private static final byte INCASE = 0;
    private static final byte FRONT = 1;
    private static final byte BACK = 2;
    private static final byte LEFT = 3;
    private static final byte RIGHT = 4;
    private static final byte TOP = 5;
    private static final byte BOTTOM = 6;
    
    
    private BodyPartNode startingNode;
    
    private HashMap<String,BodyPartNode> nodeIDMap;
    
    public BodyGraph(Gene[] DNA){
        this.parseDNA(DNA);
    }
    
    
    private void parseDNA(Gene[] DNA) {
        // Starts by adding brain
        this.startingNode = new BodyPartNode(new Brain());
        nodeIDMap.put("AAAA", startingNode);
    }
    
    private void addBodyPart(String identifier, BodyPart bp, String target, byte side){
        if (nodeIDMap.containsKey(target)){
            
        } // else jst pretend the DNA doesn't exist
    }
    
    public void updateBodyParts(){
        for(Entry<String, BodyPartNode> entry : nodeIDMap.entrySet()) {
            entry.getValue().getBodyPart().update();
        }
    }
    
    public float getBloodVolume(){
        return 0;
    }
    
    public float getMaxBloodVolume() {
        int blood = 0;
        for(Entry<String, BodyPartNode> entry : nodeIDMap.entrySet()) {
            blood+=entry.getValue().getBodyPart().getBloodVolume();
        }
        return blood;
    }
    
    public Movement bodyMotion(){
        // Based on current system behavior, calculate movement change
        return null;
    }
}
