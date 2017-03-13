
import NLP.ProcessedWord;
import NLP.NLPmodule;
import Levenshtein.Compare;
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
        QueryExpand qe = new QueryExpand();
        Compare lvn = new Compare();

        //userInput.requestUserInput();
        //String userQuery = "Onde nasceu o Albert Einstein?" ;//userInput.getUserQuery();   
        try {
            while (true) {
                System.out.println("Waiting for user input: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
                String userQuery = input.readLine();
                HashSet<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
                Iterator<ProcessedWord> i = lpw.iterator();
//                while (i.hasNext()) {
//                    ProcessesdWord next = i.next();
//                    System.out.println("Word :" + next.getLemma() + " POS identifier: " + next.getPosTag());
//                }
                lpw = qe.getExpandedQuery(lpw);
                //lvn.compareWords(lpw);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //bnm.printExpandedSynsets();
        //System.out.println("\n\tPROCESSED WORDS\n");
        /*for(ProcessedWord pw : lpw){
            pw.printInfo();
            //bnm.printEdges(bnm.getSynsets(pw));
        }*/
        //bnm.printSenses(lpw);
        //bnm.printSynsets(lpw);
    }
}
