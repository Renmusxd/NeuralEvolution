/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralEvolution.SpecificGameClasses;

import NeuralEvolution.BodyClasses.Bact;
import java.util.ArrayList;

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
        // TODO threaded updates
    }
    
}
