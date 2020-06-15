package fr.rushcubeland.rcbapi.account;

import org.bukkit.entity.Player;

import java.util.UUID;

public class DBoosters extends AbstractData {

    private boolean hasBooster;
    private long end;
    private BoostersUnit booster;
    private long start;

    public DBoosters(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean playerHasBooster(){
        if(booster == null){
            return false;
        }
            return true;
    }

    public BoostersUnit getBooster(){
        return booster;
    }

    public void setBooster(BoostersUnit boostersUnit){
        booster = boostersUnit;
        start = System.currentTimeMillis();
        end = -1;

    }

    public void setBooster(BoostersUnit boostersUnit, long start, long end){
        booster = boostersUnit;
        this.start = start;
        this.end = end;

    }

    public void removeBooster(){
        booster = null;
        hasBooster = false;
    }

    public void extendBooster(Integer hours){
        end = end+(hours*3600000);

    }

    public boolean isTemporary(){
        return end != -1;
    }

    public boolean isValid(){
        if(end != -1 && end < System.currentTimeMillis()){
            removeBooster();
            return false;
        }
        return true;
    }

    public long getEnd(){
        return end;
    }

    public long getStart(){
        return start;
    }


}
