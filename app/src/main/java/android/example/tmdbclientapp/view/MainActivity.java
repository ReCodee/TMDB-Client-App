package android.example.tmdbclientapp.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.example.tmdbclientapp.R;
import android.example.tmdbclientapp.databinding.ActivityMainBinding;
import android.example.tmdbclientapp.databinding.ActivityMovieBinding;
import android.example.tmdbclientapp.model.Movie;
import android.example.tmdbclientapp.model.MovieDBResponse;
import android.example.tmdbclientapp.service.MovieDataService;
import android.example.tmdbclientapp.service.RetrofitInstance;
import android.os.Bundle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ArrayList<Movie> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     getSupportActionBar().setTitle("Popular Movies");
     activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
     viewPopularMovies();
      swipeRefreshLayout = activityMainBinding.swipe;
      swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              viewPopularMovies();
          }
      });
    }

    public void viewPopularMovies(){

        MovieDataService movieDataService = RetrofitInstance.getService();
         Call<MovieDBResponse> movieDBResponse = movieDataService.getPopularMovies(this.getString(R.string.apiKey));
               movieDBResponse.enqueue(new Callback<MovieDBResponse>() {
                   @Override
                   public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                       MovieDBResponse movieDBResponse1 = response.body();
                       arrayList =(ArrayList<Movie>) movieDBResponse1.getResults();
                       showOnRecyclerView();
                   }

                   @Override
                   public void onFailure(Call<MovieDBResponse> call, Throwable t) {

                   }
               });
    }

    public void showOnRecyclerView(){
        recyclerView = activityMainBinding.rvMovies;
        adapter = new MovieAdapter(this, arrayList);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}