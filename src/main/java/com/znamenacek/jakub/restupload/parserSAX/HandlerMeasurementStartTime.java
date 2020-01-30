package com.znamenacek.jakub.restupload.parserSAX;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Getter
public class HandlerMeasurementStartTime extends DefaultHandler {
    // List to hold Employees object
    private Long measurementStartTime = null;

    private boolean bMeasurementStartTime = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("MeasurementStartTime")){
            bMeasurementStartTime = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("MeasurementStartTime")){
            bMeasurementStartTime = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if(bMeasurementStartTime){
            measurementStartTime = Long.parseLong(new String(ch, start, length)+"000");
        }
    }
}