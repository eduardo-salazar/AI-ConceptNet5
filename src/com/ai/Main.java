package com.ai;
import com.ai.com.ai.model.Markov;
import com.ai.conceptnet5.Relation;
import com.ai.conceptnet5.services.Search;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import com.ai.com.ai.model.LexicalGroup;

import com.sharethis.common.IOUtils;
import com.sun.deploy.xml.XMLable;
import com.sun.jna.StringArray;
import com.sun.media.sound.RealTimeSequencerProvider;
import com.sun.tools.javac.code.Attribute;
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


import java.io.FileReader;
import java.util.Iterator;

import edu.stanford.nlp.util.ArraySet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {



    public static void main(String[] args) {
	// write your code her
        List<String> sentences = new ArrayList<>();
        System.out.println("Testing first concept net call");
        String input1 = "penguin";
        String input2 = "fly";
//        String input1 = "penguin";
//        String input2 = "fly";
        try {
            List<Relation> resultDirect = new ArrayList<Relation>();
            List<List<Relation>> result = new ArrayList<List<Relation>>();
            List<Relation> list = new Search(input1,input2).call();
            resultDirect.addAll(list);
            for(Relation l:list){
                List<Relation> el = new ArrayList<>();
                el.add(l);
                result.add(el);
            }


            //Find realtionsc
            List<Relation> list1 = new Search(input1,"").call();
            List<Relation> list2 = new Search("",input2).call();
            List<Relation> way2 = new ArrayList<>();
            for(Relation r:list1){
                List<Relation> elements = new ArrayList<Relation>();
                elements.add(r);
                for(Relation r2:list2){
                    if(r.getEnd().equals(r2.getStart())) {
                        elements.add(r2);
                    }
                }

                if (elements.size()>1){
                    result.add(elements);
                }

            }
            for(Relation r:list2){
                List<Relation> elements = new ArrayList<Relation>();
                elements.add(r);
                for(Relation r2:list1){
                    if(r.getEnd().equals(r2.getStart())) {
                        elements.add(r2);
                    }
                }

                if (elements.size()>1){
                    result.add(elements);
                }

            }
            list1 = new Search(input2,"").call();
            list2 = new Search("",input1).call();

            for(Relation r:list1){
                List<Relation> elements = new ArrayList<Relation>();
                elements.add(r);
                for(Relation r2:list2){
                    if(r.getEnd().equals(r2.getStart())) {
                        elements.add(r2);
                    }
                }

                if (elements.size()>1){
                    result.add(elements);
                }

            }
            for(Relation r:list2){
                List<Relation> elements = new ArrayList<Relation>();
                elements.add(r);
                for(Relation r2:list1){
                    if(r.getEnd().equals(r2.getStart())) {
                        elements.add(r2);
                    }
                }

                if (elements.size()>1){
                    result.add(elements);
                }

            }




            System.out.println("Total relations:"+result.size());


            sentences.addAll(getIndirectSentences(result));

            for(String sentence: sentences){
                System.out.println("New Sentence : " + sentence);
            }

            //Rank sentences
            //String res_path = "C:/"+TextRank.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"com/ai/res" ;
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }


    public static void printRelations(List<Relation> relations){
        // Print Direct and Indirect Relations
        for(int i=0;i<relations.size();i++){
            System.out.println(relations.get(i));
        }
        System.out.println();

    }

    public static List<String> getIndirectSentences(List<List<Relation>> result){
        // Parse and get Lexical of sentence
        // Try to create sentence with each group of relations
        List<String> sentences= new ArrayList<>();
        List<LexicalGroup> lstLexical = new ArrayList<>();
        Markov mark = new Markov();
        System.out.println("Init of markov group");
        for(List<Relation> relations: result){
            // 1. Group word by tags
            LexicalGroup groups = parseGroups(relations);
            lstLexical.add(groups);
            // 2. Generate new sentence using Markov
            //System.out.println("Init of markov group");
            //Markov mark = new Markov();
            for(String[] words: groups.getSentences()) {
                //System.out.println("Adding sentence: " + String.join(" ", words));
                mark.addWords(words);
            }

           // System.out.println("========================================");

            // Sow Matrix
//            System.out.println("Matrix of word and tags");
//            groups.printLexicalGroups();
        }

        // 2. Generate new sentence using all the sentences

        System.out.println("Generation random sentences");

        for (int i=0;i<100;i++) {
            sentences.add(mark.generateSentence(5));
            //System.out.println("New Sentence : " + mark.generateSentence(5));
        }

        System.out.println("=================================");
        System.out.println("NEW RULESSSS");
        for(LexicalGroup group : lstLexical){
            group.inferRelations();
        }
        return sentences;
    }

    private static LexicalGroup parseGroups(List<Relation> relations){
        LexicalGroup groupedWord = new LexicalGroup(); // this is the result
        groupedWord.setLstRelations(relations);

        LexicalizedParser lp = LexicalizedParser.loadModel(
                "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                "-maxLength", "80", "-retainTmpSubcategories");

        //First Sentence (This sentence is the relation between X1 -> R1 - C1)
        String[] text = splitSentence(relations.get(0).getSurfaceText());// + " " + result.get(i).get(j).getSurfaceText()
        Tree parse = lp.apply(Sentence.toWordList(text));
        List<TaggedWord> nodes = parse.taggedYield();
        groupedWord.addSentence(text);
        //System.out.println(relations.get(0).toString());
        for(TaggedWord word : nodes){
            //Add tags to result
            groupedWord.add(word.tag(),word.word());
        }


        for(int i=1; i<relations.size();i++) {
            //Rest sentences (The rest are relation between C1 - R2 -> X2)
            String[] text2 = splitSentence(relations.get(i).getSurfaceText());// + " " + result.get(i).get(j).getSurfaceText()
            Tree parse2 = lp.apply(Sentence.toWordList(text2));
            List<TaggedWord> nodes2 = parse2.taggedYield();
            groupedWord.addSentence(text2);
            //System.out.println(relations.get(i).toString());
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
        // Add dot at the end of the sentence to make this the end of the iteration
        int lastIndex = values.size()-1;
        String newWord = values.get(values.size()-1).concat(".");
        values.remove(lastIndex);
        values.add(newWord);

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
