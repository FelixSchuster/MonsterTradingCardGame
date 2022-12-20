package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
public class Card {
    @JsonAlias({"Id"})
    String id;
    @JsonAlias({"Name"})
    String name;
    @JsonAlias({"Damage"})
    Float damage;

    // Jackson needs the default constructor

    public Card() {}
    public Card(String id, String name, Float damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Float getDamage() {
        return damage;
    }
    public void setDamage(Float damage) {
        this.damage = damage;
    }
}
