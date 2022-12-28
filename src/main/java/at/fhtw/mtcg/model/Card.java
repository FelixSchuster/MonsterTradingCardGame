package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    public Boolean isSpellCard() {
        return this.name.endsWith("Spell");
    }
    @JsonIgnore
    public Boolean isMonsterCard() {
        return !isSpellCard();
    }
    @JsonIgnore
    public Boolean isWaterCard() {
        return this.name.startsWith("Water");
    }
    @JsonIgnore
    public Boolean isFireCard() {
        return this.name.startsWith("Fire");
    }
    @JsonIgnore
    public Boolean isRegularCard() {
        return !isWaterCard() && !isFireCard();
    }
    @JsonIgnore
    public Boolean isGoblinCard() {
        return this.name.endsWith("Goblin");
    }
    @JsonIgnore
    public Boolean isDragonCard() {
        return this.name.endsWith("Dragon");
    }
    @JsonIgnore
    public Boolean isWizardCard() {
        return this.name.endsWith("Wizard");
    }
    @JsonIgnore
    public Boolean isOrkCard() {
        return this.name.endsWith("Ork");
    }
    @JsonIgnore
    public Boolean isKnightCard() {
        return this.name.endsWith("Knight");
    }
    @JsonIgnore
    public Boolean isKrakenCard() {
        return this.name.endsWith("Kraken");
    }
    @JsonIgnore
    public Boolean isElfCard() {
        return this.name.endsWith("Elf");
    }
    @JsonIgnore
    public float calculateDamage(Card opponentCard) {
        Boolean isPureMonsterFight = this.isMonsterCard() && opponentCard.isMonsterCard(); // element type does not affect pure monster fights

        if(!isPureMonsterFight) {
            if(this.isWaterCard() && opponentCard.isFireCard() || this.isFireCard() && opponentCard.isRegularCard() || this.isRegularCard() && opponentCard.isWaterCard()) {
                return this.damage * 2; // is effective
            }
            if(this.isFireCard() && opponentCard.isWaterCard() || this.isRegularCard() && opponentCard.isFireCard() || this.isWaterCard() && opponentCard.isRegularCard()) {
                return this.damage / 2; // is not effective
            }
        }

        else {
            if(this.isGoblinCard() && opponentCard.isDragonCard()) {
                return 0;
            }
            if(this.isOrkCard() && opponentCard.isWizardCard()) {
                return 0;
            }
            if(this.isWaterCard() && this.isSpellCard() && opponentCard.isKnightCard()) {
                return Float.MAX_VALUE;
            }
            if(this.isSpellCard() && opponentCard.isKrakenCard()) {
                return 0;
            }
            if(this.isDragonCard() && opponentCard.isFireCard() && opponentCard.isElfCard()) {
                return 0;
            }
        }

        return this.damage;
    }
}
