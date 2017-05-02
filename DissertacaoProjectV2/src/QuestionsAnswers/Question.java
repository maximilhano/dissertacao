package QuestionsAnswers;

import Database.LocalDatabase;
import Logging.Logger;
import NLP.NLPmodule;
import NLP.ProcessedWord;
import Semantic.SemanticAnalysis;
import it.uniroma1.lcl.babelnet.BabelSynsetType;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

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

    private QuestionTypes questionType = null;
    private HashSet<String> answerType = new HashSet<>();

    private final String userQuery;
    private HashSet<ProcessedWord> processedWordList;
    private HashSet<String> entitiesInQuestion = new HashSet<>(); // entidades nomeadas na pergunta
    private HashSet<String> conceptsInQuestion = new HashSet<>(); //  conceitos na pergunta
    private HashSet<String> propertiesInQuestion; // propriedades, são conceitos não reconhecidos.

    private String questionDBpediaQuery = "";
    private String questionLocalQuery = "";

    private HashSet<String> possiblePredicates = new HashSet<>();
    private HashSet<String> possibleClasses = new HashSet<>();

    private final Logger log = new Logger();

    public Question(NLPmodule nlpm, QuestionTypeAnalysis questionTypeAnalysis, AnswerTypeAnalysis answerTypeAnalysis, SemanticAnalysis semanticAnalysis, LocalDatabase localDatabase, String userQuery) {
        this.nlpm = nlpm;
        this.questionTypeAnalysis = questionTypeAnalysis;
        this.answerTypeAnalysis = answerTypeAnalysis;
        this.semanticAnalysis = semanticAnalysis;
        this.localDatabase = localDatabase;
        this.userQuery = userQuery;

        // first, analise morfologica
        System.out.println("\nAnalise morfologica..\n");
        processedWordList = this.nlpm.analyzeUserQuery(this.userQuery); // lemma + POStag

        if (!processedWordList.isEmpty()) {

            for (ProcessedWord p : processedWordList) {
                System.out.println("\n -l- " + p.getLemma() + " -p- " + p.getPosTag());
            }

            // second question type
            this.getQuestionType();
            
            if (questionType != null) {
                //third, answer type
                System.out.println("\nTipo de pergunta: " + this.questionType.toString());
                answerType = this.answerTypeAnalysis.getAnswerType(questionType);
                System.out.println("\nTipo de resposta: " + this.answerType);
            }

            //fourth semantic analysis
            System.out.println("\nAnalise semantica:\n");
            processedWordList = this.semanticAnalysis.setSemanticData(processedWordList); // list of synsets + type

            // fifth, focus entities
            System.out.println("Conceitos e entidades nomeadas");
            setQuestionEntitiesAndConcepts(); // lista de entidade

            System.out.println("\nEntities: \n");
            for (String s : entitiesInQuestion) {
                System.out.println("\n -e- " + s);
            }

            System.out.println("\nConcepts: \n");
            for (String s : conceptsInQuestion) {
                System.out.println("\n -c- " + s);
            }

            log.writeQuestionInfo(this.userQuery, this.questionType, this.answerType, this.processedWordList, this.entitiesInQuestion, this.conceptsInQuestion);

//            if (!entitiesInQuestion.isEmpty() && !conceptsInQuestion.isEmpty()) {
//                // sixth, 
//                localDatabase.getTriples(entitiesInQuestion);
//
//                Iterator<String> entities = entitiesInQuestion.iterator();
//                while (entities.hasNext()) {
//                    System.out.println("1");
//                    String entity = entities.next();
//                    System.out.println("2");
//                    addNamedEntityToQuery(entity);
//                    System.out.println("3");
//                    if(!answerType.isEmpty())
//                        addAnswerTypeToQuery();
//                    System.out.println("4");
//                    HashSet<String> aux = localDatabase.getPossibleProperties(entity, conceptsInQuestion);
//                    System.out.println("4.5 - aux.size: " + aux.size());
//                    if (!aux.isEmpty()) {
//                        System.out.println("5");
//                        possiblePredicates.addAll(aux);
//                        addPropertyToQuery();
//                        log.writePossiblePredicates(possiblePredicates);
//                    }
//                    System.out.println("6");
//
//                    getAnswer();
//                    System.out.println("7");
//                    questionLocalQuery = "";
//                    possiblePredicates.clear();
//                }
//            }
        }
    }

    private void getQuestionType() {
        Iterator<ProcessedWord> it = this.processedWordList.iterator(); // to do
        while (it.hasNext()) {
            ProcessedWord next = it.next();
            if (next.getPosTag() == BabelPOS.ADVERB) {
                this.questionType = this.questionTypeAnalysis.getQuestionType(next.getLemma());
                it.remove();
                break;
            }
        }
    }

    private void setQuestionEntitiesAndConcepts() {
        Iterator<ProcessedWord> it = processedWordList.iterator();
        while (it.hasNext()) {
            ProcessedWord processedWord = it.next();

            Map<String, BabelSynsetType> processedWordSynsets = processedWord.getSynsets();
            if (processedWordSynsets != null) {
                for (Map.Entry<String, BabelSynsetType> entry : processedWordSynsets.entrySet()) {
                    String synset = entry.getKey();
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

    private void addNamedEntityToQuery(String namedEntity) {
        questionLocalQuery += "  <http://dbpedia.org/resource/" + namedEntity + "> ?p ?o .\n OPTIONAL { ?o rdf:type ?otype. } \n";
    }

    private void addAnswerTypeToQuery() {

        String filter = " FILTER (";
        Iterator<String> it = answerType.iterator();

        while (it.hasNext()) {
            String answerType = it.next();
            if (answerType.startsWith("xsd")) {

                filter += " datatype(?o) = " + answerType;
            } else if (answerType.startsWith("db")) {
                filter += " ?otype = " + answerType;
            }
            if (it.hasNext()) {
                filter += " ||";
            }
        }

        filter += ")  \n";

//
//        switch (questionType) {
//            case HOW:
//                filter += "FILTER (";
//
//                while (it.hasNext()) {
//                    String next = it.next();
//                    filter += "datatype(?o) = " + next;
//                    if (it.hasNext()) {
//                        filter += " || ";
//                    }
//                }
//                filter += ").\n";
//
//                break;
//            case WHICH:
//                filter += "FILTER (datatype(?o) != '') .\n";
//                break;
//            default:
//                filter += "?o rdf:type ?otype .\n FILTER (";
//
//                while (it.hasNext()) {
//                    String next = it.next();
//                    filter += "?otype = " + next;
//                    if (it.hasNext()) {
//                        filter += " || ";
//                    }
//                }
//                filter += ").\n";
//
//                break;
//        }
        questionLocalQuery += filter;
//        System.out.println("addAnswerTypeToQuery: \n Query: " + questionLocalQuery);
    }

    private void addPropertyToQuery() {
        String filter = "FILTER (";

        Iterator<String> it = possiblePredicates.iterator();
        while (it.hasNext()) {
            String next = it.next();
            filter += "?p = <" + next + ">";
            if (it.hasNext()) {
                filter += " || ";
            }
        }
        filter += ").\n";

        questionLocalQuery += filter;
    }

    private void getAnswer() {
        log.writeFinalQuery(questionLocalQuery);
        HashSet<QuerySolution> result = localDatabase.runFusekiSelectQuery("SELECT * WHERE{" + questionLocalQuery + "}");
        if(!result.isEmpty())
        {
             log.writeResultOfFinalQuery(result);
        }
    }

}
