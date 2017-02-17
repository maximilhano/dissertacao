package BabelNet;

import File.FileLogger;
import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maksym
 */
public class BabelnetModule {

    private final BabelNet bn = BabelNet.getInstance();
    private final Language lang = Language.PT;

    /**
     * Função devolve todos as palavras que partilham o mesmo significado
     * (sinonimos) de uma dada palavra
     *
     * @param pw
     * @return
     */
    public List<BabelSynset> getSynsets(ProcessedWord pw) {
        List<BabelSynset> result = null;
        try {
            result = bn.getSynsets(pw.getLemma(), lang, pw.getPosTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    public HashSet<Edge> getEdges(int level, BabelSynset synset) {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<String> ids = new HashSet<>(); // armazena os id's de synsets, e serve para controlar o id's repetidos

        for (BabelSynsetIDRelation edge : synset.getEdges()) {
            String edgeSynsetId = edge.getBabelSynsetIDTarget().toString();
            
            if (isRightPointer(edge.getPointer()) && !ids.contains(edgeSynsetId)) {
                try {
                    String pointer = edge.getPointer().getName(); // identifica a relação existente entre o synset e edges
                    String edgeSynsetLemma = edge.getBabelSynsetIDTarget().toBabelSynset().getMainSense(Language.EN).getLemma();
                    Edge edgeSynset = new Edge(edgeSynsetId, edgeSynsetLemma, pointer);

                    for (int i = 0; i < level; i++) { // consoante o nivel de expanção, adiciona edges do edge
                        edgeSynset.setEdges(getEdges(0, edge.getBabelSynsetIDTarget().toBabelSynset()));
                    }
                    //System.out.println("Pointer: " + pointer + " | edgeSynsetID: " + edgeSynsetId + " | edgeLemma: " + edgeSynsetLemma);
                    ids.add(edgeSynsetId);
                    edges.add(edgeSynset);
                } catch (IOException ex) {
                    Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return edges;
    }

    private boolean isRightPointer(BabelPointer pointer) {
        return !pointer.getSymbol().equals("r");
    }
}
