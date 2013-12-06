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
public class Skin extends BodyPart {
    private static final String NAME = "Skin";
    private static final int WALKING_BONUS = 0;
    private static final int BLOOD_CONSUMPTION_COEF = 1;
    private static final int BLOOD_PRODUCTION = 0;
    private static final int BLOOD_VOLUME = 0;
    private static final int AIRATION_RATE = 0;
    private static final int REHEAL_RATE = 5;
    private static final int DIGESTION_RATE = 0;
    private static final int SIGHT_DISTANCE = 0;
    private static final int MUSCLE_DENSITY = 0;
    private static final int DAMAGE_RESIST = 5;
    private static final int TOXIN_REMOVE_RATE = 0;
    private static final boolean NEURAL_CENTER = false;
    
    private NeuralNode painNode;
    public Skin(){
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
        
        painNode = new NeuralNode(false,false);
        this.nodeList.add(painNode);
    }
    
    @Override
    public void updateNodes(){
        
    }
}
