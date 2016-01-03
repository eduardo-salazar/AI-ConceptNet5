package com.ai;
import com.ai.conceptnet5.Relation;
import com.ai.conceptnet5.services.Search;
import com.ai.conceptnet5.services.Concept;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.*;
import java.util.Collection;
import edu.stanford.nlp.ling.Sentence;

public class Main {

    public static void main(String[] args) {
	// write your code her
        System.out.println("Testing first concept net call");
        String input1 = "teach";
        String input2 = "student";
        try {
            List<Relation> resultDirect = new ArrayList<Relation>();
            List<List<Relation>> result = new ArrayList<List<Relation>>();
            List<Relation> list = new Search(input1,input2).call();
            resultDirect.addAll(list);

            //Find more realtions
            List<Relation> p1 = new Search(input1,"").call();
            for(int i=0;i<p1.size();i++){
                List<Relation> p2 = new Search(p1.get(i).getEnd(),"/c/en/"+input2).call2();
                List<Relation> elements = new ArrayList<Relation>();
                if(p2.size()>0){
                    elements.add(p1.get(i));
                    elements.addAll(p2);
                    result.add(elements);
                }
            }
            //Find more realtions
            p1 = new Search(input2,"").call();
            for(int i=0;i<p1.size();i++){
                List<Relation> p2 = new Search(p1.get(i).getEnd(),"/c/en/"+input1).call2();
                List<Relation> elements = new ArrayList<Relation>();
                if(p2.size()>0){
                    elements.add(p1.get(i));
                    removeDuplicated(p2);
                    elements.addAll(p2);
                    result.add(elements);
                }
            }

            // Print Direct and Indirect Relations
            System.out.println("Direct relations");
            for(int i=0;i<resultDirect.size();i++){
                System.out.println(resultDirect.get(i));
            }
            System.out.println();
            System.out.println("Indirect relations");
            for(int i=0;i<result.size();i++){

                for(int j=0;j<result.get(i).size();j++){
                    System.out.println(result.get(i).get(j));
                }
                System.out.println();
            }


            // Parse and get Lexical of sentence
            LexicalizedParser lp = LexicalizedParser.loadModel(
                    "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                    "-maxLength", "80", "-retainTmpSubcategories");
            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            // Uncomment the following line to obtain original Stanford Dependencies
            // tlp.setGenerateOriginalDependencies(true);
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            for(int i=0;i<result.size();i++){

                for(int j=0;j<result.get(i).size();j++){
                    Tree parse = lp.apply(Sentence.toWordList(result.get(i).get(j).getSurfaceText().replaceAll("\\[","").replaceAll("\\]","").split(" ")));
                    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                    Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
                    System.out.println(tdl);
                }
                System.out.println();
            }


        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }

    public static void removeDuplicated(List<Relation> elements){
        for(int i=0;i<elements.size();i++){
            for(int j=i+1;j<elements.size();j++){
                String start = elements.get(i).getStart();
                String end = elements.get(i).getEnd();
                String rel = elements.get(i).getRel();

                // Compare with the rest of the list
                if(elements.get(j).getStart().equals(start)
                        && elements.get(j).getEnd().equals(end)
                        && elements.get(j).getRel().equals(rel)) {
                    elements.remove(j);
                    --j;
                }
            }
        }
    }
}
