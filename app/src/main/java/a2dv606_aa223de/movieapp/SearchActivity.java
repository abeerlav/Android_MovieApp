package a2dv606_aa223de.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import a2dv606_aa223de.movieapp.Model.MovieModel;
import a2dv606_aa223de.movieapp.SQLiteDB.MovieDataSource;

/**
 * created by Abeer Alkhars 8/12/2017.
 *
 * SearchActivity allows the user to search for a specific movie using its title and optionally its year
 * The user able to add movies to the favorite list.
 * AsyncTask is used to request data from omdb Api in the background and publish the result in the ui
 * Universal Image Loader lib is used to preform caching and loading image from the server
 * sqlite db used to store the movie poster.
 */

public class SearchActivity extends AppCompatActivity {
    /*
    url for accessing omdb api. It must includes api key
     */
    private final String OMDB_URL= "http://www.omdbapi.com/?apikey=b86427a6&";
    private final String TITLE_PARAM= "t=";
    private final String YEAR_PARA= "y=";
    private MovieModel movieModel;
    private MovieDataSource db;
    private ArrayList<MovieModel> movies;
    private SearchView searchView;
    private LinearLayout layout ;
    private boolean isSavedFlag= false;
    private ProgressDialog dialog;
    private Button searchButton;
    private EditText titleEd ,yearEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        db = new MovieDataSource(getApplicationContext());
        db.open();
        movies = db.getAllMovies();
        initLoadingDialog();
        searchButton = (Button) findViewById(R.id.search_butto);
        titleEd = (EditText)findViewById(R.id.title_editview);
        yearEt = (EditText)findViewById(R.id.year_editview);


/*
  button to search for movie using a title and optionally a year to give more specific result
 */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                String title =titleEd.getText().toString();
                String year= yearEt.getText().toString();
                // checking if the title is not empty
                if(!title.isEmpty()){
                    // adding the query params to the url
                    String titleQuery= null;
                    try {
                        titleQuery = URLEncoder.encode(title, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url =OMDB_URL+TITLE_PARAM+titleQuery+"&"+YEAR_PARA+year;
                    // executing data request in the background
                     if(checkNetWorkConnection())
                        new JsonTask().execute((url));
                    else Toast.makeText(getApplicationContext(), getResources().getString(R.string.InternetConnectionNotAvailable),
                             Toast.LENGTH_SHORT).show();
                }
                 else{Toast.makeText(getApplicationContext(), getResources().getString(R.string.title_not_specificed),
                        Toast.LENGTH_SHORT).show();}


            }


        });


        /*
        enable DisplayImageOptions to perform image caching
         */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        layout = (LinearLayout)findViewById(R.id.layout_view);





    }

    private boolean checkNetWorkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting()&&networkInfo.isConnected();
        }



    private void initLoadingDialog() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(getResources().getString(R.string.progress_loading));
    }

/*
  AsyncTask class to preform the request in the background
 */
    public class JsonTask extends AsyncTask<String, String ,MovieModel > {

        @Override
        protected MovieModel doInBackground(String... params) {
            HttpURLConnection httpUrlConnection = null;
            BufferedReader reader = null;


            URL url = null;
            try {
                url = new URL(params[0]);
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.connect();

                InputStream inputStream = httpUrlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine())!=null){
                    buffer.append(line);

                }
                String data= buffer.toString();
                try {

                    movieModel =  new JsonParser().parseJsonObject(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return movieModel;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing loading dialog
            dialog.show();

        }


        @Override
        public void onPostExecute(MovieModel result){
            super.onPostExecute(result);
            // closing loading dialog after receiving data
            dialog.dismiss();
            // making sure that the data has received
            if(result!=null)
                displayMovieData(result);
            else
                Toast.makeText(getApplicationContext(),"Movie not found",Toast.LENGTH_LONG).show();

        }
    }

    private void displayMovieData(final MovieModel movie) {
        // displaying the poster in the layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view);
        layout.setVisibility(View.VISIBLE);
        TextView titleTextView= (TextView) findViewById(R.id.textView_title);
        TextView yearTextView= (TextView) findViewById(R.id.textView_year);
        TextView releaseDateTextView= (TextView) findViewById(R.id.textView_release);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.rating);
        ImageButton shareButton = (ImageButton) findViewById(R.id.forward_button);
        final ImageView poster = (ImageView)findViewById(R.id.poster_view);

        final ProgressBar progressBar =(ProgressBar)findViewById(R.id.progressBar);
        final Button addButton = (Button) findViewById(R.id.add_button);
        isSavedFlag=false;
        titleTextView.setText(movie.getMovieTitle());
        yearTextView.setText("Year: "+movie.getYear());
        releaseDateTextView.setText(movie.getReleasedDate());
        ratingBar.setStepSize(0.01f);
        poster.setImageResource(0);
        if(!movie.getImdbRating().equals("N/A"))
           ratingBar.setRating(Float.parseFloat(movie.getImdbRating()));
        addButton.setEnabled(true);

        for(MovieModel m: movies){
            if( m.isEqual(movie)){
                addButton.setEnabled(false);
                isSavedFlag=true;
                break;

            }
        }

            /*
            button to add movie to db
             */
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isSavedFlag){
                    db.createMovie(movie);
                    addButton.setEnabled(false);
                    Toast toast = Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT);
                    toast.show();
                 }

            }
        });
/*
button to share the poster with other apps
 */
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String posterToShare = "I Just wanted to share this poster" + "\n"
                        + movie.getMovieTitle() + " " + movie.getReleasedDate() + "\n"
                        + "Imdb Rating: " + movie.getImdbRating() + "\n" +
                        movie.getImage();
                shareIntent.putExtra(Intent.EXTRA_TEXT, posterToShare);
                shareIntent.setType("text/plain");

                //check the apps respond to the intent
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager
                        .queryIntentActivities(shareIntent, 0);
                boolean isIntentSafe = activities.size() > 0;
                System.out.println("isIntentSafe: " + isIntentSafe);
                if (isIntentSafe) {

                    Intent chooser = Intent.createChooser(shareIntent, getResources().getString((R.string.send_to)));
                    startActivity(chooser);
                } else Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_apps_found),
                        Toast.LENGTH_SHORT).show();


            }
        });



       if(movie.getImage().equals("N/A"))
       { poster.setImageResource(getResources().getIdentifier("drawable/not_available"
             , null,getPackageName()));
         progressBar.setVisibility(View.GONE);}
        else
        ImageLoader.getInstance().displayImage(movie.getImage(), poster, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }}

/**
 * The following codes make it possible to search & show favorite from the toolbar
 */

  /*  @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println( "SearchOnQueryTextSubmit: " + query);
                String params= parser.findQueryParams(query);
                String url = OMDB_URL+TITLE_PARAM+params;
                System.out.println(url);
                new JsonTask().execute(url);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                layout.setVisibility(View.INVISIBLE);
                System.out.println( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_favorite) {
            Intent intent = new Intent(getApplicationContext(), FavoriteMoviesActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }}
    */
