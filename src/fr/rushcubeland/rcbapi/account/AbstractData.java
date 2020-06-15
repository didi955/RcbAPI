package fr.rushcubeland.rcbapi.account;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

abstract class AbstractData {

    public UUID uuid;

    public String getUUID() {
        return uuid.toString();
    }

    Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
