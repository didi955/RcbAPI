package fr.rushcubeland.rcbapi.account;

import java.util.UUID;

public class DCoins extends AbstractData {

    private long coins;

    public DCoins(UUID uuid) {
        this.uuid = uuid;
    }

    public void setCoins(long coins){
        if(coins < 0)
            coins = 0;

        this.coins = coins;
    }

    public long getCoins(){
        return coins;
    }

    public void addCoins(long coins){
        this.coins += coins;
    }

    public void removeCoins(long coins){
        if(coins > this.coins)
            coins = this.coins;

        this.coins -= coins;
    }

    public boolean hasCoins(long coins){
        if(coins <= 0)
            return false;

        return this.coins >= coins;
    }
}
