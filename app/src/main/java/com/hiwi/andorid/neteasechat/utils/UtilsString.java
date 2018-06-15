package com.hiwi.andorid.neteasechat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by x6 on 2017/1/18.
 */
public class UtilsString {


    public static String getStrFromAssets(Context context,String name) {
        String jsonString = "";
        String resultString = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getClass().getClassLoader().getResourceAsStream("assets/" + name)));

            while ((jsonString = bufferedReader.readLine()) != null) {
                resultString += jsonString;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

}
