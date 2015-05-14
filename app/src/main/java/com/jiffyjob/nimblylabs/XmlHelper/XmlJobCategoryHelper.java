package com.jiffyjob.nimblylabs.XmlHelper;

import android.content.Context;

import com.jiffyjob.nimblylabs.app.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 8/4/2015.
 */
public class XmlJobCategoryHelper {
    public XmlJobCategoryHelper(Context context) {
        try {
            this.context = context;
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            myParser = xmlFactoryObject.newPullParser();

            InputStream inputStream = context.getResources().openRawResource(R.raw.jobcategory);
            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(inputStream, null);
        } catch (XmlPullParserException e) {

        }
    }

    public List<String> parseXML() {
        if (myParser == null) return null;
        int event;
        String text = null;

        jobTypeList = new ArrayList<String>();
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
                        if (name.equals("job")) {
                            jobTypeList.add(text);
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobTypeList;
    }

    private List<String> jobTypeList = null;
    private Context context;
    private XmlPullParser myParser = null;
}
