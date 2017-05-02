
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
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
public class Relation {
    private BabelSynsetID synsetId;
    private String synsetLemma;
    private BabelPointer pointer;// identifica a relação existente entre o synset e o edge
    private HashSet<Relation> edges;

    public Relation(BabelSynsetID synsetId, String synsetLemma, BabelPointer pointer) {
        this.synsetId = synsetId;
        this.synsetLemma = synsetLemma;
        this.pointer = pointer;
    }
    
    public void addEdge(BabelSynsetID edgeSynsetId, String edgeSynsetLemma, BabelPointer pointer){
        edges.add(new Relation(synsetId, synsetLemma, pointer));
    }

    public BabelSynsetID getSynsetId() {
        return synsetId;
    }

    public String getSynsetLemma() {
        return synsetLemma;
    }

    public BabelPointer getPointer() {
        return pointer;
    }

    public HashSet<Relation> getEdges() {
        return edges;
    }
    
    @Override
    public String toString(){
        String output = getSynsetId() + "," + getSynsetLemma();
        if(pointer != null){
            output += "," + pointer.getName() + "\n";
        }else{
            output+= "\n";
        }
        
        if(!edges.isEmpty()){
            Iterator iterator = edges.iterator();
            while(iterator.hasNext()){
                output+= iterator.next().toString();
            }
        }
        return output;
    }

    public void setSynsetId(BabelSynsetID synsetId) {
        this.synsetId = synsetId;
    }
    
    
}
