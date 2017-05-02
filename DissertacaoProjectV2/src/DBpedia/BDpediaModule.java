/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBpedia;

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
public class BDpediaModule {

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
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n";

    public void getTriples(String value) {

        String query = PREFIX
                + "SELECT ?p ?o ?type WHERE "
                + "{ :" + value + " ?p ?o ."
                + "?p rdf:type ?ptype . "
                + "?o rdf:type ?otype . }";

        System.out.println("\n\t SELECT QUERY: " + query);

        QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpediaEndpoint, query);
        ResultSet selectResults = qexec.execSelect();

        String updateQuery = PREFIX + "INSERT DATA { ";
        QuerySolution soln; //soln2;

        while (selectResults.hasNext()) {
            soln = selectResults.nextSolution();

            updateQuery += " :" + value + " <" + soln.get("p") + "> <" + soln.get("o") + "> . <" + soln.get("o") + "> rdf:type <" + soln.get("type") + "> .";

            //System.out.println("P: " + soln.get("p"));
            //System.out.println("O: " + soln.get("o"));
            //System.out.println("TYPE: " + soln.get("type"));
        }

        updateQuery += " }";
        System.out.println("Update Query: " + updateQuery);
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery), fusekiUpdate);
        upp.execute();

        /*        
            String a;

            if (soln.get("o").isResource()) {
                updateQuery = PREFIX + "INSERT DATA { ";
                updateQuery += ":" + value + " <" + soln.get("p") + "> <" + soln.get("o") + "> .\n";

                System.out.println("URI: " + soln.getResource("o").getURI());
                System.out.println("NameSpace: " + soln.getResource("o").getNameSpace());
                System.out.println("LocalName: " + soln.getResource("o").getLocalName());
                System.out.println("GET: " + soln.get("o"));
                //System.out.println("REsourceValue: " + soln.getResource("o").getPropertyResourceValue());
                //query2 ="SELECT * WHERE { \"" + soln.get("o") + "\" rdf:type ?type }"; 
                a = soln.getResource("o").getURI();

                // to do tratar do literal
                String object = soln.get("o").toString();
                //object = object.replace("http://www.w3.org/2002/07/owl#", "owl:");
                query2 = PREFIX + "SELECT ?type WHERE { <" + a + "> rdf:type ?type }";
                System.out.println("\n QUERY 2: " + query2);

                // new
                QueryExecution qexec2 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql/", query2);
                ResultSet results2 = qexec2.execSelect();

                int i = 0;
                while (results2.hasNext() && i < 100) {
                    soln2 = results2.nextSolution();

                    updateQuery += "<" + a + "> rdf:type <" + soln2.get("type") + "> .\n";
                    System.out.println("2: " + soln2);
                    i++;
                }

                updateQuery += " }";
            }

            //System.err.println("UPDATE QUERY: " + updateQuery);

            UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery), fusekiUpdate);
            upp.execute();

        }*/
    }
}
