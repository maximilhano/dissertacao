/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Print;

import BabelNet.Synset;
import it.uniroma1.lcl.babelnet.BabelSynset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author maksym
 */
public class PrintIterator {

    public void BabelSynset(List<BabelSynset> synsets) {
        Iterator<BabelSynset> i = synsets.iterator();
        while (i.hasNext()) {
            System.out.println("BabelSynset: " + i.next().toString());
        }
    }
    
    public void Synset(List<Synset> synsets){
        Iterator<Synset> i = synsets.iterator();
        while (i.hasNext()) {
            System.out.println("Synset: " + i.next().toString());
        }
    }
    
    public void CompareSynset(HashSet hashset){
        Iterator<Synset> i = hashset.iterator();
        while (i.hasNext()) {
            System.out.println(i.next().toString());
        }
    }
    
}
