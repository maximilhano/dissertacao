package QuestionsAnswers;

import NLP.NLPmodule;
import NLP.ProcessedWord;
import Semantic.SemanticAnalysis;
import java.io.IOException;
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
public class Question {

    private final NLPmodule nlpm;
    private final QuestionTypeAnalysis questionTypeAnalysis;
    private final AnswerTypeAnalysis answerTypeAnalysis;
    private final SemanticAnalysis semanticAnalysis;

    private final QuestionTypes questionType;
    private final List<String> answerType;

    private final String userQuery;
    private HashSet<ProcessedWord> processedWordList;

    public Question(NLPmodule nlpm, QuestionTypeAnalysis questionTypeAnalysis, AnswerTypeAnalysis answerTypeAnalysis, SemanticAnalysis semanticAnalysis, String userQuery) {
        this.nlpm = nlpm;
        this.questionTypeAnalysis = questionTypeAnalysis;
        this.answerTypeAnalysis = answerTypeAnalysis;
        this.semanticAnalysis = semanticAnalysis;
        this.userQuery = userQuery;

        processedWordList = this.nlpm.analyzeUserQuery(this.userQuery);
        questionType = this.questionTypeAnalysis.getQuestionType(processedWordList);
        answerType = this.answerTypeAnalysis.getAnswerType(questionType);
        processedWordList = this.semanticAnalysis.setSemanticData(processedWordList);
        
        
    }
}
