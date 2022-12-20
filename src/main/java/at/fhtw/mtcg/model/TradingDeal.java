package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
public class TradingDeal {
    @JsonAlias({"Id"})
    String id;
    @JsonAlias({"CardToTrade"})
    String cardToTrade;
    @JsonAlias({"Type"})
    String type;
    @JsonAlias({"MinimumDamage"})
    Float minimumDamage;

    // Jackson needs the default constructor
    public TradingDeal() {}
    public TradingDeal(String id, String cardToTrade, String type, Float minimumDamage) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCardToTrade() {
        return cardToTrade;
    }
    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Float getMinimumDamage() {
        return minimumDamage;
    }
    public void setMinimumDamage(Float minimumDamage) {
        this.minimumDamage = minimumDamage;
    }
}
