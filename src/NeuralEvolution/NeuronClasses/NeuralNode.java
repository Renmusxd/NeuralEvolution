package NeuralEvolution.NeuronClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The NeuralNode class controls the actions of nodes
 * in the Bact's neural network.
 * @author Sumner
 */
public class NeuralNode {
    private String id = null;
    private static final Random rand = new Random();
    
    public enum INOP{
        AND, OR, EQ, LT, DLATCH, ADD, SUB;
    }
    public enum OUTOP{
        ANY,ONE,ALL;
    }
    
    // DEFAULT
    private INOP in = INOP.OR;
    private OUTOP out = OUTOP.ALL;
    private boolean inverse;
    
    private int state = 0;
    int lastState = 0;
    
    private final ArrayList<NeuralNode> inputNodes;
    private final ArrayList<NeuralNode> outputNodes;
    
    int totalValue = 0; // Add one for new input, add one for learning
    private final HashMap<NeuralNode,Integer> outWeights;
    
    /** Number of updates this cycle, limited to #inputs **/
    private byte updates;
    private boolean acceptsInput = true;
    private boolean editableOp = true;
    
    /**
     * Makes a constant
     * @param s
     * @param v constant value
     */
    public NeuralNode(String s, int v){
        this(s,false,false);
        this.state = v;
    }
    public NeuralNode(String s, boolean acceptsInput, boolean editableoperation){
        this();
        id = s;
        this.acceptsInput = acceptsInput;
        editableOp = false;
    }
    public NeuralNode(String s, boolean acceptsInput){
        this();
        id = s;
        this.acceptsInput = acceptsInput;
    }
    public NeuralNode(){
        inputNodes = new ArrayList<>();
        outputNodes = new ArrayList<>();
        outWeights = new HashMap<>();
    }
    public void addInput(NeuralNode nn){
        inputNodes.add(nn);
    }
    public void addOutput(NeuralNode nn){
        outputNodes.add(nn);
        outWeights.put(nn,1);
        totalValue++;
    }
    /**
     * Must be called each time step to clear the update limiter.
     */
    public void clear(){
        updates = 0;
    }
    /**
     * Updates this node, it then gathers information about its input nodes.
     * A Node can only be updated n times per time step where n in the total
     * number of nodes connected to this node.
     */
    public void update(){
        updates++;
        if (updates<=inputNodes.size() && acceptsInput){
            int newstate = 0;
            if (in == INOP.AND && inputNodes.size()>0){ // Largest if all on
                newstate = inputNodes.get(0).getState();
                for (NeuralNode n : inputNodes){
                    int t = n.getState();
                    if (t==0){
                        newstate = 0;
                        break;
                    } else if (newstate<t){
                        newstate = t;
                    } 
                }
            } else if (in == INOP.OR){
                for (NeuralNode n : inputNodes){
                    if (n.getState()>newstate){
                        newstate = n.getState();
                    }
                }
            } else if (in == INOP.EQ && inputNodes.size()>0){ // All equal
                newstate = 1;
                int v = this.inputNodes.get(0).getState();
                for (NeuralNode n : inputNodes)
                    if (n.getState()!=v){newstate=0;break;}
            } else if (in == INOP.LT && inputNodes.size()>0){ // In ascending order
                newstate = 1;
                int v = this.inputNodes.get(0).getState()-1; // So first checks out
                for (NeuralNode n : inputNodes)
                    if (n.getState()<=v){newstate=0;break;}
                    else {v = n.getState();}
            } else if (in == INOP.DLATCH && inputNodes.size()>0){ // Or if active, else hold
                boolean isActive = (inputNodes.get(0).getState()!=0);
                if (isActive){
                    for (NeuralNode n : inputNodes.subList(1,inputNodes.size())){
                        if (n.getState()>newstate){
                            newstate = n.getState();
                        }
                    }   
                } else {
                    newstate = lastState;
                }
            } else if (in == INOP.ADD){ // sum
                for (NeuralNode n : inputNodes)
                    newstate += n.getState();
            }
            this.setState(newstate);
            this.updateOutputs();
        }
    }

    /** 
     * Chooses which nodes to poke when values are updated 
     * Only updates output if there have been changes.
     */
    public void updateOutputs(){
        if (outputNodes.size()>0 && state!=lastState){
            if (out == OUTOP.ALL){
                for (NeuralNode n : outputNodes) {
                    n.update();
                }
            } else if (out == OUTOP.ONE){
                int r = rand.nextInt(totalValue);
                for (NeuralNode n : outputNodes){
                    r -= outWeights.get(n);
                    if (r<=0) {
                        n.update();
                        break;
                    }
                }
            } else if (out == OUTOP.ANY){
                for (NeuralNode n : outputNodes){
                    if (rand.nextDouble()<(outWeights.get(n)/(double)totalValue)){
                        n.update();
                    }
                }
            }
            lastState = state;
        }
    }
    
    /**
     * Rewards choices up to n time steps back. 
     * Rewarded choices are more likely to be chosen in the future.
     * @param n 
     */
    public void reward(int n){
        // TODO reward choices up to n cycles back
    }
    
    public void setOperationString(String op){
        if (!editableOp)
            return;
        if (op.length()==4){
            switch (op.substring(2)){
                case "AA":
                    in = INOP.OR; out = OUTOP.ALL; inverse = false;
                    break;
                case "AB": 
                    in = INOP.OR; out = OUTOP.ONE; inverse = false;
                    break;
                case "AC": 
                    in = INOP.OR; out = OUTOP.ANY; inverse = false;
                    break;
                case "AD": 
                    in = INOP.AND; out = OUTOP.ALL; inverse = false;
                    break;
                case "BA": 
                    in = INOP.AND; out = OUTOP.ONE; inverse = false;
                    break;
                case "BB": 
                    in = INOP.AND; out = OUTOP.ANY; inverse = false;
                    break;
                case "BC": 
                    in = INOP.OR; out = OUTOP.ALL; inverse = true;
                    break;
                case "BD": 
                    in = INOP.OR; out = OUTOP.ONE; inverse = true;
                    break;
                case "CA": 
                    in = INOP.OR; out = OUTOP.ANY; inverse = true;
                    break;
                case "CB": 
                    in = INOP.AND; out = OUTOP.ALL; inverse = true;
                    break;
                case "CC": 
                    in = INOP.AND; out = OUTOP.ONE; inverse = true;
                    break;
                case "CD": 
                    in = INOP.AND; out = OUTOP.ANY; inverse = true;
                    break;
                case "DA":
                    in = INOP.EQ; // Other values don't matter
                case "DB":
                    in = INOP.LT;
                case "DC":
                    in = INOP.DLATCH;
                case "DD":
                    in = INOP.ADD;
            }
        }
    }
    /**
     * Return current state of node.
     * @return state
     */
    public int getState(){
        if (inverse){
            // Inverse can only output boolean
            return (state==0)?1:0;
        } else {
            return state;
        }
    }

    public void setInverse(boolean b){
        this.inverse = b;
    }
    
    public void setState(int state){
        this.state = state;
    }
    public void setIDWeak(String s){
        if (id==null){
            id = s;
        } else {
            id = s + " ("+id+")";
        }
    }
    public void setID(String s){
        id = s;
    }
    public String getID(){
        return id;
    }
    public String opCode(){
        String inopc = "";
        switch (in){
            case AND: inopc = "&";  break;
            case OR: inopc = "|";   break;
            case EQ: inopc = "=";   break;
            case LT: inopc = "<";   break;
            case DLATCH: inopc = "D";   break;
            case ADD: inopc = "+";  break;
            case SUB: inopc = "-";  break;
        }
        String outopc = "";
        switch (out){
            case ANY: outopc = "?";  break;
            case ONE: outopc = "1";   break;
            case ALL: outopc = "*";   break;
        }
        return (inverse?"!":"")+inopc+outopc;
    }
    
    public boolean getAcceptsInput(){
        return this.acceptsInput;
    }
    public ArrayList<NeuralNode> getInputs(){
        return this.inputNodes;
    }
    public ArrayList<NeuralNode> getOutputs(){
        return this.outputNodes;
    }
    
    @Override
    public String toString(){
        String s = id + "\t["+in.name()+", "+out.name()+", "+inverse+"]";
        for (NeuralNode n : outputNodes){
            s+="\n-->"+n.getID();
        }
        return s;
    }
}
