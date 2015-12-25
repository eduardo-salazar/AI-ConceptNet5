package com.ai.conceptnet5;


/**
 * Created by eduardosalazar1 on 1/3/16.
 * This is the basic class to encapsulate the object
 * of a relationship between two concept.
 * It is recommended to use a list of this object to ensure
 * that everything is correct.
 */
public class Relation {
    private String rel;
    private String start;
    private String end;
    private String surfaceStart;
    private String surfaceEnd;
    private String surfaceText;
    private Double weight;

    public Relation(){}


    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSurfaceStart() {
        return surfaceStart;
    }

    public void setSurfaceStart(String surfaceStart) {
        this.surfaceStart = surfaceStart;
    }

    public String getSurfaceEnd() {
        return surfaceEnd;
    }

    public void setSurfaceEnd(String surfaceEnd) {
        this.surfaceEnd = surfaceEnd;
    }

    public String getSurfaceText() {
        return surfaceText;
    }

    public void setSurfaceText(String surfaceText) {
        this.surfaceText = surfaceText;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
//        return "Relation{" +
//                "rel='" + rel + '\'' +
//                ", start='" + start + '\'' +
//                ", end='" + end + '\'' +
//                ", surfaceStart='" + surfaceStart + '\'' +
//                ", surfaceEnd='" + surfaceEnd + '\'' +
//                ", surfaceText='" + surfaceText + '\'' +
//                ", weight=" + weight +
//                '}';

        return surfaceStart + " -> " + rel + " -> " + surfaceEnd;
    }
}
