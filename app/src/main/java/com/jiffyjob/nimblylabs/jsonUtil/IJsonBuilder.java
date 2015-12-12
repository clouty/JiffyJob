package com.jiffyjob.nimblylabs.jsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by NimblyLabs on 17/10/2015.
 */
public interface IJsonBuilder {
    JSONObject getJSON(Object object);

    JSONArray getJSONArray(List<Object> object);
}
