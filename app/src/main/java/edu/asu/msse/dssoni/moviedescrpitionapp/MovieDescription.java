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

import org.json.JSONObject;

import java.io.Serializable;

public class MovieDescription implements Serializable {
    public String title;
    public String year;
    public String rated;
    public String released;
    public String runTime;
    public String genre;
    public String actors;
    public String plot;

    MovieDescription(String jsonString) {
        try {
            JSONObject jo = new JSONObject(jsonString);
            title = jo.getString("Title");
            year = jo.getString("Year");
            rated = jo.getString("Rated");
            released = jo.getString("Released");
            runTime = jo.getString("Runtime");
            genre = jo.getString("Genre");
            actors = jo.getString("Actors");
            plot = jo.getString("Plot");

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Error Converting To/From JSON");
        }
    }
    public String toJsonString(String jsonStr){
        String ret="";
        try{
            JSONObject jo = new JSONObject();
            jo.put("Title",title);
            jo.put("Year",year);
            jo.put("Rated",rated);
            jo.put("Released",released);
            jo.put("Runtime",runTime);
            jo.put("Genre",genre);
            jo.put("Actors",actors);
            jo.put("Plot",plot);
            ret = jo.toString();
        }
        catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Error Converting To/From JSON");
        }
        return ret;
    }

    public MovieDescription(JSONObject jsonObj) {
        try {
            title = jsonObj.getString("Title");
            year = jsonObj.getString("Year");
            rated = jsonObj.getString("Rated");
            released = jsonObj.getString("Released");
            runTime = jsonObj.getString("Runtime");
            genre = jsonObj.getString("Genre");
            actors = jsonObj.getString("Actors");
            plot = jsonObj.getString("Plot");
        } catch (Exception ex) {
            System.out.println(this.getClass().getSimpleName() + ": error converting from json string");
        }
    }

    public String getTitle(){
        return this.title;
    }
    public String getYear(){
        return this.year;
    }
    public String getRated(){
        return this.rated;
    }
    public String getReleased(){
        return this.released;
    }
    public String getRunTime(){
        return this.runTime;
    }
    public String getGenre(){
        return this.genre;
    }
    public String getActors(){
        return this.actors;
    }
    public String getPlot(){
        return this.plot;
    }
}
