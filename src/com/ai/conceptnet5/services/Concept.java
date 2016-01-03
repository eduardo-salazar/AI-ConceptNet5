package com.ai.conceptnet5.services;
import java.io.IOException;
import java.util.List;

import com.ai.conceptnet5.Relation;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
/**
 * Created by eduardosalazar1 on 1/3/16.
 */
public class Concept {

    private static final String SEARCHENDPOINT = "http://conceptnet5.media.mit.edu/data/5.4";
    private String concept;
    private Content response;

    public Concept(String concept) {
        this.setConcept(concept);
    }


    public void setConcept(String concept) {
        this.concept = conceptPath(concept);
    }



    private String conceptPath(String node){
        return "/c/en/"+node;
    }

    public List<Relation> call() throws IOException {
        // The fluent API relieves the user from having to deal with manual deallocation of system
        // resources at the cost of having to buffer response content in memory in some case
        String uri = SEARCHENDPOINT+concept;
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
