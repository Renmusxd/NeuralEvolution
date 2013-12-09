/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import java.util.ArrayList;
/**
 *
 * @author Sumner
 */
public class BodyPartNode {
    
    private BodyPart bp;
    private ArrayList<BodyPartNode> previousNodes = null;
    private ArrayList<BodyPartNode> nextNodes = null;
    
    
    public BodyPartNode(BodyPart bp){
        this.bp = bp;
    }
    
    public BodyPartNode[] getNext(){return this.nextNodes.toArray(new BodyPartNode[0]);}
    public void setNext(BodyPartNode bpn){this.nextNodes.add(bpn);}
    public BodyPartNode[] getPrevious(){return this.nextNodes.toArray(new BodyPartNode[0]);}
    public void setPrevious(BodyPartNode bpn){this.previousNodes.add(bpn);}
    public BodyPart getBodyPart(){return bp;}
    
}
