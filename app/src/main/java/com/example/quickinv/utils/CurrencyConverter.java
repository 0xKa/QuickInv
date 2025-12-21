package com.example.quickinv.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {

    private static final String API_KEY = "cebd5e755274a937d7d5926c"; // Free tier API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    private Context context;
    private OnConversionListener listener;

    public interface OnConversionListener {
        void onSuccess(String result);
        void onError(String error);
    }

    public CurrencyConverter(Context context, OnConversionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void convertCurrency(String baseCurrency, String targetCurrency, double amount) {
        // Run API call in background thread
        new Thread(() -> {
            try {
                String urlString = API_URL + API_KEY + "/pair/" + baseCurrency.toUpperCase() +
                        "/" + targetCurrency.toUpperCase() + "/" + amount;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if ("success".equals(jsonObject.getString("result"))) {
                        double conversionRate = jsonObject.getDouble("conversion_rate");
                        double convertedAmount = jsonObject.getDouble("conversion_result");

                        String resultText = String.format(
                                "%.2f %s = %.2f %s\n(Rate: %.4f)",
                                amount, baseCurrency.toUpperCase(),
                                convertedAmount, targetCurrency.toUpperCase(),
                                conversionRate
                        );

                        if (listener != null) {
                            listener.onSuccess(resultText);
                        }
                    } else {
                        String errorType = jsonObject.optString("error-type", "Unknown error");
                        if (listener != null) {
                            listener.onError("API Error: " + errorType);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Connection error: " + responseCode);
                    }
                }

                connection.disconnect();

            } catch (Exception e) {
                if (listener != null) {
                    listener.onError("Error: " + e.getMessage());
                }
            }
        }).start();
    }
}