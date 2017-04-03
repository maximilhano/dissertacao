/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author maksym
 */
public class DetectSpecialCharacter {
    public boolean hasSpecialCharacter(String input){
        Pattern special =  Pattern.compile("[*['']]");
        Matcher m = special.matcher(input);
        return m.find();
    }
}
