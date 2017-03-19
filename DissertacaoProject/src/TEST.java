/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maksym
 */


import DBpedia.BDpediaModule;
public class TEST {
    private static BDpediaModule dbm = new BDpediaModule();
    public static void main(String[] args) {
        dbm.getTriples("Albert_Einstein");
        //dbm.getTriples("Birth");
        //dbm.getTriples("Where");
    }
}
