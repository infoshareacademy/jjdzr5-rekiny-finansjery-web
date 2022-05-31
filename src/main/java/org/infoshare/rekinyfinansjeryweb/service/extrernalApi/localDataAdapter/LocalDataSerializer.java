package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.localDataAdapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDataSerializer implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}