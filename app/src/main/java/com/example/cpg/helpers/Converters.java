package com.example.cpg.helpers;

import androidx.room.TypeConverter;

import com.example.cpg.model.Chord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/*
Type Converter class used by Room to convert ArrayList<Chord> back and forth for storage in SQLite DB.
 */
public class Converters {

    @TypeConverter
    public static ArrayList<Chord> toChordsFromString(String value) {

        Type listType = new TypeToken<ArrayList<Chord>>(){}.getType();

        return new Gson().fromJson(value, listType);

    }

    @TypeConverter
    public static String toStringFromArrayList(ArrayList<Chord> list) {

        Gson gsonObject = new Gson();

        String jsonObject = gsonObject.toJson(list);

        return jsonObject;

    }

}