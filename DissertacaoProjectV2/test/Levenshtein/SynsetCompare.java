package Levenshtein;


import BabelNet.Edge;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */
public class SynsetCompare {
    private final Edge synset1;
    private final Edge synset2;
    //private final int score;

    public SynsetCompare(Edge synset1, Edge synset2/*, int score*/) {
        this.synset1 = synset1;
        this.synset2 = synset2;
        //this.score = score;
    }

    @Override
    public String toString() {
        return /*synset1.getId() + "," + synset2.getId() + "\n";*/synset2.getId() + "," + synset2.getLemma() + "\n"; //To change body of generated methods, choose Tools | Templates.
    }
    
    

    /*public int getScore() {
        return score;
    }*/
}
