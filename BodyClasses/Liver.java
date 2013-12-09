/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

/**
 *
 * @author Sumner
 */
public class Liver extends BodyPart{
    private static final String NAME = "Liver";
    private static final int SIZE = 1887;
    private static final int WALKING_BONUS = 0;
    private static final int BLOOD_CONSUMPTION_COEF = 5;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 4;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 0;
    private static final int DAMAGE_RESIST = 2;
    private static final int TOXIN_REMOVE_RATE = 5;
    private static final boolean NEURAL_CENTER = false;
    public Liver(){
        super();
        this.setName(NAME);
        this.setBloodConsumptionCoef(BLOOD_CONSUMPTION_COEF);
        this.setRehealRate(REHEAL_RATE);
        this.setDamageResist(DAMAGE_RESIST);
        this.setToxinRemovalRate(TOXIN_REMOVE_RATE);
    }
}
