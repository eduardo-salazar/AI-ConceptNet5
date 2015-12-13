package com.ai.conceptnet5.services;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by eduardosalazar1 on 12/13/15.
 */
public class SearchTest {

    Search seachObj = new Search();

    @Test
    public void testConstructorEmpty() throws Exception{
        Search testObject = new Search();
    }

    @Test
    public void testConstructorWithValues() throws Exception{
        Search testObject = new Search("teacher","");
    }

    @Test
    public void testSetStart() throws Exception {
        seachObj.setStart("teacher");
    }

    @Test
    public void testSetEnd() throws Exception {
        seachObj.setEnd("");
    }

    @Test
    public void testAssertThatCallConstainsString() throws Exception {
        String result = seachObj.call().toString();
        assertThat(result,containsString("edges"));
    }
}