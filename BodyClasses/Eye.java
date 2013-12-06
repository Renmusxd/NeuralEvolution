/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

/**
 *
 * @author Sumner
 */
public class Eye extends BodyPart{
    private static final String NAME = "Eye";
    private static final int WALKING_BONUS = 0;
    private static final int BLOOD_CONSUMPTION_COEF = 1;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 2;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 0;
    private static final int DAMAGE_RESIST = 1;
    private static final int TOXIN_REMOVE_RATE = 0;
    private static final boolean NEURAL_CENTER = false;
    public Eye(){
        super();
        this.setName(NAME);
        this.setAirationRate(AIRATION_RATE);
        this.setBloodUsage(BLOOD_CONSUMPTION_COEF);
        this.setBloodProduction(BLOOD_PRODUCTION);
        this.setBloodVolume(BLOOD_VOLUME);
        this.setDigestionRate(DIGESTION_RATE);
        this.setNeuralCenter(NEURAL_CENTER);
        this.setRehealRate(REHEAL_RATE);
        this.setSightDistance(SIGHT_DISTANCE);
        this.setWalkingBonus(WALKING_BONUS);
    }
}