package fr.rushcubeland.rcbapi.network;

import java.util.ArrayList;

public enum ServerGroup {

    Lobby(),
    Minigame();

    private ArrayList<ServerUnit> serversInServerGroup = new ArrayList<>();

    ServerGroup(){
    }

    public ArrayList<ServerUnit> getServersInGroup(){
        return serversInServerGroup;
    }


}
