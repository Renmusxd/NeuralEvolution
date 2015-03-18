/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.SpecificGameClasses;

/**
 *
 * @author Sumner
 */
public abstract class Scorer<T> {
    public T top;
    public int topscore;
    public abstract int getScore(T b);
}
