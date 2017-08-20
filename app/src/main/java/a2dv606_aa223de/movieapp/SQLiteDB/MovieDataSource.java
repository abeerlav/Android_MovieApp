package a2dv606_aa223de.movieapp.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import a2dv606_aa223de.movieapp.Model.MovieModel;

/**
 * Created by Abeer Alkhars on 8/11/2017.
 * MovieDataSource perform create &read & delete operation
 */

public class MovieDataSource {

    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;

    private String[] allMovieColumns = {dbHelper.COLUMN_ID ,
            dbHelper.COLUMN_TITLE,dbHelper.COLUMN_IMAGE,
        dbHelper.COLUMN_RATING,dbHelper.COLUMN_YEAR, dbHelper.COLUMN_RELEASE_DATE
           };


    public MovieDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

/*
   create movie record
 */
public void createMovie(MovieModel movieModel) {

    ContentValues values = new ContentValues();
    values.put(dbHelper.COLUMN_TITLE, movieModel.getMovieTitle());
    values.put(dbHelper.COLUMN_IMAGE, movieModel.getImage());
    values.put(dbHelper.COLUMN_RATING, movieModel.getImdbRating());
    values.put(dbHelper.COLUMN_YEAR, movieModel.getYear());
    values.put(dbHelper.COLUMN_RELEASE_DATE, movieModel.getReleasedDate());

    long insertId = database.insert(dbHelper.MOVIE_TABLE_NAME, null, values);
    System.out.println("id "+insertId+" inserted!");
}
    /*
      read all the records in the movie table
     */
    public ArrayList<MovieModel> getAllMovies() {
        ArrayList<MovieModel> movies = new ArrayList<MovieModel>();
        Cursor cursor = database.query(dbHelper.MOVIE_TABLE_NAME,
                allMovieColumns  , null, null, null, null,dbHelper.COLUMN_ID+" DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MovieModel movie = cursorToMovies(cursor);
            System.out.println("movie: "+movie);
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return movies;

    }
   /*
   delete movie
    */
    public void deleteMovie(MovieModel movie) {
        long id = movie.getId();
        database.delete(SQLiteHelper.MOVIE_TABLE_NAME, SQLiteHelper.COLUMN_ID
                + " = " + id, null);

    }
      // get movie data from cursor
    private MovieModel cursorToMovies(Cursor cursor) {
        MovieModel movie = new MovieModel();
        movie.setId(cursor.getLong(0));
        movie.setMovieTitle(cursor.getString(1));
        movie.setImage(cursor.getString(2));
        movie.setImdbRating(cursor.getString(3));
        movie.setYear(cursor.getString(4));
        movie.setReleasedDate(cursor.getString(5));
        return movie;
    }
}