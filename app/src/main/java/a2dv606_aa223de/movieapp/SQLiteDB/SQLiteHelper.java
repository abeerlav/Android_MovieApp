package a2dv606_aa223de.movieapp.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Abeer on 8/11/2017.
 * SQLiteHelper contains
 * table name , fields and create statement that helps to create implement sqlite db
 */



public class SQLiteHelper extends SQLiteOpenHelper {



    public static final String MOVIE_TABLE_NAME = "movie_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING="imdb_rating";
    public static final String COLUMN_YEAR="year";
    public static final String COLUMN_RELEASE_DATE="release_date";
    public static final String COLUMN_IMAGE="image";
    public static final String DATABASE_NAME = "moives00.db";
    public static final int DATABASE_VERSION = 1;


    /*
     create movie table
     */
    private static final String CREATE_MOVIE_TABLE = "create table " +MOVIE_TABLE_NAME
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            +COLUMN_TITLE+ " string, "
            + COLUMN_IMAGE + " string, "
            + COLUMN_RATING + " string, "
            + COLUMN_YEAR + " string, "
            + COLUMN_RELEASE_DATE + " string " +
            ");";





    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);

    }
       /*
       update sqlite version
        */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w( SQLiteHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        onCreate(db);
    }

}