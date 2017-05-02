
import Database.LocalDatabase;
import Semantic.SemanticAnalysis;
import NLP.NLPmodule;
import QuestionsAnswers.AnswerTypeAnalysis;
import QuestionsAnswers.Question;
import QuestionsAnswers.QuestionTypeAnalysis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        NLPmodule nlpm = new NLPmodule();
        QuestionTypeAnalysis questionTypeAnalysis = new QuestionTypeAnalysis();
        AnswerTypeAnalysis answerTypeAnalysis = new AnswerTypeAnalysis();
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
        LocalDatabase localDatabase = new LocalDatabase();

        File qFile = new File("questions");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile), "UTF8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                Question question = new Question(nlpm, questionTypeAnalysis, answerTypeAnalysis, semanticAnalysis, localDatabase, line);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

//        
//
//        try {
//            while (true) {
//                System.out.println("Waiting for user input: ");
//                BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
//                String userQuery = input.readLine();
//
//                
//            }
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
