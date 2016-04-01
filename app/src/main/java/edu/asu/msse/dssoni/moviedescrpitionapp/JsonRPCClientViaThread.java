/**
 *  Copyright 2016 Dhawal Soni
 *
 *  I give the Instructor and Arizona State University right to use
 *  this application source code to build and evaluate the software package.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Created by Dhawal Soni on 3/13/2016.
 *
 *  @author Dhawal Soni mailto:dhawal.soni@asu.edu
 *  @version March 13, 2016
 */

package edu.asu.msse.dssoni.moviedescrpitionapp;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class JsonRPCClientViaThread extends Thread {

    private final Map<String, String> headers;
    private URL url;
    private String method;
    private String requestData;
    private Handler handler;
    private AddActivity parent;

    public JsonRPCClientViaThread(URL url, Handler handler, AddActivity parent, String method, String paramsArray) {
        this.url = url;
        this.method = method;
        this.handler = handler;
        this.parent=parent;
        this.headers = new HashMap<String, String>();
        requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+method+"\", \"params\":"+paramsArray+
                ",\"id\":3}";
    }

    public void run(){
        try {
            String respData = this.post(url, headers, requestData);
            Log.d(this.getClass().getSimpleName(),"Result of JsonRPC request: "+respData);
            if(method.equals("GET")){
                JSONObject jo = new JSONObject(respData);
                MovieDescription md = new MovieDescription(jo);
                handler.post(new DisplayDetails(parent,md));
            }
        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(),"Exception in JsonRPC request: "+ex.getMessage());
        }
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String call(String requestData) throws Exception {
        String respData = post(url, headers, requestData);
        return respData;
    }

    private String post(URL url, Map<String, String> headers, String data) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(
                        "Unexpected status from post: " + statusCode);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        Log.d(this.getClass().getSimpleName(),"json rpc request via http returned string "+bos.toString());
        return bos.toString();
    }
}

class DisplayDetails extends Thread {

    MovieDescription movieDescription;
    AddActivity parent;

    DisplayDetails(AddActivity parent, MovieDescription movieDescription) {
        this.parent = parent;
        this.movieDescription = movieDescription;
    }

    public void run() {
        this.parent.titleText.setText(this.movieDescription.title);
        this.parent.yearText.setText(this.movieDescription.year);
        this.parent.ratingText.setText(this.movieDescription.rated);
        this.parent.releasedText.setText(this.movieDescription.released);
        this.parent.plotText.setText(this.movieDescription.plot);
        this.parent.actorsText.setText(this.movieDescription.actors);
        this.parent.runText.setText(this.movieDescription.runTime);
        String[] all = this.movieDescription.genre.split(",");
        this.parent.spinner.setSelection(this.parent.arrayAdapter.getPosition(""+all[0].trim()+""));
        //Intent intent = new Intent(parent, AddActivity.class);
        //intent.putExtra("SelectedMovie", movieDescription);
        //parent.setResult(4, intent);
        //parent.finish();
    }
}
