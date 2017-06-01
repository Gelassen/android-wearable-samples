package com.example.android.sunshine.library.model;


import android.database.Cursor;

import org.json.JSONObject;

import java.util.List;

public interface IConverter <I, O> {
    O convert(I input);
    List<O> convert(List<I> input);
    JSONObject toJson(O input);
    JSONObject toJsonAsList(List<O> input);
    O fromJson(JSONObject json);
    List<O> fromJsonAsList(String json);
    List<O> fromJsonAsList(JSONObject json);
    O fromCursor(Cursor cursor);
    List<O> fromCursorAsList(Cursor cursor);
}
