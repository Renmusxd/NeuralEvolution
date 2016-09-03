package NeuralEvolution.NeuronClasses;

import java.util.Collection;

/**
 * Created by Sumner on 9/3/16.
 */
public interface BrainNode {
    void update();

    String getID();

    int getState();

    void setIDWeak(String s);

    void updateOutputs();

    void clear();

    void setOperationString(String op);

    void addInput(BrainNode node1);
    void addOutput(BrainNode node2);

    void setState(int red);

    boolean getAcceptsInput();

    Collection<BrainNode> getInputs();

    String opCode();
}
