
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
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
    
    public void printSenses(List<ProcessedWord> lpw){
        for(ProcessedWord pw : lpw){
            System.out.println(getSenses(pw));
        }
    }

    private List<BabelSense> getSenses (ProcessedWord pw){
        List<BabelSense> result = null;
        try {
            result = this.bn.getSenses(pw.getLemma(), lang, pw.getposTag());
        } catch (IOException ex) {
            Logger.getLogger(BabelnetModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
