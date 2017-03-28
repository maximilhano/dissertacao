/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author maksym
 */
public class LocalDatabase {

    private final String DB = "http://localhost:3030/default/";
    private final String dbpediaEndpoint = "http://dbpedia.org/sparql/";
    private final String fusekiQuery = DB + "query";
    private final String fusekiUpdate = DB + "update";

    private final String PREFIX = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX : <http://dbpedia.org/resource/>\n"
            + "PREFIX dbpedia2: <http://dbpedia.org/property/>\n"
            + "PREFIX dbpedia: <http://dbpedia.org/>\n"
            + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n ";
    
    private String selectQuery = PREFIX;

    
    
    private void getPredicates(String object){
        
    }
    
    private void getObject(){
        
    }
    
    private void constructNamedEntityQuery(String namedEntity){
        selectQuery += "SELECT ?p ?o ?type WHERE "
                     + "{ :" + namedEntity + " ?p ?o ."
                     + "?p rdf:type ?ptype . "
                     + "?o rdf:type ?otype . }";
    }
    
    private void runSelectQuery(String query){
        
    }
    
    private void runUpdateQuery(String query){
        
    }

    public HashSet<String> getTriples(HashSet<String> entitiesInQuestion, HashSet<String> conceptsInQuestion) {
        
    }
}
