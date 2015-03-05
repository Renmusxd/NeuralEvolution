package NeuralEvolution.NeuronClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.SpecificGameClasses.Gene;
import java.util.HashMap;

/**
 * Requires Node ID Lengths of 4 for automatic placement.
 * @author Sumner
 */
public final class NeuralNetworkManager {

    private String ID_LOW = "AAAA";
    
    private final HashMap<String,NeuralNode> nodeIDMap;
    
    public NeuralNetworkManager(Bact b){
        nodeIDMap = new HashMap<>();
    }
    
    public NeuralNetworkManager(Bact b, Gene[] DNA){
        this(b);
        this.parseDNA(DNA);
    }
    
    public void update(){
        // TODO update input nodes from Bact Body
    }
    
    public void registerNode(NeuralNode n){
        registerNode(ID_LOW, n);
        incrementID();
    }
    
    public void registerNode(String s, NeuralNode n){
        nodeIDMap.put(s, n);
    }
    
    public String encodeNetwork(){
        return null;
    }

    public void parseDNA(Gene[] DNA){
         for (Gene gene : DNA){
            if (gene.getPrimer()==Bact.NEURON_PRIMER){
                if (gene.getGene().length()==Bact.NEURON_LENGTH){
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
                if (gene.getGene().length()==Bact.NPATH_PRIMER){
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
    
    private void incrementID(){
        // Increment starting on left
        char[] idArr = ID_LOW.toCharArray();
        
        int i = 0;
        while (i<Bact.ALPHABET.length()) {
            char x = idArr[i];
            if (Bact.ALPHABET.indexOf(x) == Bact.ALPHABET.length()-1){
                idArr[i] = Bact.ALPHABET.charAt(0);
                i++;
            } else {
                idArr[i] = Bact.ALPHABET.charAt(i+1);
                break;
            }
        }
        if (i==Bact.ALPHABET.length()){
            System.out.println("[!] Ran out of registers for nodes");
        }
        ID_LOW = new String(idArr);
    }
    
}
