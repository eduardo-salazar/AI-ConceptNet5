package com.ai.com.ai.model;

import java.util.*;

/**
 * Created by eduardosalazar1 on 1/4/16.
 */
public class LexicalGroup {

    private HashMap<String,List<String>> map;

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
            System.out.println();
        }
    }
}
