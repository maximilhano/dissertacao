package Levenshtein;

import BabelNet.Edge;
import File.FileLogger;
import NLP.ProcessedWord;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.LevenshteinDistance;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maksym
 */
public class LevenshteinCompare {

    CosineDistance lvns = new CosineDistance();

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

        Integer[][] outputMatrix = new Integer[bigger.length * smaller.length][2];
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

    public void call() {
        System.out.println("birth_place".contains("birth"));
    }

    public static void main(String[] args) {

//        String [] data = { "birth", "birth place", "birth", "birth date", "rosettacode", "raisethysword" };
//        for (int i = 0; i < data.length; i += 2)
//            System.out.println("distance(" + data[i] + ", " + data[i+1] + ") = " + distance(data[i], data[i+1]));
        //FileLogger fl = new FileLogger();
        //fl.newFile("test");
        //fl.writeHashToFile(fl.getExpandedSynsets("nascer"));
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

    public void calculateDistance(HashSet<ProcessedWord> lpw) {
        ProcessedWord[] pwordsArray = lpw.toArray(new ProcessedWord[lpw.size()]);
        HashSet<HashSet<SynsetCompare>> compareList = new HashSet<>();
        
        for (int i = 0; i++ < pwordsArray.length; i++) {
            compareList.add(getDistanceMatrix(pwordsArray[i], pwordsArray[i++]));
        }

        Iterator<HashSet<SynsetCompare>> iterator = compareList.iterator();
        while (iterator.hasNext()) {
            Iterator<SynsetCompare> it = iterator.next().iterator();
            while (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        }

    }

    private HashSet<SynsetCompare> getDistanceMatrix(ProcessedWord pword1, ProcessedWord pword2) {
        HashSet<Edge> pword1Data = pword1.getEdges();
        HashSet<Edge> pword2Data = pword2.getEdges();
        HashSet<SynsetCompare> compareHash = new HashSet<SynsetCompare>();

        Iterator<Edge> iterator1 = pword1Data.iterator();

        while (iterator1.hasNext()) {
            Edge e1 = iterator1.next();

            Iterator<Edge> iterator2 = pword2Data.iterator();
            while (iterator2.hasNext()) {
                Edge e2 = iterator2.next();
                //int similarity = lvns.apply(e1.getCompareValue(pword1.getPosTag()), e2.getCompareValue(pword2.getPosTag()));
                if (e1.getCompareValue(pword1.getPosTag()).contains(e2.getCompareValue(pword2.getPosTag()))
                        || e2.getCompareValue(pword2.getPosTag()).contains(e1.getCompareValue(pword1.getPosTag()))) {
                    compareHash.add(new SynsetCompare(e1, e2, 0));
                }
            }
        }

        return compareHash;


        /*String[] array1 = pword1Data.toArray(new Edge[pword1Data.size()]);
        String[] array2 = pword2Data.toArray(new Edge[pword2Data.size()]);
        
        for(int i=0; i < array2.length; i++){
            System.out.println(array2[i]);
        }

        Integer[][] distanceMatrix = new Integer[array1.length][array2.length];
        

        for (int i = 0; i < array1.length; i++) {
            for (int f = 0; f < array2.length; f++) {
                distanceMatrix[i][f] = distance(array1[i], array2[f]);
                //System.out.println("Edge lemma 1: " + array1[i] + " | Edge lemma 2: " + array2[f] + " | Distance: " + distanceMatrix[i][f]);
            }
        }*/
        //getBestDistance(0, distanceMatrix);
    }

    private void getBestDistance(int n, HashSet<SynsetCompare> compareHash) {
        SynsetCompare[] result = new SynsetCompare[n];

        Iterator<SynsetCompare> iterator = compareHash.iterator();

        while (iterator.hasNext()) {
            SynsetCompare s = iterator.next();
            int synsetScore = s.getScore();

            for (int i = 0; i < result.length; i++) {
                if (synsetScore < result[i].getScore()) {
                    for (int j = result.length; j > i; j--) {
                        if (result[j] != null) {
                            result[j] = result[j--];
                        }
                        result[i] = s;
                    }
                }

            }

            /*Arrays.sort(distanceMatrix, new Comparator<Integer[]>() {
            @Override
            public int compare(final Integer[] entry1, final Integer[] entry2) {
                final int n1 = entry1[0];
                final int n2 = entry2[0];
                return Integer.compare(n1, n2);
            }
        });

        for (final Integer[] i : distanceMatrix) {
            System.out.println("N1 : " + i[0] + " : N2 : " + i[1]);
        }*/
        }
    }
}
