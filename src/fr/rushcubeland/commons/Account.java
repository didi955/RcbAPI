package fr.rushcubeland.commons;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.rank.RankUnit;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Account implements Cloneable {

    private UUID uuid;

    private long coins;

    private RankUnit grade;
    private long grade_end;

    public Account() {
    }

    public Account(UUID uuid, RankUnit grade, long coins) {
        this(uuid, grade, -1, coins);
    }

    public Account(UUID uuid, RankUnit grade, long grade_end, long coins) {
        this.uuid = uuid;
        this.grade = grade;
        this.coins = coins;
        this.grade_end = grade_end;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public RankUnit getRank() {
        return grade;
    }

    public long getRank_end() {
        return grade_end;
    }

    public void setRank(RankUnit rank) {
        if(grade != null){
            for(String perm : grade.getPermissions()){
                Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, false);
            }
        }
        grade = rank;
        grade_end= -1;
        for(String perm : rank.getPermissions()){
            Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
        }
    }

    public void setRank(RankUnit rank, long seconds) {
        if(grade_end <= 0){
            setRank(rank);
        }
        else
        {
            if(grade != null){
                for(String perm : grade.getPermissions()){
                    Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, false);
                }
            }
            grade = rank;
            this.grade_end = seconds*1000 + System.currentTimeMillis();
            for(String perm : rank.getPermissions()){
                Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
            }
        }
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public boolean rankIsTemporary(){
        return grade_end != -1;
    }

    public boolean rankIsValid(){
        return grade_end != -1 && grade_end < System.currentTimeMillis();
    }

    public Account clone(){
        try {

            return (Account) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getGrade_end() {
        return grade_end;
    }

    public void setGrade_end(long grade_end) {
        this.grade_end = grade_end;
    }
}