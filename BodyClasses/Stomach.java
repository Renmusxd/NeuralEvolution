/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

/**
 *
 * @author Sumner
 */
public class Stomach extends BodyPart{
    private static final String NAME = "Stomach";
    private static final int WALKING_BONUS = 0;
    private static final int BLOOD_CONSUMPTION_COEF = 1;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 2;
    private static final int DIGESTION_RATE = 10;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 1;
    private static final int DAMAGE_RESIST = 2;
    private static final int TOXIN_REMOVE_RATE = 1;
    private static final boolean NEURAL_CENTER = false;
    public Stomach(){
        super();
        this.setName(NAME);
        this.setBloodConsumptionCoef(BLOOD_CONSUMPTION_COEF);
        this.setDigestionRate(DIGESTION_RATE);
        this.setRehealRate(REHEAL_RATE);
        this.setToxinRemovalRate(TOXIN_REMOVE_RATE);
    }
}
