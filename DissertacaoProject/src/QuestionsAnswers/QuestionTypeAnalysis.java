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

    public QuestionTypes getQuestionType(String lemma) {

        switch (lemma) {
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

        return null;
    }

}
