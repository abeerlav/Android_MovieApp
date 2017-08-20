package a2dv606_aa223de.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/*
   created by Abeer Alkhars  8/11/2017.
   main page show tow option:
   movie search & favorites movies
 */

public class MainActivity extends AppCompatActivity  implements View.OnClickListener  {


    private Button searchButton;
    private Button favoriteButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (Button) findViewById(R.id.search_button);
        favoriteButton=(Button)findViewById(R.id.favorite_button);
        searchButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);



}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_button:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.favorite_button:
                startActivity(new Intent(this, FavoriteMoviesActivity.class));
                break;
        }
    }}