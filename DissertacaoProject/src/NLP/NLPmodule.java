package NLP;


import edu.upc.freeling.Analysis;
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTree;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.LangIdent;
import edu.upc.freeling.ListAnalysis;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.ParseTree;
import edu.upc.freeling.SWIGTYPE_p_splitter_status;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;
import edu.upc.freeling.Word;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maksym
 */
public class NLPmodule {

    private static final String FREELINGDIR = "/usr/local";
    private static final String DATA = FREELINGDIR + "/share/freeling/";
    private static final String LANG = "pt";

    private static LangIdent lgid;
    private static Tokenizer tk;
    private static Splitter sp;
    private static SWIGTYPE_p_splitter_status sid;

    private static HmmTagger tg;
    private static ChartParser parser;
    private static DepTxala dep;
    private static Nec neclass;

    private static Senses sen;
    private static Ukb dis;

    private static MacoOptions op;
    private static Maco mf;

    public NLPmodule() {
        inicialize();
    }

    private static void inicialize() {
        System.loadLibrary("freeling_javaAPI");

        Util.initLocale("default");

        lgid = new LangIdent(DATA + "/common/lang_ident/ident.dat");
        tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
        sp = new Splitter(DATA + LANG + "/splitter.dat");
        sid = sp.openSession();
        tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);
        parser = new ChartParser(DATA + LANG + "/chunker/grammar-chunk.dat");
        dep = new DepTxala(DATA + LANG + "/dep_treeler/dependences.dat", parser.getStartSymbol());
        neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");
        sen = new Senses(DATA + LANG + "/senses.dat"); // sense dictionary
        dis = new Ukb(DATA + LANG + "/ukb.dat"); // sense disambiguator

        // Create options set for maco analyzer.
        // Default values are Ok, except for data files.
        op = new MacoOptions(LANG);

        op.setDataFiles("",
                DATA + "common/punct.dat",
                DATA + LANG + "/dicc.src",
                DATA + LANG + "/afixos.dat",
                "",
                DATA + LANG + "/locucions.dat",
                DATA + LANG + "/np.dat",
                DATA + "common/quantities_default.dat",
                DATA + LANG + "/probabilitats.dat");

        mf = new Maco(op);
        mf.setActiveOptions(false, true, true, true, // select which among created 
                true, true, false, true, // submodules are to be used. 
                true, true, true, true);  // default: all created submodules
    }

    public HashSet<ProcessedWord> analyzeUserQuery(String userQuery) {
        // Extract the tokens from the line of text.
        ListWord l = tk.tokenize(userQuery);

        // Split the tokens into distinct sentences.
        ListSentence ls = sp.split(sid, l, false);

        // Perform morphological analysis
        mf.analyze(ls);

        // Perform part-of-speech tagging.
        tg.analyze(ls);

        // Perform named entity (NE) classificiation.
        neclass.analyze(ls);

        sen.analyze(ls);
        dis.analyze(ls);
        // Chunk parser

        parser.analyze(ls);
        // Dependency parser
        dep.analyze(ls);

        return getProcessedWords(ls);
    }

    private HashSet<ProcessedWord> getProcessedWords(ListSentence ls) {
        HashSet<ProcessedWord> processedWords = new HashSet<>();
        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        
        while (sIt.hasNext()) {
            Sentence s = sIt.next();
            ListWordIterator wIt = new ListWordIterator(s);
            while (wIt.hasNext()) {
                Word w = wIt.next();
                if(w.getLemma().length() > 2){ //to do use stopwords https://gist.github.com/alopes/5358189
                    processedWords.add(new ProcessedWord(w.getLemma(), this.posIdentifier(w.getTag())));
                }
            }
        }
        
        return processedWords;
    }


    private void sensesIterator(ListSentence ls) {
        ListSentenceIterator sIt = new ListSentenceIterator(ls);

        while (sIt.hasNext()) {
            Sentence s = sIt.next();
            //this.printDepTree(0, this.getDepTree(s));
            //this.printParseTree(0, this.getParseTree(s));
            this.wordIterator(s);
        }
    }

    private BabelPOS posIdentifier(String tag) {
        BabelPOS resTag =null;
        char tagFirstChar = tag.charAt(0);
        
        switch (tagFirstChar) {
            case 'A':
                resTag = BabelPOS.ADJECTIVE;
                break;
            case 'C':
                resTag = BabelPOS.CONJUNCTION; // não vai haver 
                break;
            case 'D':
                resTag = BabelPOS.DETERMINER; // não vai haver
                break;
            case 'I':
                resTag = BabelPOS.INTERJECTION; // não vai haver
                break;
            case 'N':
                resTag = BabelPOS.NOUN;
                break;
            case 'P':
                resTag = BabelPOS.PRONOUN;
                break;
            case 'R':
                resTag = BabelPOS.ADVERB;
                break;
            case 'S':
                //resTag = "adposition";
                break;
            case 'V':
                resTag = BabelPOS.VERB;
                break;
            case 'Z':
                //resTag = "number";
                break;
            case 'W':
                //resTag = "date";
                break;
        }
        return resTag;
    }

    private void printDepTree(int depth, DepTree tr) {

        DepTree child = null;
        DepTree fchild = null;
        long nch;
        int last, min;
        Boolean trob;

        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        System.out.print(
                tr.begin().getLink().getLabel() + "/"
                + tr.begin().getLabel() + "/");

        Word w = tr.begin().getWord();

        System.out.print(
                "(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
        //printSenses(w);
        System.out.print(")");

        nch = tr.numChildren();

        if (nch > 0) {
            System.out.println(" [");

            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);

                if (child != null) {
                    if (!child.begin().isChunk()) {
                        printDepTree(depth + 1, child);
                    }
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            // Print chunks (in order)
            last = 0;
            trob = true;

            // While an unprinted chunk is found, look for the one with lower
            // chunk_ord value.
            while (trob) {
                trob = false;
                min = 9999;

                for (int i = 0; i < nch; i++) {
                    child = tr.nthChildRef(i);

                    if (child.begin().isChunk()) {
                        if ((child.begin().getChunkOrd() > last)
                                && (child.begin().getChunkOrd() < min)) {
                            min = child.begin().getChunkOrd();
                            fchild = child;
                            trob = true;
                        }
                    }
                }
                if (trob && (child != null)) {
                    printDepTree(depth + 1, fchild);
                }

                last = min;
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.print("]");
        }

        System.out.println("");
    }

    private void printParseTree(int depth, ParseTree tr) {
        Word w;
        long nch;

        // Indentation
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        nch = tr.numChildren();

        if (nch == 0) {
            // The node represents a leaf
            if (tr.begin().getInformation().isHead()) {
                System.out.print("+");
            }
            w = tr.begin().getInformation().getWord();
            System.out.print("(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
            //printSenses(w);
            System.out.println(")");
        } else {
            // The node is non-terminal
            if (tr.begin().getInformation().isHead()) {
                System.out.print("+");
            }

            System.out.println(tr.begin().getInformation().getLabel() + "_[");

            for (int i = 0; i < nch; i++) {
                ParseTree child = tr.nthChildRef(i);

                if (!child.empty()) {
                    printParseTree(depth + 1, child);
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.println("]");
        }
    }
    
    public void analysisIterator(Word w) {
        System.out.println("Analysis Iterator\n");
        ListAnalysis la = w.getAnalysis();

        while (la.front() != null) {
            Analysis a = la.front();
            System.out.println(a.getLemma() + a.getTag() + a.getProb() + a.getDistance());
        }
    }
    
    public void getWordAnalysis(Word w) {
        Analysis anl = w.getAnalysis().front();
        //System.out.println("\n\t Analysis \n");
        //System.out.println("Lemma: " + anl.getLemma());
        //System.out.println("Tag: " + anl.getTag());
        //System.out.println("Distance: " + anl.getDistance());
        //System.out.println("Probability: " + anl.getProb());
        //System.out.println("Senses string: " + anl.getSensesString());
        //anl.setLemma(this.posIdentifier(anl.getLemma()));
        //anl.setLemma(posIdentifier(w.getTag()));
    }
    
    public void wordIterator(Sentence s) {
        ListWordIterator wIt = new ListWordIterator(s);
        while (wIt.hasNext()) {
            Word w = wIt.next();

            /*System.out.println("Word lemma: " + w.getLemma());
            System.out.println("Word senses: " + w.getSensesString());
            System.out.println("Word tag: " + w.getTag());
            System.out.println("Word position: " + w.getPosition());
            System.out.println("Analyses: " + w.getAnalysis());*/
            //this.analysisIterator(w);
            this.getWordAnalysis(w);
        }
    }
    
    public DepTree getDepTree(Sentence s) {
        return s.getDepTree();
    }

    public ParseTree getParseTree(Sentence s) {
        return s.getParseTree();
    }
}
