
import Semantic.SemanticAnalysis;
import NLP.ProcessedWord;
import NLP.NLPmodule;
import Levenshtein.Compare;
import QuestionsAnswers.Question;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
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
public class Main {

    public static void main(String[] args) {
        UserInput userInput = new UserInput();
        NLPmodule nlpm = new NLPmodule();
        SemanticAnalysis sa = new SemanticAnalysis();
        Compare lvn = new Compare();
        
        Question question;

        //userInput.requestUserInput();
        //String userQuery = "Onde nasceu o Albert Einstein?" ;//userInput.getUserQuery();   
        try {
            while (true) {
                System.out.println("Waiting for user input: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
                String userQuery = input.readLine();
                
                question = new Question(userQuery, nlpm, sa);
                
//                HashSet<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
//                Iterator<ProcessedWord> i = lpw.iterator();
//                while (i.hasNext()) {
//                    ProcessedWord next = i.next();
//                    System.out.println("Word :" + next.getLemma() + " POS identifier: " + next.getPosTag());
//                }
//                lpw = sa.getExpandedQuery(lpw);
                //lvn.compareWords(lpw);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
