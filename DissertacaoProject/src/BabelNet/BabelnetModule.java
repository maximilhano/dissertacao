package BabelNet;

import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maksym
 */
public class BabelnetModule {

    private final BabelNet babelNet = BabelNet.getInstance();
    private final Language LANG = Language.PT;
    private final Language sLANG = Language.EN;

    /**
     * Função devolve todos as palavras que partilham o mesmo significado
     * (sinonimos) de uma dada palavra
     *
     * @param lemma
     * @param posTag
     * @return
     */
    public List<BabelSynset> getSynsets(String lemma, BabelPOS posTag) {
        List<BabelSynset> result = null;
        
        switch (posTag) {
                case NOUN:
                    result = babelNet.getSynsets(next.getLemma(), LANG, BabelPOS.NOUN, BabelSenseSource.WIKI));
                default:
                    it = babelNet.getSynsets(lemma, LANG).iterator();
                    break;
            }
        
        
        
        try {
            result = babelNet.getSynsets(lemma, LANG, posTag);
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public HashSet<Edge> getEdges(int level, BabelSynset synset) {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<String> ids = new HashSet<>(); // armazena os id's de synsets, e serve para controlar o id's repetidos

        for (BabelSynsetIDRelation edge : synset.getEdges()) {
            String edgeSynsetId = edge.getBabelSynsetIDTarget().toString();

            if (isRightPointer(edge.getPointer()) && !ids.contains(edgeSynsetId)) {
                try {
                    String pointer = edge.getPointer().getName(); // identifica a relação existente entre o synset e edges
                    String edgeSynsetLemma = edge.getBabelSynsetIDTarget().toBabelSynset().getMainSense(Language.EN).getLemma();
                    Edge edgeSynset = new Edge(edgeSynsetId, edgeSynsetLemma, pointer);

                    for (int i = 0; i < level; i++) { // consoante o nivel de expanção, adiciona edges do edge
                        edgeSynset.setEdges(getEdges(0, edge.getBabelSynsetIDTarget().toBabelSynset()));
                    }
                    //System.out.println("Pointer: " + pointer + " | edgeSynsetID: " + edgeSynsetId + " | edgeLemma: " + edgeSynsetLemma);
                    ids.add(edgeSynsetId);
                    edges.add(edgeSynset);
                } catch (IOException ex) {
                    Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return edges;
    }

    public HashSet<ProcessedWord> setSemanticData(HashSet<ProcessedWord> pwords) throws IOException {
        Iterator<ProcessedWord> it = lpw.iterator();

        while (it.hasNext()) {
            ProcessedWord next = it.next();

            switch (next.getPosTag()) {
                case NOUN:
                    next.setSynsets(babelNet.getSynsets(next.getLemma(), LANG, BabelPOS.NOUN, BabelSenseSource.WIKI));
                default:
                    it = babelNet.getSynsets(lemma, LANG).iterator();
                    break;
            }
        }

        while (it.hasNext()) {
            result.add(it.next().getMainSense(sLANG).getLemma());
        }

        return lpw;
    }

    private boolean isRightPointer(BabelPointer pointer) {
        return !pointer.getSymbol().equals("r");
    }
}
