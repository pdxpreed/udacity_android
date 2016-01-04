package com.app.preed.moviesort;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieInfoActivityFragment extends Fragment {

    private ImageView thumbnail;
    private TextView title;
    private TextView release;
    private TextView rating;
    private TextView summary;
    public MovieInfoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_info, container, false);

        Intent intent = getActivity().getIntent();
        thumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
        title = (TextView) rootView.findViewById(R.id.title_tv);
        release = (TextView) rootView.findViewById(R.id.release_tv);
        rating = (TextView) rootView.findViewById(R.id.rating_tv);
        summary = (TextView) rootView.findViewById(R.id.summary_tv);
        if (intent != null && intent.hasExtra("movie")) {
            MovieItem movie = intent.getExtras().getParcelable("movie");
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + movie.posterPath).into(thumbnail);
            title.setText(movie.movieTitle);
            release.setText("Released " + movie.releaseDate);
            rating.setText(Float.toString(movie.rating) + "/10.0");
            summary.setText(movie.overview);
        }


        return rootView;
    }
}
