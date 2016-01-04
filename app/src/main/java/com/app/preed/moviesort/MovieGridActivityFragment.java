package com.app.preed.moviesort;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridActivityFragment extends Fragment {

    private static String SORT_KEY = "SORT_KEY";

    private MovieItemAdapter movieAdapter;
    GridView gridView;

    public MovieGridActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        gridView = (GridView) rootView.findViewById(R.id.movie_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent detailIntent = new Intent(getActivity(), MovieInfoActivity.class);
                detailIntent.putExtra("movie", movieAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });

        setHasOptionsMenu(true);

        return rootView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sort_popularity:
                setOrder(getString(R.string.popularity));
                return true;

            case R.id.menu_sort_vote_average:
                setOrder(getString(R.string.vote));
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovies();
    }

    private void setOrder(String order) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SORT_KEY, order);
        editor.commit();
        fetchMovies();
    }

    private void fetchMovies() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sortOrder = sharedPref.getString(SORT_KEY, getString(R.string.popularity));
        new FetchMovieTask().execute(sortOrder);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {

        private ArrayList<MovieItem> getMovieDataFromJson(String movieDbJsonStr) throws JSONException {

            ArrayList<MovieItem> movies = new ArrayList<>();

            final String MDB_RESULTS = "results";

            JSONObject movieDbJson = new JSONObject(movieDbJsonStr);
            JSONArray movieArray = movieDbJson.getJSONArray(MDB_RESULTS);

            for (int i = 0; i < movieArray.length(); i++) {


                    try {
                        movies.add(new MovieItem(movieArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return movies;



        }

        @Override
        protected ArrayList<MovieItem> doInBackground(String... order) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            int page = 1;
            String key = "b7ad9e9c2d1b95bb0ad02505cd6b1c48";

            // Will contain the raw JSON response as a string.
            String movieDbJsonStr;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String POPULARITY_PARAM = "sort_by";
                final String PAGE_PARAM = "page";
                final String API_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().
                        appendQueryParameter(POPULARITY_PARAM, order[0]).
                        appendQueryParameter(PAGE_PARAM, Integer.toString(page)).
                        appendQueryParameter(API_PARAM, key).build();


                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieDbJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(this.getClass().getSimpleName(), "Error closing stream", e);
                    }

                }

            }


            try {
                return getMovieDataFromJson(movieDbJsonStr);
            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieItems) {
            if (movieItems != null) {
                movieAdapter = new MovieItemAdapter(getActivity(), movieItems);
                gridView.setAdapter(movieAdapter);
            }
        }
    }
}
