package com.android.m3m9;

import java.util.Date;
public class ExampleElement {

    private int attributeInt;
    private Date attributeDate;
    private String attributeString;

    /**
     * Builders
     */

    /**
     * Basic Builder
     */
    public ExampleElement(){}

    public ExampleElement(int a1, Date a2, String a3){
        attributeInt = a1;
        attributeDate = a2;
        attributeString = a3;
    }

    //Getters
//    @NonNull
    public int getAttributeInt() { return attributeInt; }
    public Date getAttributeDate() { return attributeDate; }
    public String getAttributeString() { return attributeString; }

    //Setters
    public void setAttributeInt(int newAttribute) { attributeInt = newAttribute; }
    public void setAttributeDate(Date newAttribute) { attributeDate = newAttribute; }
    public void setAttributeString(String newAttribute) { attributeString = newAttribute; }
}
