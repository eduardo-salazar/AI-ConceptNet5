package com.ai;
import com.ai.conceptnet5.Relation;
import com.ai.conceptnet5.services.Search;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import com.ai.com.ai.model.LexicalGroup;

import com.sun.deploy.xml.XMLable;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.ReadDataTagged;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.*;
import java.util.Collection;
import edu.stanford.nlp.parser.common.ParserQuery;
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

//            // Print Direct and Indirect Relations
//            System.out.println("Direct relations");
//            for(int i=0;i<resultDirect.size();i++){
//                System.out.println(resultDirect.get(i));
//            }
//            System.out.println();
//            System.out.println("Indirect relations");
//            for(int i=0;i<result.size();i++){
//
//                for(int j=0;j<result.get(i).size();j++){
//                    System.out.println(result.get(i).get(j));
//                }
//                System.out.println();
//            }


            standforImplementation(result);



        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }

    public static void standforImplementation(List<List<Relation>> result){
        // Parse and get Lexical of sentence
        // Try to create sentence with each group of relations
        for(List<Relation> relations: result){
            // 1. Group word by tags
            LexicalGroup groups = parseGroups(relations);

            // Sow Matrix
            System.out.println("Matrix of word and tags");
            groups.printLexicalGroups();
        }

    }

    private static LexicalGroup parseGroups(List<Relation> relations){
        LexicalGroup groupedWord = new LexicalGroup(); // this is the result


        LexicalizedParser lp = LexicalizedParser.loadModel(
                "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                "-maxLength", "80", "-retainTmpSubcategories");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        tlp.setGenerateOriginalDependencies(true);

        //First Sentence (This sentence is the relation between X1 -> R1 - C1)
        String[] text = splitSentence(relations.get(0).getSurfaceText());// + " " + result.get(i).get(j).getSurfaceText()
        Tree parse = lp.apply(Sentence.toWordList(text));
        List<TaggedWord> nodes = parse.taggedYield();
        for(TaggedWord word : nodes){
            //Add tags to result
            groupedWord.add(word.tag(),word.word());
        }


        for(int i=1; i<relations.size();i++) {
            //Rest sentences (The rest are relation between C1 - R2 -> X2)
            String[] text2 = splitSentence(relations.get(i).getSurfaceText());// + " " + result.get(i).get(j).getSurfaceText()
            Tree parse2 = lp.apply(Sentence.toWordList(text2));
            List<TaggedWord> nodes2 = parse2.taggedYield();
            for(TaggedWord word : nodes2){
                //Add tags to result
                groupedWord.add(word.tag(),word.word());
            }
        }

        return groupedWord;

    }

    public static String[] splitSentence(String sentence){
        List<String> values = new ArrayList<String>();
        String cleanedSentence = cleanSentence(sentence);
        for(String e:cleanedSentence.split("\\s+(?![^\\[]*\\])")){
            values.add(e.replaceAll("\\[\\[","").replaceAll("\\]\\]","").trim());
        }
        return values.toArray(new String[values.size()]);
    }

    private static String cleanSentence(String sent){
        return sent.replaceAll("(\\.|\\*|\")","");
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
