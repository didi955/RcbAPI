package fr.rushcubeland.rcbapi.account;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbapi.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DRank extends AbstractData {

    private RankUnit grade;
    private long end;

    public DRank(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(RankUnit rank){
        if(grade != null){
            for(String perm : grade.getPermissions()){
                Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, false);
            }
        }
        grade = rank;
        end = -1;
        for(String perm : rank.getPermissions()){
            Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
        }
        RcbAPI.getInstance().getTablist().setTabListPlayer(getPlayer(), rank);
    }
    public void setRank(RankUnit rank, long seconds){
        if(seconds <= 0){
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
            end = seconds*1000 + System.currentTimeMillis();
            for(String perm : rank.getPermissions()){
                Bukkit.getPlayer(uuid).addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
            }
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

    private void ChangeRankToProxy(RankUnit rank, long seconds){
        Player player = Bukkit.getPlayer(getUUID());
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ChangeRank");
        out.writeUTF(getUUID());
        out.writeUTF(rank.getName());
        out.writeLong(seconds);

        if (player != null) {
            player.sendPluginMessage(RcbAPI.getInstance(), "rcbproxy:main", out.toByteArray());
        }
    }

}
