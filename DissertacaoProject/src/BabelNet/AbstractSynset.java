/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BabelNet;

import NLP.Word;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author maksym
 */
public abstract class AbstractSynset extends Word{
    private String id;
    private HashSet<Edge> edges;

    public AbstractSynset(String id, String lemma) {
        super(lemma);
        this.id = id;
    }

    protected String getId() {
        return id;
    }

    protected HashSet<Edge> getEdges() {
        return edges;
    }
    
    protected void addEdge(Edge edge){
        if(!edges.contains(edge)){
            edges.add(edge);
        }
    }

    public void setEdges(HashSet<Edge> edges) {
        this.edges = edges;
    }
    
    public String toStirng(){
        String output = id + "," + super.getLemma() + "\n";
        Iterator iterator = edges.iterator();
        while(iterator.hasNext()){
            output += iterator.next().toString();
        }
        return output;
    }
}
