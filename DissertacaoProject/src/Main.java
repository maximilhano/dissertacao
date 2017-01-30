
import java.util.List;

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
        BabelnetModule bnm = new BabelnetModule();

        //userInput.requestUserInput();
        String userQuery = "Quando nasceu o Albert Einstein??" ;//userInput.getUserQuery();  

        List<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
        bnm.doRequest(lpw);
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
