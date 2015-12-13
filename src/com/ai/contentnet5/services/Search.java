package com.ai.contentnet5.services;
import java.io.IOException;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

/**
 * Created by eduardosalazar1 on 12/13/15.
 */
public class Search {

    private static final String SEARCHENDPOINT = "http://conceptnet5.media.mit.edu/data/5.4/search?";
    private String start;
    private String end;
    private Content response;

    public Search(String startNode, String endNode) {
        this.setStart(startNode);
        this.setEnd(endNode);
    }

    public Search(){}


    public void setStart(String start) {
        this.start = "start="+conceptPath(start);
    }


    public void setEnd(String end) {
        this.end = "end="+conceptPath(end);
    }

    private String conceptPath(String node){
        return "/c/en/"+node;
    }

    public Content call() throws IOException {
        // The fluent API relieves the user from having to deal with manual deallocation of system
        // resources at the cost of having to buffer response content in memory in some case
        String uri = SEARCHENDPOINT+start+'&'+end;
        System.out.println("Trying to request "+ uri);
        response = Request.Get(uri).execute().returnContent();
        return response;
    }


}
