package a2dv606_aa223de.movieapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.List;
import a2dv606_aa223de.movieapp.Model.MovieModel;
import a2dv606_aa223de.movieapp.SQLiteDB.MovieDataSource;

/**
 * created by Abeer 8/11/2017.
 * FavoriteMoviesActivity shows all favorite
 * movies of the user.
 * The user able to delete movie from the local db.
 * The user able to share the data of specific movie with other apps.
 *
 *  listView is used to show the data to the user.
 *
 */
public class FavoriteMoviesActivity extends AppCompatActivity {
    private MovieDataSource db ;
    private ListView listView;
    private List<MovieModel> values ;
    private ArrayAdapter<MovieModel> adapter;
    private  AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
           /*enable DisplayImageOptions to perform image caching
         */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        db = new MovieDataSource(getApplicationContext());
        db.open();
        values= db.getAllMovies();
        listView = (ListView) findViewById(R.id.view_list);
        adapter = new myListAdapter();
        listView.setAdapter(adapter);

    }

    /*
     show a vertical list of scrollable items using
     a ListView using an ArrayAdapter which will converts the ArrayList of  movie objects
     into View items loaded into the ListView container.
     */
        private class myListAdapter extends ArrayAdapter<MovieModel> {

            public myListAdapter() {
                super(FavoriteMoviesActivity.this, R.layout.movie_raw, values);
            }

            public View getView(final int position, View convertView, ViewGroup parent) {

                View itemView = convertView;
                if (itemView == null) {
                    itemView = getLayoutInflater().inflate(
                            R.layout.movie_raw, parent, false);
                }

              TextView titleTv=(TextView) itemView.findViewById(R.id.tv_title);

                TextView releaseTv =(TextView)  itemView.findViewById(R.id.tv_release);

                TextView yearTv = (TextView)itemView.findViewById(R.id.tv_year);
                RatingBar ratingTv = (RatingBar)itemView.findViewById(R.id.rb_rating);
               ImageView imageView =(ImageView)itemView.findViewById(R.id.img_view);
                final ProgressBar progressBar = (ProgressBar)itemView.findViewById(R.id.progressbar);
                final ImageButton shareButton = (ImageButton) itemView.findViewById(R.id.forward_button);
                final String  title =values.get(position).getMovieTitle();
                final String rating = values.get(position).getImdbRating();
                final String year = values.get(position).getYear();
                final String image =values.get(position).getImage();
                final String release =values.get(position).getReleasedDate();
                 titleTv.setText(title);
                 yearTv.setText("Year: "+year);
                 releaseTv.setText(release);
                 ratingTv.setStepSize(0.01f);
                   if(!rating.equals("N/A"))
                 ratingTv.setRating(Float.parseFloat(rating));
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        String posterToShare = "I Just wanted to share this poster"+"\n"
                                +title+", "+year+"\n" +"Imdb Rating: " +rating+"\n"+ image;
                        sendIntent.putExtra(Intent.EXTRA_TEXT,posterToShare);
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));



                    }
                });
                if(image.equals("N/A"))
                { imageView.setImageResource(getResources().getIdentifier("drawable/not_available"
                        , null,getPackageName()));
                    progressBar.setVisibility(View.GONE);}
                else
                ImageLoader.getInstance().displayImage(image, imageView, new ImageLoadingListener() {
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

                 /*
                 enable onclick itemView Listener to perform delete movie obj from db
                  */
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteMoviesActivity.this);
                        builder.setMessage("Delete movie!");

                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MovieModel movieModel = values.get(position);
                                db.deleteMovie(movieModel);
                                adapter.clear();
                                adapter.addAll(db.getAllMovies());
                                listView.setAdapter(adapter);
                            }
                        });

                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.dismiss();
                            }
                        });
                       alert = builder.create();
                        alert.show();

                    }
                });


                return itemView;
            }
        }

    }


