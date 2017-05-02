/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BabelNet;

import NLP.Word;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/**
 *
 * @author maksym
 */
public abstract class AbstractSynset extends Word{
    protected final String id; // id do synset
    protected HashSet<Edge> edges; 

    public AbstractSynset(String id, String lemma) {
        super(lemma);
        this.id = id;
    }

    protected String getId() {
        return id;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }
    
    public HashSet<String> getEdgesLemma(){
        HashSet<String> output = new HashSet<>();
        
        Iterator<Edge> i = edges.iterator();
        while (i.hasNext()) {
            Edge next = i.next();
            output.add(next.getLemma());
        }
        return output;
    }
    
    public HashSet<String> getEdgesPointer(){
        HashSet<String> output = new HashSet<>();
        
        Iterator<Edge> i = edges.iterator();
        while (i.hasNext()) {
            Edge next = i.next();
            output.add(next.getPointer());
        }
        return output;
    }
    
    protected void addEdge(Edge edge){
        //if(!edges.contains(edge)){
        edges.add(edge);
        //}
    }

    public void setEdges(HashSet<Edge> edges) {
        this.edges = edges;
    }
   

    @Override
    public String toString() {
        String output = id + "," + lemma + "\n";
        
        Iterator<Edge> i = edges.iterator();
        while(i.hasNext()){
            output += i.next().toString();
        }
        return output;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractSynset other = (AbstractSynset) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
