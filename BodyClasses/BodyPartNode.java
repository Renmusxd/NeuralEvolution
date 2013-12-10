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
    private BodyPartNode previous;
    private ArrayList<BodyPartNode> inside;
    private ArrayList<BodyPartNode> outside;
    private ArrayList<BodyPartNode> front;
    private ArrayList<BodyPartNode> back;
    private ArrayList<BodyPartNode> left;
    private ArrayList<BodyPartNode> right;
    private ArrayList<BodyPartNode> top;
    private ArrayList<BodyPartNode> bottom;
    
    
    public BodyPartNode(BodyPart bp){
        this.bp = bp;
        inside = new ArrayList<BodyPartNode>();
        outside = new ArrayList<BodyPartNode>();
        front = new ArrayList<BodyPartNode>();
        back = new ArrayList<BodyPartNode>();
        left = new ArrayList<BodyPartNode>();
        right = new ArrayList<BodyPartNode>();
        top = new ArrayList<BodyPartNode>();
        bottom = new ArrayList<BodyPartNode>();
    }
    public void setPrevious(BodyPartNode bpn){previous=bpn;}
    public void addInside(BodyPartNode bpn){inside.add(bpn);}
    public void addOutside(BodyPartNode bpn){outside.add(bpn);}
    public void addFront(BodyPartNode bpn){front.add(bpn);}
    public void addBack(BodyPartNode bpn){back.add(bpn);}
    public void addLeft(BodyPartNode bpn){left.add(bpn);}
    public void addRight(BodyPartNode bpn){right.add(bpn);}
    public void addTop(BodyPartNode bpn){top.add(bpn);}
    public void addBottom(BodyPartNode bpn){bottom.add(bpn);}
    
    public BodyPartNode getPrevious(){return previous;}
    public BodyPartNode[] getInside(){return inside.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getOutside(){return outside.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getFront(){return front.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getBack(){return back.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getLeft(){return left.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getRight(){return right.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getTop(){return top.toArray(new BodyPartNode[0]);}
    public BodyPartNode[] getBottom(){return bottom.toArray(new BodyPartNode[0]);}
    
    public BodyPart getBodyPart(){return bp;}
}
