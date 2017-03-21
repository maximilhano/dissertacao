package Semantic;


import NLP.ProcessedWord;
import BabelNet.BabelnetModule;
import BabelNet.Synset;
import File.FileLogger;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;
import java.util.HashSet;
import java.util.List;
import Print.PrintIterator;
import java.io.IOException;
import java.util.Iterator;
import DBpedia.BDpediaModule;

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
    
    BabelnetModule bnm = new BabelnetModule();
    PrintIterator it = new PrintIterator();
    BDpediaModule dbm = new BDpediaModule();
     
    /**
     * Recebe a lista de palavras processadas, adiciona synsets a cada uma e devolve
     * @param pwords
     * @return a lista de processed words com os synsets 
     */
    public HashSet<ProcessedWord> getExpandedQuery(HashSet<ProcessedWord> pwords) throws IOException{
        
        Iterator<ProcessedWord> i = pwords.iterator();
        
        while (i.hasNext()) {
            ProcessedWord next = i.next();
            next.setSynsets(bnm.getBabelNetData(next.getLemma(), next.getPosTag()));
            System.out.println("\t ProcessedWord: " +next.getLemma() + " has definitions: " + next.getSynsets());
            
//            System.out.println("\n\t DBPEDIA results \n");
//            Iterator<String> it = next.getDefinitions().iterator();
//            while (it.hasNext()) {
//                String next1 = it.next();
//                dbm.getTriples(next1);
//            }
        }
        
        
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
        return pwords;
    }
    
    private HashSet<Synset> getExpandedSynsets(List<BabelSynset> babelSynsets){
        HashSet<Synset> synsets = new HashSet<>();
        
        Iterator<BabelSynset> i = babelSynsets.iterator();
        while (i.hasNext()) {
            BabelSynset next = i.next();
            Synset synset = new Synset(next.getId().toString(), next.getMainSense(Language.EN).getLemma());
            synset.setEdges(bnm.getEdges(0, next));
            synsets.add(synset);
        }
        return synsets;
    } 
}
