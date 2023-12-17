package edu.example.food.utilities;

import com.google.gson.Gson;

public class JsonUtil {

    public static String getToJson(Object object) {
        Gson json = new Gson();
        return json.toJson(object);
    }
    public static <T> T getFromJson(String object, Class<T> typeData) {
        Gson json = new Gson();
        return json.fromJson(object, typeData);
    }
}
