package QuestionsAnswers;

import Database.LocalDatabase;
import NLP.NLPmodule;
import NLP.ProcessedWord;
import Semantic.SemanticAnalysis;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private final LocalDatabase localDatabase;

    private final QuestionTypes questionType;
    private final List<String> answerType;

    private final String userQuery;
    private HashSet<ProcessedWord> processedWordList;
    private HashSet<String> entitiesInQuestion; // entidades nomeadas na pergunta
    private HashSet<String> conceptsInQuestion; // conceitos na pergunta
    private HashSet<String> propertiesInQuestion; // propriedades, são conceitos não reconhecidos.

    public Question(NLPmodule nlpm, QuestionTypeAnalysis questionTypeAnalysis, AnswerTypeAnalysis answerTypeAnalysis, SemanticAnalysis semanticAnalysis, LocalDatabase localDatabase, String userQuery) {
        this.nlpm = nlpm;
        this.questionTypeAnalysis = questionTypeAnalysis;
        this.answerTypeAnalysis = answerTypeAnalysis;
        this.semanticAnalysis = semanticAnalysis;
        this.localDatabase = localDatabase;
        this.userQuery = userQuery;

        processedWordList = this.nlpm.analyzeUserQuery(this.userQuery);
        questionType = this.questionTypeAnalysis.getQuestionType(processedWordList);
        answerType = this.answerTypeAnalysis.getAnswerType(questionType);
        processedWordList = this.semanticAnalysis.setSemanticData(processedWordList);
        setQuestionFocusEntities();
        propertiesInQuestion = localDatabase.getTriples(entitiesInQuestion,conceptsInQuestion);
        
    }
    
    private void setQuestionFocusEntities(){
        Iterator<ProcessedWord> it = processedWordList.iterator();
        while (it.hasNext()) {
            ProcessedWord processedWord = it.next();
            
            Map<String, BabelSynsetType> processedWordSynsets = processedWord.getSynsets();
            
            for(Map.Entry<String, BabelSynsetType> entry : processedWordSynsets.entrySet() ){
                String synset = entry.getKey();
                BabelSynsetType synsetType = entry.getValue();
                
                switch(synsetType){
                        case NAMED_ENTITY:
                            this.entitiesInQuestion.add(synset);
                            break;
                        case CONCEPT:
                            this.conceptsInQuestion.add(synset);
                            break;
                }
            }
        }
    }
}
