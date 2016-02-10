package com.jiffyjob.nimblylabs.xmlHelper;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 13/6/2015.
 * This reader only reads simple xml tree, that doesn't have multiple child in a single node
 */
public class SimpleXMLReader {
    public SimpleXMLReader(Context context, int rawXMLResource) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            myParser = xmlFactoryObject.newPullParser();

            InputStream inputStream = context.getResources().openRawResource(rawXMLResource);
            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(inputStream, null);
        } catch (XmlPullParserException e) {

        }
    }

    public List<String> parseXML() {
        if (myParser == null) return null;
        int event;
        String text = null;

        resultList = new ArrayList<>();
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name != null && !text.contains("\n")) {
                            resultList.add(text);
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Pass in attribute to retrieve data with required attribute.<br>
     * This method only handle a single attribute in xml element.
     *
     * @param attribute only handles single attribute, cannot be null or empty
     * @return Return list of strings, return null or empty list if error occurs
     */
    public List<String> parseXML(String attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        if (myParser == null) return null;
        int event;
        String text = null;
        String curAttribute = null;
        resultList = new ArrayList<>();
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                if (myParser.getAttributeCount() > 0) {
                    curAttribute = myParser.getAttributeValue(0);
                }
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name != null && !text.contains("\n") && curAttribute.equalsIgnoreCase(attribute)) {
                            resultList.add(text);
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private List<String> resultList = null;
    private XmlPullParser myParser = null;
}
