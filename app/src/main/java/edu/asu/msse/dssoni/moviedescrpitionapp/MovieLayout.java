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
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MovieLayout extends AppCompatActivity {

    MovieDescription md;
    private Intent intent;
    TextView yearText;
    TextView releasedText;
    TextView runText;
    TextView actorText;
    TextView genreText;
    TextView ratingText;
    TextView plotText;
    String movieTitle="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_layout);
        TextView title = (TextView) findViewById(R.id.textView);
        yearText = (TextView) findViewById(R.id.textView10);
        releasedText = (TextView) findViewById(R.id.textView11);
        runText = (TextView) findViewById(R.id.textView12);
        actorText = (TextView) findViewById(R.id.textView15);
        genreText = (TextView) findViewById(R.id.textView14);
        ratingText = (TextView) findViewById(R.id.textView13);
        plotText = (TextView) findViewById(R.id.textView16);
        yearText.setKeyListener(null);
        releasedText.setKeyListener(null);
        runText.setKeyListener(null);
        actorText.setKeyListener(null);
        genreText.setKeyListener(null);
        ratingText.setKeyListener(null);
        plotText.setKeyListener(null);
        Intent newIntent = getIntent();

        md = (MovieDescription)(newIntent.getSerializableExtra("SelectedMovie"));
        if(md == null) {
            movieTitle = newIntent.getStringExtra("title");
            title.setText(newIntent.getStringExtra("title"));
            yearText.setText(newIntent.getStringExtra("year"));
            releasedText.setText(newIntent.getStringExtra("released"));
            runText.setText(newIntent.getStringExtra("runTime"));
            actorText.setText(newIntent.getStringExtra("actors"));
            genreText.setText(newIntent.getStringExtra("genre"));
            ratingText.setText(newIntent.getStringExtra("rating"));
            plotText.setText(newIntent.getStringExtra("plot"));
        } else {
            movieTitle = md.title;
            title.setText(md.title);
            yearText.setText(md.year);
            releasedText.setText(md.released);
            runText.setText(md.runTime);
            actorText.setText(md.actors);
            genreText.setText(md.genre);
            ratingText.setText(md.rated);
            plotText.setText(md.plot);
        }
    }

    public void removeMovie(View view) {
        intent = new Intent(this,MainActivity.class);
        String removeTitle =  movieTitle;
        intent.putExtra("removeMovieJSON", removeTitle);
        setResult(1, intent);
        finish();
    }

}
