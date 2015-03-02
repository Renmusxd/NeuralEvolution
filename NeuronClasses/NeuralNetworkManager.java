package NeuralEvolution.NeuronClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.SpecificGameClasses.Gene;
import java.util.HashMap;

/**
 *
 * @author Sumner
 */
public class NeuralNetworkManager {

    private HashMap<String,NeuralNode> nodeIDMap;
    
    public NeuralNetworkManager(Gene[] DNA){
        this.parseDNA(DNA);
        nodeIDMap = new HashMap<String,NeuralNode>();
    }
    
    public void registerNode(String s, NeuralNode n){
        nodeIDMap.put(s, n);
    }
    
    public String encodeNetwork(){
        return null;
    }

    private void parseDNA(Gene[] DNA){
         for (Gene gene : DNA){
            if (gene.getPrimer()==Bact.NEURON_PRIMER){
                if (gene.getGene().length()==(Bact.NEURON_LENGTH-1)){
                    // Check if neuron exists, edit info or create
                    String n = gene.getGene().substring(1,5);
                    String op = gene.getGene().substring(5,7);
                    if (nodeIDMap.containsKey(n)){
                        nodeIDMap.get(n).setOperationString(op);
                    } else {
                        NeuralNode node = new NeuralNode();
                        node.setOperationString(op);
                        this.registerNode(op, node);
                    }
                }
            }
            if (gene.getPrimer()==Bact.NPATH_PRIMER){
                if (gene.getGene().length()==(Bact.NPATH_PRIMER-1)){
                    String n1 = gene.getGene().substring(1,5);
                    String n2 = gene.getGene().substring(5,9);
                    NeuralNode node1; NeuralNode node2;
                    if (nodeIDMap.containsKey(n1)){
                        node1 = nodeIDMap.get(n1);
                        
                    } else {
                        node1 = new NeuralNode();
                    }
                    if (nodeIDMap.containsKey(n2)){
                        node2 = nodeIDMap.get(n2);
                        node1.addOutput(node2);
                        node2.addInput(node1);
                    } else {
                        node2 = new NeuralNode();
                        node1.addOutput(node2);
                        node2.addInput(node1);
                    }
                    
                }
            }
        }
    }
}
