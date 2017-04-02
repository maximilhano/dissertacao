package Semantic;

import BabelNet.BabelnetModule;
import BabelNet.Edge;
import DBpedia.BDpediaModule;

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
            if (next.getPosTag() != BabelPOS.ADVERB) {
                try {
                    next.setSynsets(getSynsets(next.getLemma(), next.getPosTag()));
                } catch (IOException ex) {
                    Logger.getLogger(SemanticAnalysis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return processedWordList;
    }

    private Map<String, BabelSynsetType> getSynsets(String lemma, BabelPOS posTag) throws IOException {
        Map<String, BabelSynsetType> result = new HashMap<>();
        List<BabelSynset> babelSynsets = null;

        switch (posTag) {
            case NOUN:
                babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.NOUN, BabelSenseSource.WIKI);
                System.out.println(" 1 synsets size: " + babelSynsets.size());
                if (babelSynsets.isEmpty()) {
                    babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.NOUN, BabelSenseSource.WIKT);
                }
                System.out.println(" 2 synsets size: " + babelSynsets.size());
                break;
            case VERB:
                babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.VERB, BabelSenseSource.OMWN);
                if(!babelSynsets.isEmpty())
                    babelSynsets.addAll(getEdges(babelSynsets, BabelPointer.DERIVATIONALLY_RELATED));
                    System.out.println("3 synsets size: " + babelSynsets.size());
                break;
        }

        if (babelSynsets != null) {
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

//Iterator<ProcessedWord> i = processedWordList.iterator();        
//        while (i.hasNext()) {
//            ProcessedWord next = i.next();
//            next.setSynsets(bnm.setSemanticData(next.getLemma(), next.getPosTag()));
//            System.out.println("\t ProcessedWord: " +next.getLemma() + " has definitions: " + next.getSynsets());
//            
////            System.out.println("\n\t DBPEDIA results \n");
////            Iterator<String> synsetIterator = next.getDefinitions().iterator();
////            while (synsetIterator.hasNext()) {
////                String next1 = synsetIterator.next();
////                dbm.getTriples(next1);
////            }
//        }
//
//            if(fl.fileExists(next.getLemma())){ // verificar se lemmas já existem na pasta
//                System.out.println("O lemma: " + next.getLemma());
//                next.setSynsets(fl.getExpandedSynsets(next.getLemma()));
//            }else{
//                List<BabelSynset> synsets = bnm.getSynsets(next);
//                next.setSynsets(getExpandedSynsets(synsets));
//                fl.saveExpandedSynsets(next);
//                synsetIterator.BabelSynset(synsets);
//            }
//}
        //setSemanticData(processedWordList);
