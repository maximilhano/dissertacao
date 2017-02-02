package File;


import BabelNet.Edge;
import BabelNet.Synset;
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

    public boolean newFile(String fileName) {
        currentFile = new File(directory + fileName);
        
        if (fileExists(fileName)) {
            try {
                currentFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return fileExists(fileName);
    }

    /**
     * Retorna a lista de todos os ficheiros referentes a cada lemma existentes
     * na pasta
     * @return 
     */
    public HashSet<String> getFileList() {
        HashSet<String> fileList = new HashSet<>();
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fileList.add(listOfFiles[i].getName());
            }
        }
        return fileList;
    }

    public boolean fileExists(String filename) {
        return getFileList().contains(filename);
    }

    public ArrayList<String> getFileContent(String fileName) {
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

    public void printHash(HashSet<String> hash) {
        Iterator iterator = hash.iterator();
        while (iterator.hasNext()) {
            System.out.println("File line: " + iterator.next());
        }
    }

    public String[][] getSynsetMatrix(HashSet<String> hashset) {
        int matrixSize = hashset.size();
        String[][] outputMatrix = new String[matrixSize][2];
        Iterator iterator = hashset.iterator();
        int l = 0;

        while (iterator.hasNext()) {
            String line = (String) iterator.next();
            String[] info = line.split(",");
            outputMatrix[l][0] = info[2];
            outputMatrix[l][1] = info[4];
            l++;
        }
        return outputMatrix;
    }

    public void writeHashToFile(HashSet<Synset> hashset) {
        try {
            PrintWriter pw = null;
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(currentFile), "UTF-8"));
            
            for (Synset synset : hashset) {
                pw.append(synset.toStirng());
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashSet<Synset> getExpandedSynsets(String lemma) {
        HashSet<Synset> synsets = new HashSet<>();
        ArrayList<String> fileContent = getFileContent(lemma);
        Iterator iterator = fileContent.iterator();
                
        Synset synset = null;
        HashSet<Edge> edges = new HashSet<>();
        
        while(iterator.hasNext()){
            String line = (String) iterator.next();
            String [] info = line.split(",");
            if(info.length <= 2){
                if(synset != null){
                    synset.setEdges(edges);
                    synsets.add(synset);
                    edges.clear();
                }
                synset = new Synset(info[0], info[1]);
            }else{
               edges.add(new Edge(info[0],info[1],info[2]));
            }
        }
        
        return synsets;
    }
}
