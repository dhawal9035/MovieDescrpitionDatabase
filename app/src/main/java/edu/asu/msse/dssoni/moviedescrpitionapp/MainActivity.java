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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String selectedStuff;
    public ExpandableListView elview;
    public ExpandableMovieListAdapter myListAdapter;
    public LinkedHashMap<String,List<String>> map;
    static final int DELETE_CONTACT_REQUEST = 1;
    static final int ADD_CONTACT_REQUEST = 2;
    Handler handler;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        map = new LinkedHashMap<String,List<String>>();
        elview = (ExpandableListView) findViewById(R.id.lvExp);
        myListAdapter = new ExpandableMovieListAdapter(this);
        elview.setAdapter(myListAdapter);
        myListAdapter.notifyDataSetChanged();

        try{
            MovieDB db = new MovieDB((Context)this);
            SQLiteDatabase movDB = db.openDB();
            Cursor cur = movDB.rawQuery("select title,genre from movie;", new String[]{});
            while(cur.moveToNext()){
                try{
                    String title = cur.getString(0);
                    String genre = cur.getString(1);
                    if(!map.containsKey(genre)){
                        List<String> res = new ArrayList<>();
                        res.add(title);
                        map.put(genre,res);
                    } else {
                        List<String> list = map.get(genre);
                        list.add(title);
                        map.put(genre,list);
                    }
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            myListAdapter.model = map;
            myListAdapter.notifyDataSetChanged();
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }

    }

    public void setSelectedStuff(String selectedStuff) {
        this.selectedStuff = selectedStuff;
        try{
            MovieDB db = new MovieDB((Context)this);
            SQLiteDatabase movDB = db.openDB();
            Cursor c = movDB.rawQuery("select * from movie where title='"+this.selectedStuff+"';", new String[]{});
            try{
                while(c.moveToNext()) {
                    String movTitle = c.getString(0);
                    String released = c.getString(1);
                    String rating = c.getString(2);
                    String genre = c.getString(3);
                    String actors = c.getString(4);
                    String plot = c.getString(5);
                    String year = c.getString(6);
                    String runtime = c.getString(7);
                    Intent intent = new Intent(this, MovieLayout.class);
                    intent.putExtra("title", movTitle);
                    intent.putExtra("released", released);
                    intent.putExtra("rating", rating);
                    intent.putExtra("genre", genre);
                    intent.putExtra("actors", actors);
                    intent.putExtra("plot", plot);
                    intent.putExtra("year", year);
                    intent.putExtra("runTime", runtime);
                    startActivityForResult(intent,1);
                }
            }catch(Exception ex){
                android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
            }
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        Intent intent = new Intent(this, AddActivity.class);
        //Intent searchIntent = new Intent(this,DialogActivity.class);
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(intent, 2);
                return true;
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DELETE_CONTACT_REQUEST){
            if (resultCode == DELETE_CONTACT_REQUEST){
                String removeTitle = data.getStringExtra("removeMovieJSON");
                myListAdapter.notifyDataSetChanged();
                try{
                    MovieDB db = new MovieDB((Context)this);
                    SQLiteDatabase movDB = db.openDB();
                    String delete = "delete from movie where title='"+removeTitle+"'";
                    movDB.execSQL(delete);
                    movDB.close();
                    db.close();
                    for(String genre:map.keySet()){
                        List<String> list = map.get(genre);
                        if(list.contains(removeTitle)){
                            list.remove(removeTitle);
                            if(list.isEmpty()){
                                map.remove(genre);
                            }
                        }
                    }
                    myListAdapter.notifyDataSetChanged();
                    this.recreate();
                    //this.loadFields();
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"Exception adding student information: "+
                            ex.getMessage());
                }
                this.recreate();
            }
        }

        if(requestCode == ADD_CONTACT_REQUEST){
            if (resultCode == ADD_CONTACT_REQUEST){
                String jsonString = (String) data.getSerializableExtra("newJsonString");
                MovieDescription md = new MovieDescription(jsonString);
                try{
                    MovieDB db = new MovieDB((Context)this);
                    SQLiteDatabase movDB = db.openDB();
                    String insert = "insert into movie values('"+md.title.replace("'","")+"','"+ md.released.replace("'","")+"','"+md.rated.replace("'","")+"','"+ md.genre.replace("'","")+"'," +
                            "'"+md.actors.replace("'","")+"','"+md.plot.replace("'","")+"','"+md.year.replace("'","")+"','"+md.runTime.replace("'","")+"');";
                    movDB.execSQL(insert);
                    movDB.close();
                    db.close();
                    if(!map.containsKey(md.genre)){
                        List<String> res = new ArrayList<>();
                        res.add(md.title);
                        map.put(md.genre,res);
                    } else {
                        List<String> list = map.get(md.genre);
                        list.add(md.title);
                        map.put(md.genre,list);
                    }
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"Exception adding movie information: "+
                            ex.getMessage());
                }
                MovieDB db = new MovieDB(this);
                System.out.println(db.checkDB());
                myListAdapter.notifyDataSetChanged();
                this.recreate();
            }
        }


    }

}


