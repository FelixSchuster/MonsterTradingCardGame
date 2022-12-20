package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
public class UserStats {
    @JsonAlias({"Name"})
    String name;
    @JsonAlias({"Elo"})
    Integer elo;
    @JsonAlias({"Wins"})
    Integer wins;
    @JsonAlias({"Losses"})
    Integer losses;

    // Jackson needs the default constructor
    public UserStats() {}
    public UserStats(String name, Integer elo, Integer wins, Integer losses) {
        this.name = name;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getElo() {
        return elo;
    }
    public void setElo(Integer elo) {
        this.elo = elo;
    }
    public Integer getWins() {
        return wins;
    }
    public void setWins(Integer wins) {
        this.wins = wins;
    }
    public Integer getLosses() {
        return losses;
    }
    public void setLosses(Integer losses) {
        this.losses = losses;
    }
}
