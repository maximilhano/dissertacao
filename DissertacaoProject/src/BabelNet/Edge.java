/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BabelNet;

import it.uniroma1.lcl.babelnet.data.BabelPOS;

/**
 *
 * @author maksym
 */
public class Edge extends AbstractSynset{
    
    private String pointer; // adopting the terms of babelnet
    
    public Edge( String id, String lemma, String pointer) {
        super(id, lemma);
        this.pointer = pointer;
    }

    public String getPointer() {
        return pointer;
    }
    
    public String getCompareValue(BabelPOS posTag){
        if (posTag != BabelPOS.NOUN) {
            return super.lemma;
        } else {
            return pointer;
        }
    }
    
    @Override
    public String toString(){
        return super.getId() + "," + super.getLemma() + "," + pointer + "\n";
    }
}
