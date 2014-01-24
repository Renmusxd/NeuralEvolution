/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.SpecificGameClasses.Gene;
import NeuralEvolution.UtilityClasses.Movement;
import java.util.ArrayList;
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
    public static final byte INCASE = 0;
    public static final byte INSIDE = 1;
    public static final byte FRONT = 2;
    public static final byte BACK = 3;
    public static final byte LEFT = 4;
    public static final byte RIGHT = 5;
    public static final byte TOP = 6;
    public static final byte BOTTOM = 7;
    
    private BodyPartNode startingNode;
    
    private ArrayList<BodyPartNode> muscleNodes;
    private HashMap<String,BodyPartNode> unlinkedIDMap;
    private HashMap<String,BodyPartNode> nodeIDMap;
    
    private int maxBloodVolume;
    private float totalBlood;
    
    public BodyGraph(Gene[] DNA){
        this.parseDNA(DNA);
        this.maxBloodVolume = this.getMaxBloodVolume();
        nodeIDMap = new HashMap<String,BodyPartNode>();
        unlinkedIDMap = new HashMap<String,BodyPartNode>();
    }
    
    
    private void parseDNA(Gene[] DNA) {
        // Starts by adding brain
        this.startingNode = new BodyPartNode(new Brain());
        nodeIDMap.put("AAAA", startingNode);
        // TODO parse DNA
    }
    
    private void makeBodyPart(String type, String ID){
        // TODO make body parts
        //First two n are type, second four are id
        
    }
    
    private void linkBodyPart(String identifier, String target, byte side){
        // Links body part to other body part 
        if (unlinkedIDMap.containsKey(identifier) && nodeIDMap.containsKey(target)){
            // Make sure id is not above target
            BodyPartNode newBPN = unlinkedIDMap.get(identifier);
            BodyPartNode targetBPN = nodeIDMap.get(target);
            BodyPartNode prev = newBPN.getPrevious();
            while (prev!=null){
                if (prev.equals(newBPN)){
                    // If it is above what would be itself, ignore link, carry on with life
                    return;
                }
            }
            // If newBPN is not above target
            // Add to target and set previous
            switch(side){
                case INCASE: targetBPN.addOutside(newBPN); break;
                case INSIDE: targetBPN.addInside(newBPN); break;
                case FRONT: targetBPN.addFront(newBPN); break;
                case BACK: targetBPN.addBack(newBPN); break;
                case LEFT: targetBPN.addLeft(newBPN); break;
                case RIGHT: targetBPN.addRight(newBPN); break;
                case TOP: targetBPN.addTop(newBPN); break;
                case BOTTOM: targetBPN.addBottom(newBPN); break;
            }
            newBPN.setPrevious(nodeIDMap.get(target));
        } // else jst pretend the DNA doesn't exist
    }
    
    public void updateBodyParts(){
        for(Entry<String, BodyPartNode> entry : nodeIDMap.entrySet()) {
            entry.getValue().getBodyPart().update();
        }
    }
    
    public float getBloodVolume(){
        // TODO blood loss
        return 0;
    }
    
    private int getMaxBloodVolume() {
        int blood = 0;
        for(Entry<String, BodyPartNode> entry : nodeIDMap.entrySet()) {
            blood+=entry.getValue().getBodyPart().getBloodVolume();
        }
        return blood;
    }
    
    public Movement bodyMotion(){
        for(Entry<String, BodyPartNode> entry: nodeIDMap.entrySet()){
            
        }
        return null;
    }
}
