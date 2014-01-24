package NeuralEvolution.SpecificGameClasses;

/**
 *
 * @author Sumner
 */
public class Gene {
    // primer gene pair class
    char primer;
    String gene;
    public Gene(char primer, String gene){this.primer = primer; this.gene = gene;}
    public char getPrimer() {return primer;}
    public String getGene() {return gene;}
}
