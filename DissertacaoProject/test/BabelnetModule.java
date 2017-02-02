
import File.FileLogger;
import NLP.ProcessedWord;
import BabelNet.BabelnetModule;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
    ;
    private final Language lang = Language.PT;
    private HashSet<BabelPointer> pointers = new HashSet<>();

    private HashSet<BabelSynset> synstetEdges = new HashSet<>();
    private List<HashSet<BabelSynset>> expandedSynsets = new ArrayList<>();
    
    private HashSet<Relation> relations = new HashSet<>();
    private Relation relation = null;

    FileLogger fl = new FileLogger();
    HashSet<String> toFileHash;

    public BabelnetModule() {
        setPointers();
    }

    private void clearSynsetEdges(HashSet<BabelSynset> synsetEdges) {
        expandedSynsets.add(synsetEdges);
        synstetEdges = new HashSet<>();
    }

    /**
     * Paraca cada token da query do utilizador, é efetuada a expansão com
     * objetivo de econtrar relações
     *
     * @param lpw    public Synset(String lemma, String id, ) {
        this.lemma = lemma;
        this.id = id;
    }
    
     */
    public void doRequest(List<ProcessedWord> lpw) {
        for (ProcessedWord pw : lpw) {
            fl.newFile(pw.getLemma()); // Criar o novo ficheiro "nome do processes word"
            if(relation != null){
                relations.add(relation);
            }
            List<BabelSynset> synsets = getSynsets(pw); // lista desynsets de palavra corrente
            expand(1, synsets);
        }
    }

    public void printExpandedSynsets() {
        for (HashSet<BabelSynset> hash : expandedSynsets) {
            Iterator iterator = hash.iterator();
            while (iterator.hasNext()) {
                BabelSynset synset = (BabelSynset) iterator.next();

                /*System.out.println(
                        "Synset ID: " + synset.getId() + "\t" 
                      // "Synset toString PT: " + synset.toString(lang) + "\t" 
                      //+ "Synset toString EN: " + synset.toString(Language.EN) + "\t" 
                      + "Synset main sense PT: " + synset.getMainSense(lang).getLemma() + "\t"
                      + "Synset main sense EN: " + synset.getMainSense(Language.EN).getLemma()
                /* +synset.getGlosses(lang));*/
            }
        }
    }

    /**
     *
     * @param level indica o numero de vezes que repetira a tarefa de expanção
     * @param synsets synsets de cada um dos tokens da query do utilizador
     */
    private void expand(int level, List<BabelSynset> synsets) {

        HashSet<BabelSynset> synsetEdges = new HashSet<>(); // aqui serão guardados os edges (synsets) resultantes da expansão de um conjunto de synsets
        toFileHash = new HashSet<>();

        for (int i = 0; i < level; i++) {
            if (synsetEdges.isEmpty()) {
                synsetEdges = expandSynsets(synsets);
            } else {
                synsetEdges = expandSynsets(new ArrayList<>(synsetEdges));
            }
        }
        fl.writeHashToFile(toFileHash);
        clearSynsetEdges(synsetEdges);
    }

    /**
     *
     * @param synsets
     * @return retorna o hashset com todos os edges de todos os synsets
     */
    private HashSet<BabelSynset> expandSynsets(List<BabelSynset> synsets) {
        HashSet<BabelSynset> localSynstetEdges = new HashSet<>();
        for (BabelSynset synset : synsets) {
            relation = new Relation(synset.getId(), synset.getMainSense(Language.EN).getLemma(), null); // adicionar novo synset
            getEdges(synset, localSynstetEdges);
        } // depois de obter edges, que foram guardados para variavel global synsetEdges, esta variavel duplica-se para guardar edges do proximo synset.
        return localSynstetEdges;
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
            result = bn.getSynsets(pw.getLemma(), lang, pw.getposTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private List<BabelSense> getSenses(ProcessedWord pw) {
        List<BabelSense> result = null;
        try {
            result = bn.getSenses(pw.getLemma(), lang, pw.getposTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private HashSet<BabelSynset> getEdges(BabelSynset synset, HashSet<BabelSynset> synsetEdges) {
        for (BabelSynsetIDRelation edge : synset.getEdges()) {
            if (isRightPointer(edge.getPointer())) {
                try {
                    BabelPointer pointer = edge.getPointer(); // identifica a relação existente entre o synset e edges
                    BabelSynsetID edgeSynsetId = edge.getBabelSynsetIDTarget();
                    String edgeSynsetLemma = edgeSynsetId.toBabelSynset().getMainSense(Language.EN).getLemma();
                    
                    relation.addEdge(edgeSynsetId, edgeSynsetLemma, pointer);
                                        
                    toFileHash.add(relation.toString());
                } catch (IOException ex) {
                    Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    synsetEdges.add(edge.getBabelSynsetIDTarget().toBabelSynset());
                } catch (IOException ex) {
                    Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return synsetEdges;
    }

    private boolean isRightPointer(BabelPointer pointer) {
        return !pointer.getSymbol().equals("r");
    }
    
    public void defineSynsetCompareHash(HashSet<String> input){
        
    }

}
