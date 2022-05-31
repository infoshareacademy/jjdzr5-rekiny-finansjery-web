package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infoshareacademy.api.localDataAdapter.LocalDataDeserializer;
import com.infoshareacademy.api.localDataAdapter.LocalDataSerializer;

import java.time.LocalDate;

public class ExtendedGson {
    public static Gson getExtendedGson(){
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDataSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDataDeserializer())
                .create();

    }
}
