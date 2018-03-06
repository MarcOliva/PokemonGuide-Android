package com.teamtectris.guide.pokemon.marc.oliva.pokemonguide;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
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
    GridView gridView;
    LinearLayout progressLoadPokemon, notFoundPokemon;
    FrameLayout frameTitle;
    TextView titleApp;
    //SearchView searchPokemon;
    List<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        //searchPokemon = findViewById(R.id.search_pokemon_edit_text);
        //Import new font
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/sunflowers.ttf");
        titleApp = findViewById(R.id.title_app);
        //add new font to title app
        titleApp.setTypeface(face);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressLoadPokemon = findViewById(R.id.progress);
        notFoundPokemon = findViewById(R.id.notfound);
        notFoundPokemon.setVisibility(View.GONE);
        //frameTitle = findViewById(R.id.frame_app);
        gridView = findViewById(R.id.grid_pokemon);

        gridView.setEmptyView(progressLoadPokemon);

        adapterPokemonList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, pokemonList.get(position).getmName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adapterPokemonList() {
        pokemonList = new ArrayList<>();
        AndroidNetworking.get(URL_POKEMON)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray pokemonArray = response.getJSONArray("results");
                            for (int i = 0; i < pokemonArray.length(); i++) {
                                JSONObject pokemonObject = pokemonArray.getJSONObject(i);
                                String name = pokemonObject.getString("name");
                                String urlImage = URL_SPRITE + String.valueOf(i + 1) + ".png";
                                Pokemon pokemon = new Pokemon(name, urlImage);
                                pokemonList.add(pokemon);
                            }
                            gridView.setAdapter(new PokemonAdapter(getApplicationContext(), pokemonList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (pokemonList.size() <= 0) {
                            notFoundPokemon.setVisibility(View.VISIBLE);
                        }
                        progressLoadPokemon.setVisibility(View.GONE);
                    }
                });

    }

    private void getPokemon(String namePokemon) {
        notFoundPokemon.setVisibility(View.GONE);
        progressLoadPokemon.setVisibility(View.VISIBLE);
        pokemonList = new ArrayList<>();
        if (!namePokemon.isEmpty()) {
            AndroidNetworking.get(URL_POKEMON + namePokemon)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject sprite = response.getJSONObject("sprites");
                                String name = response.getString("name");
                                String urlImage = sprite.getString("front_default");
                                Pokemon pokemon = new Pokemon(name, urlImage);
                                pokemonList.add(pokemon);
                                //bug fixed...repeat the same pokemon
                                gridView.setAdapter(new PokemonAdapter(getApplicationContext(), pokemonList));
                                progressLoadPokemon.setVisibility(View.GONE);
                                notFoundPokemon.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressLoadPokemon.setVisibility(View.GONE);
                            if (pokemonList.size() <= 0) {
                                notFoundPokemon.setVisibility(View.VISIBLE);
                            }

                        }
                    });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchable, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchPokemon = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchPokemon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    adapterPokemonList();
                    notFoundPokemon.setVisibility(View.GONE);
                } else {
                    gridView.setAdapter(null);
                    getPokemon(newText);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
