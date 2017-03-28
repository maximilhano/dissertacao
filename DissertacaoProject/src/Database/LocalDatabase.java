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
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

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
    
    private final String selectQuery = PREFIX;
    private final String updateQuery = PREFIX + "INSERT DATA { ";

    public void getTriples(HashSet<String> entitiesInQuestion/*, HashSet<String> conceptsInQuestion*/) {
        HashSet<String> properties = null;
        
        Iterator<String> entitiesInQuestionIterator = entitiesInQuestion.iterator();
        while (entitiesInQuestionIterator.hasNext()) {
            String entity = entitiesInQuestionIterator.next();
            String query = constructNamedEntitySelectQuery(entity);
            ResultSet queryResult = runSelectQuery(query);
            String upQuery = constructNamedEntityUpdateQuery(queryResult, entity);
            runUpdateQuery(upQuery);
        }
        
//        Iterator<String> conceptsInQuestionIterator = conceptsInQuestion.iterator();
//        while (conceptsInQuestionIterator.hasNext()) {
//            String concept = entitiesInQuestionIterator.next();
//            String query = constructConceptSelectQuery(concept);
//            ResultSet queryResult = runSelectQuery(query);
//            if(queryResult != null){
//                String upQuery = constructConceptUpdateQuery(queryResult, concept);
//                runUpdateQuery(upQuery);
//            }else{ // se resultados forem nulos, é uma propriedade
//                properties.add(concept);
//            }
//        }
//        return properties;
    }
    
    private String constructNamedEntitySelectQuery(String namedEntity){
       return "SELECT ?p ?o ?type WHERE "
                     + "{ :" + namedEntity + " ?p ?o ."
                     + "?p rdf:type ?ptype . "
                     + "?o rdf:type ?otype . }";
    }
    
    private String constructNamedEntityUpdateQuery(ResultSet result, String entity){
        QuerySolution soln;
        String query = "";
        while (result.hasNext()) {
            soln = result.nextSolution();
            query += " :" + entity + " <" + soln.get("p") + "> <" + soln.get("o") + "> . <" + soln.get("o") + "> rdf:type <" + soln.get("type") + "> .";
        }
        query += "}";
        
        return query;
    }
    
    private ResultSet runSelectQuery(String query){
        QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpediaEndpoint, PREFIX + query);
        return qexec.execSelect();
    }
    
    private void runUpdateQuery(String query){
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery), fusekiUpdate);
        upp.execute();
    }

//    private String constructConceptSelectQuery(String concept) {
//        return "SELECT * WHERE { " +
//                " ?s ?p owl:Class ." +
//                " FILTER regex(?s,\""+concept+"\",\"i\") }";
//    }
//
//    private String constructConceptUpdateQuery(ResultSet queryResult, String concept) {
//        
//    }
//
//    
}
