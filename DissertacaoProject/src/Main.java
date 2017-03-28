
import Semantic.SemanticAnalysis;
import NLP.NLPmodule;
import QuestionsAnswers.AnswerTypeAnalysis;
import QuestionsAnswers.Question;
import QuestionsAnswers.QuestionTypeAnalysis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
        //UserInput userInput = new UserInput();
        
        //Compare lvn = new Compare();
        
        NLPmodule nlpm = new NLPmodule();
        QuestionTypeAnalysis questionTypeAnalysis = new QuestionTypeAnalysis();
        AnswerTypeAnalysis answerTypeAnalysis = new AnswerTypeAnalysis();
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis();

        Question question = null;

        //userInput.requestUserInput();
        //String userQuery = "Onde nasceu o Albert Einstein?" ;//userInput.getUserQuery();   
        try {
            while (true) {
                System.out.println("Waiting for user input: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
                String userQuery = input.readLine();

                question = new Question(nlpm, questionTypeAnalysis, answerTypeAnalysis, semanticAnalysis, userQuery);

//                HashSet<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
//                Iterator<ProcessedWord> i = lpw.iterator();
//                while (i.hasNext()) {
//                    ProcessedWord next = i.next();
//                    System.out.println("Word :" + next.getLemma() + " POS identifier: " + next.getPosTag());
//                }
//                lpw = semanticAnalysis.setSemanticData(lpw);
                //lvn.compareWords(lpw);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
