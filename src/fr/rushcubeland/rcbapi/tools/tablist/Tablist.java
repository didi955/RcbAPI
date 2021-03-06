package fr.rushcubeland.rcbapi.tools.tablist;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.rank.RankUnit;
import fr.rushcubeland.rcbapi.tools.npc.NPC;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Tablist {

    private Scoreboard scoreboard;
    private final ArrayList<Team> teams = new ArrayList<>();
    private RcbAPI rcbAPI;

    public Tablist(RcbAPI rcbAPI) {
        this.rcbAPI = rcbAPI;
    }

    public void sendTabList(Player player){

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText("§6§nRushcubeland\n§f\n§aFais le plein d'émotions et de joie !\n§7===================");
        Object footer = new ChatComponentText("§7===================\n§aPour obtenir un grade achète en un sur:\n §6shop.rushcubeland.fr\n§ewww.rushcubeland.fr");

        try {

            Field a = packet.getClass().getDeclaredField("header");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("footer");
            b.setAccessible(true);
            a.set(packet, header);
            b.set(packet, footer);

        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e){
            e.printStackTrace();
        }
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(packet);
    }

    public void setTabListPlayer(Player player){

        RcbAPI.getInstance().getAccountCallback(player, account -> {

            Team value = null;

            for(Team team : teams){
                if(team.getName().equals(account.getRank().getPower().toString())){
                    value = team;
                    break;
                }
            }
            player.setScoreboard(scoreboard);
            if (value != null) {
                value.addEntry(player.getName());
            }

        });
    }

    public void initTabListTeam(){
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective("scoreboard", "dummy", "§6Rushcubeland");
        for(RankUnit rank : RankUnit.values()){
            Team team = scoreboard.registerNewTeam(rank.getPower().toString());
            teams.add(team);
            team.setPrefix(rank.getPrefix());
        }
        Team teamNPC = scoreboard.registerNewTeam(Integer.toString(9999));
        teamNPC.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        for(EntityPlayer npc : NPC.getNPCs()){
            teamNPC.addEntry(npc.getName());
        }
    }
}
