/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuestionsAnswers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Maksym
 */
public class AnswerTypeAnalysis {
    private final HashMap<QuestionTypes,HashSet<String>> answerTypes = 
            new HashMap<QuestionTypes,HashSet<String>>(){{
                put(QuestionTypes.WHO,new HashSet<>(Arrays.asList("dbo:Person", "dbo:Agent"))); // definir de onde vem a lista
                put(QuestionTypes.WHEN,new HashSet<>(Arrays.asList("xsd:date")));
                put(QuestionTypes.WHERE,new HashSet<>(Arrays.asList("dbo:Place")));
                put(QuestionTypes.WHICH,new HashSet<>(Arrays.asList("dbo:Person", "dbo:Place","xsd:date","xsd:time",
                                                       "owl:Thing","dbo:Game","dbo:Eukaryote","dbc:Dog_breeds")));
                put(QuestionTypes.HOW,new HashSet<>(Arrays.asList("xsd:integer", "xsd:float", "xsd:long","xsd:double",
                                                    "xsd:duration","xsd:positiveInteger","xsd:negativeInteger",
                                                    "xsd:unsignedInt","xsd:unsignedLong","xsd:unsignedShort")));
            }};
    
    public HashSet<String> getAnswerType(QuestionTypes questionType){
        return answerTypes.get(questionType);
    }
}
