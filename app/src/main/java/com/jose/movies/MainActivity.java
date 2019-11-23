package com.jose.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextInputEditText Movie;
    ImageView image;
    TextView MovieT;
    TextView Year;
    TextView Runtime;
    TextView Genre;
    TextView Director;
    TextView Actors;
    TextView Plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Movie = (TextInputEditText) findViewById(R.id.Movie);
        image = (ImageView) findViewById(R.id.Image);
        MovieT = (TextView) findViewById(R.id.MovieTitle);
        Year = (TextView) findViewById(R.id.YearL);
        Runtime = (TextView) findViewById(R.id.RuntimeL);
        Genre = (TextView) findViewById(R.id.GenreL);
        Director = (TextView) findViewById(R.id.DirectorL);
        Actors = (TextView) findViewById(R.id.ActorsL);
        Plot = (TextView) findViewById(R.id.PlotL);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void GetData(View v) {
        String key = "e8b83b09";
        String path = "https://www.omdbapi.com/?t=" + Movie.getText() + "&apikey=" + key;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONObject Movies = new JSONObject(json);

            Movie.setFocusable(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            MovieT.setText(Movies.get("Title").toString());
            Year.setText(Movies.get("Year").toString());
            Runtime.setText(Movies.get("Runtime").toString());
            Genre.setText(Movies.get("Genre").toString());
            Director.setText(Movies.get("Director").toString());
            Actors.setText(Movies.get("Actors").toString());
            Plot.setText(Movies.get("Plot").toString());
            Picasso.with(this)
                    .load(Movies.get("Poster").toString())
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerInside()
                    .into(image);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Movie.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Movie.setFocusableInTouchMode(true);
                return false;
            }
        });

    }
}
