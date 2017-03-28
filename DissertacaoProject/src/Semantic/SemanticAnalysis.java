package Semantic;

import BabelNet.BabelnetModule;
import DBpedia.BDpediaModule;

import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
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
public class SemanticAnalysis {

    private final Language LANG = Language.PT;
    private final Language sLANG = Language.EN;
    private final BabelNet babelNet = BabelNet.getInstance();
    private final BDpediaModule dbm = new BDpediaModule();

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
            next.setSynsets(getSynsets(next.getLemma(), next.getPosTag()));
        }

        //Iterator<ProcessedWord> i = processedWordList.iterator();        
//        while (i.hasNext()) {
//            ProcessedWord next = i.next();
//            next.setSynsets(bnm.setSemanticData(next.getLemma(), next.getPosTag()));
//            System.out.println("\t ProcessedWord: " +next.getLemma() + " has definitions: " + next.getSynsets());
//            
////            System.out.println("\n\t DBPEDIA results \n");
////            Iterator<String> it = next.getDefinitions().iterator();
////            while (it.hasNext()) {
////                String next1 = it.next();
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
//                it.BabelSynset(synsets);
//            }
        //}
        //setSemanticData(processedWordList);
        return processedWordList;
    }

    private HashSet<String> getSynsets(String lemma, BabelPOS posTag) throws IOException {
        List<String> result = null;
        List<BabelSynset> babelSynsets = null;

        switch (posTag) {
            case NOUN:
                babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.NOUN, BabelSenseSource.WIKI);
                break;
            case VERB:
                babelSynsets = babelNet.getSynsets(lemma, LANG, BabelPOS.VERB, BabelSenseSource.OMWN);
            default:
                babelSynsets = babelNet.getSynsets(lemma, LANG).iterator();
                break;
        }

        try {
            result = babelNet.getSynsets(lemma, LANG, posTag);
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

//    
//    private HashSet<Synset> getExpandedSynsets(List<BabelSynset> babelSynsets){
//        HashSet<Synset> synsets = new HashSet<>();
//        
//        Iterator<BabelSynset> i = babelSynsets.iterator();
//        while (i.hasNext()) {
//            BabelSynset next = i.next();
//            Synset synset = new Synset(next.getId().toString(), next.getMainSense(Language.EN).getLemma());
//            synset.setEdges(bnm.getEdges(0, next));
//            synsets.add(synset);
//        }
//        return synsets;
//    } 
}
