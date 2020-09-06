package fr.rushcubeland.commons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class APermissions implements Cloneable {

    private UUID uuid;
    private ArrayList<String> permissions = new ArrayList<>();
    private Player player;

    public APermissions(){
    }

    public APermissions(UUID uuid, ArrayList<String> permissions) {
        this.uuid = uuid;
        this.permissions = permissions;
        this.player = Bukkit.getPlayer(uuid);
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPermission(String... permissions){
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void removePermission(String... permissions){
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public Player getPlayer() {
        return player;
    }

    public APermissions clone(){
        try {

            return (APermissions) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
