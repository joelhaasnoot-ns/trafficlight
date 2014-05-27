package nl.ns.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.io.IOException;
import java.util.TimerTask;

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
        JsonObject js = getData();
        if (js != null) {
            int delayMinutes = calculateDelay(js);
            System.out.println("Got " + delayMinutes + " minutes of delay " );
        }

    }

    private JsonObject getData() {
        JsonObject result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://localhost:8080/dvs/ut");

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    }
                    return null;
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            result = new JsonParser().parse(responseBody).getAsJsonObject();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private int calculateDelay(JsonObject js) {
        PeriodFormatter p = ISOPeriodFormat.standard();
        int delayMinutes = 0;
        for (JsonElement element : js.getAsJsonArray("vertrekTijden")) {
            JsonObject train = element.getAsJsonObject().get("trein").getAsJsonObject();
            Period delayPeriod = p.parsePeriod(train.get("exacteVertrekVertraging").getAsString());
            delayMinutes += (delayPeriod.getHours() * 60) + delayPeriod.getMinutes();
        }
        return delayMinutes;
    }
}
