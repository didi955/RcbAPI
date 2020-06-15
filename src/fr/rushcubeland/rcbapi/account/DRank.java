package fr.rushcubeland.rcbapi.account;

import java.util.UUID;

public class DRank extends AbstractData {

    private RankUnit grade;
    private long end;

    public DRank(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(RankUnit rank){
        grade = rank;
        end = -1;
    }
    public void setRank(RankUnit rank, long seconds){
        if(seconds <= 0){
            setRank(rank);
        }
        else
        {
            grade = rank;
            end = seconds*1000 + System.currentTimeMillis();
        }
    }

    public RankUnit getRank(){
        return grade;
    }

    public long getEnd(){
        return end;
    }

    public boolean isTemporary(){
        return end != -1;
    }

    public boolean isValid(){
        return end != -1 && end < System.currentTimeMillis();
    }
}
