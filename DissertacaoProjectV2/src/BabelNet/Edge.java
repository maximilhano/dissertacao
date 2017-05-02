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
    
    private final String pointer; // adopting the terms of babelnet, pointer shows the relation
    
    public Edge( String id, String lemma, String pointer) {
        super(id, lemma);
        this.pointer = pointer;
    }

    public String getPointer() {
        return pointer;
    }
    
    public String getCompareValue(BabelPOS posTag){
        if (posTag != BabelPOS.NOUN) {
            return lemma;
        } else {
            return pointer;
        }
    }
       
    @Override
    public String toString(){
        return id + "," + lemma + "," + pointer + "\n";
    }

    @Override
    public String getId() {
        return id; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
