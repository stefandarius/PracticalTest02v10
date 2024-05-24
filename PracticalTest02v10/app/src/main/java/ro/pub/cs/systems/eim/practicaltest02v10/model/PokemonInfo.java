package ro.pub.cs.systems.eim.practicaltest02v10.model;

import androidx.annotation.NonNull;

import java.util.List;

public class PokemonInfo {
    List<String> types;
    List<String> abilities;

    public PokemonInfo(List<String> types, List<String> abilities) {
        this.types = types;
        this.abilities = abilities;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    @NonNull
    @Override
    public String toString() {
        return "PokemonInfo{" +
                "types=" + types +
                ", abilities=" + abilities +
                '}';
    }
}
