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
    private HashSet<BabelPointer> pointers = new HashSet<>();

    private HashSet<HashSet<Synset>> sessionSynsets = new HashSet<>();

    FileLogger fl = new FileLogger();

    public BabelnetModule() {
        setPointers();
    }

    /**
     * Para cada token da query do utilizador, é efetuada a expansão com
     * objetivo de econtrar relações
     *
     * @param lpw public Synset(String lemma, String id, ) { this.lemma = lemma;
     * this.id = id; }
     *
     */
    public void doRequest(List<ProcessedWord> lpw) {
        for (ProcessedWord pw : lpw) {
            HashSet<Synset> expandedSynsets = new HashSet<Synset>();
            // verificar se lemma já existe na pasta
//            if(fl.fileExists(pw.getLemma())){
//                // se o ficheiro existe, carrega-se o conteudo do mesmo
//                expandedSynsets = fl.getExpandedSynsets(pw.getLemma()); // obter expandedSynsets do ficheiro
//            }else{
            // se o ficheiro não existe, são feitos os pedidos a babelnet, e depois guardam se o dados no ficheiro
            fl.newFile(pw.getLemma()); // Criar o novo ficheiro "nome do processes word"
            List<BabelSynset> synsets = getSynsets(pw); // lista de synsets de palavra corrente
            expandedSynsets = getExpandedSynsets(0, synsets);
            fl.writeHashToFile(expandedSynsets); // guardar no ficheiro os expanded Synsets
            //}
            sessionSynsets.add(expandedSynsets);
            //levenhsteinAnalyze(sessionSynsets);
        }
    }

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

    /**
     *
     * @param level indica o numero de vezes que repetira a tarefa de expanção
     * @param synsets synsets de cada um dos tokens da query do utilizador
     */
    /**
     *
     * @param synsets
     * @return retorna o hashset com todos os edges de todos os synsets
     */
    private HashSet<Synset> getExpandedSynsets(int level, List<BabelSynset> synsets) {
        HashSet<Synset> output = new HashSet<>(); // lista de todos os synsets com os edges

        for (BabelSynset synset : synsets) {
            Synset s = new Synset(synset.getMainSense(Language.EN).getLemma(), synset.getId().toString());
            s.setEdges(getEdges(level, synset));
            output.add(s);
        } // depois de obter edges, que foram guardados para variavel global synsetEdges, esta variavel duplica-se para guardar edges do proximo synset.

        return output;
    }

    private HashSet<Edge> getEdges(int level, BabelSynset synset) {
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
                    System.out.println("Pointer: " + pointer + " | edgeSynsetID: " + edgeSynsetId + " | edgeLemma: " + edgeSynsetLemma);
                    ids.add(edgeSynsetId);
                    edges.add(edgeSynset);
                } catch (IOException ex) {
                    Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return edges;
    }

    public void printSenses(List<ProcessedWord> lpw) {
        for (ProcessedWord pw : lpw) {
            System.out.println(getSenses(pw));
        }
    }

    public void printSynsets(List<ProcessedWord> lpw) {
        for (ProcessedWord pw : lpw) {
            System.out.println(getSynsets(pw));
        }
    }

    public void printEdges(List<BabelSynset> synsets) {
        for (BabelSynset synset : synsets) {
            //System.out.println(getEdges(synset, Ba));
        }
    }

    public List<BabelPointer> getPointers() {
        return (List<BabelPointer>) pointers;
    }

    private void setPointers() {
        pointers.add(BabelPointer.DERIVATIONALLY_RELATED);
        pointers.add(BabelPointer.ALSO_SEE);
        pointers.add(BabelPointer.ANTONYM);
        pointers.add(BabelPointer.ANY_HOLONYM);
        pointers.add(BabelPointer.ANY_HYPERNYM);
        pointers.add(BabelPointer.ANY_HYPONYM);
        pointers.add(BabelPointer.ANY_MERONYM);
        pointers.add(BabelPointer.ATTRIBUTE);
        pointers.add(BabelPointer.CAUSE);
        pointers.add(BabelPointer.DERIVATIONALLY_RELATED);
        pointers.add(BabelPointer.ENTAILMENT);
        pointers.add(BabelPointer.HOLONYM_MEMBER);
        pointers.add(BabelPointer.HOLONYM_PART);
        pointers.add(BabelPointer.HOLONYM_SUBSTANCE);
        pointers.add(BabelPointer.HYPERNYM);
        pointers.add(BabelPointer.HYPERNYM_INSTANCE);
        pointers.add(BabelPointer.HYPONYM);
        pointers.add(BabelPointer.HYPONYM_INSTANCE);
        pointers.add(BabelPointer.MERONYM_MEMBER);
        pointers.add(BabelPointer.MERONYM_SUBSTANCE);
        pointers.add(BabelPointer.PARTICIPLE);
        pointers.add(BabelPointer.PERTAINYM);
        pointers.add(BabelPointer.REGION);
        pointers.add(BabelPointer.REGION_MEMBER);
        pointers.add(BabelPointer.SEMANTICALLY_RELATED);
        pointers.add(BabelPointer.SIMILAR_TO);
        pointers.add(BabelPointer.TOPIC);
        pointers.add(BabelPointer.TOPIC_MEMBER);
        pointers.add(BabelPointer.USAGE);
        pointers.add(BabelPointer.USAGE_MEMBER);
        pointers.add(BabelPointer.VERB_GROUP);
        pointers.add(BabelPointer.WIBI_HYPERNYM);
        pointers.add(BabelPointer.WIBI_HYPONYM);
        pointers.add(BabelPointer.WIKIDATA_HYPERNYM);
        pointers.add(BabelPointer.WIKIDATA_HYPONYM);
        pointers.add(BabelPointer.WIKIDATA_MERONYM);
        //pointers.add(BabelPointer.("semantically_related_form"));
        //pointers.add(BabelPointer.valueOf("gloss_related_form_(disambiguated)"));
        //pointers.add(BabelPointer.valueOf("gloss_related_form_(monosemous)"));
    }

    /**
     *
     * @param synset
     * @param level indica o nivel de expançao do synset
     */
    /*public void synsetExpand(BabelSynset synset, int level) throws IOException{
        List<BabelSynsetIDRelation> synsetEdges = getEdges(synset, BabelPointer.ANY_HYPERNYM);
        for(BabelSynsetIDRelation synsetEdge : synsetEdges){
            System.out.println("Synset: " + synset.toString());
            printSynsetEdgeInfo(synsetEdge);
            for(BabelSynsetIDRelation synsetEdge1 : getEdges(synsetEdge.getBabelSynsetIDTarget().toBabelSynset(), BabelPointer.ANY_HYPERNYM)){
                System.out.println("Edge of Synset Edge: " + synset.toString());
                printSynsetEdgeInfo(synsetEdge1);
            }
        }
    }*/
    private void printSynsetEdgeInfo(BabelSynsetIDRelation edge) {
        try {
            System.out.println("\n printSynsetEdgeInfo "
                    + "\n Pointer: " + edge.getPointer()
                    + "\n BabelSynsetIDTarget: " + edge.getBabelSynsetIDTarget()
                    + "\n EdgeSynset: " + edge.getBabelSynsetIDTarget().toBabelSynset().toString());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<BabelSense> getSenses(ProcessedWord pw) {
        List<BabelSense> result = null;
        try {
            result = bn.getSenses(pw.getLemma(), lang, pw.getPosTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private boolean isRightPointer(BabelPointer pointer) {
        return !pointer.getSymbol().equals("r");
    }

    public void defineSynsetCompareHash(HashSet<String> input) {

    }
}
