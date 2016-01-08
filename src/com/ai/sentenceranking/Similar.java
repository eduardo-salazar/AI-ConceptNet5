package com.ai.sentenceranking;

/**
 * Created by eduardosalazar1 on 1/8/16.
 */

/**
 *
 * @author Kalyan
 */
public interface Similar<T> {
    public double similarity(T other);
}