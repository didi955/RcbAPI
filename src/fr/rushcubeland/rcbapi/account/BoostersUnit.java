package fr.rushcubeland.rcbapi.account;

import fr.rushcubeland.rcbapi.network.ServerGroup;

import java.util.Arrays;

public enum BoostersUnit {

    Cinquante("Booster 50%", ServerGroup.Minigame),
    Cent("Booster 100%", ServerGroup.Minigame),
    Centcinquante("Booster 150%", ServerGroup.Minigame);

    private String name;
    private ServerGroup serverGroup;

    BoostersUnit(String name, ServerGroup serverGroup){
        this.name = name;
        this.serverGroup = serverGroup;
    }

    public static BoostersUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(BoostersUnit.Cinquante);
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public String getName() {
        return name;
    }



}
