package QuestionsAnswers;

import Database.LocalDatabase;
import NLP.NLPmodule;
import NLP.ProcessedWord;
import Semantic.SemanticAnalysis;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private HashSet<String> entitiesInQuestion = new HashSet<>(); // entidades nomeadas na pergunta
    private HashSet<String> conceptsInQuestion = new HashSet<>(); //  conceitos na pergunta
    private HashSet<String> propertiesInQuestion; // propriedades, são conceitos não reconhecidos.

    public Question(NLPmodule nlpm, QuestionTypeAnalysis questionTypeAnalysis, AnswerTypeAnalysis answerTypeAnalysis, SemanticAnalysis semanticAnalysis, LocalDatabase localDatabase, String userQuery) {
        this.nlpm = nlpm;
        this.questionTypeAnalysis = questionTypeAnalysis;
        this.answerTypeAnalysis = answerTypeAnalysis;
        this.semanticAnalysis = semanticAnalysis;
        this.localDatabase = localDatabase;
        this.userQuery = userQuery;

        // first, analise morfologica
        processedWordList = this.nlpm.analyzeUserQuery(this.userQuery); // lemma + POStag
        System.out.println("\nProcessedWordList\n");
 
        Iterator<ProcessedWord> it = processedWordList.iterator();
        while (it.hasNext()) {
            ProcessedWord next = it.next();
            System.out.println(next.getLemma() + "\n  lemma: " + next.getLemma() + "\n  POStag: " + next.getPosTag());
        }
        
        // second question type
        questionType = this.questionTypeAnalysis.getQuestionType(processedWordList);
        System.out.println("\nQuestionType: " + questionType);
        
        //third, answer type
        answerType = this.answerTypeAnalysis.getAnswerType(questionType);
        System.out.println("\nAnswerType: " + answerType);
        
        //fourth semantic analysis
        processedWordList = this.semanticAnalysis.setSemanticData(processedWordList); // list of synsets + type
        System.out.println("\n\n After BabelNet");
        
        it = processedWordList.iterator();
        while (it.hasNext()) {
            ProcessedWord next = it.next();
            System.out.println("\n\n  lemma: " + next.getLemma() + 
                    "\n  POStag: " + next.getPosTag() + 
                    "\n  Synsets:");
            Map<String, BabelSynsetType> processedWordSynsets = next.getSynsets();
            if(processedWordSynsets != null)
                for(Map.Entry<String, BabelSynsetType> entry : processedWordSynsets.entrySet() ){
                    String synset = entry.getKey();
                    BabelSynsetType synsetType = entry.getValue();
                    System.out.println(synset + " -> " + synsetType);
                }
        }
        
        // fifth, focus entities
        setQuestionFocusEntities(); // lista de entidade
        System.out.println("\n\n Question focus entities: " + entitiesInQuestion);
        System.out.println("\n\n Question concepts: " + conceptsInQuestion);
        
        // sixth, 
        localDatabase.getTriples(entitiesInQuestion);
        
    }
    
    private void setQuestionFocusEntities(){
        Iterator<ProcessedWord> it = processedWordList.iterator();
        while (it.hasNext()) {
            ProcessedWord processedWord = it.next();
            
            Map<String, BabelSynsetType> processedWordSynsets = processedWord.getSynsets();
            if(processedWordSynsets != null){
                for (Map.Entry<String, BabelSynsetType> entry : processedWordSynsets.entrySet()) {
                    String synset = entry.getKey();
                    System.out.println("Synset antes: " + synset);
                    synset = synset.replaceAll("[(),]", "");
                    System.out.println("Synset depois: " + synset);
                    BabelSynsetType synsetType = entry.getValue();

                    switch (synsetType) {
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
}
