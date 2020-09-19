package android.example.tmdbclientapp.ViewModel;

import android.app.Application;
import android.example.tmdbclientapp.model.Movie;
import android.example.tmdbclientapp.model.MovieDataSource;
import android.example.tmdbclientapp.model.MovieDataSourceFactory;
import android.example.tmdbclientapp.model.MovieRepository;
import android.example.tmdbclientapp.service.MovieDataService;
import android.example.tmdbclientapp.service.RetrofitInstance;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MovieDataSourceFactory factory;
    private Executor executors;
     LiveData<MovieDataSource> movieDataSourceLiveData;
     private LiveData<PagedList<Movie>> pagedListLiveData;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieRepository=new MovieRepository(application);
        MovieDataService movieDataService = RetrofitInstance.getService();
        factory = new MovieDataSourceFactory(movieDataService, application);
        movieDataSourceLiveData = factory.getMutableLiveData();

         PagedList.Config config = (new PagedList.Config.Builder())
                     .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

    executors = Executors.newFixedThreadPool(5);

  pagedListLiveData = (new LivePagedListBuilder<Long, Movie>(factory, config)).setFetchExecutor(executors).build();



    }

    public LiveData<PagedList<Movie>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    public LiveData<List<Movie>> getAllMovies(){

        return movieRepository.getMutableLiveData();
    }

}
