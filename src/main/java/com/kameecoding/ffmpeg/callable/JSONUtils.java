package com.kameecoding.ffmpeg.callable;

import org.json.JSONObject;

import java.util.List;

public class JSONUtils {

    public static boolean hasObject(JSONObject object, String objectName) {
        return object.keySet().contains(objectName);
    }
}
