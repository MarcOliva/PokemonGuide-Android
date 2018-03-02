package com.teamtectris.guide.pokemon.marc.oliva.pokemonguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamtectris.guide.pokemon.marc.oliva.pokemonguide.Pokemon;
import com.teamtectris.guide.pokemon.marc.oliva.pokemonguide.R;

import java.util.List;

/**
 * Created by ThinkSoft on 1/03/2018.
 */

public class PokemonAdapter extends BaseAdapter {
    private Context mContext;
    private List<Pokemon> mPokemonList;
    public PokemonAdapter(Context context,List<Pokemon> pokemonList) {
        mContext = context;
        mPokemonList=pokemonList;
    }

    @Override
    public int getCount() {
        return mPokemonList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if(gridView == null){
            gridView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        }
        Pokemon currentPokemon = mPokemonList.get(position);
        ImageView image = gridView.findViewById(R.id.image_pokemon);
        Picasso.with(mContext).load(currentPokemon.getmUrlImage()).into(image);
        TextView name = gridView.findViewById(R.id.name_pokemon);
        name.setText(currentPokemon.getmName());
        return gridView;

    }

}
