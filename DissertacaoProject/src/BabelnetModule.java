
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
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

    private final BabelNet bn = BabelNet.getInstance();
    private final Language lang = Language.PT;
    
    public void printSenses(List<ProcessedWord> lpw){
        for(ProcessedWord pw : lpw){
            System.out.println(getSenses(pw));
        }
    }
    
    public void printSynsets(List<ProcessedWord> lpw){
        for(ProcessedWord pw : lpw){
            System.out.println(getSynsets(pw));
        }
    }
    
    public void printEdges(List<BabelSynset> synsets){
        for(BabelSynset synset : synsets){
            //System.out.println(getEdges(synset, Ba));
        }
    }
    
    /**
     * 
     * @param synset
     * @param level indica o nivel de expançao do synset
     */
    public void synsetExpand(BabelSynset synset, int level) throws IOException{
        List<BabelSynsetIDRelation> synsetEdges = getEdges(synset, BabelPointer.ANY_HYPERNYM);
        for(BabelSynsetIDRelation synsetEdge : synsetEdges){
            System.out.println("Synset: " + synset.toString());
            printSynsetEdgeInfo(synsetEdge);
            for(BabelSynsetIDRelation synsetEdge1 : getEdges(synsetEdge.getBabelSynsetIDTarget().toBabelSynset(), BabelPointer.ANY_HYPERNYM)){
                System.out.println("Edge of Synset Edge: " + synset.toString());
                printSynsetEdgeInfo(synsetEdge1);
            }
        }
    }
    
    private void printSynsetEdgeInfo(BabelSynsetIDRelation edge){
        try {
            System.out.println("\n printSynsetEdgeInfo "
                    + "\n Pointer: " + edge.getPointer()
                    + "\n BabelSynsetIDTarget: " + edge.getBabelSynsetIDTarget()
                    + "\n EdgeSynset: " + edge.getBabelSynsetIDTarget().toBabelSynset().toString());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Função devolve todos as palavras que partilham 
     * o mesmo significado (sinonimos) de uma dada palavra
     * @param pw
     * @return 
     */
    public List<BabelSynset> getSynsets (ProcessedWord pw){
        List<BabelSynset> result = null;
        try {
            result = bn.getSynsets(pw.getLemma(), lang, pw.getposTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private List<BabelSense> getSenses (ProcessedWord pw){
        List<BabelSense> result = null;
        try {
            result = bn.getSenses(pw.getLemma(), lang, pw.getposTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private List<BabelSynsetIDRelation> getEdges(BabelSynset synset, BabelPointer pointer){
        return synset.getEdges(pointer);
    }

}
