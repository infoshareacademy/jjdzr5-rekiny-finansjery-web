package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.localDataAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDataDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(json.toString().replace("\"", ""));
        }
        catch (DateTimeParseException e){
            localDate = LocalDate.of(0,0,0);
        }
        return localDate;
    }
}