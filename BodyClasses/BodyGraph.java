/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

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
    
    public BodyGraph(Brain startingBrain){
        this(new BodyPartNode(startingBrain));
    }
    public BodyGraph(BodyPartNode bpn){
        if (bpn.getBodyPart() instanceof Brain){
            startingNode = bpn;
        } else {
            throw new Error("Brains needed");
        }
    }
    
    
    public void parseDNA(String DNA) {
        //TODO
    }
    
}
