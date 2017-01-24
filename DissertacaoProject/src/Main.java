
import edu.upc.freeling.*;
import java.io.IOException;
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

        //userInput.requestUserInput();
        String userQuery = "Quando nasceu o Albert Einstein?";//userInput.getUserQuery();

        List<ProcessedWord> lpw = nlpm.analyzeUserQuery(userQuery);
        
        for(ProcessedWord pw : lpw){
            pw.printInfo();
        }
        
    }
}
