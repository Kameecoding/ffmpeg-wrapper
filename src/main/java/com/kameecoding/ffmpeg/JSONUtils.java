package com.kameecoding.ffmpeg;

import org.json.JSONObject;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 29/09/2017.
 */
public class JSONUtils {

	public static boolean hasObject(JSONObject object, String objectName) {
		return object.keySet().contains(objectName);
	}
}
