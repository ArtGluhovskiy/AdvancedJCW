package org.art.web;

import com.google.gson.Gson;

public class Test {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String result = gson.toJson("FAIL");
        System.out.println(result);
    }
}
