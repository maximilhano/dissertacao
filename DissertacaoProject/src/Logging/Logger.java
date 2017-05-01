/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logging;

import NLP.ProcessedWord;
import QuestionsAnswers.QuestionTypes;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 *
 * @author maksym
 */
public class Logger {

    private final File logFile = new File("log");
    private String pw = "";
    private FileWriter fw;

    public Logger() {
        try {
            this.fw = new FileWriter(logFile, true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeQuestionInfo(String userQuery, QuestionTypes questionType, HashSet<String> answerType, HashSet<ProcessedWord> processedWordList, HashSet<String> entitiesInQuestion, HashSet<String> conceptsInQuestion) {
        pw = "\n\n-------------------------------------------------------------------------------------------------";
        pw += "\n\t Nova pergunta \n\n";
        pw += "  Pergunta: " + userQuery + "\n";
        pw += "  Tipo de pergunta: " + questionType + "\n";
        pw += "  Tipo de resposta: \n";
        Iterator<String> it = answerType.iterator();
        while (it.hasNext()) {
            String next = it.next();
            pw += "  - " + next + "\n";
        }

        pw += "\n\tResultado de análise morfologica e BabelNet:\n";
        Iterator<ProcessedWord> pwi = processedWordList.iterator();

        while (pwi.hasNext()) {
            ProcessedWord next = pwi.next();
            pw += "\n  lemma: " + next.getLemma();
            pw += "\n  POS: " + next.getPosTag();

            if (next.getSynsets() != null) {
                pw += "\n  synsets: ";

                Map<String, BabelSynsetType> processedWordSynsets = next.getSynsets();

                for (Map.Entry<String, BabelSynsetType> entry : processedWordSynsets.entrySet()) {
                    String synset = entry.getKey();
                    BabelSynsetType synsetType = entry.getValue();
                    pw += "\n  -s- " + synset + " -t- " + synsetType;
                }
            }
            pw += "\n ----- \n";
        }

        if (!entitiesInQuestion.isEmpty()) {
            pw += "\n\n\tEntidades nomeadas na pergunta. \n";
            for (String s : entitiesInQuestion) {
                pw += "\n  - " + s;
            }
        }

        if (!conceptsInQuestion.isEmpty()) {
            pw += "\n\n\tConceitos na pergunta \n";
            for (String s : conceptsInQuestion) {
                pw += "\n  - " + s;
            }
        }

        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeNamedEntitySelectQuery(String query) {
        pw = "\n\n\t Named Entity Query\n\n";
        pw += "  Query: " + query;
        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeNamedEntityUpdateQuery(String upQuery) {
        pw = "\n\n\t Named Entity Update Query\n\n";
        pw += "  Update Query: \n  " + upQuery;
        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writePossiblePredicates(HashSet<String> possiblePredicates) {
        pw = "\n\n\t Possible predicated\n\n";
        for(String s : possiblePredicates){
            pw+= "\n  - " + s;
        }
        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeFinalQuery(String questionLocalQuery) {
        pw = "\n\n\t Final Query\n\n";
        pw+="  " + questionLocalQuery;
        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeResultOfFinalQuery(HashSet<QuerySolution> result) {
        pw = "\n\n\t Final Query Result\n\n";
        
        for(QuerySolution s : result){
            pw+="\n  " + s;
        }
        try {
            fw.write(pw);
            fw.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
