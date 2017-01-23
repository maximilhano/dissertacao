
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.LangIdent;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.SWIGTYPE_p_splitter_status;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;
import edu.upc.freeling.Word;

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

    private ListSentence userQueryAnalysis(String userQuery) {
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

        return ls;
    }

    public void printAnalysis(String userQuery) {
        ListSentence ls = this.userQueryAnalysis(userQuery);
        ListSentenceIterator sIt = new ListSentenceIterator(ls);

        while (sIt.hasNext()) {
            Sentence s = sIt.next();
            this.wordIterator(s);
        }
    }

    public void wordIterator(Sentence s) {
        ListWordIterator wIt = new ListWordIterator(s);
        while (wIt.hasNext()) {
            Word w = wIt.next();
            System.out.println("Word: " + w.getLemma());
            System.out.println("Word form: " + w.getForm());
            System.out.println("Word lexical form: " + w.getLcForm());
            System.out.println("Word phform: " + w.getPhForm());
            System.out.println("Word senses: " + w.getSensesString());
            System.out.println("Word tag: " + w.getTag());
            System.out.println("Word position: " + w.getPosition());
            System.out.println("Analyses: " + w.getAnalysis().toString());
        }
    }
}
