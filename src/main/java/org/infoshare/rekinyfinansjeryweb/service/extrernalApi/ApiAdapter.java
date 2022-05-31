package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class ApiAdapter {

    protected String getDataFromApi(String url) {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        String body = null;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                body = response.body();
            }
        } catch(Exception e) {
            //TODO add logger
            //LOGGER.info("Problem with download data from API.");
        }
        return body;
    }
}
