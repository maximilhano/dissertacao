package NLP;


import BabelNet.Edge;
import BabelNet.Synset;
import NLP.Word;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */
public class ProcessedWord extends Word{
    private final BabelPOS posTag;
    private HashSet<Synset> synsets = new HashSet<>();

    public ProcessedWord(String lemma, BabelPOS posTag) {
        super(lemma);
        this.posTag = posTag;
    }

    public BabelPOS getPosTag() {
        return posTag;
    }

    public HashSet<Synset> getSynsets() {
        return synsets;
    }

    public void setSynsets(HashSet<Synset> synsets) {
        this.synsets = synsets;
    }
    
    public HashSet<String> getEdgesLemma(){
        HashSet<String> output = new HashSet<>();
        for(Synset synset : synsets){
            output.addAll(synset.getEdgesLemma());
        }
        return output;
    }
    
    public HashSet<Edge> getEdges(){
        HashSet<Edge> output = new HashSet<>();
        for(Synset synset : synsets){
            output.addAll(synset.getEdges());
        }
        return output;
    }
    
    
    public HashSet<String> getEdgesPointer(){
        HashSet<String> output = new HashSet<>();
        for(Synset synset : synsets){
            output.addAll(synset.getEdgesPointer());
        }
        return output;
    }
    
}
