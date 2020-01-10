package com.example.sbase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.ConnectionClosedException;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NetworkUtility {
    /**
     * A method to access an api endpoint
     * @param url the endpoint url
     * @param context the calling context
     * @param listener the callback to process results
     */
    public static void accessNetworkResource(String url, final Context context, final DataAccessCallbacks listener){
        url = url.replace(" ", "%20");
        Ion.with(context)
                .load(url)
                .setTimeout(15000)// 15 seconds
                .setLogging("Logging In", Log.ERROR)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {

                        try {
                            if (result != null) {
                                JsonObject json = result.getResult();
                                Log.e("LOGIN_RESULT_MESSAGE", result.getHeaders().message());
                                Log.e("LOGIN_RESULT_CODE", result.getHeaders().code() + "");

                                if(json!=null){
                                    Log.e("MESSAGE", json.toString());
                                listener.onDataRequestCompleted(result.getHeaders().code(), new String[] {json.toString()});}
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            boolean net = e instanceof IOException;
                            boolean closed = e instanceof ConnectionClosedException;
                            boolean timeOut = e instanceof TimeoutException;
                            boolean invalidJSON = e instanceof JSONException;
                            boolean nullPointer = e instanceof NullPointerException;
                            if (net || closed || timeOut || nullPointer||invalidJSON) {
                                // handle the stacktrace
                                Log.e("LOGIN_ERROR", "Error occurred in login");
                                Toast.makeText(context, "A network request failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
