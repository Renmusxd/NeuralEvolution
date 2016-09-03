package NeuralEvolution.NeuronClasses.NeuralTree;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.NeuronClasses.BactBrain;
import NeuralEvolution.NeuronClasses.BrainNode;
import NeuralEvolution.SpecificGameClasses.Gene;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Requires Node ID Lengths of 4 for automatic placement.
 * @author Sumner
 */
public final class NeuralTreeManager implements BactBrain {

    private String ID_LOW = "AAAA";
    
    private final ArrayList<BrainNode> inputNodes;
    private final HashMap<String,BrainNode> nodeIDMap;
    
    public NeuralTreeManager(Bact b){
        nodeIDMap = new HashMap<>();
        inputNodes = new ArrayList<>();
    }
    
    public NeuralTreeManager(Bact b, Gene[] DNA){
        this(b);
        this.parseDNA(DNA);
    }
    
    @Override
    public void update(){
        for (BrainNode n : nodeIDMap.values())
            n.clear();
        for (BrainNode n : inputNodes)
            n.updateOutputs();
    }
    
    public void registerInputNode(BrainNode n){
        registerNode(n);
        inputNodes.add(n);
    }
    
    public void registerNode(BrainNode n){
        registerNode(ID_LOW, n);
        incrementID();
    }
    
    public void registerNode(String s, BrainNode n){
        nodeIDMap.put(s, n);
        n.setIDWeak(s);
    }
    
    public String encodeNetwork(){
        return null;
    }
    
    public void reward(int n){
        
    }

    public void parseDNA(Gene[] DNA){
         for (Gene gene : DNA){
            if (gene.getPrimer()==Bact.NEURON_PRIMER){
                if (gene.getGene().length()==Bact.NEURON_LENGTH){
                    // Check if neuron exists, edit info or create
                    String n = gene.getGene().substring(0,4);
                    String op = gene.getGene().substring(4,6);
                    if (nodeIDMap.containsKey(n)){
                        nodeIDMap.get(n).setOperationString(op);
                    } else {
                        NeuralTreeNode node = new NeuralTreeNode();
                        node.setOperationString(op);
                        this.registerNode(n, node);
                    }
                }
            }
            if (gene.getPrimer()==Bact.NPATH_PRIMER){
                if (gene.getGene().length()==Bact.NPATH_LENGTH){
                    String n1 = gene.getGene().substring(0,4);
                    String n2 = gene.getGene().substring(4,8);
                    BrainNode node1; BrainNode node2;
                    if (nodeIDMap.containsKey(n1)){
                        node1 = nodeIDMap.get(n1);
                    } else {
                        node1 = new NeuralTreeNode();
                        this.registerNode(n1, node1);
                    }
                    if (nodeIDMap.containsKey(n2)){
                        node2 = nodeIDMap.get(n2);
                        node1.addOutput(node2);
                        node2.addInput(node1);
                    } else {
                        node2 = new NeuralTreeNode();
                        this.registerNode(n2, node2);
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
            int loc = Bact.ALPHABET.indexOf(x);
            if (loc == Bact.ALPHABET.length()-1){
                idArr[i] = Bact.ALPHABET.charAt(0);
                i++;
            } else {
                idArr[i] = Bact.ALPHABET.charAt(loc+1);
                break;
            }
        }
        if (i==Bact.ALPHABET.length()){
            System.out.println("[!] Ran out of registers for nodes");
        }
        ID_LOW = new String(idArr);
    }
    
    @Override
    public String toString(){
        String s = "NNetMan:\n";
        for (BrainNode n : nodeIDMap.values()){
            s+=n.toString()+"\n";
        }
        return s;
    }
    
}
