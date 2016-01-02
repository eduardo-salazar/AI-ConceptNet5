package com.ai.conceptnet5.services;
import java.io.IOException;
import java.util.List;

import com.ai.conceptnet5.Relation;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

/**
 * Created by eduardosalazar1 on 12/13/15.
 * This class has the call to the api using search function of concept net.
 * In must cases it will return an object of type Relation.
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

    public List<Relation> call() throws IOException {
        // The fluent API relieves the user from having to deal with manual deallocation of system
        // resources at the cost of having to buffer response content in memory in some case
        String uri = SEARCHENDPOINT+start+'&'+end;
        System.out.println("Trying to request "+ uri);
        String response = Request.Get(uri).execute().returnContent().toString();
        List<Relation> elements = Helper.parseResult(response);

        //Firs filter is to remove relationships with extrems null
        removeNulls(elements);

        //Second remove those relationships lower than 1
        removeWeightLowerThan(elements,new Double(1));

        return elements;
    }

    public void removeNulls(List<Relation> input){
        for(int i=0;i<input.size();i++){
            String startText = input.get(i).getSurfaceStart();
            String endText = input.get(i).getSurfaceEnd();
            if(startText.equals("") || endText.equals("")){
                input.remove(i);
                --i;
            }
        }

    }

    public void removeWeightLowerThan(List<Relation> input,Double value){
        for(int i=0;i<input.size();i++){
            if(input.get(i).getWeight()< value){
                input.remove(i);
                --i;
            }
        }

    }



}
