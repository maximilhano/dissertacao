/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuestionsAnswers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Maksym
 */
public class AnswerTypeAnalysis {
    private final HashMap<QuestionTypes,List<String>> answerTypes = 
            new HashMap<QuestionTypes,List<String>>(){{
                put(QuestionTypes.WHO,Arrays.asList("dbo:Person", "dbo:Agent"));
                put(QuestionTypes.WHEN,Arrays.asList("xsd:date"));
                put(QuestionTypes.WHERE,Arrays.asList("dbo:Place"));
                put(QuestionTypes.WHICH,Arrays.asList("dbo:Person", "dbo:Place","xsd:date","xsd:time",
                                                       "owl:Thing","dbo:Game","dbo:Eukaryote","dbc:Dog_breeds"));
                put(QuestionTypes.HOW,Arrays.asList("xsd:integer", "xsd:float", "xsd:long","xsd:double",
                                                    "xsd:duration","xsd:positiveInteger","xsd:negativeInteger",
                                                    "xsd:unsignedInt","xsd:unsignedLong","xsd:unsignedShort"));
            }};
    
    public List<String> getAnswerType(QuestionTypes questionType){
        return answerTypes.get(questionType);
    }
}
