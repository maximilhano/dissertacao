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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UserInput userInput = new UserInput();
        NLPmodule nlpm = new NLPmodule();
                
        userInput.requestUserInput();
        
        String userQuery = userInput.getUserQuery();
        
        //nlpm.printAnalysis(userQuery);
        
    }
    
}
