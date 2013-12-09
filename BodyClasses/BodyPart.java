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
    public static final int MAX_HEALTH = 100;
    
    private int health = MAX_HEALTH;
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

    public void update(){
        if (health<MAX_HEALTH){
            health+=rehealRate;
        }
    }
    
    public void updateNodes(){
        
    }
    
    public void damage(){
        
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getWalkingBonus() {
        return walkingBonus;
    }

    public void setWalkingBonus(int walkingBonus) {
        this.walkingBonus = walkingBonus;
    }

    public int getBloodConsumptionCoef() {
        return bloodConsumptionCoef;
    }

    public void setBloodConsumptionCoef(int bloodConsumptionCoef) {
        this.bloodConsumptionCoef = bloodConsumptionCoef;
    }

    public int getBloodProduction() {
        return bloodProduction;
    }

    public void setBloodProduction(int bloodProduction) {
        this.bloodProduction = bloodProduction;
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }

    public int getAirationRate() {
        return airationRate;
    }

    public void setAirationRate(int airationRate) {
        this.airationRate = airationRate;
    }

    public int getRehealRate() {
        return rehealRate;
    }

    public void setRehealRate(int rehealRate) {
        this.rehealRate = rehealRate;
    }

    public int getDigestionRate() {
        return digestionRate;
    }

    public void setDigestionRate(int digestionRate) {
        this.digestionRate = digestionRate;
    }

    public int getSightDistance() {
        return sightDistance;
    }

    public void setSightDistance(int sightDistance) {
        this.sightDistance = sightDistance;
    }

    public int getMuscleDensity() {
        return muscleDensity;
    }

    public void setMuscleDensity(int muscleDensity) {
        this.muscleDensity = muscleDensity;
    }

    public int getDamageResist() {
        return damageResist;
    }

    public void setDamageResist(int damageResist) {
        this.damageResist = damageResist;
    }

    public int getToxinRemovalRate() {
        return toxinRemovalRate;
    }

    public void setToxinRemovalRate(int toxinRemovalRate) {
        this.toxinRemovalRate = toxinRemovalRate;
    }

    public boolean isNeuralCenter() {
        return neuralCenter;
    }

    public void setNeuralCenter(boolean neuralCenter) {
        this.neuralCenter = neuralCenter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NeuralNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<NeuralNode> nodeList) {
        this.nodeList = nodeList;
    }
    
    
}
