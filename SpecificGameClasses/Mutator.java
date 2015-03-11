/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralEvolution.SpecificGameClasses;

import NeuralEvolution.BodyClasses.Bact;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * A class to mutate genes
 * @author Sumner
 */
public class Mutator {
    private final static Random r = new Random();
    
    public static Gene[] makeChild(Gene[] a, Gene[] b, double mutFreq){
        Gene[] newGenes = merge(a,b);
        newGenes = swap(newGenes,mutFreq);
        newGenes = changeBase(newGenes,mutFreq);
        return newGenes;
    }
    
    /**
     * Merges two sets of genes into a third set composed of selections from the
     * two others.
     * @param a
     * @param b
     * @return 
     */
    public static Gene[] merge(Gene[] a, Gene[] b){
        Gene[] c;
        if (a.length!=b.length){
            int d = Math.abs(a.length-b.length);
            int e = Math.min(a.length, b.length);
            c = new Gene[r.nextInt(d)+e];
        } else {
            c = new Gene[a.length];
        }
        for (int i = 0; i<c.length; i++){
            Gene g1;
            Gene g2;
            if (i>=a.length){
                g1 = a[r.nextInt(a.length)];
            } else {
                g1 = a[i];
            }
            if (i>=b.length){
                g2 = b[r.nextInt(b.length)];
            } else {
                g2 = b[i];
            }
            c[i] = (r.nextBoolean())?g1:g2;
        }
        return c;
    }
    /**
     * Randomly replaces some genes with others in the Gene pool. A swap is
     * defined as copying one gene into the position of another gene, removing a
     * gene, or adding a copy of a gene.
     * @param a original set of genes to draw from
     * @param mutFreq probability for expected number of swaps
     * @return g new genes
     */
    public static Gene[] swap(Gene[] a, double mutFreq){
        ArrayList<Gene> newGene = new ArrayList<>(Arrays.asList(a));
        ArrayList<Gene> addedGenes = new ArrayList<>();
        ArrayList<Gene> removedGenes = new ArrayList<>();
        double continueFreq = 
                (newGene.size()*mutFreq)/
                (1 + newGene.size()*mutFreq);
        while (r.nextDouble()<=continueFreq){
            Gene selGene = a[r.nextInt(a.length)];
            switch(r.nextInt(3)){
                case 0: addedGenes.add(selGene); break;
                case 1: removedGenes.add(selGene); break;
                case 2: newGene.set(r.nextInt(a.length),selGene); // No need to copy
            }
        }
        newGene.addAll(addedGenes);
        newGene.removeAll(removedGenes);
        return newGene.toArray(new Gene[0]);
    }
    /**
     * Randomly changes single nucleotides in the gene list
     * @param a original set of genes to draw from
     * @param mutFreq Expected number of mutations = a.length * mutFreq
     *        roughly equivalent to each base chance to mutate
     * @return g new genes
     */
    public static Gene[] changeBase(Gene[] a, double mutFreq){
        StringBuilder s = new StringBuilder();
        for (Gene g : a)
            s.append(g.getGene());
        char[] newDNA = s.toString().toCharArray();
        // Make changes
        double continueFreq = 
                (newDNA.length*mutFreq)/
                (1 + newDNA.length*mutFreq);
        while (r.nextDouble()<=continueFreq){
            newDNA[r.nextInt(newDNA.length)] = 
                    Bact.ALPHABET.charAt(r.nextInt(Bact.ALPHABET.length()));
        }
        return Bact.makeGenesFromString(new String(newDNA));
    }
}
