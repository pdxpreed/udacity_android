package com.app.preed.moviesort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by preed on 11/16/2015.
 */
public class MovieItemAdapter extends ArrayAdapter<MovieItem> {

    private static class ViewHolder {
        ImageView posterImage;
    }


    public MovieItemAdapter(Context context, List<MovieItem> movieItems) {
        super(context, 0, movieItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieItem movie = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
            viewHolder.posterImage = (ImageView) convertView.findViewById(R.id.movie_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + movie.posterPath).into(viewHolder.posterImage);

        return convertView;
    }
}
