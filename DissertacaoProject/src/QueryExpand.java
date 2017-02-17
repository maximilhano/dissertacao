
import NLP.ProcessedWord;
import BabelNet.BabelnetModule;
import BabelNet.Synset;
import File.FileLogger;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.jlt.util.Language;
import java.util.HashSet;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */
public class QueryExpand {
    
    BabelnetModule bnm = new BabelnetModule();
    FileLogger fl = new FileLogger();
    
    /**
     * Recebe a lista de palavras processadas, adiciona synsets a cada uma e devolve
     * @param pwords
     * @return a lista de processed words com os synsets 
     */
    public HashSet<ProcessedWord> getExpandedQuery(HashSet<ProcessedWord> pwords){
        for(ProcessedWord pword: pwords){
            // verificar se lemmas já existem na pasta
            if(fl.fileExists(pword.getLemma())){
                pword.setSynsets(fl.getExpandedSynsets(pword.getLemma()));
            }else{
                List<BabelSynset> synsets = bnm.getSynsets(pword);
                pword.setSynsets(getExpandedSynsets(synsets));
                //fl.saveExpandedSynsets(pword);
            }
        }
        return pwords;
    }
    
    private HashSet<Synset> getExpandedSynsets(List<BabelSynset> babelSynsets){
        HashSet<Synset> synsets = new HashSet<>();
        
        for(BabelSynset babelSynset : babelSynsets){
            Synset synset = new Synset(babelSynset.getMainSense(Language.EN).getLemma(), babelSynset.getId().toString());
            synset.setEdges(bnm.getEdges(0, babelSynset));
            synsets.add(synset);
        }
        
        return synsets;
    } 
}
