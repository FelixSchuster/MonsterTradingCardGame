package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
public class User {
    @JsonAlias({"user_id"})
    private Integer user_id;
    @JsonAlias({"name"})
    private String name;
    @JsonAlias({"password"})
    private String password;
    @JsonAlias({"coins"})
    private Integer coins;
    @JsonAlias({"elo"})
    private Integer elo;

    // Jackson needs the default constructor
    public User() {}
    public User(Integer user_id, String name, String password, Integer coins, Integer elo) {
        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.coins = coins;
        this.elo = elo;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getCoins() {
        return coins;
    }
    public void setCoins(Integer coins) {
        this.coins = coins;
    }
    public Integer getElo() {
        return elo;
    }
    public void setElo(Integer elo) {
        this.elo = elo;
    }
}
