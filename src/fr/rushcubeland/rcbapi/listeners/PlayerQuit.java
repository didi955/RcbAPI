package fr.rushcubeland.rcbapi.listeners;

import fr.rushcubeland.rcbapi.tools.PacketReader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();

        PacketReader reader = new PacketReader();
        reader.uninject(player);

    }

}
