
import it.uniroma1.lcl.babelnet.data.BabelPOS;

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
    private final String lemma;
    private final BabelPOS posTag;

    public ProcessedWord(String lemma, BabelPOS tag) {
        this.lemma = lemma;
        this.posTag = tag;
    }

    public String getLemma() {
        return this.lemma;
    }

    public BabelPOS getposTag() {
        return this.posTag;
    }
    
    public void printInfo(){
        System.out.println("\n\tLemma: " + this.lemma);
        System.out.println("\n\tTag: " + this.posTag.name());
    }
}
