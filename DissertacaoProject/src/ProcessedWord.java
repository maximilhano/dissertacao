/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */
public class ProcessedWord {
    private String lemma;
    private String tag;

    public ProcessedWord(String lemma, String tag) {
        this.lemma = lemma;
        this.tag = tag;
    }

    public String getLemma() {
        return this.lemma;
    }

    public String getTag() {
        return this.tag;
    }
    
    public void printInfo(){
        System.out.println("\n\tLemma: " + this.lemma);
        System.out.println("\n\tTag: " + this.tag);
    }
}
