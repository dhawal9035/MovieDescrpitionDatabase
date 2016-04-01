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
 *  Created by Dhawal Soni on 2/11/2016.
 *
 *  @author Dhawal Soni mailto:dhawal.soni@asu.edu
 *  @version February 11, 2016
 */

package edu.asu.msse.dssoni.moviedescrpitionapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AddActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    String title = "";
    String year = "";
    String rated = "";
    String released = "";
    String runtime = "";
    String genre = "";
    String actors = "";
    String plot = "";
    ArrayAdapter<CharSequence> arrayAdapter;
    private SearchView searchView;
    private Menu menu;
    Handler handler;
    String url = "";
    EditText titleText;
    EditText yearText;
    EditText ratingText;
    EditText releasedText;
    EditText actorsText;
    EditText runText;
    EditText plotText;
    Spinner spinner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        spinner = (Spinner) findViewById(R.id.spinner);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.genre, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        titleText = (EditText) findViewById(R.id.editText);
        yearText = (EditText) findViewById(R.id.editText2);
        ratingText = (EditText) findViewById(R.id.editText3);
        releasedText = (EditText) findViewById(R.id.editText4);
        actorsText = (EditText) findViewById(R.id.editText5);
        runText = (EditText) findViewById(R.id.editText7);
        plotText = (EditText) findViewById(R.id.editText8);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        Intent i = new Intent(this, AddActivity.class);
        switch (item.getItemId()) {
            case R.id.action_add:
                i.putExtra("message", "This is a add dialog");
                startActivityForResult(i, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addNewItem(View v) throws JSONException {

        title = titleText.getText().toString();
        year = yearText.getText().toString();
        rated = ratingText.getText().toString();
        released = releasedText.getText().toString();
        actors = actorsText.getText().toString();
        runtime = runText.getText().toString();
        plot = plotText.getText().toString();
        genre = spinner.getSelectedItem().toString();

        JSONObject jo = new JSONObject();
        jo.put("Title", title);
        jo.put("Year", year);
        jo.put("Rated", rated);
        jo.put("Released", released);
        jo.put("Runtime", runtime);
        jo.put("Genre", genre);
        jo.put("Actors", actors);
        jo.put("Plot", plot);

        String jsonString = jo.toString();
        Intent intent = new Intent();
        intent.putExtra("newJsonString", jsonString);
        setResult(2, intent);
        finish();
    }

    public boolean onQueryTextSubmit(String query) {
        android.util.Log.d(this.getClass().getSimpleName(), "in onQueryTextSubmit: " + query);
        this.title = query;
        //MenuItemCompat.collapseActionView((MenuItem)menu.findItem(R.id.action_search));
        searchView.clearFocus();
        String[] all = title.split(" ");
        String s = "";
        if (all.length > 1) {
            for (int i = 0; i < all.length - 1; i++) {
                s += all[i] + "+";
            }
            s += all[all.length - 1];
        } else {
            s += title;
        }

        android.util.Log.d(this.getClass().getSimpleName(), "Search string is " + s);
        try {
            url = "http://www.omdbapi.com/?t=" + s + "&y=&plot=short&r=json";
            handler = new Handler();
            JsonRPCClientViaThread names = new JsonRPCClientViaThread(new URL(url),
                    handler, this, "GET", "[]");
            names.start();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception constructing URL" +
                    " " + url + " message " + ex.getMessage());
        }
        return false;
    }

    public boolean onQueryTextChange(String query) {
        //android.util.Log.d(this.getClass().getSimpleName(), "in onQueryTextChange: " + query);
        return false;
    }
}