
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by Maksym on 1/14/2017.
 */
public class FileLogger {

    private File currentFile;

    public boolean newFile(String fileName) {
        currentFile = new File(fileName);
        boolean fileExists = false;
        if (!currentFile.exists() && !currentFile.isDirectory()) {
            try {
                currentFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            fileExists = true;
        }
        return fileExists;
    }
    
    public HashSet<String> readFile(String filename){
        HashSet<String> fileContent = new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = null;
            while((line = br.readLine()) != null){
                fileContent.add(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileContent;
    }

    public void writeHashToFile(HashSet<String> hashset) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(currentFile), "UTF-8"));
            Iterator iterator = hashset.iterator();

            while (iterator.hasNext()) {
                pw.append(iterator.next() + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }
}
