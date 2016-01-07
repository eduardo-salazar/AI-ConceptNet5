package com.ai.com.ai.model;
import com.ai.conceptnet5.Relation;
import java.util.*;

/**
 * Created by eduardosalazar1 on 1/4/16.
 */
public class LexicalGroup {

    private HashMap<String,List<String>> map;
    private List<Relation> lstRelations;
    private List<String[]> lstSentences = new ArrayList<>();

    public LexicalGroup(){
        map = new HashMap<>();
    }
    private boolean containsWord(String groupName,String word){
        if(map.containsKey(groupName)) {
            for (String w : map.get(groupName)){
                if(w.equals(word))
                    return true;
            }
        }
        return false;

    }
    public void add(String groupName, String word){
        if(map.containsKey(groupName)){
            if(!containsWord(groupName,word)) {
                map.get(groupName).add(word);
            }
        }else{
            List<String> elements = new ArrayList<>();
            elements.add(word);
            map.put(groupName, elements);

        }
    }

    public List<String> getWords(String groupName){
        if(map.containsKey(groupName))
            return map.get(groupName);
        return emptyList();
    }

    public List<String> getWords(){
        List<String> words = new ArrayList<String>();
        Set<String> keys = map.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
            String key = it.next();
            words.addAll(map.get(key));
        }
        return words;
    }

    public void setLstRelations(List<Relation> relations){
        this.lstRelations = relations;
    }

    public void addSentence(String[] sentence){
        this.lstSentences.add(sentence);
    }

    public void inferRelations(){
        List<String[]> inferenceRules = searchInferRules(lstRelations.get(0).getRel());
        String mainRule = lstRelations.get(0).getRel();
        for(int i=0;i<lstRelations.size();i++){
            for(String[] rule:inferenceRules){
                if(mainRule.equals(rule[0]) && lstRelations.get(i).getRel().equals(rule[1])){
                    //infer rule
                    System.out.println("New Rule: " + lstRelations.get(0).getStart()
                            + lstRelations.get(i).getRel()
                    + lstRelations.get(i).getEnd());
                }else  if(mainRule.equals(rule[1]) && lstRelations.get(i).getRel().equals(rule[0])){
                    //infer rule
                    System.out.println("New Rule: " + lstRelations.get(0).getStart()
                            + lstRelations.get(i).getRel()
                            + lstRelations.get(i).getEnd());
                }
            }
        }


    }

    public  List<String[]> searchInferRules(String rel){
        List<String[]> result = new ArrayList<>();
        for(String[] rule : getRules()){
            if(rel.equals(rule[0]) || rel.equals(rule[1])){
                result.add(rule);
            }
        }

        return result;
    }

    public List<String[]> getRules(){
        List<String[]> rules = new ArrayList<>();
        String[] rule1 = {"/r/IsA","/r/CapableOf"};
        rules.add(rule1);
        return rules;
    }

    public List<String[]> getSentences(){
        return this.lstSentences;
    }
    private List<String> emptyList(){
        List<String> el = new ArrayList<>();
        return el;
    }

    public void printLexicalGroups(){
        Set<String> keys = map.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
            String key = it.next();
            //Print line
            System.out.println();
            System.out.format("%5s | %s", key, Arrays.toString(map.get(key).toArray()));

        }
        System.out.println();
    }
}
