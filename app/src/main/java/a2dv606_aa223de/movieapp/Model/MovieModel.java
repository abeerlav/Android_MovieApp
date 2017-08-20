package a2dv606_aa223de.movieapp.Model;

/**
 * Created by Hussain on 8/14/2017.
 */

public class  MovieModel {

    private String movieTitle;
    private String image;
    private String imdbRating;
    private String year;
    private String releasedDate;
    private long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MovieModel(int id,String movieTitle, String image, String imdbRating, String year, String releasedDate) {

        this.movieTitle = movieTitle;
        this.image = image;
        this.imdbRating = imdbRating;
        this.year = year;
        this.releasedDate = releasedDate;
        this.id = id;
    }



    public MovieModel() {

    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String rating) {
        this.imdbRating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }
    public boolean isEqual(MovieModel movieModel){
        return (this.getMovieTitle().equals(movieModel.getMovieTitle())
                && this.getReleasedDate().equals(movieModel.getReleasedDate())
                &&this.getImdbRating().equals(movieModel.getImdbRating())
                &&this.getYear().equals(movieModel.getYear())
                &&this.getImage().equals(movieModel.getImage()));
    }
    public MovieModel(String movieTitle, String image, String imdbRating, String year, String releasedDate) {
        this.movieTitle = movieTitle;
        this.image = image;
        this.imdbRating = imdbRating;
        this.year = year;
        this.releasedDate = releasedDate;

    }



}
