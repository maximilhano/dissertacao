
import File.FileLogger;
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
public class LevenshteinDistance {

    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static Integer[][] getLevenshteinMatrix(String[][] a, String[][] b) {
        String[][] bigger;
        String[][] smaller;

        if (a.length > b.length) {
            bigger = a;
            smaller = b;
        } else {
            bigger = b;
            smaller = a;
        }

        Integer[][] outputMatrix = new Integer[bigger.length*smaller.length][2];
        int r = 0;
        //Percorrer a matriz A
        for (int i = 0; i < bigger.length; i++) { //linhas da matriz a
            for (int f = 0; f < smaller.length; f++) { // linhas da matriz b
                outputMatrix[r][0] = distance(bigger[i][1], b[f][0]);
                outputMatrix[r][1] = distance(bigger[i][1], b[f][1]);
                r++;
            }
        }
        return outputMatrix;
    }

    public static void main(String[] args) {
//        String [] data = { "birth", "birth place", "birth", "birth date", "rosettacode", "raisethysword" };
//        for (int i = 0; i < data.length; i += 2)
//            System.out.println("distance(" + data[i] + ", " + data[i+1] + ") = " + distance(data[i], data[i+1]));
        FileLogger fl = new FileLogger();
        fl.newFile("test");
        fl.writeHashToFile(fl.getExpandedSynsets("nascer"));
        
//        HashSet<String> fileHash = fl.getFileContent("nascer");
//        String [][] matrixFile = fl.getSynsetMatrix(fileHash);
//        
//        HashSet<String> fileHash1 = fl.getFileContent("albert_einstein");
//        String [][] matrixFile1 = fl.getSynsetMatrix(fileHash1);
//        
//        Integer [][] distanceMatrix = getLevenshteinMatrix(matrixFile, matrixFile1);
//        
//        for(int i = 0; i < distanceMatrix.length; i++){
//            System.out.println("Val1: " + distanceMatrix[i][0] + " Val2: " + distanceMatrix[i][1]);
        //}
    }
}
