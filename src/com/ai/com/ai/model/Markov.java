package com.ai.com.ai.model;

/**
 * Created by eduardosalazar1 on 1/6/16.
 */
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class Markov {

    // Hashmap
    public Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
    static Random rnd = new Random();

    /*
     * Main constructor
     */
    public Markov() {

        // Create the first two entries (k:_start, k:_end)
        markovChain.put("_start", new Vector<String>());
        markovChain.put("_end", new Vector<String>());

    }

    /*
     * Add words
     */
    public void addWords(String[] words) {

        // Loop through each word, check if it's already added
        // if its added, then get the suffix vector and add the word
        // if it hasn't been added then add the word to the list
        // if its the first or last word then select the _start / _end key

        for (int i=0; i<words.length; i++) {

            // Add the start and end words to their own
            if (i == 0) {
                Vector<String> startWords = markovChain.get("_start");
                startWords.add(words[i]);

                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i+1]);
                    markovChain.put(words[i], suffix);
                }

            } else if (i == words.length-1) {
                Vector<String> endWords = markovChain.get("_end");
                endWords.add(words[i]);

            } else {
                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i+1]);
                    markovChain.put(words[i], suffix);
                } else {
                    suffix.add(words[i+1]);
                    markovChain.put(words[i], suffix);
                }
            }
        }
    }


    /*
     * Generate a markov phrase
     */
    public Vector<String> generateSentence() {

        // Vector to hold the phrase
        Vector<String> newPhrase = new Vector<String>();

        // String for the next word
        String nextWord = "";

        // Select the first word
        Vector<String> startWords = markovChain.get("_start");
        int startWordsLen = startWords.size();
        nextWord = startWords.get(rnd.nextInt(startWordsLen));
        newPhrase.add(nextWord);

        // Keep looping through the words until we've reached the end
        while (nextWord.charAt(nextWord.length()-1) != '.') {
            Vector<String> wordSelection = markovChain.get(nextWord);
            int wordSelectionLen = wordSelection.size();
            nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
            newPhrase.add(nextWord);
        }

       return newPhrase;
    }

    /*
    * Generate a markov phrase
    */
    public String generateSentence(int min) {

        int lenght = 0;
        Vector<String> text = null;
        while(lenght<min){
            text = generateSentence();
            lenght = text.size();
        }

        Iterator<String> el = text.iterator();
        String result = "";
        while(el.hasNext()){
            result += " " +el.next();
        }
        return result;
    }

    public String generateSentenceThanContains(int min,String[] word) {

        int lenght = 0;
        boolean hasWords = false;
        Vector<String> text = null;
        String result="";
        while(lenght<min || !hasWords){
            text = generateSentence();
            lenght = text.size();
            int count = 0;

            Iterator<String> el = text.iterator();
            result = "";
            while(el.hasNext()){
                result += " " +el.next();
            }
            for(String w: word){
                if(result.contains(w)){
                    count++;
                }
            }
            if(count == word.length)
                hasWords = true;
            else
                hasWords = false;
        }


        return result;
    }
}