package NeuralEvolution.NeuronClasses;

import NeuralEvolution.GameClasses.Updatable;
import NeuralEvolution.SpecificGameClasses.Gene;

/**
 * Created by Sumner on 9/3/16.
 */
public interface BactBrain extends Updatable {
    void registerNode(BrainNode outputnode);

    void registerInputNode(BrainNode on);

    void parseDNA(Gene[] dna);

    void reward(int rewardAmount);
}
