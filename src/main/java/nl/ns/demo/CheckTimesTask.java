package nl.ns.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by joel on 27-5-14.
 */
public class CheckTimesTask extends TimerTask {

    private String station;

    public CheckTimesTask(String station) {
        this.station = station;
    }

    @Override
    public void run() {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

        // Start the client
        httpclient.start();

        // Execute request
        final HttpGet request1 = new HttpGet("http://localhost:8080/dvs/ut");
        request1.setHeader("Accept", "application/json");
        Future<HttpResponse> future = httpclient.execute(request1, null);
        // and wait until a response is received
        HttpResponse response1 = null;
        try {
            response1 = future.get();
            String responseText = IOUtils.toString(response1.getEntity().getContent());
            JsonObject js = new JsonParser().parse(responseText).getAsJsonObject();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
