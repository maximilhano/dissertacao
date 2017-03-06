package File;

import BabelNet.AbstractSynset;
import BabelNet.Edge;
import BabelNet.Synset;
import Levenshtein.SynsetCompare;
import NLP.ProcessedWord;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 1/14/2017.
 */
public class FileLogger {

    private File currentFile;
    private final String directory = "lemmas/";
    PrintWriter pw;

    public void newFile(String fileName) {
        currentFile = new File(directory + fileName);
        try {
            currentFile.createNewFile();
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(currentFile), "UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retorna a lista de todos os ficheiros referentes a cada lemma existentes
     * na pasta
     *
     * @return
     */
    public HashSet<String> getFileList() {
        HashSet<String> fileList = new HashSet<>();
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    public boolean fileExists(String filename) {
        return getFileList().contains(filename);
    }
    
    public boolean fileExists(String lemma1, String lemma2) {
        return getFileList().contains(lemma1+"+"+lemma2);
    }

    private ArrayList<String> getFileContent(String fileName) { // a razão de fazer com o arraylist, é a leitura linear do ficheiro, 
                                                                // porque hashset não le por ordem
        ArrayList<String> fileContent = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(directory + fileName));
            while (sc.hasNext()) {
                fileContent.add(sc.nextLine());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileContent;
    }

    private void writeLine(String line) {
        pw.append(line);
    }

    private void endWrite() {
        pw.flush();
        pw.close();
    }

    public HashSet<Synset> getExpandedSynsets(String lemma) {
        HashSet<Synset> synsets = new HashSet<>();
        ArrayList<String> fileContent = getFileContent(lemma);
        Iterator<String> i = fileContent.iterator();

        Synset synset = null;
        HashSet<Edge> edges = new HashSet<>();

        while (i.hasNext()) {
            String line = i.next();
            String[] lineSplit = line.split(",");
            if (lineSplit.length <= 2) {
                if (synset != null) {
                    synset.setEdges(edges);
                    synsets.add(synset);
                    edges.clear();
                }
                synset = new Synset(lineSplit[0], lineSplit[1]);
            } else {
                edges.add(new Edge(lineSplit[0], lineSplit[1], lineSplit[2]));
            }
        }
        synsets.add(synset);
        System.out.println("Lemma: " + lemma + " SIZE: " + synsets.size());
        return synsets;
    }

    public void saveExpandedSynsets(ProcessedWord pword) {
        if (pword != null) {
            newFile(pword.getLemma());
            writeHash(pword.getSynsets());
        }
    }

    public void saveCompareHash(String lemma1, String lemma2, HashSet<AbstractSynset> compareHash) {
        if (!compareHash.isEmpty()) {
            newFile(lemma1 + "+" + lemma2);
            writeHash(compareHash);
        }

    }

    private void writeHash(HashSet hashset) {
        Iterator i = hashset.iterator();
        while (i.hasNext()) {
            writeLine(i.next().toString());
        }
        endWrite();
    }

    public HashSet<AbstractSynset> getMatchingSynsets(String lemma, String lemma0) {
        HashSet<AbstractSynset> output = new HashSet<>();
        ArrayList<String> fileContent = getFileContent(lemma);
        Iterator<String> i = fileContent.iterator();
        
        while (i.hasNext()) {
            String next = i.next();
            String[] nextSplit = next.split(",");
            System.out.println("FILE: " + nextSplit[0] + " : " + nextSplit[1] + " : " +  nextSplit[2]);
            //output.add(new Edge(nextSplit[0], nextSplit[1], nextSplit[2]));
        }
        
        return output;
    }
}
