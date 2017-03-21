package QuestionsAnswers;

import NLP.NLPmodule;
import NLP.ProcessedWord;
import Semantic.SemanticAnalysis;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
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
public class Question {

    private final String question;
    private HashSet<ProcessedWord> lpw;
    private final QuestionTypes questionType;
    private final List<String> answerType;

    private final NLPmodule nlpm;
    private final SemanticAnalysis sa;
    private final QuestionTypeAnalysis qta = new QuestionTypeAnalysis();
    private final AnswerTypeAnalysis ata = new AnswerTypeAnalysis();

    public Question(String question, NLPmodule nlpm, SemanticAnalysis sa) throws IOException {
        this.question = question;
        this.nlpm = nlpm;
        this.sa = sa;

        lpw = this.nlpm.analyzeUserQuery(this.question);

        Iterator<ProcessedWord> i = lpw.iterator();
        while (i.hasNext()) {
            ProcessedWord next = i.next();
            System.out.println("Word :" + next.getLemma() + " POS identifier: " + next.getPosTag());
        }

        questionType = qta.getQuestionType(lpw);
        answerType = ata.getAnswerType(questionType);
        
        lpw = sa.getExpandedQuery(lpw);
        
        System.out.println(answerType);
    }

}
