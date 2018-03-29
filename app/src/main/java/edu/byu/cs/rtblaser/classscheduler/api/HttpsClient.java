package edu.byu.cs.rtblaser.classscheduler.api;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by RyanBlaser on 10/19/17.
 */

public class HttpsClient {
    public String getUrl(URL url, String request, String bearer) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Authorization", "Bearer " + bearer);
//            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                String responseBodyData = baos.toString();
                connection.disconnect();
                return responseBodyData;
            } else {
                int responseCode = connection.getResponseCode();
                System.out.print(responseCode);
            }
        }
        catch (Exception e) {
            Log.e("HttpsClient", e.getMessage(), e);
        }

        return null;
    }
}