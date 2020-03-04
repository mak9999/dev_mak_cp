package com.mak.classportal.utilities;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import static com.mak.classportal.utilities.Constant.mediaTypes;

public class MethodPlugins {

    private static String[] split;

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            Log.d("JWT_DECODED", "Signiture: " + getJson(split[2]));
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static void initMediaType(){
        mediaTypes.put("image", "1");
        mediaTypes.put("pdf", "2");
        mediaTypes.put("doc", "3");
    }

}
