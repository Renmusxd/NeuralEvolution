package NeuralEvolution.NeuronClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.GameClasses.Updatable;
import NeuralEvolution.SpecificGameClasses.Gene;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Requires Node ID Lengths of 4 for automatic placement.
 * @author Sumner
 */
public final class NeuralNetworkManager implements Updatable {

    private String ID_LOW = "AAAA";
    
    private final ArrayList<NeuralNode> inputNodes;
    private final HashMap<String,NeuralNode> nodeIDMap;
    
    public NeuralNetworkManager(Bact b){
        nodeIDMap = new HashMap<>();
        inputNodes = new ArrayList<>();
    }
    
    public NeuralNetworkManager(Bact b, Gene[] DNA){
        this(b);
        this.parseDNA(DNA);
    }
    
    @Override
    public void update(){
        for (NeuralNode n : nodeIDMap.values())
            n.clear();
        for (NeuralNode n : inputNodes)
            n.updateOutputs();
    }
    
    public void registerInputNode(NeuralNode n){
        registerNode(n);
        inputNodes.add(n);
    }
    
    public void registerNode(NeuralNode n){
        registerNode(ID_LOW, n);
        incrementID();
    }
    
    public void registerNode(String s, NeuralNode n){
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
                        NeuralNode node = new NeuralNode();
                        node.setOperationString(op);
                        this.registerNode(n, node);
                    }
                }
            }
            if (gene.getPrimer()==Bact.NPATH_PRIMER){
                if (gene.getGene().length()==Bact.NPATH_LENGTH){
                    String n1 = gene.getGene().substring(0,4);
                    String n2 = gene.getGene().substring(4,8);
                    String s_n1;
                    String s_n2;
                    NeuralNode node1; NeuralNode node2;
                    if (nodeIDMap.containsKey(n1)){
                        node1 = nodeIDMap.get(n1);
                        s_n1 = n1;
                    } else {
                        node1 = new NeuralNode();
                        this.registerNode(n1, node1);
                    }
                    if (nodeIDMap.containsKey(n2)){
                        node2 = nodeIDMap.get(n2);
                        node1.addOutput(node2);
                        node2.addInput(node1);
                        s_n2 = n2;
                    } else {
                        node2 = new NeuralNode();
                        node1.addOutput(node2);
                        node2.addInput(node1);
                        this.registerNode(n2, node2);
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
        for (NeuralNode n : nodeIDMap.values()){
            s+=n.toString()+"\n";
        }
        return s;
    }
    
}
