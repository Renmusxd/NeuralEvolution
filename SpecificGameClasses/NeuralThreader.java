/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralEvolution.SpecificGameClasses;

import NeuralEvolution.BodyClasses.Bact;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The NeuralThreader class updates the neural networks of all bacts
 * provided in parallel. This allows a much faster update cycle for the game.
 * The normal updates are done linearly.
 * @author Sumner
 */
public class NeuralThreader {
    /**
     * Updates all neural networks, returns after all have been updated.
     * @param bacts 
     */
    public static void updateNetworks(ArrayList<Bact> bacts){
        int lockNum = bacts.size();
        int i = 0;
        Thread[] ts = new Thread[lockNum];
        for (Bact b : bacts){
            Runnable r = () -> {
                b.updateNeurons();
            };
            ts[i] = new Thread(r);
            ts[i].start();
            i++;
        }
        for (i=0;i<lockNum;i++)
            try {ts[i].join();}
            catch (InterruptedException ex) {}
    }
    
    
}
