package NLP;


import BabelNet.Edge;
import BabelNet.Synset;
import NLP.Word;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import java.util.HashSet;
import java.util.Iterator;

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
    private HashSet<String> definitions;

    public ProcessedWord(String lemma, BabelPOS posTag) {
        super(lemma);
        this.posTag = posTag;
    }

    public BabelPOS getPosTag() {
        return posTag;
    }

    public HashSet<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(HashSet<String> definitions) {
        this.definitions = definitions;
    }

}
