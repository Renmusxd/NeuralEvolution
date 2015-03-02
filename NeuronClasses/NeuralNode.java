package NeuralEvolution.NeuronClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Sumner
 */
public class NeuralNode {
    /*
     * TODO Make sure there are no loops 
     * 
     */
    private static final Random rand = new Random();
    
    public enum INOP{
        AND, OR;
    }
    public enum OUTOP{
        ANY,ONE,ALL;
    }
    
    // DEFAULT
    private INOP in = INOP.OR;
    private OUTOP out = OUTOP.ALL;
    private boolean inverse;
    
    private int state;
    
    private ArrayList<NeuralNode> inputNodes;
    private ArrayList<NeuralNode> outputNodes;
    
    private HashMap<NeuralNode,Integer> outWeights;
    
    /** Number of updates this cycle, limited to #inputs **/
    private byte updates;
    
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
    }
    public void clear(){
        updates = 0;
    }
    public void update(){
        updates++;
        if (updates<=inputNodes.size()){
            // TODO Check input nodes, calculate output
            int newstate = 0;
            if (in == INOP.AND){
                newstate = 1;
                for (NeuralNode n : inputNodes){
                    if (n.getState()==0){
                        newstate = 0;
                        break;
                    }
                }
            } else if (in == INOP.OR){
                for (NeuralNode n : inputNodes){
                    if (n.getState()!=0){
                        newstate = 1;
                        break;
                    }
                }
            }
            if (inverse){
                this.setState((newstate==1)?0:1);
            } else {
                this.setState(newstate);
            }
        }
    }
    public void updateOutputs(){
        if (out == OUTOP.ALL){
            for (NeuralNode n : outputNodes) {
                n.update();
            }
        } else if (out == OUTOP.ONE){
            outputNodes.get(rand.nextInt(outputNodes.size())).update();
        } else if (out == OUTOP.ANY){
            
        }
    }
    public void setOperationString(String op){
        // 12 Possibilities
        if (op.length()==2){
            switch (op){
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
                default:
                    in = INOP.OR; out = OUTOP.ALL; inverse = false;
            }
        }
    }
    
    public int getState(){
        return this.state;
    }
    public void setInverse(boolean b){
        this.inverse = b; this.updateOutputs();
    }
    public void setState(int state){
        this.state = state;
        this.updateOutputs();
    }
}
