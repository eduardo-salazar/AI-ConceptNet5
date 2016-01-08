package com.ai.sentenceranking;

/**
 * Created by eduardosalazar1 on 1/8/16.
 */
public class Ranking {
    private String sentece;
    private Double weight;

    public Ranking(String sentece, Double weight) {
        this.sentece = sentece;
        this.weight = weight;
    }

    public String getSentece() {
        return sentece;
    }

    public void setSentece(String sentece) {
        this.sentece = sentece;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
