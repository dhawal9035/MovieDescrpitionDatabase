/**
 *  *  Copyright 2016 Dhawal Soni
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
 * Created by Dhawal Soni on 3/28/2016.
 *
 *  @author Dhawal Soni mailto:dhawal.soni@asu.edu
 *  @version March 28, 2016
 */
package edu.asu.msse.dssoni.moviedescrpitionapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class MovieDB extends SQLiteOpenHelper {
    private static final boolean debugon = false;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "moviedb";
    private String dbPath;
    private SQLiteDatabase movDB;
    private final Context context;

    public MovieDB(Context context){
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

    public void MovieDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    /**
     * does the database exist and has it been initialized? This method determines whether
     * the database needs to be copied to the data/data/pkgName/databases directory by
     * checking whether the file exists. If it does it checks to see whether the db is
     * uninitialized or whether it has the course table.
     * @return false if the database file needs to be copied from the assets directory, true
     * otherwise.
     */
    public boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + dbName + ".db";
            debug("MovieDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("MovieDB --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='movie';", null);
                    boolean movTabExists = false;
                    if(tabChk == null){
                        debug("MovieDB --> checkDB","check for movie table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("MovieDB --> checkDB","check for movie table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        movTabExists = !tabChk.isAfterLast();
                    }
                    if(movTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM movie", null);
                        c.moveToFirst();
                        while(! c.isAfterLast()) {
                            String movTitle = c.getString(0);
                            String released = c.getString(1);
                            String rating = c.getString(2);
                            String genre = c.getString(3);
                            String actors = c.getString(4);
                            String plot = c.getString(5);
                            String year = c.getString(6);
                            String runtime = c.getString(7);
                            debug("MovieDB --> checkDB","Movie table has Movie : "+
                                    movTitle+"\tCourseID: "+released);
                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("MovieDB->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("MovieDB --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.moviedb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("MovieDB --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            movDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("MovieDb --> openDB", "opened db at path: " + movDB.getPath());
        }else{
            try {
                this.copyDB();
                movDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return movDB;
    }

    @Override
    public synchronized void close() {
        if(movDB != null)
            movDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }
}
