/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuestionsAnswers;
import NLP.ProcessedWord;
import it.uniroma1.lcl.babelnet.data.BabelPOS;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Maksym
 */

//TODO 
public class QuestionTypeAnalysis {

    public QuestionTypes getQuestionType(HashSet<ProcessedWord> lpw){
        Iterator<ProcessedWord> it = lpw.iterator(); // to do
        while (it.hasNext()) {
            ProcessedWord next = it.next();
            if(next.getPosTag() == BabelPOS.ADVERB){
                switch (next.getLemma()) {
                    case "quem":
                        return QuestionTypes.WHO;
                    case "quando":
                        return QuestionTypes.WHEN;
                    case "onde":
                        return QuestionTypes.WHERE;
                    case "qual":
                        return QuestionTypes.WHICH;
                    case "quanto":
                        return QuestionTypes.HOW;
                }
                it.remove(); // remove adverb from list
            }
        }
        return null;
    }
    
}
