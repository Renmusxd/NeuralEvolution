/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralevolution.NeuralEvolution.organs;

import neuralevolution.NeuralEvolution.BodyPart;

/**
 *
 * @author Sumner
 */
public class Brain extends BodyPart{
    private static final String NAME = "Brain";
    private static final int WALKING_BONUS = 1;
    private static final int BLOOD_CONSUMPTION_COEF = 10;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 1;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 0;
    private static final int DAMAGE_RESIST = 1;
    private static final int TOXIN_REMOVE_RATE = 0;
    private static final boolean NEURAL_CENTER = true;
    public Brain(){
        super(NAME,WALKING_BONUS,BLOOD_CONSUMPTION_COEF,BLOOD_PRODUCTION,BLOOD_VOLUME,AIRATION_RATE,REHEAL_RATE,DIGESTION_RATE,SIGHT_DISTANCE,MUSCLE_DENSITY,DAMAGE_RESIST,TOXIN_REMOVE_RATE,NEURAL_CENTER);
    }
}
