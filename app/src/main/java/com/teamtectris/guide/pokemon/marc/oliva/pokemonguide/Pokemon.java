package com.teamtectris.guide.pokemon.marc.oliva.pokemonguide;

/**
 * Created by ThinkSoft on 28/02/2018.
 */

public class Pokemon {
    private String mName;
    private String mUrlImage;

    public Pokemon(String name, String urlImage) {
        this.mName = name;
        this.mUrlImage = urlImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmUrlImage() {
        return mUrlImage;
    }
}
