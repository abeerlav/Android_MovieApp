package a2dv606_aa223de.movieapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import a2dv606_aa223de.movieapp.Model.MovieModel;

/**
 * Created by Abeer on 8/11/2017.
 * JsonParser class allows parsing  String objects.
 */

public class JsonParser {

    public JsonParser() {
    }
      /*
      parse json object
       */
    public MovieModel parseJsonObject(String data) throws JSONException {

        JSONObject obj = new JSONObject(data);
        System.out.println( obj.toString());
        if(obj.has("Error"))
              return null;
            String movieTitle = obj.getString("Title");
            String image = obj.getString("Poster");
            String imdbRating = obj.getString("imdbRating");
            String year = obj.getString("Year");
            String releasedDate = obj.getString("Released");

            MovieModel movie = new MovieModel(movieTitle, image, imdbRating, year, releasedDate);
       /*     System.out.println("Title " + movie.getMovieTitle());
            System.out.println("Poster " + movie.getImage());
            System.out.println("imdbRating " + movie.getImdbRating());
            System.out.println("Released " + movie.getReleasedDate());
            System.out.println("year " + movie.getYear());*/
            return movie;

    }



}
