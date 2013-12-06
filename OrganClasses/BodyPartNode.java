/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralevolution.NeuralEvolution.OrganClasses;

/**
 *
 * @author Sumner
 */
public class BodyPartNode {
    
    private BodyPart bp;
    private BodyPartNode previousNode = null;
    private BodyPartNode nextNode = null;
    
    
    public BodyPartNode(BodyPart bp){
        this.bp = bp;
    }
    
    public BodyPartNode getNext(){return this.nextNode;}
    public void setNext(BodyPartNode bpn){this.nextNode = bpn;}
    public BodyPartNode getPrevious(){return this.previousNode;}
    public void setPrevious(BodyPartNode bpn){this.previousNode = bpn;}
    public BodyPart getBodyPart(){return bp;}
    
}
