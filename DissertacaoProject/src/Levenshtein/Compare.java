package Levenshtein;

import BabelNet.AbstractSynset;
import BabelNet.Edge;
import BabelNet.Synset;
import File.FileLogger;
import NLP.ProcessedWord;
import java.util.HashSet;
import java.util.Iterator;
import Print.PrintIterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maksym
 */
public class Compare {

    FileLogger fl = new FileLogger();
    PrintIterator it = new PrintIterator();

    public void compareWords(HashSet<ProcessedWord> lpw) {
        ProcessedWord[] pwordsArray = lpw.toArray(new ProcessedWord[lpw.size()]);
        HashSet<HashSet<AbstractSynset>> compareList = new HashSet<>();

        for (int i = 0; i + 1 <= pwordsArray.length; i++) {
            for (int f = i + 1; f < pwordsArray.length; f++) { // cada palavra é comparada com outras.
                HashSet<AbstractSynset> matchingSynsets = getMatchingSynsets(pwordsArray[i], pwordsArray[f]);
                fl.saveCompareHash(pwordsArray[i].getLemma(), pwordsArray[f].getLemma(), matchingSynsets);
                compareList.add(matchingSynsets);
            }
        }
    }

    private HashSet<AbstractSynset> getMatchingSynsets(ProcessedWord pword1, ProcessedWord pword2) {

        HashSet<AbstractSynset> matchingHash = new HashSet<>();

        if (fl.fileExists(pword1.getLemma(), pword2.getLemma())) {
            matchingHash = fl.getMatchingSynsets(pword1.getLemma(), pword2.getLemma());
        } else {
            HashSet<Edge> pword1Data = pword1.getEdges();
            HashSet<Edge> pword2Data = pword2.getEdges();

            Iterator<Edge> iterator1 = pword1Data.iterator();

            while (iterator1.hasNext()) {
                Edge e1 = iterator1.next();

                Iterator<Edge> iterator2 = pword2Data.iterator();
                while (iterator2.hasNext()) {
                    Edge e2 = iterator2.next();

                    if (e1.getCompareValue(pword1.getPosTag()).contains(e2.getCompareValue(pword2.getPosTag()))
                            || e2.getCompareValue(pword2.getPosTag()).contains(e1.getCompareValue(pword1.getPosTag()))) {
                        matchingHash.add(e2);
                    }
                }
            }
        }

        return matchingHash;
    }
}
