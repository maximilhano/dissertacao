/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.HashSet;

/**
 *
 * @author maksym
 */
public class testDB {
    public static void main(String[] args) {
        HashSet<String> a = new HashSet<>();
        a.add("Albert_Einstein");
        LocalDatabase l = new LocalDatabase();
        l.getTriples(a);
    }
}
