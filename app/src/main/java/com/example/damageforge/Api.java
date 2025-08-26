package com.example.damageforge;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Api {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void getJSON(final String urlString, final ReadDataHandler handler) {
        executor.execute(() -> {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Optional: add headers (good practice)
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                int status = connection.getResponseCode();

                InputStream stream = (status >= 400) ? connection.getErrorStream() : connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                stream.close();
                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Switch back to UI thread
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> {
                String response = result.toString();

                if (response.isEmpty()) {
                    // Log error if response is empty
                    Log.e("ApiError", "Empty response from API");
                } else {
                    handler.setJson(response);
                    handler.sendEmptyMessage(0);
                }
            });
        });
    }
}
   /* public static void getJSON(String url, final ReadDataHandler rdh) {

        AsyncTask<String, Void, String>task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                try{
                    URL link = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) link.openConnection();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = br.readLine()) != null) {
                        response += line + "\n";
                    }

                    br.close();
                    connection.disconnect();

                } catch(Exception e) {
                    e.printStackTrace();
                }



                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                rdh.setJson(response);
                rdh.sendEmptyMessage(0);
            }
        };

        task.execute(url);

    }*/
   /*public static void getJSON(String url, final ReadDataHandler rdh) {

       AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
           @Override
           protected String doInBackground(String... strings) {
               StringBuilder response = new StringBuilder();
               try{
                   URL link = new URL(strings[0]);
                   HttpURLConnection connection = (HttpURLConnection) link.openConnection();

                   BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                   String line;
                   while((line = br.readLine()) != null) {
                       response.append(line).append("\n");
                   }

                   br.close();
                   connection.disconnect();

               } catch(Exception e) {
                   e.printStackTrace();
               }
               return response.toString();

           }

           @Override
           protected void onPostExecute(String response) {
               if(response == null || response.trim().isEmpty()){
                   Log.e("ApiError", "Empty response from API");
                   return;
               }

               rdh.setJson(response);
               rdh.sendEmptyMessage(0);
           }
       };

       task.execute(url);

   }*/
    //last try
    /*public static void getJSON(final String urlString, final ReadDataHandler handler) {
        new Thread (() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while((line =  reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                in.close();
                connection.disconnect();

                Handler mainHandler = new Handler (Looper.getMainLooper());
                mainHandler.post(()-> {
                    handler.setJson(result.toString());
                    handler.sendEmptyMessage(0);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }*/

    // Create a single-threaded executor

