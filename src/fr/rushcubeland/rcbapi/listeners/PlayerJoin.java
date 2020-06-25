package fr.rushcubeland.rcbapi.listeners;

import fr.rushcubeland.rcbapi.tools.PacketReader;
import fr.rushcubeland.rcbapi.tools.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    public static NPC npc = null;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        PacketReader reader = new PacketReader();
        reader.inject(player);

        if(NPC.getNPCs() == null || NPC.getNPCs().isEmpty()){
            return;
        }
        NPC.addJoinPacket(player);
    }

}
