package com.teamtectris.guide.pokemon.marc.oliva.pokemonguide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.teamtectris.guide.pokemon.marc.oliva.pokemonguide.Adapter.PokemonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String URL_POKEMON = "https://pokeapi.co/api/v2/pokemon/";
    private static String URL_SPRITE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    private String linkImage;
    GridView gridView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        progressBar = findViewById(R.id.progress);

        gridView = findViewById(R.id.grid_pokemon);
        gridView.setEmptyView(progressBar);

        adapterPokemonList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void adapterPokemonList() {
        final List<Pokemon> pokemonList = new ArrayList<>();
        AndroidNetworking.get(URL_POKEMON)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray pokemonArray = response.getJSONArray("results");
                            for (int i = 0 ;i<pokemonArray.length();i++){
                                JSONObject pokemonObject = pokemonArray.getJSONObject(i);
                                String name = pokemonObject.getString("name");
                                String urlImage = URL_SPRITE+String.valueOf(i+1)+".png";
                                Pokemon pokemon = new Pokemon(name,urlImage);
                                pokemonList.add(pokemon);
                            }
                            gridView.setAdapter(new PokemonAdapter(getApplicationContext(),pokemonList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private String getUrlImage(String url) {
        final String[] urlImage = {" "};
        AndroidNetworking.get(url)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject pokemonObject = response.getJSONObject("sprites");
                            urlImage[0] = pokemonObject.getString("front_default");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        return urlImage[0];
    }

    private class pokemonAsyncTask extends AsyncTask<String, Integer, List<Pokemon>> {


        @Override
        protected List<Pokemon> doInBackground(String... url) {
            if (url.length < 1 || url[0] == null) {
                return null;
            }
            final List<Pokemon> pokemonList = new ArrayList<>();
            AndroidNetworking.get(URL_POKEMON)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray pokemonArray = response.getJSONArray("results");
                                for (int i = 0; i < pokemonArray.length(); i++) {
                                    JSONObject pokemonObject = pokemonArray.getJSONObject(i);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return pokemonList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Pokemon> pokemons) {
            if (pokemons.size() > 1) {
                gridView.setAdapter(new PokemonAdapter(getApplicationContext(), pokemons));
            }
        }
    }


}
