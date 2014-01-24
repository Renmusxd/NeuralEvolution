/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.NeuronClasses.NeuralNode;

/**
 *
 * @author Sumner
 */
public class Muscle extends BodyPart{
    private static final String NAME = "Muscle";
    private static final int SIZE = 0; //special
    private static final int WALKING_BONUS = 2;
    private static final int BLOOD_CONSUMPTION_COEF = 2;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 2;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 2;
    private static final int DAMAGE_RESIST = 2;
    private static final int TOXIN_REMOVE_RATE = 0;
    private static final boolean NEURAL_CENTER = false;
    
    private NeuralNode pushMuscle, pullMuscle; // mostly for legs, moving side forward and backwards
    
    public Muscle(){
        super();
        this.setName(NAME);
        this.setBloodConsumptionCoef(BLOOD_CONSUMPTION_COEF);
        this.setRehealRate(REHEAL_RATE);
        this.setWalkingBonus(WALKING_BONUS);
        this.setDamageResist(DAMAGE_RESIST);
        
        pushMuscle = new NeuralNode(NeuralNode.OR_OPERATION,false);
        pullMuscle = new NeuralNode(NeuralNode.OR_OPERATION,false);
        this.nodeList.add(pushMuscle);
        this.nodeList.add(pullMuscle);
    }
    @Override
    public boolean isPushingBact(){return (pushMuscle.getState()||pullMuscle.getState());} 
    
    public boolean isPushing(){return pushMuscle.getState();}
    public boolean isPulling(){return pullMuscle.getState();}
    
}
