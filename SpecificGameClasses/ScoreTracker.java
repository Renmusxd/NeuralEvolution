/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralEvolution.SpecificGameClasses;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that tracks different type of scores.
 * For example, high score for lifespan, high score for
 * explored area, high score for other bacts killed.
 * @author Sumner
 * @param <T> Type of object scores of which will be tracked
 */
public class ScoreTracker<T> implements Iterable<T> {
    public ArrayList<Scorer<T>> scores = new ArrayList<>();

    public void registerScore(Scorer s){
        scores.add(s);
    }
    
    public void checkScore(T t){
        for (Scorer<T> st : scores){
            int a = st.getScore(t);
            if (a>st.topscore){
                st.top = t;
                st.topscore = a;
            }
        }
    }
    
    public void clear(){
        for (Scorer<T> st : scores){
            st.top = null;
            st.topscore = 0;
        }
    }
    
    public T[] getHighScorers(){
        Object[] os = new Object[scores.size()];
        for (int i = 0; i<os.length; i++){
            os[i] = scores.get(i).top;
        }
        return (T[])os;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            int index = 0;
            @Override
            public boolean hasNext() {return index<scores.size()-1;}
            @Override
            public T next() {return scores.get(index++).top;}
        };
    }
    
    public int size(){return scores.size();}
    
    
}
