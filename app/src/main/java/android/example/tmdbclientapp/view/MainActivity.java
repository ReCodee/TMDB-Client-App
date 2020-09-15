package android.example.tmdbclientapp.view;


import androidx.appcompat.app.AppCompatActivity;
import android.example.tmdbclientapp.R;
import android.example.tmdbclientapp.model.MovieDBResponse;
import android.example.tmdbclientapp.service.MovieDataService;
import android.example.tmdbclientapp.service.RetrofitInstance;
import android.os.Bundle;

import retrofit2.Call;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     getSupportActionBar().setTitle("Popular Movies");

     viewPopularMovies();

    }

    public void viewPopularMovies(){

        MovieDataService movieDataService = RetrofitInstance.getService();
         Call<MovieDBResponse> movieDBResponse = movieDataService.getPopularMovies(this.getString(R.string.apiKey));

    }



}