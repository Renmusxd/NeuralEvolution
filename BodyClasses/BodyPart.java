/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.BodyClasses;

import NeuralEvolution.NeuronClasses.NeuralNode;
import java.util.ArrayList;

/**
 *
 * @author Sumner
 */
public class BodyPart {
    /*
     * A predefined body part, heart, lungs, bone, muscle, etc...
     * each comes with certain features, others added on via bact DNA
     */
        
    // Heart, Lungs, Liver, Stomach, muscle, bone, leg, arm, sharpWeapon (like teeth)
    // Each body part has a reheal rate, blood use rate (can be zero [bone]), and utilities
    
    //          for speed      + bleed      marrow          heart       lungs           all         stomach         eyes        general
    private int walkingBonus, bloodConsumptionCoef, bloodProduction, bloodVolume, airationRate,
                rehealRate, digestionRate, sightDistance, muscleDensity, damageResist,
                toxinRemovalRate;
            
    private boolean neuralCenter;
    private String name;
    public ArrayList<NeuralNode> nodeList;
    
    public BodyPart(){
        this.name = "Body Part";
        this.walkingBonus = 0;
        this.bloodConsumptionCoef = 0;
        this.bloodProduction = 0;
        this.bloodVolume = 0;
        this.airationRate = 0;
        this.rehealRate = 0;
        this.digestionRate = 0;
        this.sightDistance = 0;
        this.neuralCenter = false;
        this.muscleDensity = 0;
        this.damageResist = 0;
        this.toxinRemovalRate = 0;
        this.nodeList = new ArrayList<NeuralNode>();
    }

    public void updateNodes(){
        
    }
    
    public int getWalkingBonus() {
        return walkingBonus;
    }

    public int getBloodUsage() {
        return bloodConsumptionCoef;
    }

    public int getBloodProduction() {
        return bloodProduction;
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public int getAirationRate() {
        return airationRate;
    }

    public int getRehealRate() {
        return rehealRate;
    }

    public int getDigestionRate() {
        return digestionRate;
    }

    public int getSightDistance() {
        return sightDistance;
    }

    public boolean isNeuralCenter() {
        return neuralCenter;
    }

    public String getName() {
        return name;
    }

    public void setWalkingBonus(int walkingBonus) {
        this.walkingBonus = walkingBonus;
    }

    public void setBloodUsage(int bloodConsumptionCoef) {
        this.bloodConsumptionCoef = bloodConsumptionCoef;
    }

    public void setBloodProduction(int bloodProduction) {
        this.bloodProduction = bloodProduction;
    }

    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }

    public void setAirationRate(int airationRate) {
        this.airationRate = airationRate;
    }

    public void setRehealRate(int rehealRate) {
        this.rehealRate = rehealRate;
    }

    public void setDigestionRate(int digestionRate) {
        this.digestionRate = digestionRate;
    }

    public void setSightDistance(int sightDistance) {
        this.sightDistance = sightDistance;
    }

    public void setNeuralCenter(boolean neuralCenter) {
        this.neuralCenter = neuralCenter;
    }

    public void setName(String name) {
        this.name = name;
    }
}
