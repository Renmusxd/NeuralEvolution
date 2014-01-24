package NeuralEvolution.BodyClasses;

/**
 *
 * @author Sumner
 */
public class Bone extends BodyPart{
    private static final String NAME = "Bone";
    private static final int SIZE = 500;
    private static final int WALKING_BONUS = 0;
    private static final int BLOOD_CONSUMPTION_COEF = 0;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 1;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 0;
    private static final int DAMAGE_RESIST = 20;
    private static final int TOXIN_REMOVE_RATE = 0;
    private static final boolean NEURAL_CENTER = false;
    public Bone(){
        super();
        this.setName(NAME);
        this.setRehealRate(REHEAL_RATE);
        this.setWalkingBonus(WALKING_BONUS);
        this.setDamageResist(DAMAGE_RESIST);
    }
}
