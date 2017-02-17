package Levenshtein;


import BabelNet.Edge;
import BabelNet.Synset;

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
    private final int score;

    public SynsetCompare(Edge synset1, Edge synset2, int score) {
        this.synset1 = synset1;
        this.synset2 = synset2;
        this.score = score;
    }

    @Override
    public String toString() {
        return " Edge1 lemma : " + synset1.getLemma() + "\n Edge1 pointer : " + synset1.getPointer() + "\n Edge2 lemma : " + synset2.getLemma() + "\n Edge2 pointer : " + synset2.getPointer() + "\n SCORE : " + score; //To change body of generated methods, choose Tools | Templates.
    }

    public int getScore() {
        return score;
    }
}
