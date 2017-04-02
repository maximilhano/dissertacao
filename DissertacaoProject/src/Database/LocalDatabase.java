/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.HashSet;
import java.util.Iterator;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
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

    private final String updateQuery = PREFIX + "INSERT DATA { ";

    public void getTriples(HashSet<String> entitiesInQuestion) {
        Iterator<String> entitiesInQuestionIterator = entitiesInQuestion.iterator();
        while (entitiesInQuestionIterator.hasNext()) {
            String entity = entitiesInQuestionIterator.next();
            String query = constructNamedEntitySelectQuery(entity);
            ResultSet queryResult = runSelectQuery(query);
            String upQuery = constructNamedEntityUpdateQuery(queryResult, entity);
            runUpdateQuery(upQuery);
        }
    }

    private String constructNamedEntitySelectQuery(String namedEntity) {
        return "SELECT * WHERE "
                + "{ :" + namedEntity + " ?p ?o ."
                + "OPTIONAL { ?p rdf:type ?ptype }"
                + "OPTIONAL { ?o rdf:type ?otype }}";
    }

    private String constructNamedEntityUpdateQuery(ResultSet result, String entity) {
        QuerySolution soln;
        String query = updateQuery;
        while (result.hasNext()) {
            soln = result.nextSolution();

            RDFNode predicate = soln.get("p");
            RDFNode object = soln.get("o");
            RDFNode predicateType = soln.get("ptype");
            RDFNode objectType = soln.get("otype");

            if (predicate != null && object != null) {
                if (object.isLiteral()) {
                    String[] split = null;
                    if (object.toString().contains("@")) {
                        split = object.toString().split("@");
                        query += " :" + entity + " <" + predicate + "> \"" + split[0] + "\"@" + split[1] + " .";
                        System.out.println(" :" + entity + " <" + predicate + "> \"" + split[0] + "\"@" + split[1] + " .");
                    } else if (object.toString().contains("^^")) {
                        split = object.toString().split("http://www.w3.org/2001/XMLSchema#");
                        
                        if (split.length > 1) {
                            split[0] = split[0].replace("^", "");
                            query += " :" + entity + " <" + predicate + "> \"" + split[0] + "\"^^xsd:" + split[1] + " .";
                            System.out.println(" :" + entity + " <" + predicate + "> \"" + split[0] + "\"^^xsd:" + split[1] + " .");
                        }
                    }
                } else {
                    //query += " :" + entity + " <" + predicate + "> <" + object + "> .";
                }
            }

            //System.out.println("UPDATE QUERY: " + query);
//            if (predicateType != null) {
//                query += " <" + predicate + "> rdf:type <" + predicateType + "> .";
//            }
//
//            if (objectType != null) {
//                query += " <" + object + "> rdf:type <" + objectType + "> .";
//            }
        }
        query += " }";

        return query;
    }

    private ResultSet runSelectQuery(String query) {
        QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpediaEndpoint, PREFIX + query);
        ResultSet result = qexec.execSelect();
        //printQueryResult(result);
        return result;
    }

    private void runUpdateQuery(String query) {
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(query), fusekiUpdate);
        upp.execute();
    }

    private void printQueryResult(ResultSet result) {
        QuerySolution soln;
        while (result.hasNext()) {
            soln = result.nextSolution();

            RDFNode predicate = soln.get("p");
            RDFNode object = soln.get("o");
            RDFNode predicateType = soln.get("ptype");
            RDFNode objectType = soln.get("otype");

            System.out.println("predicate: " + predicate);
            System.out.println("predicateType: " + predicateType);
            System.out.println("predicateType.isLiteral(): " + predicate.isLiteral());
            System.out.println("predicateType.isResource(): " + predicate.isResource());
            System.out.println("predicateType.isURIResource(): " + predicate.isURIResource());

            System.out.println("object: " + object);
            System.out.println("objectType: " + objectType);
            System.out.println("objectType.isLiteral(): " + object.isLiteral());
            System.out.println("objectType.isResource(): " + object.isResource());
            System.out.println("objectType.isURIResource(): " + object.isURIResource());
        }
    }
}
