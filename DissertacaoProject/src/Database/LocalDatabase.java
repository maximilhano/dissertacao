/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Logging.Logger;
import NLP.DetectSpecialCharacter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private final DetectSpecialCharacter specialCharacter = new DetectSpecialCharacter();
    private final Logger log = new Logger();

    QueryExecution qexecFuseki;

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
            + "PREFIX dbc: <http://dbpedia.org/category/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n ";

    private final String updateQuery = PREFIX + "INSERT DATA { ";

    public void getTriples(HashSet<String> entitiesInQuestion) {
        Iterator<String> entitiesInQuestionIterator = entitiesInQuestion.iterator();
        while (entitiesInQuestionIterator.hasNext()) {
            String entity = entitiesInQuestionIterator.next();
            String query = constructNamedEntitySelectQuery(entity);
            log.writeNamedEntitySelectQuery(query);
            ResultSet queryResult = runSelectQuery(query);
            String upQuery = constructNamedEntityUpdateQuery(queryResult, entity);
            if (!upQuery.isEmpty()) {
                log.writeNamedEntityUpdateQuery(upQuery);
                runUpdateQuery(upQuery);
            }

        }
    }

    private String constructNamedEntitySelectQuery(String namedEntity) {
        return "SELECT * WHERE "
                + "{ <http://dbpedia.org/resource/" + namedEntity + "> ?p ?o ."
                + "OPTIONAL { ?o rdf:type ?otype }}";
    }

    private String constructNamedEntityUpdateQuery(ResultSet result, String entity) {
        QuerySolution soln;
        String query = "";

        while (result.hasNext()) {
            query = "";
            soln = result.nextSolution();

            RDFNode predicate = soln.get("p");
            RDFNode object = soln.get("o");
            //RDFNode predicateType = soln.get("ptype");
            RDFNode objectType = soln.get("otype");

            if (predicate != null && object != null) {

                if (object.isLiteral()) {
                    String[] split = null;

                    if (object.toString().contains("@")) {

                        split = object.toString().split("@");
                        if (!specialCharacter.hasSpecialCharacter(split[0])) {
                            query += " <http://dbpedia.org/resource/" + entity + "> <" + predicate + "> \"" + split[0] + "\"@" + split[1] + " .\n";
                            //System.out.println("SPLIT 0: " + split[0]);
                            //System.err.println("SPLIT 1: " + split[1]);
                        }

                    } else if (object.toString().contains("^^")) {

                        split = object.toString().split("http://www.w3.org/2001/XMLSchema#");

                        if (split.length > 1) {
                            split[0] = split[0].replace("^", "");
                            if (!specialCharacter.hasSpecialCharacter(split[0])) {
                                query += " <http://dbpedia.org/resource/" + entity + "> <" + predicate + "> \"" + split[0] + "\"^^xsd:" + split[1] + " .\n";
                                //System.out.println("SPLIT 0: " + split[0]);
                                //System.err.println("SPLIT 1: " + split[1]);
                            }
                        }

                    } else {
                        if (!specialCharacter.hasSpecialCharacter(object.toString())) {
                            query += " <http://dbpedia.org/resource/" + entity + "> <" + predicate + "> \"" + object + "\" .\n";
                        }
                    }

                } else if (object.isResource()) {
                    query += " <http://dbpedia.org/resource/" + entity + "> <" + predicate + "> <" + object + "> .\n";

//                    if (predicateType != null) {
//                        query += " <" + predicate + "> rdf:type <" + predicateType + "> .\n";
//                    }
                    if (objectType != null) {
                        query += " <" + object + "> rdf:type <" + objectType + "> .\n";
                    }

                }
            }
            if (!query.isEmpty()) {
                runUpdateQuery(query);
            }
        }

        return query;
    }

    private ResultSet runSelectQuery(String query) {
        QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpediaEndpoint, PREFIX + query);
        ResultSet result = qexec.execSelect();
        return result;
    }

    private void runUpdateQuery(String query) {
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery + query + " }"), fusekiUpdate);
        upp.execute();
    }

    public HashSet<String> getPossibleProperties(String namedEntity, HashSet<String> concepts) {
        HashSet<String> res = new HashSet<>();
        Iterator<String> it = concepts.iterator();
        System.out.println("getPossibleProperties 1");
        while (it.hasNext()) {
            String next = it.next();
            System.out.println("getPossibleProperties 2");
            String askQuery = "ASK "
                    + "{ <http://dbpedia.org/resource/" + namedEntity + "> ?p ?o . "
                    + "FILTER regex(str(?p),\"" + next + "\",\"i\")}";
             System.out.println("getPossibleProperties 3 + askquery: " + askQuery);
            if (runFusekiAskQuery(askQuery)) {
                 System.out.println("getPossibleProperties 4 ");
                String query = "SELECT ?p WHERE "
                        + "{ <http://dbpedia.org/resource/" + namedEntity + "> ?p ?o . "
                        + "FILTER regex(str(?p),\"" + next + "\",\"i\")}";

                System.out.println("A verificar se entidade: " + namedEntity + " tem a seguinte propriedade: " + next + "\n com a seguinte query: " + query);

                HashSet<QuerySolution> result = runFusekiSelectQuery(query);
                
                for(QuerySolution s : result){
                    System.out.println("A entidade: " + namedEntity + " tem a seguinte propriedade: " + next + " com URI: " + s.get("p").toString());
                    res.add(s.get("p").toString());
                }
            }
            System.out.println("getPossibleProperties 5 ");
        }
        System.out.println("getPossibleProperties return res: " + res);
        return res;
    }

//    public ResultSet runFusekiSelectQuery(String query) {
//        qexecFuseki = QueryExecutionFactory.sparqlService(fusekiQuery, PREFIX + query);
//        ResultSet result = qexecFuseki.execSelect();
//        qexecFuseki.close();
//        return result;
//    }
    
    public HashSet<QuerySolution> runFusekiSelectQuery(String query) {
        HashSet<QuerySolution> res = new HashSet<>();
        qexecFuseki = QueryExecutionFactory.sparqlService(fusekiQuery, PREFIX + query);
        ResultSet result = qexecFuseki.execSelect();
        while (result.hasNext()) {
            res.add(result.next());
        }
        qexecFuseki.close();
        return res;
    }

    private boolean runDBpediaAskQuery(String query) {
        QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpediaEndpoint, PREFIX + query);
        return qexec.execAsk();
    }

    private boolean runFusekiAskQuery(String query) {
        System.out.println("runFusekiAskQuery 1 query: " + query);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(fusekiQuery, PREFIX + query);
        
        
        boolean res = qexec.execAsk();
        qexec.close();
        System.out.println("runFusekiAskQuery 2 resiltado: " + res);
        return res;
    }

    public HashSet<String> getPossibleClasses(String namedEntity, HashSet<String> concepts) {
        HashSet<String> res = new HashSet<>();
        Iterator<String> it = concepts.iterator();
        while (it.hasNext()) {
            String next = it.next();
            String query = "SELECT ?o WHERE "
                    + "{ :" + namedEntity + " ?p ?o . "
                    + " ?o rdf:type ?otype"
                    + "FILTER regex(str(?otype),\"" + next + "\",\"i\")}";
//            ResultSet queryResult = runFusekiSelectQuery(query);
//
//            while (queryResult.hasNext()) {
//                QuerySolution next1 = queryResult.next();
//                res.add(next1.get("o").toString());
//            }
        }
        return res;
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
