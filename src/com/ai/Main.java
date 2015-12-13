package com.ai;
import com.ai.contentnet5.services.Search;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code her
        System.out.println("Testing first concept net call");
        try {
            System.out.println(new Search("teacher","").call().toString());
        } catch (IOException e) {
            System.out.println("Error getting response:");
            e.printStackTrace();
        }
    }
}
