
import NLP.ProcessedWord;
import NLP.NLPmodule;
import Levenshtein.Compare;
import java.util.HashSet;

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
        String userQuery = "Onde nasceu o Albert Einstein?" ;//userInput.getUserQuery();   

        HashSet<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
        lpw = qe.getExpandedQuery(lpw);
        lvn.compareWords(lpw);
       
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
