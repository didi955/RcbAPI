package fr.rushcubeland.rcbapi.tools.cameras;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CameraManager {

    HashMap<Player, Player> playersInSpectateToP = new HashMap<>();
    HashMap<Player, Entity> playersInSpectateToE = new HashMap<>();

    public void Spectate(Player Pfrom, Player Pto) {
        if(Pfrom instanceof Player && Pto instanceof Player) {
            Player from = (Player) Pfrom;
            if(!playersInSpectateToP.containsKey(Pfrom)) {
                playersInSpectateToP.put(Pfrom,Pto);
                from.setGameMode(GameMode.SPECTATOR);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.hidePlayer(from);

                }
                PacketPlayOutCamera packet = new PacketPlayOutCamera((Entity) Pto);
                ((CraftPlayer)from).getHandle().playerConnection.sendPacket(packet);
            }
            else
            {
                playersInSpectateToP.remove(from);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.showPlayer(from);
                }
                PacketPlayOutCamera packet2 = new PacketPlayOutCamera((Entity) Pto);
                ((CraftPlayer)from).getHandle().playerConnection.sendPacket(packet2);
            }
        }

    }

    public void Spectate(Player Pfrom, Entity Eto) {
        if(Pfrom instanceof Player) {
            Player from = (Player) Pfrom;
            if(!playersInSpectateToE.containsKey(Pfrom)) {
                playersInSpectateToE.put(Pfrom, Eto);
                from.setGameMode(GameMode.SPECTATOR);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.hidePlayer(from);

                }
                PacketPlayOutCamera packet = new PacketPlayOutCamera(Eto);
                ((CraftPlayer)from).getHandle().playerConnection.sendPacket(packet);
            }
            else
            {
                playersInSpectateToE.remove(from);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.showPlayer(from);
                }
                PacketPlayOutCamera packet2 = new PacketPlayOutCamera(Eto);
                ((CraftPlayer)from).getHandle().playerConnection.sendPacket(packet2);
            }
        }

    }
}
