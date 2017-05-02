
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class UserInput {
    private String userQuery;
    private BufferedReader input;

    public String getUserQuery() {
        return this.userQuery;
    }
    
    public void requestUserInput(){
        try {
            System.out.println("Waiting for user input: ");
            input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            userQuery = this.input.readLine();
        } catch (IOException ex) {
            Logger.getLogger(UserInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
