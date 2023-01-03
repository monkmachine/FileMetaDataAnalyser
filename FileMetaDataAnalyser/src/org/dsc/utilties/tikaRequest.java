package org.dsc.utilties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class tikaRequest {

    private HttpClient client = HttpClient.newHttpClient();
    private String UploadFile;
    private String serviceUrl;
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setUploadFile(String uploadFile) {
        UploadFile = uploadFile;
    }




    public InputStream tikaRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .header("Accept","application/json")
                .PUT(HttpRequest.BodyPublishers.ofFile(Paths.get(UploadFile)))
                .build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return response.body();
    }
}
