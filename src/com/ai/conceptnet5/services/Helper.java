package com.ai.conceptnet5.services;

import com.ai.conceptnet5.Relation;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by eduardosalazar1 on 1/3/16.
 */
public class Helper {
    public static List<Relation> parseResult(String response){
        JSONObject json = new JSONObject(response);
        JSONArray arr = json.getJSONArray("edges");
        List<Relation> result = new ArrayList<Relation>();
        for (int i = 0; i < arr.length(); i++)
        {
            Relation newRelation = new Relation();
            newRelation.setStart(!arr.getJSONObject(i).isNull("start")?arr.getJSONObject(i).getString("start"):"");
            newRelation.setEnd(!arr.getJSONObject(i).isNull("end")?arr.getJSONObject(i).getString("end"):"");
            newRelation.setRel(!arr.getJSONObject(i).isNull("rel")?arr.getJSONObject(i).getString("rel"):"");
            newRelation.setSurfaceStart(!arr.getJSONObject(i).isNull("surfaceStart")?arr.getJSONObject(i).getString("surfaceStart"):"");
            newRelation.setSurfaceEnd(!arr.getJSONObject(i).isNull("surfaceEnd")?arr.getJSONObject(i).getString("surfaceEnd"):"");
            newRelation.setSurfaceText(!arr.getJSONObject(i).isNull("surfaceText")?arr.getJSONObject(i).getString("surfaceText"):"");
            newRelation.setWeight(!arr.getJSONObject(i).isNull("weight")?arr.getJSONObject(i).getDouble("weight"):new Double(0));
            result.add(newRelation);
        }
        return result;
    }
}
