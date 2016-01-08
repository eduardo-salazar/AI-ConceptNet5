package com.ai;
import com.ai.com.ai.model.Markov;
import com.ai.conceptnet5.Relation;
import com.ai.conceptnet5.services.Search;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.ai.com.ai.model.LexicalGroup;


import com.ai.sentenceranking.Hits;
import com.ai.sentenceranking.Ranking;
import com.ai.sentenceranking.SentenceRanking;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class Main {



    public static void main(String[] args) {
	// write your code her
        List<String> sentences = new ArrayList<>();
        System.out.println("Testing first concept net call");
        String input1 = "teach";
        String input2 = "";
        String[] words = {input1,input2};
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


            sentences.addAll(getIndirectSentences(result,words));

//            for(String sentence: sentences){
//                System.out.println("New Sentence : " + sentence);
//            }

            String join = "";
            for(String sent : sentences){
                join += sent;
            }

            //Rank sentences
            Hits rank;
            rank = new Hits();
            List<Ranking> rankHits = rank.rank(join);

            System.out.println("===========Top 1 sentences==========");
            if(resultDirect.size()>0){
                //Use direct realtions
                System.out.println("1 | "+bestSentence(resultDirect,5));
            }else{
                System.out.println(rankHits.get(0).getWeight()+" | "+rankHits.get(0).getSentece());
            }



//            SentenceRanking rk = new SentenceRanking();
//            rankHits = rk.rank(join);
//            System.out.println("===========Top 1 sentences==========");
//            System.out.println(rankHits.get(0).getWeight()+"|"+rankHits.get(0).getSentece());
//



        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }

    public static String bestSentence(List<Relation> realtions,int min){

        String sentence ="";
        int longest = 0;
        for(Relation rel: realtions) {
            String[] text = splitSentence(rel.getSurfaceText());
            if(text.length >= min){
                //lsttext.add(text);
                if(text.length>longest){
                    //change longest
                    longest = text.length;
                    sentence = getSentence(text);
                }
            }
        }
        return sentence;
    }

    public static String getSentence(String[] text){
        String result = "";
        for(String w: text){
            result += " " + w;
        }
        return result;
    }



    public static void printRelations(List<Relation> relations){
        // Print Direct and Indirect Relations
        for(int i=0;i<relations.size();i++){
            System.out.println(relations.get(i));
        }

        System.out.println();

    }

    public static List<String> getIndirectSentences(List<List<Relation>> result,String[] input){
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

        for (int i=0;i<300;i++) {
            sentences.add(mark.generateSentenceThanContains(5,input));
            //System.out.println("New Sentence : " + mark.generateSentence(5));
        }

        //System.out.println("=================================");
        //System.out.println("NEW RULESSSS");
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
