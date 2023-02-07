package com.movie.mandiri.utils.retrofit2;

import com.movie.mandiri.models.ByGenreModel;
import com.movie.mandiri.models.DetailModel;
import com.movie.mandiri.models.GenreModel;
import com.movie.mandiri.models.ReviewModel;
import com.movie.mandiri.models.TrailerModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("genre/movie/list")
    Observable<GenreModel> getGenreMovie(
            @Query("api_key") String key
    );

    @GET("discover/movie")
    Observable<ByGenreModel> getMovieByGenre(
            @Query("api_key") String apiKey,
            @Query("with_genres") String id_genre,
            @Query("page") String page
    );

    @GET("movie/{id}")
    Observable<DetailModel> getMovieDetail(
            @Path(value = "id", encoded = true) String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/videos")
    Observable<TrailerModel> getMovieTrailer(
            @Path(value = "id", encoded = true) String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/reviews")
    Observable<ReviewModel> getMovieReview(
            @Path(value = "id", encoded = true) String id,
            @Query("api_key") String apiKey
    );
}
