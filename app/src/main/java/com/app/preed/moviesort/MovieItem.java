package com.app.preed.moviesort;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by preed on 11/12/2015.
 */
public class MovieItem implements Parcelable {

    String movieTitle;
    String overview;
    String releaseDate;
    String posterPath;
    float rating;
    int id;

    public MovieItem(JSONObject object){
        try {
            this.movieTitle = object.getString("original_title");
            this.overview = object.getString("overview");
            this.releaseDate = object.getString("release_date");
            this.posterPath = object.getString("poster_path");
            this.id = object.getInt("id");
            this.rating = object.getLong("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();;
        }
    }

    private MovieItem(Parcel in) {
        movieTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        rating = in.readFloat();
        id = in.readInt();
    }

    public static ArrayList<MovieItem> fromJson(JSONArray jsonObjects) {
        ArrayList<MovieItem> movies = new ArrayList<>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                movies.add(new MovieItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();;
            }
        }

        return movies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeFloat(rating);
        parcel.writeInt(id);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }
    };
}
