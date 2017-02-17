package NLP;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */
public abstract class Word {
    protected String lemma;

    public Word(String lemma) {
        this.lemma = lemma;
    }

    public String getLemma() {
        return lemma;
    }
}
