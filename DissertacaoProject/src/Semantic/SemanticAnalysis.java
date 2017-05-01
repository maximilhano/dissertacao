package Semantic;

import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class SemanticAnalysis {

    private final Language LANG = Language.PT;
    private final Language sLANG = Language.EN;
    private final BabelNet babelNet = BabelNet.getInstance();
    private final List<BabelSenseSource> babelSources = new ArrayList<>();

    public SemanticAnalysis() {
        babelSources.add(BabelSenseSource.WIKI);
        babelSources.add(BabelSenseSource.WN);
        babelSources.add(BabelSenseSource.WIKIDATA);
        babelSources.add(BabelSenseSource.OMWIKI);
        babelSources.add(BabelSenseSource.WIKIRED);
        babelSources.add(BabelSenseSource.WIKT);
        babelSources.add(BabelSenseSource.WIKITR);
    }

    /**
     * Recebe a lista de palavras processadas, adiciona synsets a cada uma e
     * devolve
     *
     * @param processedWordList
     * @return a lista de processed words com os synsets
     */
    public HashSet<ProcessedWord> setSemanticData(HashSet<ProcessedWord> processedWordList) {

        Iterator<ProcessedWord> it = processedWordList.iterator();

        while (it.hasNext()) {
            ProcessedWord next = it.next();
            if (next.getLemma() != "" && next.getPosTag() != null) {
                
                String lemma = next.getLemma().replace("_de_a_", "_da_");
                
                Map<String, BabelSynsetType> synsets = getSynsets(lemma, next.getPosTag());
                if (!synsets.isEmpty()) {
                    next.setSynsets(synsets);
                }
            }

        }

        return processedWordList;
    }

    private Map<String, BabelSynsetType> getSynsets(String lemma, BabelPOS posTag) {
        Map<String, BabelSynsetType> result = new HashMap<>();
        List<BabelSynset> babelSynsets = new ArrayList<>();
        Iterator<BabelSenseSource> sourceIt = babelSources.iterator();

        switch (posTag) {
            case NOUN:
                while (sourceIt.hasNext()) {
                    BabelSenseSource source = sourceIt.next();
                    try {
                        babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.NOUN, source);
                    } catch (IOException ex) {
                        Logger.getLogger(SemanticAnalysis.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    if (!babelSynsets.isEmpty()) { // ATIVAR
//                        break;
//                    }
                }
                break;
            case VERB: {
                try {
                    babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.VERB, BabelSenseSource.OMWN);
                } catch (IOException ex) {
                    Logger.getLogger(SemanticAnalysis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!babelSynsets.isEmpty()) {
                babelSynsets.addAll(getEdges(babelSynsets, BabelPointer.DERIVATIONALLY_RELATED));
            }
            break;
        }

        if (!babelSynsets.isEmpty()) {
            Iterator<BabelSynset> synsetIterator = babelSynsets.iterator();

            while (synsetIterator.hasNext()) {
                BabelSynset babelSynset = synsetIterator.next();
                result.put(babelSynset.getMainSense(sLANG).getLemma(), babelSynset.getSynsetType());
            }
        }

        return result;
    }

    public List<BabelSynset> getEdges(List<BabelSynset> synsets, BabelPointer pointer) {

        List<BabelSynset> edges = new ArrayList<>();

        Iterator<BabelSynset> synsetIterator = synsets.iterator();
        while (synsetIterator.hasNext()) {
            BabelSynset babelSynset = synsetIterator.next();
            Iterator<BabelSynsetIDRelation> edgeIterator = babelSynset.getEdges(pointer).iterator();
            while (edgeIterator.hasNext()) {
                try {
                    BabelSynsetIDRelation babelSynsetEdge = edgeIterator.next();
                    edges.add(babelSynsetEdge.getBabelSynsetIDTarget().toBabelSynset());
                } catch (IOException ex) {
                    Logger.getLogger(SemanticAnalysis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return edges;
    }
}
