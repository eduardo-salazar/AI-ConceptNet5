package com.ai;
import com.ai.conceptnet5.Relation;
import com.ai.conceptnet5.services.Search;
import java.util.List;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code her
        System.out.println("Testing first concept net call");
        try {

            List<Relation> list = new Search("teacher","").call();
            for(int i=0;i<list.size();i++){
                System.out.println(list.get(i));
            }
        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }
}
