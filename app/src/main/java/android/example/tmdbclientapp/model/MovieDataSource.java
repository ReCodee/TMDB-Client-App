package android.example.tmdbclientapp.model;

import android.app.Application;
import android.example.tmdbclientapp.R;
import android.example.tmdbclientapp.service.MovieDataService;
import android.example.tmdbclientapp.service.RetrofitInstance;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Long, Movie> {

    private MovieDataService movieDataService;
    private Application application;

    public MovieDataSource(MovieDataService movieDataService, Application application) {
        this.movieDataService = movieDataService;
        this.application = application;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Movie> callback) {
      movieDataService = RetrofitInstance.getService();
        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.apiKey), 1);
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();
                ArrayList<Movie> movieArrayList = new ArrayList<>();
                movieArrayList = (ArrayList<Movie>) movieDBResponse.getResults();
                callback.onResult(movieArrayList, null, (long)2);
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Movie> callback) {

        movieDataService = RetrofitInstance.getService();
        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.apiKey), params.key);
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();
                ArrayList<Movie> movieArrayList = new ArrayList<>();
                movieArrayList = (ArrayList<Movie>) movieDBResponse.getResults();
                callback.onResult(movieArrayList, params.key+1);
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

    }
}
