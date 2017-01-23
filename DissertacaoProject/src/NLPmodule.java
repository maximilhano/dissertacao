
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.LangIdent;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.SWIGTYPE_p_splitter_status;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;

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
    private final String FREELINGDIR = "/usr/local";
    private final String DATA = FREELINGDIR + "/share/freeling/";
    private final String LANG = "pt";

    private LangIdent lgid;
    private Tokenizer tk;
    private Splitter sp;
    private SWIGTYPE_p_splitter_status sid;

    private HmmTagger tg;
    private ChartParser parser;
    private DepTxala dep;
    private Nec neclass;

    private Senses sen;
    private Ukb dis;

    private MacoOptions op;
    private Maco mf;

    public NLPmodule() {
        inicialize();
    }
    
    private void inicialize(){
        System.loadLibrary("freeling_javaAPI");

        Util.initLocale("default");

        this.lgid = new LangIdent(DATA + "/common/lang_ident/ident.dat");
        this.tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
        this.sp = new Splitter(DATA + LANG + "/splitter.dat");
        this.sid = sp.openSession();
        this.tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);
        this.parser = new ChartParser(DATA + LANG + "/chunker/grammar-chunk.dat");
        this.dep = new DepTxala(DATA + LANG + "/dep_treeler/dependences.dat", parser.getStartSymbol());
        this.neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");
        this.sen = new Senses(DATA + LANG + "/senses.dat"); // sense dictionary
        this.dis = new Ukb(DATA + LANG + "/ukb.dat"); // sense disambiguator

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
    
    public void userQueryAnalysis(String userQuery){
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
    }
}
