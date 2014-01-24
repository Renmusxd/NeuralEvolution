package NeuralEvolution.NeuronClasses;

import java.util.ArrayList;

/**
 *
 * @author Sumner
 */
public class NeuralNode {
    /*
     * TODO Make sure there are no loops 
     * 
     */
    public static final boolean AND_OPERATION = false;
    public static final boolean OR_OPERATION = true;
    
    private String name = null;
    
    private boolean operationMode = false;
    
    private boolean state = false;
    private boolean inverse = false;
    
    private ArrayList<NeuralNode> inputNodes;
    private ArrayList<NeuralNode> outputNodes;
    
    public NeuralNode(boolean operation, boolean inverse){
        inputNodes = new ArrayList<NeuralNode>();
        outputNodes = new ArrayList<NeuralNode>();
        this.operationMode = operation;
        this.inverse = inverse;
    }
    public void addInput(NeuralNode nn){
        inputNodes.add(nn);
    }
    public void addOutput(NeuralNode nn){
        outputNodes.add(nn);
    }
    public void update(){
        for (NeuralNode n:inputNodes){
            if (n.getState()==operationMode){                           // If a single input is (operationMode)
                this.setState((inverse)?!operationMode:operationMode);  // set state to (operationMode) [unless inverse, then set !]
                return; 
                // You have to use a bit of logic to figure this out, but it should work, or depends on a single true, and depends on a single false.
            }
        }
        this.setState((inverse)?operationMode:!operationMode);          // set to the opposite of what it otherwise would be
    }
    public void updateOutputs(){
        for (NeuralNode n:outputNodes){n.update();}
    }
    public void setOperation(boolean operation){
        this.operationMode = operation;
        this.update();
    }
    public boolean getState(){
        return this.state;
    }
    public void setInverse(boolean b){
        this.inverse = b; this.updateOutputs();
    }
    public void setState(boolean state){
        this.state = state;
        this.updateOutputs();
    }
    public void setName(String name){
        this.name = name;
    }
}
